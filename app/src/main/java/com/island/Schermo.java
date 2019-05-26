package com.island;
import android.app.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
public abstract class Schermo extends Activity
{
	static
	{
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
			{
				Thread.UncaughtExceptionHandler vecchio=Thread.getDefaultUncaughtExceptionHandler();
				public void uncaughtException(Thread p1,Throwable p2)
				{
					Lista.debug(p2);
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException e)
					{
						Lista.debug(e);
					}
					vecchio.uncaughtException(p1,p2);
				}
			});
	}
	private DebuggerManager dm=(debug()&&debugger()?new DebuggerManager():null);
	{
		if(debug())
		{
			if("HUAWEI".equals(Build.MANUFACTURER))try
			{
				PrintStream out=new PrintStream(new File(Environment.getExternalStorageDirectory(),"/AppProjects/.nomedia"));
				System.setOut(out);
				System.setErr(out);
			}
			catch(FileNotFoundException e)
			{
				Lista.debug(e);
			}
		}
	}
	public Typeface typeface()
	{
		return typeface;
	}
	public int rallentamento()
	{
		return 0;
	}
	public int rallentamentoConnessione()
	{
		return 0;
	}
	public boolean immagini()
	{
		return true;
	}
	public boolean suoni()
	{
		return true;
	}
	public boolean debug()
	{
		return true;
	}
	public boolean debugger()
	{
		return true;
	}
	public boolean scimmia()
	{
		return false;
	}
	public void debug(Throwable t)
	{
		StackTraceElement[]ste=t.getStackTrace();
		StringBuilder sb=new StringBuilder((ste.length+1)*42);
		sb.append(t);
		for(StackTraceElement el:ste)
		{
			sb.append(linea).append(at).append(el.getClassName()).append(punto).append(el.getMethodName()).append(tonda);
			int n=el.getLineNumber();
			if(n==-2)sb.append(nativo);
			else sb.append(el.getFileName()).append(punti).append(n);
			sb.append(tonda2);
		}
	}
	public void font(Typeface typeface,int type)
	{
		this.typeface=Typeface.create(typeface,type);
	}
	public int orientamento()
	{
		return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
	}
	public int tema()
	{
		return android.R.style.Theme_Black_NoTitleBar_Fullscreen;
	}
	private static Schermo salvataggio;
	public static Schermo schermo;
	private Handler h=new Handler();
	int[]dimensioni=new int[2];
	private int frame,frameS,ramGiusta,back,gc,frame2,frameS2;
	private Typeface typeface;
	private long tempo,tempo2;
	private Processo processo;
	float volume=1;
	Processo suono;
	Lista<Suono>suoni=new Lista<Suono>();
	public Lista<Finestra>finestre=new Lista<Finestra>();
	private Lista<Bitmap>caricati=new Lista<Bitmap>();
	private Lista<Rect>risorse=new Lista<Rect>();
	private Point cache=new Point();
	Gruppo view;
	public Paint paint=new Paint();
	public RectF rect=new RectF();
	public Matrix matrix=new Matrix();
	public Random random=new Random();
	private View debug;
	private Runtime runtime=Runtime.getRuntime();
	private boolean sotto;
	private Random scimmia=new Random();
	private String punto=".";
	private String at="at ";
	private String tonda="(";
	private String tonda2=")";
	private String punti=":";
	private String nativo="Native Method";
	private String linea="\n";
	private Runnable garbageCollector;
	private void garbage()
	{
		new Object()
		{
			public void finalize()
			{
				gc++;
				if(debug())runOnUiThread(garbageCollector);
			}
		};
	}
	private Runnable riciclato;
	private Runnable framegrafici;
	private Runnable runnable;
	public void sempreGrafico(){}
	public void sempre(){}
	public int[]dimensioni()
	{
		return dimensioni;
	}
	public void displayNormale(Point p)
	{
		Display d=getWindowManager().getDefaultDisplay();
		p.x=d.getWidth();
		p.y=d.getHeight();
		/*if(debug!=null)
		{
			p.y=debug.getBottom();
			p.x=debug.getRight();
		}
		else p.x=0;*/
		/*Display d=getWindowManager().getDefaultDisplay();
		if(debug!=null)
		{
			p.x=debug.getRight()*2-d.getWidth();
			p.y=debug.getBottom()*2-d.getHeight();
		}
		else p.x=0;
		if(p.x<=0)
		{
			p.x=d.getWidth();
			p.y=d.getHeight();
		}*/
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
		{
			int x=p.x;
			p.x=p.y;
			p.y=x;
		}
	}
	public void display(Point p)
	{
		displayNormale(p);
	}
	public void displayPiccolo(Point p)
	{
		displayPiccolo(p,240,320,120);
	}
	public void displayPiccolo(Point p,int x,int y,double dpi)
	{
		p.x=x;
		p.y=y;
		double rapporto=dpi()/dpi;
		p.x*=rapporto;
		p.y*=rapporto;
	}
	public void displayDenso(Point p)
	{
		p.x=240;
		p.y=320;
	}
	public void setContentView(View view)
	{
		super.setContentView(view);
		if(view!=debug)
		{
			if(debug!=null)
			{
				((ViewManager)view.getParent()).removeView(debug);
				addContentView(debug,new ViewGroup.LayoutParams(-1,-1));
			}
			this.view=(Gruppo)view;
			sotto=true;
		}
	}
	public void addContentView(View view,ViewGroup.LayoutParams params)
	{
		super.addContentView(view,params);
		sotto=true;
	}
	protected final void onCreate(Bundle b)
	{
		setTheme(tema());
		setRequestedOrientation(orientamento());
		super.onCreate(b);
		schermo=this;
		if(b==null)
		{
			Point p=new Point();
			display(p);
			if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
			{
				int x=p.x;
				p.x=p.y;
				p.y=x;
			}
			dimensioni[0]=p.x;
			dimensioni[1]=p.y;
			ramGiusta=(int)(Math.sqrt(p.x*p.y)*16/Math.sqrt(240*320));
			android.os.Process.setThreadPriority(-20);
			if(Build.VERSION.SDK_INT>=23)
			{
				try
				{
					String[]permessi=getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_PERMISSIONS).requestedPermissions;
					if(permessi!=null)for(String s:permessi)if(checkSelfPermission(s)==PackageManager.PERMISSION_DENIED)requestPermissions(new String[]{s},0);
				}
				catch(PackageManager.NameNotFoundException e)
				{
					Lista.debug(e);
				}
			}
		}
		else
		{
			try
			{
				Field[]fields=getClass().getDeclaredFields();
				for(Field f:fields)
				{
					boolean accessibile=f.isAccessible();
					if(!accessibile)f.setAccessible(true);
					f.set(this,f.get(salvataggio));
					if(!accessibile)f.setAccessible(false);
				}
				fields=getClass().getSuperclass().getDeclaredFields();
				for(Field f:fields)f.set(this,f.get(salvataggio));
				back=0;
			}
			catch(Exception e)
			{
				Lista.debug(e);
				debug(e);
			}
		}
		processo=new Processo()
		{
			public void esegui()
			{
				loop();
			}
			public void sempre()
			{
				if(debug())for(int a=0;a<rallentamento();a++);
				if(finestre!=null)
				{
					for(Finestra finestra:finestre)finestra.sempre();
					Schermo.this.sempre();
				}
			}
		};
		suono=new Processo()
		{
			public void esegui()
			{
				loop();
			}
			public void sempre()
			{
				if(debug())for(int a=0;a<rallentamento();a++);
			}
		};
		if(debug())
		{
			garbage();
			debug=new View(this)
			{
				private StringBuilder sb=new StringBuilder(11);
				private long ramVecchia;
				private int max;
				private String fps="FPS:";
				private String fps2="MAIN:";
				private String speed="VEL:";
				private String sound="SUONO:";
				private String ram="RAM:";
				private String garbage="GC:";
				private String networkt="NETT:";
				private String networkr="NETR:";
				public void onDraw(Canvas c)
				{
					max=c.getWidth();
					if(c.getHeight()<max)max=c.getHeight();
					paint.reset();
					paint.setTextSize(max/15);
					paint.setTypeface(typeface);
					//p.setTextScaleX(c.getWidth()/c.getHeight());
					paint.setColor(Color.WHITE);
					paint.setTextAlign(Paint.Align.LEFT);
					sb.setLength(0);
					c.drawText(sb.append(fps).append(frame()),0,sb.length(),0,c.getHeight()/10,paint);
					sb.setLength(0);
					c.drawText(sb.append(fps2).append(frame2()),0,sb.length(),0,c.getHeight()/10*3,paint);
					sb.setLength(0);
					c.drawText(sb.append(speed).append(velocita()),0,sb.length(),0,c.getHeight()/10*5,paint);
					sb.setLength(0);
					c.drawText(sb.append(sound).append(velocitaSuono()),0,sb.length(),0,c.getHeight()/10*7,paint);
					paint.setTextAlign(Paint.Align.RIGHT);
					sb.setLength(0);
					long ramNuova=ram();
					if(ramVecchia!=ramNuova)c.drawText(sb.append(ram).append(ram()).append(File.separator).append(ramGiusta()),0,sb.length(),c.getWidth(),c.getHeight()/10,paint);
					sb.setLength(0);
					c.drawText(sb.append(garbage).append(gc()),0,sb.length(),c.getWidth(),c.getHeight()/10*3,paint);
					sb.setLength(0);
					c.drawText(sb.append(networkt).append(trasferito()),0,sb.length(),c.getWidth(),c.getHeight()/10*5,paint);
					sb.setLength(0);
					c.drawText(sb.append(networkr).append(ricevuto()),0,sb.length(),c.getWidth(),c.getHeight()/10*7,paint);
				}
				public boolean onTouchEvent(MotionEvent e)
				{
					if(e.getAction()==MotionEvent.ACTION_DOWN)
					{
						float x=e.getX();
						float y=e.getY();
						if(x>getRight()/10*8&&x<getRight()&&y>getBottom()/10*3-max/15&&y<getBottom()/10*3)
						{
							runtime.gc();
						}
					}
					return super.onTouchEvent(e);
				}
			};
			setContentView(debug);
		}
		garbageCollector=new Runnable()
		{
			public void run()
			{
				garbage();
			}
		};
		riciclato=new Runnable()
		{
			public void run()
			{
				getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
			}
		};
		framegrafici=new Runnable()
		{
			public void run()
			{
				h.postDelayed(this,16);
				if(System.currentTimeMillis()>=tempo)
				{
					tempo=System.currentTimeMillis()+1000;
					frameS=frame;
					frame=0;
				}
				frame++;
			}
		};
		runnable=new Runnable()
		{
			private MotionEvent prec;
			public void run()
			{
				h.postDelayed(this,50);
				display(cache);
				if(cache.x!=dimensioni[0])
				{
					if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
					{
						int x=cache.x;
						cache.x=cache.y;
						cache.y=x;
					}
					dimensioni[0]=cache.x;
					dimensioni[1]=cache.y;
					ramGiusta=(int)(Math.sqrt(cache.x*cache.y)*16/Math.sqrt(240*320));
					if(view!=null)view.aggiorna();
					for(Finestra f:finestre)if(f.view!=null)f.view.aggiorna();
				}
				if(debug())
				{
					for(int a=0;a<rallentamento();a++);
					if(ram()>ramGiusta())
					{
						runtime.runFinalization();
						runtime.gc();
					}
					if(scimmia())
					{
						if(prec==null)
						{
							long tempo=SystemClock.uptimeMillis();
							float x=dimensioni[0]*scimmia.nextFloat();
							float y=dimensioni[1]*scimmia.nextFloat();
							prec=MotionEvent.obtain(tempo,tempo,MotionEvent.ACTION_DOWN,x,y,0);
							getWindow().getDecorView().dispatchTouchEvent(prec);
						}
						else if(scimmia.nextBoolean())
						{
							long tempo=prec.getDownTime();
							float aggx=scimmia.nextFloat()*2-1;
							float aggy=scimmia.nextFloat()*2-1;
							float x=prec.getX()+aggx;
							float y=prec.getY()+aggy;
							prec.recycle();
							prec=MotionEvent.obtain(tempo,SystemClock.uptimeMillis(),MotionEvent.ACTION_MOVE,x,y,0);
							getWindow().getDecorView().dispatchTouchEvent(prec);
						}
						else
						{
							getWindow().getDecorView().dispatchTouchEvent(MotionEvent.obtain(prec.getDownTime(),SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,prec.getX(),prec.getY(),0));
							prec.recycle();
							prec=null;
						}
					}
				}
				if(System.currentTimeMillis()>=tempo2)
				{
					tempo2=System.currentTimeMillis()+1000;
					frameS2=frame2;
					frame2=0;
				}
				frame2++;
				Oggetto sfondo=null;
				Gruppo gruppo=view;
				if(finestre.size()>0)
				{
					gruppo=finestre.get(finestre.size()-1).view;
					if(gruppo!=null)sfondo=gruppo.sfondo;
				}
				else if(gruppo!=null)sfondo=gruppo.sfondo;
				if(sfondo==null)
				{
					if(back!=0)
					{
						back=0;
						getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
					}
				}
				else
				{
					if(sfondo.colore()!=Color.TRANSPARENT)
					{
						if(back!=sfondo.colore())
						{
							back=sfondo.colore();
							getWindow().setBackgroundDrawable(new ColorDrawable(back));
						}
					}
					else if(sfondo.immagine()!=0)
					{
						if(back!=sfondo.immagine())
						{
							if(esiste(sfondo.immagine()))
							{
								back=sfondo.immagine();
								Bitmap b=immagine(back);
								if(!b.isRecycled())getWindow().setBackgroundDrawable(new BitmapDrawable(b));
							}
						}
					}
					else if(back!=0)
					{
						back=0;
						getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
					}
				}
				if(debug!=null&&sotto)
				{
					debug.bringToFront();
					sotto=false;
				}
				sempreGrafico();
				for(Finestra finestra:finestre)finestra.sempreGrafico();
				if(debug!=null)debug.invalidate();
			}
		};
		if(b==null)crea();
		else
		{
			if(debug!=null)((ViewGroup)debug.getParent()).removeView(debug);
			if(view!=null)((ViewGroup)view.getParent()).removeView(view);
			if(debug!=null)setContentView(debug);
			if(view!=null)setContentView(view);
			for(ViewGroup v:finestre)
			{
				((ViewGroup)v.getParent()).removeView(v);
				addContentView(v,new ViewGroup.LayoutParams(-1,-1));
			}
		}
	}
	public abstract void crea()
	public abstract void distruggi()
	protected final void onDestroy()
	{
		super.onDestroy();
		if(salvataggio==null)
		{
			for(Finestra finestra:finestre)finestra.cancel();
			for(Suono suono:suoni)suono.cancella();
			h=null;
			cache=null;
			runnable=null;
			riciclato=null;
			framegrafici=null;
			dimensioni=null;
			paint=null;
			rect=null;
			matrix=null;
			typeface=null;
			scimmia=null;
			garbageCollector=null;
			processo.chiudi();
			processo=null;
			suono.chiudi();
			suono=null;
			suoni=null;
			finestre=null;
			for(Bitmap bitmap:caricati)bitmap.recycle();
			caricati=null;
			risorse=null;
			ViewManager vm=null;
			if(view!=null)
			{
				vm=((ViewManager)view.getParent());
				vm.removeView(view);
			}
			if(debug!=null)
			{
				if(vm==null)vm=((ViewManager)debug.getParent());
				vm.removeView(debug);
			}
			debug=null;
			view=null;
			if(dm!=null)dm.finito();
			dm=null;
			punto=null;
			at=null;
			tonda=null;
			tonda2=null;
			punti=null;
			nativo=null;
			linea=null;
			runtime.runFinalization();
			runtime.gc();
			runtime=null;
			schermo=null;
		}
	}
	protected void onStop()
	{
		super.onStop();
		h.removeCallbacks(runnable);
		if(debug())h.removeCallbacks(framegrafici);
		processo.pausa();
		suono.pausa();
		for(Finestra finestra:finestre)finestra.pausa();
		for(Suono suono:suoni)suono.pausaSchermo();
	}
	protected void onStart()
	{
		super.onStart();
		salvataggio=null;
		processo.riprendi();
		suono.riprendi();
		h.postDelayed(runnable,50);
		if(debug())h.postDelayed(framegrafici,16);
		for(Finestra finestra:finestre)finestra.riprendi();
		for(Suono suono:suoni)suono.riprendiSchermo();
	}
	public void onBackPressed()
	{
		if(libero())super.onBackPressed();
		else finestra().onBackPressed();
	}
	public Schermo schermo()
	{
		return schermo;
	}
	public Schermo toast(final CharSequence testo)
	{
		if(debug())runOnUiThread(new Runnable()
		{
			public void run()
			{
				Toast.makeText(Schermo.this,testo,0).show();
			}
		});
		return this;
	}
	public int frame()
	{
		return frameS;
	}
	public int frame2()
	{
		return frameS2*5;
	}
	public int velocitaSuono()
	{
		return suono.velocita();
	}
	public int velocita()
	{
		return processo.velocita();
	}
	public Schermo carica(final int risorsa,int x,int y)
	{
		return carica((Oggetto)null,risorsa,x,y);
	}
	public Schermo carica(final Bitmap immagine,final int risorsa,int x,int y)
	{
		return carica((Oggetto)null,immagine,risorsa,x,y);
	}
	public Schermo carica(final Oggetto oggetto,final int risorsa,final int x,final int y)
	{
		try
		{
			Thread.sleep(1);
		}
		catch(InterruptedException e)
		{
			Lista.debug(e);
		}
		while(processo.handler()==null);
		if(processo!=null&&immagini()&&!esiste(risorsa,x,y))processo.handler().post(new Runnable()
			{
				public void run()
				{
					if(!esiste(risorsa,x,y))
					{
						BitmapFactory.Options opt=new BitmapFactory.Options();
						opt.inMutable=true;
						opt.inJustDecodeBounds=true;
						BitmapFactory.decodeResource(getResources(),risorsa,opt);
						double scalatura=0.5;
						while(opt.outWidth/(scalatura*2)>x&&opt.outHeight/(scalatura*2)>y)scalatura*=2;
						opt.inSampleSize=(int)(scalatura);
						opt.inJustDecodeBounds=false;
						try
						{
							Bitmap b=BitmapFactory.decodeResource(getResources(),risorsa,opt);
							//toast(x+" "+y+" "+b.getWidth()+" "+b.getHeight());
							Bitmap immagine;
							if(x<b.getWidth()||y<b.getHeight())immagine=Bitmap.createScaledBitmap(b,x,y,true);
							else immagine=b;
							//carica(oggetto,nuovo,risorsa,x,y);

							if(risorse!=null)
							{
								boolean ok=true;
								for(int a=0;a<risorse.size();a++)
								{
									final Rect p=risorse.get(a);
									if(p.right==risorsa)
									{
										if(x>p.left||y>p.top)
										{
											Bitmap b1=caricati.get(a);
											caricati.set(a,immagine);
											b1.recycle();
											risorse.get(a).left=x;
											risorse.get(a).top=y;
										}
										ok=false;
									}
								}
								if(ok)
								{
									caricati.add(immagine);
									risorse.add(new Rect(x,y,risorsa,0));
								}
								if(oggetto!=null)runOnUiThread(oggetto.invalida);
							}

							if(immagine!=b)b.recycle();
						}catch(OutOfMemoryError e)
						{
							Lista.debug(e);
						}
						catch(IllegalArgumentException e)
						{
							Lista.debug(e);
						}
					}
				}
			});
		return this;
	}
	public Schermo carica(final Oggetto oggetto,final Bitmap immagine,final int risorsa,final int x,final int y)
	{
		if(processo!=null&&immagini())processo.handler().post(new Runnable()
			{
				public void run()
				{
					if(risorse!=null)
					{
						boolean ok=true;
						for(int a=0;a<risorse.size();a++)
						{
							final Rect p=risorse.get(a);
							if(p.right==risorsa)
							{
								if(x>p.left||y>p.top)
								{
									Bitmap b=caricati.get(a);
									caricati.set(a,immagine);
									b.recycle();
									risorse.get(a).left=x;
									risorse.get(a).top=y;
								}
								ok=false;
							}
						}
						if(ok)
						{
							caricati.add(immagine);
							risorse.add(new Rect(x,y,risorsa,0));
						}
						if(oggetto!=null)runOnUiThread(oggetto.invalida);
					}
				}
			});
		return this;
	}
	public Schermo cancella(final int risorsa)
	{
		processo.handler().post(new Runnable()
			{
				public void run()
				{
					for(int a=0;a<risorse.size();a++)if(risorse.get(a).right==risorsa)
					{
						if(risorse.get(a).right==back)
						{
							grafica().postAtFrontOfQueue(riciclato);
						}
						caricati.get(a).recycle();
						risorse.remove(a);
						caricati.remove(a);
						break;
					}
					//System.gc();
				}
			});
		return this;
	}
	public boolean esiste(int risorsa,int x,int y)
	{
		if(risorsa==0)return false;
		for(int a=0;a<risorse.size();a++)
		{
			Rect p=risorse.get(a);
			if(p.right==risorsa&&p.left>=x&&p.top>=y&&!caricati.get(a).isRecycled())return true;
		}
		return false;
	}
	public boolean esiste(int risorsa)
	{
		if(risorsa==0)return false;
		for(Rect p:risorse)if(p.right==risorsa)return true;
		return false;
	}
	public Bitmap immagine(int nome)
	{
		for(int a=0;a<risorse.size();a++)if(risorse.get(a).right==nome&&!caricati.get(a).isRecycled())return caricati.get(a);
		return null;
	}
	public Schermo volume(float volume)
	{
		this.volume=volume;
		for(Suono suono:suoni)suono.volume();
		return this;
	}
	public long ram()
	{
		return (runtime.totalMemory()-runtime.freeMemory())/1048576;
	}
	public int ramGiusta()
	{
		return ramGiusta;
	}
	public int gc()
	{
		return gc;
	}
	public long trasferito()
	{
		return TrafficStats.getUidTxBytes(android.os.Process.myUid())/1024;
	}
	public long ricevuto()
	{
		return TrafficStats.getUidRxBytes(android.os.Process.myUid())/1024;
	}
	public int dpi()
	{
		return(int)getResources().getDisplayMetrics().xdpi;
	}
	public boolean libero()
	{
		if(finestre!=null)return finestre.size()==0;
		else return true;
	}
	public Finestra finestra()
	{
		return finestre.get(finestre.size()-1);
	}
	public Handler grafica()
	{
		return h;
	}
	public Handler thread()
	{
		return processo.handler();
	}
	public int g()
	{
		return 1800;
	}
	public int g2()
	{
		return 5000;
	}
	public int e()
	{
		return 22500;
	}
	public int g3()
	{
		return 87500;
	}
	public int h()
	{
		return 1800000;
	}
	public int g4()
	{
		return 18750000;
	}
	public void inizioDebug(int id)
	{
		if(dm!=null)dm.inizio(id);
	}
	public void fineDebug(int id)
	{
		if(dm!=null)dm.fine(id);
	}
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		salvataggio=this;
	}
}
