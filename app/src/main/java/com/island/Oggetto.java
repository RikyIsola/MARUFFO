package com.island;
import android.graphics.*;
import android.view.*;
import android.view.accessibility.*;
import android.widget.*;
public class Oggetto extends View
{
	public static double distanza(double x,double y,double larghezza,double altezza,double baseX,double baseY,double baseLarghezza,double baseAltezza)
	{
		return Math.sqrt(Math.pow(Oggetto.zero(Oggetto.distanzaX(x,larghezza,baseX,baseLarghezza)),2)+Math.pow(Oggetto.zero(Oggetto.distanzaY(y,altezza,baseY,baseAltezza)),2));
	}
	public static double distanzaImprecisa(double x,double y,double larghezza,double altezza,double baseX,double baseY,double baseLarghezza,double baseAltezza)
	{
		return Oggetto.zero(Oggetto.distanzaX(x,larghezza,baseX,baseLarghezza))+Oggetto.zero(Oggetto.distanzaY(y,altezza,baseY,baseAltezza));
	}
	public static double distanzaX(double x,double larghezza,double baseX,double baseLarghezza)
	{
		return Math.abs(x+(larghezza-x)/2-(baseX+(baseLarghezza-baseX)/2))-(larghezza-x)/2-(baseLarghezza-baseX)/2;
	}
	public static double distanzaY(double y,double altezza,double baseY,double baseAltezza)
	{
		return Math.abs(y+(altezza-y)/2-(baseY+(baseAltezza-baseY)/2))-(altezza-y)/2-(baseAltezza-baseY)/2;
	}
	public static boolean toccando(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1,double raggio)
	{
		return distanza(x,y,larghezza,altezza,x1,y1,larghezza1,altezza1)<=raggio;
	}
	public static boolean toccando(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1)
	{
		return distanza(x,y,larghezza,altezza,x1,y1,larghezza1,altezza1)<=0;
	}
	public static boolean toccandoImpreciso(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1,double raggio)
	{
		return distanzaImprecisa(x,y,larghezza,altezza,x1,y1,larghezza1,altezza1)<=raggio;
	}
	public static boolean toccandoImpreciso(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1)
	{
		return distanzaImprecisa(x,y,larghezza,altezza,x1,y1,larghezza1,altezza1)<=0;
	}
	public static boolean toccandoBase(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1,double raggio)
	{
		return x<larghezza1+raggio&&y<altezza1+raggio&&larghezza+raggio>x1&&altezza+raggio>y1;
	}
	public static boolean toccandoBase(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1)
	{
		return x<larghezza1&&y<altezza1&&larghezza>x1&&altezza>y1;
	}
	public static boolean toccandoBaseUguale(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1,double raggio)
	{
		return x<=larghezza1+raggio&&y<=altezza1+raggio&&larghezza+raggio>=x1&&altezza+raggio>=y1;
	}
	public static boolean toccandoBaseUguale(double x,double y,double larghezza,double altezza,double x1,double y1,double larghezza1,double altezza1)
	{
		return x<=larghezza1&&y<=altezza1&&larghezza>=x1&&altezza>=y1;
	}
	public static double zero(double valore)
	{
		if(valore<0)return 0;
		else return valore;
	}
	private int immagine,concatenazione,concatenazione2;
	private int alpha=255;
	private int colore=Color.TRANSPARENT;
	Info info;
	private double rotazione;
	private Gruppo g;
	private Bitmap concatenato;
	private boolean riconcatena,lazyLoad;
	private Canvas concatenatoc;
	private Paint scorta;
	private Rect scortar;
	void lazyLoad(int immagine)
	{
		//schermo().toast("lazyload "+(getRight()-getLeft())+" "+(getBottom()-getTop()));
		if((getRight()-getLeft()>0)&&(getBottom()-getTop()>0)&&immagine!=0&&!schermo().esiste(immagine,(int)(large()*unitaX()),(int)(tall()*unitaY())))schermo().carica(this,immagine,(int)(large()*unitaX()),(int)(tall()*unitaY()));
	}
	public boolean lazyLoad()
	{
		return lazyLoad;
	}
	public Oggetto lazyLoad(boolean lazyLoad)
	{
		this.lazyLoad=lazyLoad;
		return this;
	}
	public Runnable invalida=new Runnable()
	{
		public void run()
		{
			invalidate();
		}
	};
	public Oggetto rotazione(double rotazione)
	{
		this.rotazione=rotazione;
		schermo().runOnUiThread(invalida);
		return this;
	}
	public double rotazione()
	{
		return rotazione;
	}
	public double distanza(Oggetto oggetto)
	{
		return distanza(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public double distanzaImprecisa(Oggetto oggetto)
	{
		return distanzaImprecisa(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public int alpha()
	{
		return alpha;
	}
	public Oggetto alpha(int alpha)
	{
		this.alpha=alpha;
		schermo().runOnUiThread(invalida);
		return this;
	}
	public double distanza(double x,double y,double larghezza,double altezza)
	{
		return distanza(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	public double distanzaImprecisa(double x,double y,double larghezza,double altezza)
	{
		return distanzaImprecisa(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	public int immagine()
	{
		return immagine;
	}
	public Oggetto immagine(int immagine)
	{
		if(this.immagine!=immagine)
		{
			this.immagine=immagine;
			if(!lazyLoad)
			{
				if(immagine!=0&&!schermo().esiste(immagine,(int)(large()*unitaX()),(int)(tall()*unitaY())))schermo().carica(this,immagine,(int)(large()*unitaX()),(int)(tall()*unitaY()));
				else schermo().runOnUiThread(invalida);
			}
		}
		return this;
	}
	public Oggetto concatena(int immagine)
	{
		int img=this.immagine;
		immagine(immagine);
		immagine(img);
		concatenazione=immagine;
		riconcatena=true;
		return this;
	}
	public int concatenato()
	{
		return concatenazione;
	}
	public Oggetto concatena2(int immagine)
	{
		int img=this.immagine;
		immagine(immagine);
		immagine(img);
		concatenazione2=immagine;
		riconcatena=true;
		return this;
	}
	public int concatenato2()
	{
		return concatenazione2;
	}
	public Oggetto immagine(Bitmap immagine,int nome)
	{
		if(this.immagine!=nome)
		{
			this.immagine=nome;
			if(!schermo().esiste(nome,(int)(large()*unitaX()),(int)(tall()*unitaY())))schermo().carica(this,immagine,nome,(int)(large()*unitaX()),(int)(tall()*unitaY()));
			else schermo().runOnUiThread(invalida);
		}
		return this;
	}
	public Oggetto colore(int colore)
	{
		this.colore=colore;
		schermo().runOnUiThread(invalida);
		return this;
	}
	public int colore()
	{
		return colore;
	}
	public Oggetto(final Gruppo gruppo,final double x,final double y,final double larghezza,final double altezza)
	{
		super(gruppo.getContext());
		g=gruppo;
		info=new Info(x,y,larghezza,altezza,gruppo.unitaX(),gruppo.unitaY());
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					gruppo.addView(Oggetto.this);
					setLayoutParams(info);
				}
			}
		);
	}
	public double tall()
	{
		return altezza()-y();
	}
	public double large()
	{
		return larghezza()-x();
	}
	public boolean distanzia(Oggetto oggetto,double velocita)
	{
		return distanzia(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),velocita);
	}
	public boolean distanzia(double x,double y,double larghezza,double altezza,double velocita)
	{
		boolean ritorno;
		if(ritorno=toccando(x,y,larghezza,altezza))
		{
			final double centroX=x+(larghezza-x)/2;
			final double centroY=y+(altezza-y)/2;
			if(x()>centroX)x(x()+velocita);
			if(larghezza()<centroX)x(x()-velocita);
			if(y()>centroY)y(y()+velocita);
			if(altezza()<centroY)y(y()-velocita);
		}
		return ritorno;
	}
	public boolean distanziaImpreciso(double x,double y,double larghezza,double altezza,double velocita)
	{
		boolean ritorno;
		if(ritorno=toccandoImpreciso(x,y,larghezza,altezza))
		{
			final double centroX=x+(larghezza-x)/2;
			final double centroY=y+(altezza-y)/2;
			if(x()>centroX)x(x()+velocita);
			if(larghezza()<centroX)x(x()-velocita);
			if(y()>centroY)y(y()+velocita);
			if(altezza()<centroY)y(y()-velocita);
		}
		return ritorno;
	}
	public boolean distanziaBase(double x,double y,double larghezza,double altezza,double velocita)
	{
		boolean ritorno;
		if(ritorno=toccandoBase(x,y,larghezza,altezza))
		{
			final double centroX=x+(larghezza-x)/2;
			final double centroY=y+(altezza-y)/2;
			if(x()>centroX)x(x()+velocita);
			if(larghezza()<centroX)x(x()-velocita);
			if(y()>centroY)y(y()+velocita);
			if(altezza()<centroY)y(y()-velocita);
		}
		return ritorno;
	}
	public boolean distanziaBaseUguale(double x,double y,double larghezza,double altezza,double velocita)
	{
		boolean ritorno;
		if(ritorno=toccandoBaseUguale(x,y,larghezza,altezza))
		{
			final double centroX=x+(larghezza-x)/2;
			final double centroY=y+(altezza-y)/2;
			if(x()>centroX)x(x()+velocita);
			if(larghezza()<centroX)x(x()-velocita);
			if(y()>centroY)y(y()+velocita);
			if(altezza()<centroY)y(y()-velocita);
		}
		return ritorno;
	}
	public boolean toccando(double x,double y,double larghezza,double altezza,double raggio)
	{
		return toccando(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza,raggio);
	}
	public boolean toccando(Oggetto oggetto,double raggio)
	{
		return toccando(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),raggio);
	}
	public boolean toccando(Oggetto oggetto)
	{
		return toccando(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public boolean toccando(double x,double y,double larghezza,double altezza)
	{
		return toccando(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	public boolean toccandoImpreciso(double x,double y,double larghezza,double altezza,double raggio)
	{
		return toccandoImpreciso(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza,raggio);
	}
	public boolean toccandoImpreciso(Oggetto oggetto,double raggio)
	{
		return toccandoImpreciso(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),raggio);
	}
	public boolean toccandoImpreciso(Oggetto oggetto)
	{
		return toccandoImpreciso(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public boolean toccandoImpreciso(double x,double y,double larghezza,double altezza)
	{
		return toccandoImpreciso(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	public boolean toccandoBase(double x,double y,double larghezza,double altezza,double raggio)
	{
		return toccandoBase(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza,raggio);
	}
	public boolean toccandoBase(Oggetto oggetto,double raggio)
	{
		return toccandoBase(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),raggio);
	}
	public boolean toccandoBase(Oggetto oggetto)
	{
		return toccandoBase(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public boolean toccandoBase(double x,double y,double larghezza,double altezza)
	{
		return toccandoBase(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	public boolean toccandoBaseUguale(double x,double y,double larghezza,double altezza,double raggio)
	{
		return toccandoBaseUguale(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza,raggio);
	}
	public boolean toccandoBaseUguale(Oggetto oggetto,double raggio)
	{
		return toccandoBaseUguale(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza(),raggio);
	}
	public boolean toccandoBaseUguale(Oggetto oggetto)
	{
		return toccandoBaseUguale(oggetto.x(),oggetto.y(),oggetto.larghezza(),oggetto.altezza());
	}
	public boolean toccandoBaseUguale(double x,double y,double larghezza,double altezza)
	{
		return toccandoBaseUguale(x(),y(),larghezza(),altezza(),x,y,larghezza,altezza);
	}
	private LightingColorFilter filtro;
	private int filtroColore=Color.TRANSPARENT;
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if(lazyLoad)lazyLoad(immagine());
		schermo().matrix.reset();
		schermo().matrix.postRotate((float)rotazione,canvas.getWidth()/2,canvas.getHeight()/2);
		schermo().paint.reset();
		canvas.concat(schermo().matrix);
		int alpha=Color.alpha(colore);
		if(alpha!=0)
		{
			if(!schermo().esiste(immagine))canvas.drawColor(colore);
			else
			{
				if(filtro==null||filtroColore!=colore)
				{
					int a=255-alpha;
					filtro=new LightingColorFilter(Color.rgb(a,a,a),Color.rgb(Color.red(colore)*alpha/255,Color.green(colore)*alpha/255,Color.blue(colore)*alpha/255));
					filtroColore=colore;
				}
				schermo().paint.setColorFilter(filtro);
			}
		}
		schermo().paint.setAlpha(this.alpha);
		schermo().rect.set(0,0,canvas.getWidth(),canvas.getHeight());
		if(schermo().esiste(immagine))
		{
			Bitmap b=schermo().immagine(immagine);
			if(!b.isRecycled())
			{
				if(schermo().esiste(concatenazione)||schermo().esiste(concatenazione2))
				{
					Bitmap b1=schermo().immagine(concatenazione);
					Bitmap b2=schermo().immagine(concatenazione2);
					if((b1!=null&&!b1.isRecycled())||(b2!=null&&!b2.isRecycled()))
					{
						if(riconcatena)
						{
							if(concatenato==null)
							{
								concatenato=Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_4444);
								concatenatoc=new Canvas(concatenato);
								scorta=new Paint();
								scortar=new Rect(0,0,100,100);
							}
							concatenato.eraseColor(Color.TRANSPARENT);
							concatenatoc.drawBitmap(b,null,scortar,scorta);
							if(b1!=null)concatenatoc.drawBitmap(b1,null,scortar,scorta);
							if(b2!=null)concatenatoc.drawBitmap(b2,null,scortar,scorta);
							riconcatena=false;
						}
						canvas.drawBitmap(concatenato,null,schermo().rect,schermo().paint);
					}
				}
				else
				{
					try
					{
						canvas.drawBitmap(b,null,schermo().rect,schermo().paint);
					}
					catch(RuntimeException e)
					{
						Lista.debug(e);
					}
				}
			}
		}
		else if(schermo().esiste(concatenazione))
		{
			Bitmap b=schermo().immagine(concatenazione);
			if(!b.isRecycled())
			{
				try
				{
					canvas.drawBitmap(b,null,schermo().rect,schermo().paint);
				}
				catch(RuntimeException e)
				{
					Lista.debug(e);
				}
			}
		}
		else if(schermo().esiste(concatenazione2))
		{
			Bitmap b=schermo().immagine(concatenazione2);
			if(!b.isRecycled())
			{
				try
				{
					canvas.drawBitmap(b,null,schermo().rect,schermo().paint);
				}
				catch(RuntimeException e)
				{
					Lista.debug(e);
				}
			}
		}
	}
	public Oggetto y(double y)
	{
		info.altezza=y+info.altezza-info.y;
		info.y=y;
		return this;
	}
	public Oggetto x(double x)
	{
		info.larghezza=x+info.larghezza-info.x;
		info.x=x;
		return this;
	}
	public Oggetto trans(double transX,double transY)
	{
		info.trans(transX,transY);
		return this;
	}
	public double transX()
	{
		return info.transX;
	}
	public double transY()
	{
		return info.transY;
	}
	public Oggetto larghezza(double larghezza)
	{
		info.larghezza=larghezza;
		return this;
	}
	public Oggetto altezza(double altezza)
	{
		info.altezza=altezza;
		return this;
	}
	public Oggetto large(double large)
	{
		info.larghezza=x()+large;
		return this;
	}
	public Oggetto tall(double tall)
	{
		info.altezza=y()+tall;
		return this;
	}
	public Oggetto antiTrans(boolean transX,boolean transY)
	{
		info.antiTrans(transX,transY);
		return this;
	}
	public boolean antiTransX()
	{
		return info.antiTransX;
	}
	public boolean antiTransY()
	{
		return info.antiTransY;
	}
	public double x()
	{
		return info.x;
	}
	public double y()
	{
		return info.y;
	}
	public double larghezza()
	{
		return info.larghezza;
	}
	public double altezza()
	{
		return info.altezza;
	}
	public double unitaX()
	{
		return info.unitaX;
	}
	public double unitaY()
	{
		return info.unitaY;
	}
	public Schermo schermo()
	{
		return Schermo.schermo;
	}
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event)
	{
		return true;
	}
	public double largeMeta()
	{
		return large()/2;
	}
	public double tallMeta()
	{
		return tall()/2;
	}
	public double centroX()
	{
		return x()+largeMeta();
	}
	public double centroY()
	{
		return y()+tallMeta();
	}
	public void largeMeta(double largeMeta)
	{
		large(largeMeta*2);
	}
	public void tallMeta(double tallMeta)
	{
		tall(tallMeta*2);
	}
	public void centroX(double centroX)
	{
		x(centroX-largeMeta());
	}
	public void centroY(double centroY)
	{
		y(centroY-tallMeta());
	}
	public boolean onTouchEvent(MotionEvent event)
	{
		/*g.onTouchEvent(event);
		super.onTouchEvent(event);
		return true;*/
		g.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
