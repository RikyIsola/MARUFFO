package com.island.maruffo;
import android.graphics.*;
import android.os.*;
import android.view.*;
import com.island.*;
import java.util.*;
import android.widget.*;
public class Architetti extends Finestra
{
	/*public void salva(Bundle b)
	{
		super.salva(b);
		b.putBoolean("ARCHITETTI",true);
	}*/
	public void onStop()
	{
		schermo().cancella(R.drawable.giocoarchitetti);
		schermo().cancella(R.drawable.architetto1);
		schermo().cancella(R.drawable.architetto2);
		schermo().cancella(R.drawable.architetto3);
		schermo().cancella(R.drawable.architetto4);
		schermo().cancella(R.drawable.squadre);
		schermo().cancella(R.drawable.progetto);
		schermo().cancella(R.drawable.matita);
		schermo().cancella(R.drawable.moneta);
		suono.rilascia();
		super.onStop();
	}
	private Gruppo g;
	private Oggetto uomo;
	private double vel;
	private Lista<Oggetto>ostacoli=new Lista<Oggetto>();
	private Random r=new Random();
	private int difficolta=20;
	private int separazione,animazione,animazionetempo,delay,max;
	private boolean gioco;
	private Suono suono;
	private int punti=0;
	private Testo punteggio,migliore;
	Architetti(Schermo s)
	{
		super(s);
		g=new Gruppo(this,100,50).immagine(R.drawable.giocoarchitetti);
		g.setOnTouchListener(touch);
		uomo=new Oggetto(g,20,g.maxY()-6,30,g.maxY()-1).immagine(R.drawable.architetto1);
		suono=new Suono(s,R.raw.architetti).infinito(true).start();
		punteggio=new Testo(g,10,5,30,8,"PUNTI:0",Color.WHITE,Color.BLACK).carattere(1.5).larghezzaX(1.2);
		migliore=new Testo(g,10,8,30,11,"MAX:"+(max=Memoria.leggi(schermo(),"ARCHITETTI",0)),Color.WHITE,Color.BLACK).carattere(1.5).larghezzaX(1.2);
		gioco=true;
	}
	public void sempre()
	{
		if(gioco)
		{
			if(delay==20)
			{
				if(difficolta>1)difficolta--;
				delay=0;
			}
			else delay++;
			if(animazionetempo<=0)
			{
				animazione++;
				if(animazione>3)animazione=0;
				final int immagine;
				if(animazione==0)immagine=R.drawable.architetto1;
				else if(animazione==1)immagine=R.drawable.architetto2;
				else if(animazione==2)immagine=R.drawable.architetto3;
				else immagine=R.drawable.architetto4;
				uomo.immagine(immagine);
				animazionetempo=2;
			}
			else animazionetempo--;
			uomo.y(uomo.y()+vel);
			if(vel!=0)vel+=0.026;
			if(uomo.altezza()>g.maxY()-1)
			{
				uomo.y(g.maxY()-1-uomo.tall());
				vel=0;
			}
			if(separazione==0)
			{
				if(r.nextInt(difficolta)==0)
				{
					ostacoli.add(new Oggetto(g,g.maxX(),g.maxY()-4,g.maxX()+13,g.maxY()).immagine(immagine()));
					separazione=50;
				}
			}
			else separazione--;
			for(final Oggetto o:ostacoli)
			{
				o.x(o.x()-1);
				if(o.x()==50.0)if(r.nextBoolean())o.y(g.maxY()-6);
				if(o.altezza()<g.maxY())o.y(o.y()-0.5);
				if(o.larghezza()<0)
				{
					schermo().runOnUiThread(new Runnable()
					{
						public void run()
						{
							if(o.immagine()!=R.drawable.moneta)punti++;
							if(punteggio!=null)punteggio.scrivi("PUNTI:"+String.valueOf(punti));
							if(punti>max)
							{
								max=punti;
								migliore.scrivi("MAX:"+max);
								Memoria.salva(schermo(),"ARCHITETTI",max);
							}
							g.removeView(o);
						}
					});
					ostacoli=Lista.rimuovi(ostacoli,o);
				}
				if(o.toccando(uomo))
				{
					if(o.immagine()==R.drawable.moneta)
					{
						schermo().runOnUiThread(new Runnable()
							{
								public void run()
								{
									punti+=3;
									if(punteggio!=null)punteggio.scrivi("PUNTI:"+String.valueOf(punti));
									if(punti>max)
									{
										max=punti;
										migliore.scrivi("MAX:"+max);
										Memoria.salva(schermo(),"ARCHITETTI",max);
									}
									g.removeView(o);
								}
							});
						ostacoli=Lista.rimuovi(ostacoli,o);
					}
					else gioco=false;
				}
			}
		}
	}
	public void sempreGrafico()
	{
		g.aggiorna();
	}
	private View.OnTouchListener touch=new View.OnTouchListener()
	{
		public boolean onTouch(View p1,MotionEvent p2)
		{
			if(p2.getAction()==MotionEvent.ACTION_DOWN)
			{
				if(gioco)
				{
					if(vel==0)vel=-0.62;
				}
				else cancel();
			}
			return true;
		}
	};
	public int immagine()
	{
		int n=r.nextInt(5);
		if(n==0)return R.drawable.squadre;
		else if(n==1)return R.drawable.matita;
		else if(n==2)return R.drawable.pipozu;
		else if(n==3)return R.drawable.progetto;
		else return R.drawable.moneta;
	}
}
