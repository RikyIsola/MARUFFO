package com.island.maruffo;
import android.graphics.*;
import android.view.*;
import com.island.*;
import java.util.*;
import android.os.*;
public class Astrati extends Finestra
{
	/*public void salva(Bundle b)
	{
		super.salva(b);
		b.putBoolean("ASTRATI",true);
	}*/
	public void onStop()
	{
		schermo().cancella(R.drawable.giocoastrati);
		schermo().cancella(R.drawable.carrello);
		schermo().cancella(R.drawable.testa);
		schermo().cancella(R.drawable.vaso);
		sottofondo.rilascia();
		super.onStop();
	}
	private Oggetto razzo,razzoinv;
	private Gruppo g;
	private double x=320;
	private boolean toccando,gioco;
	private Lista<Oggetto>oggetti=new Lista<Oggetto>();
	private Lista<Point>punti=new Lista<Point>();
	private int aumento,punteggio,vecchioX,max;
	private int difficolta=50;
	private Testo testoPunteggio,ram,velocita,frame,velocitaSuono,migliore;
	private Suono sottofondo;
	private Random r=new Random();
	Astrati(Schermo s)
	{
		super(s);
		g=new Gruppo(this,640,1140).immagine(R.drawable.giocoastrati);
		g.setOnTouchListener(touch);
		razzo=new Oggetto(g,268,1026,372,1140).immagine(R.drawable.carrello);
		razzoinv=new Oggetto(g,288,1026,352,1140);
		razzoinv.setOnTouchListener(comandi);
		testoPunteggio=new Testo(g,50,50,170,100,"PUNTI:0",Color.WHITE,Color.BLACK);
		migliore=new Testo(g,50,100,170,150,"MAX:"+(max=Memoria.leggi(schermo(),"ASTRATI",0)),Color.WHITE,Color.BLACK);
		if(debug())
		{
			ram=new Testo(g,400,0,640,50,"RAM:"+ram());
			velocita=new Testo(g,250,0,390,50,"VEL:"+velocita());
			frame=new Testo(g,250,50,390,100,"FPS:"+frame());
			velocitaSuono=new Testo(g,400,50,640,100,"SUONO:"+velocitaSuono());
		}
		g.aggiorna();
		sottofondo=new Suono(schermo(),R.raw.astrati).infinito(true).start();
		vecchioX=r.nextInt((int)g.maxX());
		gioco=true;
	}
	public void sempre()
	{
		if(gioco)
		{
			if(aumento<20)aumento++;
			else
			{
				difficolta--;
				aumento=0;
			}
			if(razzo.getVisibility()==View.VISIBLE)
			{
				if(x+10<razzo.centroX())razzo.centroX(razzo.centroX()-20);
				else if(x-10>razzo.centroX())razzo.centroX(razzo.centroX()+20);
				if(!toccando)razzoinv.x(razzo.x());
			}
			if(difficolta>0)
			{
				if(r.nextInt(difficolta)==0)
				{
					oggetti.add(new Oggetto(g,355,139,411,204).immagine(immagine()));
					int n=r.nextInt(300);
					if((vecchioX+n>g.maxX()||r.nextBoolean())&&vecchioX-n>0)n=-n;
					int x=vecchioX+n;
					vecchioX=x;
					if(x<0)x=0;
					else if(x>g.maxX())x=(int)g.maxX();
					punti.add(new Point(x,0));
				}
			}
			else if(difficolta==-3)
			{
				oggetti.add(new Oggetto(g,0,-640,640,0).immagine(immagine()));
				punti.add(new Point((int)g.maxX()/2,-500));
				difficolta--;
			}
			for(int a=0;a<oggetti.size();a++)
			{
				if(punti.get(a).y==1)
				{
					final Oggetto o=oggetti.get(a);
					oggetti.remove(a);
					punti.remove(a);
					schermo().runOnUiThread(new Runnable()
						{
							public void run()
							{
								g.removeView(o);
							}
						});
				}
				else if(punti.get(a).y>1)
				{
					punti.get(a).y-=1;
				}
				else
				{
					if(razzo.toccando(oggetti.get(a)))
					{
						punteggio++;
						testoPunteggio.scrivi("PUNTI:"+String.valueOf(punteggio));
						if(punteggio>max)
						{
							max=punteggio;
							migliore.scrivi("MAX:"+max);
							Memoria.salva(schermo(),"ASTRATI",max);
						}
						punti.get(a).y=1;
					}
					int velocita;
					if(punti.get(a).y==0)velocita=10;
					else velocita=1;
					if(punti.get(a).x+10<oggetti.get(a).centroX())oggetti.get(a).x(oggetti.get(a).x()-20);
					else if(punti.get(a).x-10>oggetti.get(a).centroX())oggetti.get(a).x(oggetti.get(a).x()+20);
					if(oggetti.get(a).altezza()<g.maxY())oggetti.get(a).y(oggetti.get(a).y()+velocita);
					else
					{
						gioco=false;
						razzo.immagine(R.drawable.esplosione);
						new Suono(schermo(),R.raw.fireball).start();
					}
				}
			}
		}
	}
	public void sempreGrafico()
	{
		if(debug())
		{
			ram.scrivi("RAM:"+ram()+"/"+ramGiusta());
			frame.scrivi("FPS:"+frame());
			velocita.scrivi("VEL:"+velocita());
			velocitaSuono.scrivi("SUONO:"+velocitaSuono());
		}
		g.aggiorna();
	}
	private View.OnTouchListener touch=new View.OnTouchListener()
	{
		public boolean onTouch(View p1,MotionEvent p2)
		{
			if(p2.getAction()==MotionEvent.ACTION_DOWN&&razzo.getVisibility()==View.VISIBLE&&!gioco)cancel();
			if(p2.getAction()==MotionEvent.ACTION_MOVE)x=p2.getX()/g.unitaX();
			return true;
		}
	};
	private View.OnTouchListener comandi=new View.OnTouchListener()
	{
		public boolean onTouch(View p1,MotionEvent evento)
		{
			Oggetto o=(Oggetto)p1;
			if(evento.getAction()==MotionEvent.ACTION_DOWN)toccando=true;
			if(evento.getAction()==MotionEvent.ACTION_MOVE)
			{
				x=evento.getX()/g.unitaX()+o.x();
			}
			else if(evento.getAction()==MotionEvent.ACTION_UP)
			{
				if(evento.getX()<o.large()*o.unitaX()&&evento.getY()<o.tall()*o.unitaY()&&evento.getX()>0&&evento.getY()>0)o.performClick();
				toccando=false;
			}
			return true;
		}
	};
	private int immagine()
	{
		int n=r.nextInt(4);
		if(n==0)return R.drawable.vaso;
		else if(n==1)return R.drawable.testa;
		else if(n==2)return R.drawable.astrati;
		else return R.drawable.ancora;
	}
}
