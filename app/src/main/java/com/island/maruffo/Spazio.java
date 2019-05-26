package com.island.maruffo;
import android.graphics.*;
import android.os.*;
import android.view.*;
import com.island.*;
import java.util.*;
public class Spazio extends Finestra
{
	/*public void salva(Bundle b)
	{
		super.salva(b);
		b.putBoolean("SPAZIO",true);
	}*/
	public void onStop()
	{
		schermo().cancella(R.drawable.gioco);
		schermo().cancella(R.drawable.razzo);
		schermo().cancella(R.drawable.laser);
		schermo().cancella(R.drawable.nemico);
		sottofondo.rilascia();
		super.onStop();
	}
	private Oggetto razzo,razzoinv;
	private Gruppo g;
	private double x=320;
	private boolean toccando,gioco;
	private Lista<Oggetto>oggetti=new Lista<Oggetto>();
	private Lista<Oggetto>laser=new Lista<Oggetto>();
	private Lista<Point>punti=new Lista<Point>();
	private int colpi,aumento,punteggio,max;
	private int difficolta=50;
	private double ricarica,delay;
	private Testo testoPunteggio,ram,velocita,frame,velocitaSuono,migliore;
	private Suono sottofondo;
	private Random r=new Random();
	Spazio(Schermo s)
	{
		super(s);
		g=new Gruppo(this,640,1140).immagine(R.drawable.giocospazio);
		g.setOnTouchListener(touch);
		razzo=new Oggetto(g,288,1026,352,1140).immagine(R.drawable.razzovero);
		razzoinv=new Oggetto(g,288,1026,352,1140);
		razzoinv.setOnClickListener(spara);
		razzoinv.setOnTouchListener(comandi);
		testoPunteggio=new Testo(g,50,50,170,100,"PUNTI:0",Color.WHITE,Color.BLACK);
		migliore=new Testo(g,50,100,170,150,"MAX:"+(max=Memoria.leggi(schermo(),"SPAZIO",0)),Color.WHITE,Color.BLACK);
		if(debug())
		{
			ram=new Testo(g,400,0,640,50,"RAM:"+ram());
			velocita=new Testo(g,250,0,390,50,"VEL:"+velocita());
			frame=new Testo(g,250,50,390,100,"FPS:"+frame());
			velocitaSuono=new Testo(g,400,50,640,100,"SUONO:"+velocitaSuono());
		}
		g.aggiorna();
		sottofondo=new Suono(schermo(),R.raw.battle_1).infinito(true).start();
		gioco=true;
	}
	private View.OnClickListener spara=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			if(ricarica<=0)
			{
				colpi=5;
				ricarica=3;
				delay=0;
			}
		}
	};
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
				ricarica-=0.05;
				if(colpi>0)
				{
					delay+=0.05;
					if(delay>=0.2)
					{
						final Oggetto l=new Oggetto(g,razzo.x()+22,razzo.y()+5,razzo.larghezza()-22,razzo.altezza()-5).immagine(R.drawable.laser);
						laser=Lista.aggiungi(laser,l);
						new Suono(schermo(),R.raw.torpedo).start();
						l.rotazione(0.0001);
						colpi-=1;
						delay=0;
					}
				}
			}
			if(difficolta>0)
			{
				if(r.nextInt(difficolta)==0)
				{
					oggetti=Lista.aggiungi(oggetti,new Oggetto(g,361,152,400,193).immagine(R.drawable.nemico));
					punti=Lista.aggiungi(punti,new Point(r.nextInt((int)g.maxX()),0));
				}
			}
			else if(difficolta==-3)
			{
				oggetti=Lista.aggiungi(oggetti,new Oggetto(g,0,-640,640,0).immagine(R.drawable.nemico));
				punti=Lista.aggiungi(punti,new Point((int)g.maxX()/2,-800));
				difficolta--;
			}
			for(int a=0;a<oggetti.size();a++)
			{
				if(punti.get(a).y==1)
				{
					final Oggetto o=oggetti.get(a);
					oggetti=Lista.<Oggetto>rimuovi(oggetti,a);
					punti=Lista.<Point>rimuovi(punti,a);
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
					if(difficolta>0&&r.nextInt(difficolta*50)==0)
					{
						new Suono(schermo(),R.raw.torpedo).start();
						final Oggetto l=new Oggetto(g,oggetti.get(a).x()+10,oggetti.get(a).y(),oggetti.get(a).larghezza()-10,oggetti.get(a).altezza()).immagine(R.drawable.laser);
						l.rotazione(180);
						laser=Lista.aggiungi(laser,l);
					}
					if(razzo.toccando(oggetti.get(a)))
					{
						gioco=false;
						razzo.immagine(R.drawable.esplosione);
						new Suono(schermo(),R.raw.fireball).start();
					}
					int velocita;
					if(punti.get(a).y==0)velocita=10;
					else velocita=1;
					if(punti.get(a).x+10<oggetti.get(a).centroX())oggetti.get(a).x(oggetti.get(a).x()-20);
					else if(punti.get(a).x-10>oggetti.get(a).centroX())oggetti.get(a).x(oggetti.get(a).x()+20);
					if(oggetti.get(a).altezza()<g.maxY())oggetti.get(a).y(oggetti.get(a).y()+velocita);
					else punti.get(a).y=1;
				}
			}
			for(final Oggetto o:laser)
			{
				boolean distruggi=false;
				if(o.rotazione()==0.0001)
				{
					if(o.y()>0)o.y(o.y()-20);
					else distruggi=true;
					for(int a=0;a<oggetti.size();a++)if(punti.get(a).y<=0&&o.toccando(oggetti.get(a)))
						{
							punteggio+=1;
							testoPunteggio.scrivi("PUNTI:"+punteggio);
							if(punteggio>max)
							{
								max=punteggio;
								migliore.scrivi("MAX:"+max);
								Memoria.salva(schermo(),"SPAZIO",max);
							}
							punti.get(a).y+=20;
							if(punti.get(a).y==20)oggetti.get(a).immagine(R.drawable.esplosione);
							distruggi=true;
							new Suono(schermo(),R.raw.fireball).start();
						}
					for(Oggetto o1:laser)if(o1.rotazione()==180&&o.toccando(o1))
					{
						distruggi=true;
						o1.rotazione(179);
					}
				}
				else if(o.rotazione()==180)
				{
					if(o.altezza()<g.maxY())o.y(o.y()+20);
					else distruggi=true;
					if(o.toccando(razzo))
					{
						gioco=false;
						razzo.immagine(R.drawable.esplosione);
						distruggi=true;
						new Suono(schermo(),R.raw.fireball).start();
					}
				}
				else if(o.rotazione()==179)distruggi=true;
				if(distruggi)
				{
					schermo().runOnUiThread(new Runnable()
						{
							public void run()
							{
								if(g!=null)g.removeView(o);
							}
						});
					laser=Lista.rimuovi(laser,o);
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
			if(p2.getAction()==MotionEvent.ACTION_DOWN&&razzo.getVisibility()==View.VISIBLE&&!gioco)
			{
				/*razzo.immagine(R.drawable.razzo);
				 razzo.setVisibility(View.INVISIBLE);
				 titolo.setVisibility(View.VISIBLE);
				 videocamera.setVisibility(View.VISIBLE);
				 astra.setVisibility(View.VISIBLE);
				 pipo.setVisibility(View.VISIBLE);
				 razzoinv.setVisibility(View.INVISIBLE);
				 testoPunteggio.setVisibility(View.INVISIBLE);
				 sottofondo=sottofondo.rilascia();
				 g.immagine(R.drawable.torre);
				 for(Oggetto oggetto:oggetti)g.removeView(oggetto);
				 for(Oggetto oggetto:laser)g.removeView(oggetto);
				 oggetti=new Oggetto[0];
				 laser=new Oggetto[0];
				 punti=new Point[0];
				 colpi=0;
				 aumento=0;
				 punteggio=0;
				 delay=0;
				 difficolta=50;
				 ricarica=0;
				 g.aggiorna();*/
				cancel();
			}
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
}
