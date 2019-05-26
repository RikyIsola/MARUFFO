package com.island.maruffo;
import android.os.*;
import com.island.*;
import android.graphics.*;
public class Camera extends Finestra
{
	/*public void salva(Bundle b)
	{
		super.salva(b);
		b.putBoolean("CAMERA",true);
		b.putIntArray("CAMERA IMMAGINI",immagini);
		b.putBoolean("CAMERA SPAZIO",spazio);
	}*/
	private int[]immagini;
	private boolean spazio;
	private Gruppo g;
	public Camera(MainActivity m,final int[]immagini,boolean spazio)
	{
		super(m);
		this.immagini=immagini;
		this.spazio=spazio;
		g=new Gruppo(this,10,10);
		int dis;
		if(spazio)dis=8;
		else dis=9;
		for(int a=0;a<immagini.length;a++)new Bottone(g,a*9,1,a*9+dis,9,Color.TRANSPARENT,Color.TRANSPARENT).lazyLoad(true).immagine(immagini[a]);
		g.aggiorna();
	}
	public void onStop()
	{
		for(int a=0;a<immagini.length;a++)schermo().cancella(immagini[a]);
		immagini=null;
		g=null;
		super.onStop();
	}
	public void sempreGrafico()
	{
		g.sempre(0.01);
	}
}
