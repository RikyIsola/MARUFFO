package com.island;
import android.graphics.*;
import android.view.*;
import android.widget.*;
public class Bottone extends Testo
{
	private int sfondo,sfondoPremuto,sfondoRilascio,colore,colorePremuto,coloreRilascio;
	private Gruppo g;
	public Bottone(Gruppo gruppo,double x,double y,double larghezza,double altezza,int coloreRilascio,int colorePremuto)
	{
		super(gruppo,x,y,larghezza,altezza,null,Color.WHITE,Color.TRANSPARENT);
		g=gruppo;
		this.colorePremuto=colorePremuto;
		this.coloreRilascio=coloreRilascio;
		colore=coloreRilascio;
	}
	public Bottone(Gruppo gruppo,double x,double y,double larghezza,double altezza)
	{
		this(gruppo,x,y,larghezza,altezza,Color.LTGRAY,Color.DKGRAY);
	}
	public Bottone colore(int coloreRilascio,int colorePremuto)
	{
		if(colore==this.colorePremuto)colore=colorePremuto;
		else colore=coloreRilascio;
		this.colorePremuto=colorePremuto;
		this.coloreRilascio=coloreRilascio;
		Schermo.schermo.runOnUiThread(invalida);
		return this;
	}
	public int colorePremuto()
	{
		return colorePremuto;
	}
	public int coloreRilascio()
	{
		return coloreRilascio;
	}
	public Bottone inverti()
	{
		if(colore==colorePremuto)colore=coloreRilascio;
		else colore=colorePremuto;
		int cache=colorePremuto;
		colorePremuto=coloreRilascio;
		coloreRilascio=cache;
		Schermo.schermo.runOnUiThread(invalida);
		return this;
	}
	public void onDraw(Canvas canvas)
	{
		Schermo schermo=Schermo.schermo;
		Paint paint=schermo.paint;
		RectF rect=schermo.rect;
		int width=canvas.getWidth();
		int height=canvas.getHeight();
		float dimensioni=(float)dimensioni(width,height);
		float dimensioni4=dimensioni/4;
		if(lazyLoad())lazyLoad(immagine());
		paint.reset();
		paint.setColor(colore);
		rect.set(0,0,width,height);
		canvas.drawRoundRect(rect,dimensioni4,dimensioni4,Schermo.schermo.paint);
		paint.setColor(Color.BLACK);
		paint.setAlpha(alpha());
		if(schermo.esiste(immagine()))
		{
			Bitmap b=Schermo.schermo.immagine(immagine());
			if(!b.isRecycled())
			{
				float restovero=(float)resto(width,height,dimensioni,true)/2;
				float restofalso=(float)resto(width,height,dimensioni,false)/2;
				rect.set(restovero,restofalso,dimensioni+restovero,dimensioni+restofalso);
				canvas.drawBitmap(b,null,rect,paint);
			}
		}
		else super.onDraw(canvas);
	}
	private static double dimensioni(int larghezza,int altezza)
	{
		if(altezza>larghezza)return larghezza;
		else return altezza;
	}
	private static double resto(int larghezza,int altezza,double dimensioni,boolean large)
	{
		if(large)return larghezza-dimensioni;
		else return altezza-dimensioni;
	}
	public boolean onTouchEvent(MotionEvent evento)
	{
		if(evento.getAction()==MotionEvent.ACTION_DOWN)
		{
			colore=colorePremuto;
			sfondo=sfondoPremuto;
			Schermo.schermo.runOnUiThread(invalida);
		}
		else if(evento.getAction()==MotionEvent.ACTION_UP)
		{
			colore=coloreRilascio;
			sfondo=sfondoRilascio;
			Schermo.schermo.runOnUiThread(invalida);
		}
		return super.onTouchEvent(evento);
	}
}
