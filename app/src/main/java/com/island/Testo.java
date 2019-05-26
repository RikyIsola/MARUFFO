package com.island;
import android.graphics.*;
import android.widget.*;
public class Testo extends Oggetto
{
	private CharSequence testo;
	private int colore;
	private Paint.Align textAlign=Paint.Align.CENTER;
	private Typeface typeface;
	private double textX=0.5;
	private double larghezzaX=1;
	private double carattere=1;
	public Testo(Gruppo gruppo,double x,double y,double larghezza,double altezza,CharSequence testo,int colore,final int sfondo)
	{
		super(gruppo,x,y,larghezza,altezza);
		typeface=gruppo.schermo().typeface();
		this.testo=testo;
		this.colore=colore;
		colore(sfondo);
	}
	public Testo(Gruppo gruppo,double x,double y,double larghezza,double altezza,CharSequence testo)
	{
		this(gruppo,x,y,larghezza,altezza,testo,Color.WHITE,Color.TRANSPARENT);
	}
	public Testo scrivi(CharSequence testo,int colore,final int sfondo)
	{
		CharSequence vecchio=this.testo;
		this.testo=testo;
		this.colore=colore;
		if(!vecchio.equals(testo))Schermo.schermo.runOnUiThread(invalida);
		return this;
	}
	public Testo scrivi(CharSequence testo,int colore)
	{
		CharSequence vecchio=this.testo;
		this.testo=testo;
		this.colore=colore;
		if(vecchio!=null&&!vecchio.equals(testo))Schermo.schermo.runOnUiThread(invalida);
		return this;
	}
	public Testo scrivi(CharSequence testo)
	{
		CharSequence vecchio=this.testo;
		this.testo=testo;
		if(vecchio!=null&&!vecchio.equals(testo))Schermo.schermo.runOnUiThread(invalida);
		return this;
	}
	public Testo scrivi(int colore,final int sfondo)
	{
		this.colore=colore;
		colore(sfondo);
		Schermo.schermo.runOnUiThread(invalida);
		return this;
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if(testo!=null)
		{
			Schermo.schermo.paint.reset();
			Schermo.schermo.paint.setTextAlign(textAlign);
			Schermo.schermo.paint.setColor(colore);
			if(typeface!=null)Schermo.schermo.paint.setTypeface(typeface);
			Schermo.schermo.paint.setTextSize((float)(canvas.getHeight()/2*carattere));
			Schermo.schermo.paint.setTextScaleX((float)(unitaX()/unitaY()*larghezzaX));
			canvas.drawText(testo,0,testo.length(),(float)(canvas.getWidth()*textX),canvas.getHeight()/1.5f,Schermo.schermo.paint);
		}
	}
	public CharSequence testo()
	{
		return testo;
	}
	public Testo allineamento(Paint.Align allineamento)
	{
		if(allineamento==Paint.Align.CENTER)textX=0.5;
		else if(allineamento==Paint.Align.LEFT)textX=0;
		else textX=1;
		this.textAlign=allineamento;
		return this;
	}
	public Paint.Align textAlign()
	{
		return textAlign;
	}
	public Testo larghezzaX(double larghezzaX)
	{
		this.larghezzaX=larghezzaX;
		return this;
	}
	public Testo carattere(double carattere)
	{
		this.carattere=carattere;
		return this;
	}
	public int coloreTesto()
	{
		return colore;
	}
	public void coloreTesto(int colore)
	{
		this.colore=colore;
	}
	public void font(Typeface typeface,int type)
	{
		this.typeface=Typeface.create(typeface,type);
	}
	public Typeface typeface()
	{
		return typeface;
	}
}
