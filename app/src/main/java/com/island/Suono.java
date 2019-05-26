package com.island;
import android.content.res.*;
import android.media.*;
import java.io.*;
public class Suono
{
	public static boolean esiste(Schermo s,int risorsa)
	{
		for(Suono suono:s.suoni)if(suono.risorsa==risorsa)return true;
		return false;
	}
	private Runnable start=new Runnable()
	{
		public void run()
		{
			if(media!=null)media.start();
		}
	};
	private Runnable pausare=new Runnable()
	{
		public void run()
		{
			try
			{
				if(media!=null)media.pause();
			}catch(IllegalStateException e)
			{
				Lista.debug(e);
			}
		}
	};
	private Runnable rilascia=new Runnable()
	{
		public void run()
		{
			if(media!=null)media.release();
		}
	};
	private Runnable stop=new Runnable()
	{
		public void run()
		{
			if(media!=null)media.stop();
		}
	};
	private Runnable rimuovi=new Runnable()
	{
		public void run()
		{
			try
			{
				if(Schermo.schermo.suoni!=null&&media!=null)Schermo.schermo.suoni.remove(Suono.this);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				Lista.debug(e);
			}
		}
	};
	public Suono(final Schermo schermo,final int suono)
	{
		risorsa=suono;
		if(schermo.suoni())schermo.suono.handler().post(new Runnable()
		{
			public void run()
			{
				try
				{
					media=new MediaPlayer();
					AssetFileDescriptor afd=schermo.getResources().openRawResourceFd(suono);
					media.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
					afd.close();
					media.prepare();
					media.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
						{
							public void onCompletion(MediaPlayer p1)
							{
								rilascia();
							}
						}
					);
					schermo.suoni.add(Suono.this);
				}
				catch(IOException e)
				{
					Lista.debug(e);
				}
			}
		});
	}
	private boolean suonando,pausa,pausaSchermo;
	private MediaPlayer media;
	private float volume;
	private int risorsa;
	public Suono pausa()
	{
		if(suonando)
		{
			suonando=false;
			pausa=true;
			controllo();
		}
		return this;
	}
	public Suono riprendi()
	{
		if(pausa)start();
		return this;
	}
	public Suono stop()
	{
		if(suonando)
		{
			suonando=false;
			pausa=false;
			controllo();
		}
		return this;
	}
	public Suono start()
	{
		if(!suonando)
		{
			suonando=true;
			pausa=false;
			controllo();
		}
		return this;
	}
	public Schermo schermo()
	{
		return Schermo.schermo;
	}
	private Suono controllo()
	{
		if(Schermo.schermo.suono!=null)
		{
			if(suonando&&!pausaSchermo)Schermo.schermo.suono.handler().post(start);
			else if(pausaSchermo||pausa)Schermo.schermo.suono.handler().post(pausare);
			else Schermo.schermo.suono.handler().post(stop);
		}
		return this;
	}
	public Suono volume(float volume)
	{
		this.volume=volume;
		return volume();
	}
	public int posizione()
	{
		if(media!=null)return media.getCurrentPosition();
		else return 0;
	}
	public Suono posizione(final int posizione)
	{
		Schermo.schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					if(media!=null)media.seekTo(posizione);
				}
			});
		return this;
	}
	Suono volume()
	{
		final float finale=Schermo.schermo.volume*volume;
		Schermo.schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					if(media!=null)media.setVolume(finale,finale);
				}
			});
		return this;
	}
	Suono pausaSchermo()
	{
		pausaSchermo=true;
		controllo();
		return this;
	}
	Suono riprendiSchermo()
	{
		pausaSchermo=false;
		controllo();
		return this;
	}
	Suono cancella()
	{
		if(Schermo.schermo.suono!=null)Schermo.schermo.suono.handler().post(rilascia);
		return null;
	}
	public Suono rilascia()
	{
		if(Schermo.schermo.suono!=null)Schermo.schermo.suono.handler().post(rimuovi);
		return cancella();
	}
	public Suono infinito(final boolean infinito)
	{
		Schermo.schermo.suono.handler().post(new Runnable()
			{
				public void run()
				{
					if(media!=null)media.setLooping(infinito);
				}
			});
		return this;
	}
	public int suono()
	{
		return risorsa;
	}
}
