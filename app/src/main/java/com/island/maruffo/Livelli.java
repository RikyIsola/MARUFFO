package com.island.maruffo;
import android.graphics.*;
import android.os.*;
import android.view.*;
import com.island.*;
public class Livelli extends Finestra
{
	/*public void salva(Bundle b)
	{
		super.salva(b);
		b.putBoolean("LIVELLI",true);
	}*/
	private Testo g1,g2,g3,g4;
	private boolean prec;
	public void onStop()
	{
		g1=null;
		g2=null;
		g3=null;
		g4=null;
		schermo().cancella(R.drawable.sfondolivelli);
		schermo().cancella(R.drawable.scatolaarchitetti);
		schermo().cancella(R.drawable.scatolaastrati);
		schermo().cancella(R.drawable.scatolamedioevo);
		schermo().cancella(R.drawable.scatolaspazio);
		super.onStop();
	}
	Livelli(Schermo s)
	{
		super(s);
		Gruppo g=new Gruppo(this,20,20).immagine(R.drawable.sfondolivelli);
		new Bottone(g,1,3.5,9,11.5,Color.TRANSPARENT,Color.TRANSPARENT).larghezzaX(0.5).carattere(0.5).immagine(R.drawable.scatolamedioevo).setOnClickListener(click(0));
		g1=new Testo(g,1,11,9,12,"MIGLIORE: "+Memoria.leggi(schermo(),"MEDIOEVO",0));
		new Bottone(g,11,3.5,19,11.5,Color.TRANSPARENT,Color.TRANSPARENT).larghezzaX(0.5).carattere(0.5).immagine(R.drawable.scatolaspazio).setOnClickListener(click(1));
		g2=new Testo(g,11,11,19,12,"MIGLIORE: "+Memoria.leggi(schermo(),"SPAZIO",0));
		new Bottone(g,1,11,9,19,Color.TRANSPARENT,Color.TRANSPARENT).larghezzaX(0.5).carattere(0.5).immagine(R.drawable.scatolaarchitetti).setOnClickListener(click(2));
		g3=new Testo(g,1,18,9,19,"MIGLIORE: "+Memoria.leggi(schermo(),"ARCHITETTI",0));
		new Bottone(g,11,11,19,19,Color.TRANSPARENT,Color.TRANSPARENT).larghezzaX(0.5).carattere(0.5).immagine(R.drawable.scatolaastrati).setOnClickListener(click(3));
		g4=new Testo(g,11,18,19,19,"MIGLIORE: "+Memoria.leggi(schermo(),"ASTRATI",0));
		g.aggiorna();
	}
	private View.OnClickListener click(final int n)
	{
		return new View.OnClickListener()
		{
			public void onClick(View p1)
			{
				if(libero())
				{
					if(n==0)new Medioevo(schermo());
					else if(n==1)new Spazio(schermo());
					else if(n==2)new Architetti(schermo());
					else new Astrati(schermo());
				}
			}
		};
	}
	public void aggiorna()
	{
		g1.scrivi("MIGLIORE: "+Memoria.leggi(schermo(),"MEDIOEVO",0));
		g2.scrivi("MIGLIORE: "+Memoria.leggi(schermo(),"SPAZIO",0));
		g3.scrivi("MIGLIORE: "+Memoria.leggi(schermo(),"ARCHITETTI",0));
		g4.scrivi("MIGLIORE: "+Memoria.leggi(schermo(),"ASTRATI",0));
	}
	public void sempreGrafico()
	{
		boolean libero=libero();
		if(libero!=prec)
		{
			if(!prec)aggiorna();
			prec=libero;
		}
	}
}
