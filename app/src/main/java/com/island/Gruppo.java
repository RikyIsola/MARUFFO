package com.island;
import android.graphics.*;
import android.view.*;
import android.view.accessibility.*;
import android.graphics.drawable.*;
public class Gruppo extends ViewGroup
{
	private double precedenteX,precedenteY,defaultX,defaultY,piuX,piuY,transX,transY,unitaX,unitaY,maxX,maxY,schermoX,schermoY,velocitaX,velocitaY,menoX,menoY;
	private boolean antiScrollX,antiScrollY,toccato,antiPiu;
	private Gruppo gruppo;
	private Info info;
	Oggetto sfondo;
	private Runnable rimuoviSfondo=new Runnable()
	{
		public void run()
		{
			removeView(sfondo);
		}
	};
	private Runnable fronte=new Runnable()
	{
		public void run()
		{
			for(int a=0;a<getChildCount();a++)
			{
				View v=getChildAt(0);
				if(v!=sfondo)v.bringToFront();
			}
		}
	};
	public void removeAllViews()
	{
		super.removeAllViews();
		if(sfondo!=null&&gruppo!=null)addView(sfondo);
	}
	public Gruppo immagine(int immagine)
	{
		if(sfondo==null)
		{
			sfondo=new Oggetto(this,0,0,maxX(),maxY()).antiTrans(true,true);
			schermo().runOnUiThread(fronte);
			if(gruppo==null)
			{
				schermo().runOnUiThread(rimuoviSfondo);
			}
		}
		sfondo.immagine(immagine);
		return this;
	}
	public Gruppo immagine(Bitmap immagine,int nome)
	{
		if(sfondo==null)
		{
			sfondo=new Oggetto(this,0,0,maxX(),maxY()).antiTrans(true,true);
			schermo().runOnUiThread(fronte);
			if(gruppo==null)
			{
				schermo().runOnUiThread(rimuoviSfondo);
			}
		}
		sfondo.immagine(immagine,nome);
		return this;
	}
	public int immagine()
	{
		return sfondo.immagine();
	}
	public Gruppo rotazione(double rotazione)
	{
		sfondo.rotazione(rotazione);
		return this;
	}
	public double rotazione()
	{
		return sfondo.rotazione();
	}
	public Gruppo colore(int colore)
	{
		if(sfondo==null)
		{
			sfondo=new Oggetto(this,0,0,maxX(),maxY()).antiTrans(true,true);
			schermo().runOnUiThread(fronte);
			if(gruppo==null)
			{
				schermo().runOnUiThread(rimuoviSfondo);
			}
		}
		sfondo.colore(colore);
		return this;
	}
	public int colore()
	{
		return sfondo.colore();
	}
	public Gruppo alpha(int alpha)
	{
		sfondo.alpha(alpha);
		return this;
	}
	public int alpha()
	{
		return sfondo.alpha();
	}
	public Gruppo(final Gruppo gruppo,final double x,final double y,final double larghezza,final double altezza,double caselleX,double caselleY)
	{
		super(gruppo.getContext());
		this.gruppo=gruppo;
		maxX=caselleX;
		maxY=caselleY;
		info=new Info(x,y,larghezza,altezza,gruppo.unitaX(),gruppo.unitaY());
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					setLayoutParams(info);
					gruppo.addView(Gruppo.this);
				}
			}
		);
		ripristino();
	}
	public Gruppo(final Schermo schermo,double caselleX,double caselleY)
	{
		super(schermo);
		maxX=caselleX;
		maxY=caselleY;
		info=new Info(0,0,1,1,schermoX,schermoY);
		schermo.runOnUiThread(new Runnable()
			{
				public void run()
				{
					setLayoutParams(info);
					schermo.setContentView(Gruppo.this);
				}
			}
		);
		ripristino();
	}
	void ripristino()
	{
		if(gruppo==null)
		{
			schermoX=Schermo.schermo.dimensioni()[0];
			schermoY=Schermo.schermo.dimensioni()[1];
		}
		else
		{
			schermoX=gruppo.unitaX*(larghezza()-x());
			schermoY=gruppo.unitaY*(altezza()-y());
		}
		unitaX=schermoX/maxX;
		unitaY=schermoY/maxY;
		
	}
	public Gruppo(final Finestra finestra,double caselleX,double caselleY)
	{
		super(finestra.getContext());
		maxX=caselleX;
		maxY=caselleY;
		info=new Info(0,0,1,1,schermoX,schermoY);
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					setLayoutParams(info);
					finestra.setContentView(Gruppo.this);
				}
			}
		);
		ripristino();
	}
	protected void onLayout(boolean change,int l,int t,int r,int d){}
	private Runnable aggiorna=new Runnable()
	{
		public void run()
		{
			if(gruppo==null)
			{
				schermoX=Schermo.schermo.dimensioni()[0];
				schermoY=Schermo.schermo.dimensioni()[1];
			}
			else
			{
				schermoX=gruppo.unitaX*(larghezza()-x());
				schermoY=gruppo.unitaY*(altezza()-y());
			}
			info.unitaX=schermoX;
			info.unitaY=schermoY;
			unitaX=schermoX/maxX;
			unitaY=schermoY/maxY;
			if(!antiPiu)
			{
				piuX=defaultX-maxX;
				piuY=defaultY-maxY;
			}
			for(int a=0;a<getChildCount();a++)if(getChildAt(a).getVisibility()==View.VISIBLE)
				{
					final View oggetto=getChildAt(a);
					final Info info=(Info)oggetto.getLayoutParams();
					double x,y,altezza,larghezza;
					if(!info.antiTransX)x=-transX+info.transX*unitaX;
					else x=0;
					if(!info.antiTransY)y=-transY+info.transY*unitaY;
					else y=0;
					larghezza=info.larghezza*unitaX+x;
					altezza=info.altezza*unitaY+y;
					x=info.x*unitaX+x;
					y=info.y*unitaY+y;
					if(larghezza>0&&altezza>0&&x<maxX*unitaX&&y<maxY*unitaY)oggetto.layout((int)x,(int)y,(int)larghezza,(int)altezza);
					else oggetto.layout(0,0,0,0);
					info.schermo(unitaX,unitaY);
					if(!antiPiu)
					{
						if(info.larghezza-maxX()>piuX)piuX=info.larghezza-maxX();
						if(info.altezza-maxY()>piuY)piuY=info.altezza-maxY();
					}
					if(getChildAt(a)instanceof Gruppo)((Gruppo)getChildAt(a)).aggiorna();
				}
			if(!antiScrollX)
			{
				if(piuX<menoX*unitaX&&!antiPiu)piuX=menoX*unitaX;
				if(transX>piuX*unitaX)transX=piuX*unitaX;
				if(transX<menoX*unitaX)transX=menoX*unitaX;
			}
			if(!antiScrollY)
			{
				if(piuY<menoY*unitaY&&!antiPiu)piuY=menoY*unitaY;
				if(transY>piuY*unitaY)transY=piuY*unitaY;
				if(transY<menoY*unitaY)transY=menoY*unitaY;
			}
		}
	};
	public Gruppo aggiorna()
	{
		schermo().runOnUiThread(aggiorna);
		return this;
	}
	public Gruppo migra(final Gruppo gruppo)
	{
		if(gruppo!=null)
		{
			info.x=x();
			info.y=y();
			info.larghezza=larghezza();
			info.altezza=altezza();
			info.unitaX=gruppo.unitaX;
			info.unitaY=gruppo.unitaY;
			final Gruppo g=this.gruppo;
			schermo().runOnUiThread(new Runnable()
				{
					public void run()
					{
						g.removeView(Gruppo.this);
						gruppo.addView(Gruppo.this);
					}
				}
			);
			this.gruppo=gruppo;
			schermoX=gruppo.unitaX*(larghezza()-x());
			schermoY=gruppo.unitaY*(altezza()-y());
			unitaX=gruppo.unitaX*(larghezza()-x())/maxX;
			unitaY=gruppo.unitaY*(altezza()-y())/maxY;
		}
		return this;
	}
	public Gruppo large(double large)
	{
		info.larghezza=x()+large;
		return this;
	}
	public Gruppo tall(double tall)
	{
		info.altezza=y()+tall;
		return this;
	}
	public Gruppo max(double maxX,double maxY)
	{
		if(sfondo!=null)sfondo.larghezza(maxX).altezza(maxY);
		piuX+=this.maxX-maxX;
		piuY+=this.maxY-maxY;
		double transX=transX();
		double transY=transY();
		this.maxX=maxX;
		this.maxY=maxY;
		trans(transX,transY);
		return this;
	}
	public double maxX()
	{
		return maxX;
	}
	public double maxY()
	{
		return maxY;
	}
	public double unitaX()
	{
		return unitaX;
	}
	public double unitaY()
	{
		return unitaY;
	}
	public double transX()
	{
		return transX/unitaX;
	}
	public double transY()
	{
		return transY/unitaY;
	}
	public Gruppo limiti(double defaultX,double defaultY)
	{
		this.defaultX=defaultX;
		this.defaultY=defaultY;
		return this;
	}
	public Gruppo antiScroll(boolean antiScrollX,boolean antiScrollY)
	{
		this.antiScrollX=antiScrollX;
		this.antiScrollY=antiScrollY;
		return this;
	}
	public Gruppo trans(double transX,double transY)
	{
		this.transX=transX*unitaX;
		this.transY=transY*unitaY;
		return this;
	}
	public Gruppo y(double y)
	{
		info.altezza=y+info.altezza-info.y;
		info.y=y;
		return this;
	}
	public Gruppo x(double x)
	{
		info.larghezza=x+info.larghezza-info.x;
		info.x=x;
		return this;
	}
	public Gruppo larghezza(double larghezza)
	{
		info.larghezza=larghezza;
		schermoX=gruppo.unitaX*(larghezza-x());
		unitaX=gruppo.unitaX*(larghezza-x())/maxX;
		aggiorna();
		return this;
	}
	public Gruppo altezza(double altezza)
	{
		info.altezza=altezza;
		schermoY=gruppo.unitaY*(altezza-y());
		unitaY=gruppo.unitaY*(altezza-y())/maxY;
		aggiorna();
		return this;
	}
	public Gruppo antiTrans(boolean transX,boolean transY)
	{
		info.antiTrans(transX,transY);
		return this;
	}
	public double tall()
	{
		return altezza()-y();
	}
	public double large()
	{
		return larghezza()-x();
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
	public boolean onTouchEvent(MotionEvent event)
	{
		onTouchEvent(this,event);
		if(gruppo!=null)gruppo.onTouchEvent(event);
		return true;
	}
	public static void onTouchEvent(Gruppo gruppo,MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			gruppo.precedenteX=event.getX();
			gruppo.precedenteY=event.getY();
			gruppo.toccato=true;
		}
		else if(event.getAction()==MotionEvent.ACTION_MOVE)
		{
			gruppo.scrolla(event.getX(),event.getY());
		}
		else if(event.getAction()==MotionEvent.ACTION_UP)gruppo.toccato=false;
	}
	public Schermo schermo()
	{
		return Schermo.schermo;
	}
	private void scrolla(double x,double y)
	{
		velocitaX=precedenteX-x;
		velocitaY=precedenteY-y;
		if(!antiScrollX)transX+=velocitaX;
		if(!antiScrollY)transY+=velocitaY;
		precedenteX=x;
		precedenteY=y;
		if(transX>piuX*unitaX&&!antiScrollX)transX=piuX*unitaX;
		if(transY>piuY*unitaY&&!antiScrollY)transY=piuY*unitaY;
		if(transX<menoX*unitaX&&!antiScrollX)transX=menoX*unitaX;
		if(transY<menoY*unitaY&&!antiScrollY)transY=menoY*unitaY;
		aggiorna();
	}
	public Gruppo meno(double x,double y)
	{
		menoX=x;
		menoY=y;
		return this;
	}
	public Gruppo piu(double x,double y)
	{
		piuX=x;
		piuY=y;
		return antiPiu(true);
	}
	public double piuX()
	{
		return piuX;
	}
	public double piuY()
	{
		return piuY;
	}
	public double menoX()
	{
		return menoX;
	}
	public double menoY()
	{
		return menoY;
	}
	public Gruppo antiPiu(boolean antiPiu)
	{
		this.antiPiu=antiPiu;
		return this;
	}
	public void sempre(double decelerazione)
	{
		if(!toccato)
		{
			final int piuX,piuY;
			double molt=decelerazione;
			double soglia=molt/2;
			if(velocitaX<-soglia*unitaX)piuX=1;
			else if(velocitaX>soglia*unitaX)piuX=-1;
			else piuX=0;
			if(velocitaY<-soglia*unitaY)piuY=1;
			else if(velocitaY>soglia*unitaY)piuY=-1;
			else piuY=0;
			if(piuX!=0||piuY!=0)scrolla(precedenteX-(velocitaX+piuX*molt*unitaX),precedenteY-(velocitaY+piuY*molt*unitaY));
		}
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
}
