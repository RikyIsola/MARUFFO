package com.island;
import android.app.*;
import android.net.wifi.*;
import java.io.*;
import java.net.*;
public abstract class Lan
{
	private ServerSocket server;
	private Lista<Socket>connessi;
	private Lista<Processo>mandareServer,ricevereServer;
	private Lista<OutputStream>outputServer;
	private Lista<byte[]>outputDataServer;
	private Lista<Integer>indexServer;
	private Socket client;
	private Processo accettazione,mandare,ricevere;
	private OutputStream output;
	private byte[]outputData;
	private int index;
	private byte lineabyte=10;
	private char lineachar=10;
	private int messaggioSize=1420;
	private int timeout=0;
	public Lan(int dim,int timeout)
	{
		this(null,dim,timeout);
	}
	public Lan(Schermo schermo,int dim,int timeout)
	{
		messaggioSize=dim;
		this.timeout=timeout;
	}
	public Lan(int dim)
	{
		this(null,dim);
	}
	public Lan(Schermo schermo,int dim)
	{
		this(schermo,dim,0);
	}
	public Lan()
	{
		this(null,0);
	}
	public Lan(Schermo schermo)
	{
		this(schermo,0);
	}
	public static String local()
	{
		return"127.0.0.1";
	}
	public static String ip(Socket s)
	{
		while(!s.isConnected());
		return s.getInetAddress().getHostAddress();
	}
	public static int maxSize()
	{
		return 1420;
	}
	public Socket client()
	{
		return client(false);
	}
	public Socket client(boolean blocca)
	{
		while(client==null&&blocca);
		return client;
	}
	public static String ip(Schermo schermo)
	{
		return android.text.format.Formatter.formatIpAddress(((WifiManager)schermo.getSystemService(Activity.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
	}
	public Socket connesso(int id)
	{
		return connesso(id,false);
	}
	public Socket connesso(int id,boolean blocca)
	{
		while(blocca&&(connessi==null||connessi.size()<=id||connessi.get(id)==null));
		return connessi.get(id);
	}
	public int inizioServer(int porta,final int membri)
	{
		try
		{
			server=new ServerSocket(porta);
			if(connessi!=null)connessi.clear();
			else connessi=new Lista<Socket>();
			if(mandareServer!=null)mandareServer.clear();
			else mandareServer=new Lista<Processo>();
			if(ricevereServer!=null)ricevereServer.clear();
			else ricevereServer=new Lista<Processo>();
			if(outputServer!=null)outputServer.clear();
			else outputServer=new Lista<OutputStream>();
			if(outputDataServer!=null)outputDataServer.clear();
			else outputDataServer=new Lista<byte[]>();
			if(indexServer!=null)indexServer.clear();
			else indexServer=new Lista<Integer>();
			accettazione=new Processo()
			{
				long tempo=System.currentTimeMillis();
				public void esegui()
				{
					loop();
				}
				public void sempre()
				{
					if(connessi.size()<membri)
					{
						try
						{
							final Socket socket=server.accept();
							if(timeout!=0)socket.setSoTimeout(timeout);
							Processo mandare=new Processo()
							{
								long tempo=System.currentTimeMillis();
								public void esegui()
								{
									loop();
								}
								public void sempre()
								{
									if(Lan.this.schermo()!=null&&Lan.this.schermo().debug())for(int a=0;a<Lan.this.schermo().rallentamento();a++);
								}
							}.riprendi();
							Processo ricevere=new Processo()
							{
								public void esegui()
								{
									try
									{
										InputStream in=socket.getInputStream();
										int volte=0;
										long tempo=System.currentTimeMillis();
										int bite=0;
										StringBuilder messaggio=new StringBuilder();
										while(true)
										{
											messaggio.setLength(0);
											int letto;
											while(true)
											{
												letto=in.read();
												if(letto==-1)throw new IOException();
												else if(letto!=lineachar)messaggio.append((char)letto);
												else break;
											}
											leggi(messaggio,socket);
											if(Lan.this.schermo()!=null&&Lan.this.schermo().debug())
											{
												bite+=messaggio.length()+1;
												do
												{
													volte++;
													long attuale=System.currentTimeMillis();
													if(attuale-tempo>=1000)
													{
														tempo=attuale;
														volte=0;
														bite=0;
														break;
													}
												}while(volte<Lan.this.schermo().rallentamento()/1000||(Lan.this.schermo().rallentamentoConnessione()!=0&&bite>=Lan.this.schermo().rallentamentoConnessione()));
											}
										}
									}
									catch(final IOException e){}
									try
									{
										int id=-1;
										while(id==-1)for(int a=0;a<connessi.size();a++)if(connessi.get(a)==socket)id=a;
										uscito(id);
									}
									catch(NullPointerException e)
									{
										Lista.debug(e);
									}
								}
							};
							connessi.add(socket);
							mandareServer.add(mandare);
							ricevereServer.add(ricevere);
							outputServer.add(socket.getOutputStream());
							if(messaggioSize!=0)outputDataServer.add(new byte[messaggioSize]);
							indexServer.add(0);
							entrato(socket);
						}
						catch(IOException e){}
					}
				}
			}.riprendi();
			return server.getLocalPort();
		}
		catch(final IOException e)
		{
			Lista.debug(e);
			return 0;
		}
	}
	public void inizioClient(final String ip,final int porta)
	{
		new Processo()
		{
			public void esegui()
			{
				try
				{
					client=new Socket(ip,porta);
					if(timeout!=0)client.setSoTimeout(timeout);
					output=client.getOutputStream();
					if(messaggioSize!=0)outputData=new byte[messaggioSize];
					ricevere=new Processo()
					{
						public void esegui()
						{
							try
							{
								InputStream in=client.getInputStream();
								int volte=0;
								long tempo=System.currentTimeMillis();
								int bite=0;
								StringBuilder messaggio=new StringBuilder();
								while(true)
								{
									messaggio.setLength(0);
									int letto;
									while(true)
									{
										letto=in.read();
										if(letto==-1)throw new IOException();
										else if(letto!=lineachar)messaggio.append((char)letto);
										else break;
									}
									leggi(messaggio,client);
									if(Lan.this.schermo()!=null&&Lan.this.schermo().debug())
									{
										bite+=messaggio.length()+1;
										do
										{
											volte++;
											long attuale=System.currentTimeMillis();
											if(attuale-tempo>=1000)
											{
												tempo=attuale;
												volte=0;
												bite=0;
												break;
											}
										}while(volte<Lan.this.schermo().rallentamento()/1000||(Lan.this.schermo().rallentamentoConnessione()!=0&&bite>=Lan.this.schermo().rallentamentoConnessione()));
									}
								}
							}
							catch(IOException e){}
							fine();
						}
					};
					mandare=new Processo()
					{
						long tempo=System.currentTimeMillis();
						public void esegui()
						{
							loop();
						}
						public void sempre()
						{
							if(Lan.this.schermo()!=null&&Lan.this.schermo().debug())for(int a=0;a<Lan.this.schermo().rallentamento();a++);
						}
					}.riprendi();
					entrato(client);
				}
				catch(final IOException e)
				{
					Lista.debug(e);
				}
			}
		};
	}
	public boolean connesso()
	{
		return client!=null;
	}
	public void chiudiServer()
	{
		try
		{
			accettazione.chiudi();
			server.close();
		}
		catch(IOException e)
		{
			Lista.debug(e);
		}
	}
	private void uscito(int id)
	{
		try
		{
			Socket socket=connessi.get(id);
			uscito(socket);
			Processo mandare=mandareServer.get(id);
			Processo ricevere=ricevereServer.get(id);
			OutputStream output=outputServer.get(id);
			connessi.remove(socket);
			mandareServer.remove(mandare);
			ricevereServer.remove(ricevere);
			outputServer.remove(output);
			try
			{
				mandare.chiudi();
				output.flush();
				socket.close();
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			Lista.debug(e);
		}
	}
	public void uscito(Socket s){}
	public void entrato(Socket s){}
	public void pausa()
	{
		if(accettazione!=null)accettazione.pausa();
		if(mandare!=null)mandare.pausa();
		if(mandareServer!=null)for(Processo p:mandareServer)if(p!=null)p.pausa();
	}
	public void riprendi()
	{
		if(accettazione!=null)accettazione.riprendi();
		if(mandare!=null)mandare.riprendi();
		if(mandareServer!=null)for(Processo p:mandareServer)if(p!=null)p.riprendi();
	}
	public void fine()
	{
		try
		{
			if(outputServer!=null)for(OutputStream o:outputServer)o.flush();
			if(connessi!=null)for(Socket s:connessi)s.close();
			if(mandareServer!=null)for(Processo p:mandareServer)p.chiudi();
			if(output!=null)output.flush();
			if(client!=null)client.close();
			if(accettazione!=null)accettazione.chiudi();
			if(mandare!=null)mandare.chiudi();
			if(server!=null)server.close();
			outputServer=null;
			connessi=null;
			mandareServer=null;
			output=null;
			client=null;
			accettazione=null;
			mandare=null;
			server=null;
			ricevere=null;
			ricevereServer=null;
			
		}
		catch(IOException e)
		{
			Lista.debug(e);
		}
	}
	public abstract void leggi(StringBuilder messaggio,Socket socket);
	private int mandato,mandatoServer;
	private long tempo=System.currentTimeMillis();
	public Lan manda(CharSequence messaggio)
	{
		return manda(messaggio,false);
	}
	public Lan flush()
	{
		return flush(false);
	}
	public Lan flush(boolean blocca)
	{
		try
		{
			while((mandare==null||mandare.handler()==null)&&blocca);
			mandare.handler().post(new Runnable()
				{
					public void run()
					{
						try
						{
							index=0;
							output.write(outputData);
							output.flush();
						}
						catch(IOException e)
						{
							Lista.debug(e);
						}
					}
				});
		}
		catch(NullPointerException e)
		{
			Lista.debug(e);
		}
		return this;
	}
	public Lan manda(final CharSequence messaggio,boolean blocca)
	{
		try
		{
			while((mandare==null||mandare.handler()==null)&&blocca);
			mandare.handler().post(new Runnable()
				{
					public void run()
					{
						try
						{
							int length=messaggio.length();
							if(messaggioSize!=0)for(int a=0;a<=length;a++)
							{
								byte b;
								if(a==length)b=lineabyte;
								else b=(byte)messaggio.charAt(a);
								outputData[index]=b;
								index++;
								if(index==outputData.length)
								{
									index=0;
									output.write(outputData);
									output.flush();
								}
							}
							else
							{
								for(int a=0;a<length;a++)output.write(messaggio.charAt(a));
								output.write(lineabyte);
							}
							if(Lan.this.schermo()!=null&&Lan.this.schermo().debug()&&Lan.this.schermo().rallentamentoConnessione()!=0)
							{
								mandato+=messaggio.length()+1;
								do
								{
									long attuale=System.currentTimeMillis();
									if(attuale-tempo>=1000)
									{
										mandato=0;
										tempo=attuale;
									}
								}while(mandato>=Lan.this.schermo().rallentamentoConnessione());
							}
						}
						catch(IOException e)
						{}
					}
				});
		}
		catch(NullPointerException e)
		{
			Lista.debug(e);
		}
		return this;
	}
	public Lan manda(final CharSequence messaggio,final Socket socket)
	{
		return manda(messaggio,socket,false);
	}
	public Lan flush(Socket s)
	{
		return flush(s,false);
	}
	public Lan flush(Socket socket,boolean blocca)
	{
		try
		{
			int id=-1;
			do
			{
				for(int a=0;a<connessi.size();a++)if(connessi.get(a)==socket)id=a;
			}
			while(id==-1&&blocca);
			final int fid=id;
			if(mandareServer.get(id).handler()!=null)mandareServer.get(id).handler().post(new Runnable()
				{
					public void run()
					{
						try
						{
							indexServer.set(fid,0);
							outputServer.get(fid).write(outputDataServer.get(fid));
							outputServer.get(fid).flush();
						}
						catch(IOException e)
						{
							Lista.debug(e);
						}
					}
				});
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			Lista.debug(e);
		}
		catch(NullPointerException e)
		{
			Lista.debug(e);
		}
		return this;
	}
	public Lan manda(final CharSequence messaggio,final Socket socket,boolean blocca)
	{
		try
		{
			int id=-1;
			do
			{
				for(int a=0;a<connessi.size();a++)if(connessi.get(a)==socket)id=a;
			}
			while(id==-1&&blocca);
			final int fid=id;
			if(mandareServer.get(id).handler()!=null)mandareServer.get(id).handler().post(new Runnable()
					{
						public void run()
						{
							try
							{
								int length=messaggio.length();
								if(messaggioSize!=0)for(int a=0;a<=length;a++)
								{
									int index=indexServer.get(fid);
									byte b;
									if(a==length)b=lineabyte;
									else b=(byte)messaggio.charAt(a);
									outputDataServer.get(fid)[index]=b;
									index++;
									if(index==outputDataServer.get(fid).length)
									{
										indexServer.set(fid,0);
										outputServer.get(fid).write(outputDataServer.get(fid));
										outputServer.get(fid).flush();
									}
									else indexServer.set(fid,index);
								}
								else
								{
									for(int a=0;a<length;a++)outputServer.get(fid).write(messaggio.charAt(a));
									outputServer.get(fid).write(lineabyte);
								}
								if(Lan.this.schermo()!=null&&Lan.this.schermo().debug()&&Lan.this.schermo().rallentamentoConnessione()!=0)
								{
									mandatoServer+=messaggio.length()+1;
									do
									{
										long attuale=System.currentTimeMillis();
										if(attuale-tempo>=1000)
										{
											mandatoServer=0;
											tempo=attuale;
										}
									}while(mandatoServer>=Lan.this.schermo().rallentamentoConnessione());
								}
							}
							catch(ArrayIndexOutOfBoundsException e)
							{
								Lista.debug(e);
							}
							catch(IOException e)
							{
								Lista.debug(e);
							}
						}
					});
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			Lista.debug(e);
		}
		catch(NullPointerException e)
		{
			Lista.debug(e);
		}
		return this;
	}
	public Schermo schermo()
	{
		return Schermo.schermo;
	}
	public Lan schermo(Schermo schermo)
	{
		return this;
	}
	public int connessi()
	{
		if(connessi!=null)return connessi.size();
		else return 0;
	}
	public int velocita()
	{
		while(mandare==null);
		return mandare.velocita();
	}
	public int velocita(Socket socket)
	{
		int id=-1;
		while(id==-1)for(int a=0;a<connessi.size();a++)if(connessi.get(a)==socket)id=a;
		return mandareServer.get(id).velocita();
	}
}
