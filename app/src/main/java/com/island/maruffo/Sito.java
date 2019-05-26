package com.island.maruffo;
import android.os.*;
import com.island.*;
import android.graphics.*;
public class Sito extends Finestra
{
	/*public void salva(Bundle b)
	{
		super.salva(b);
		b.putBoolean("SITO",true);
		b.putString("SITO URL",url);
	}*/
	private Web w;
	private String url;
	private Oggetto o;
	private int img,animazionetempo;
	Sito(Schermo schermo,String sito)
	{
		super(schermo);
		Gruppo g=new Gruppo(this,1,1);
		g.colore(Color.WHITE);
		o=new Oggetto(g,0.4,0.4,0.6,0.6).immagine(R.drawable.architetto1);
		url=sito;
		w=new Web(g,0,0,1,1,sito);
		g.aggiorna();
	}
	public void onStop()
	{
		w.distruggi();
		w=null;
		url=null;
		o=null;
		schermo().cancella(R.drawable.architetto1);
		schermo().cancella(R.drawable.architetto2);
		schermo().cancella(R.drawable.architetto3);
		schermo().cancella(R.drawable.architetto4);
		super.onStop();
	}
	public void pausa()
	{
		w.pausa();
	}
	public void riprendi()
	{
		w.riprendi();
	}
	public void sempre()
	{
		if(animazionetempo<=0)
		{
			img++;
			if(img>4)img=0;
			int immagine;
			if(img==0)immagine=R.drawable.architetto1;
			else if(img==1)immagine=R.drawable.architetto2;
			else if(img==2)immagine=R.drawable.architetto3;
			else immagine=R.drawable.architetto4;
			o.immagine(immagine);
			animazionetempo=2;
		}
		else animazionetempo--;
	}
}
