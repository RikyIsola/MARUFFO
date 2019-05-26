package com.island;
import android.graphics.*;
public class SuperTesto extends Oggetto
{
	private CharSequence testo;
	private int colore;
	private Paint.Align textAlign=Paint.Align.CENTER;
	private Typeface typeface;
	private double textX=0.5;
	private StringBuilder cache=new StringBuilder();
	private StringBuilder testosb=new StringBuilder();
	private StringBuilder aggiunta=new StringBuilder();
	private StringBuilder riga=new StringBuilder();
	private double larghezzaX=1;
	private double carattere=1;
	private StringBuilder finale=new StringBuilder();
	private char linea=38;
	private String spazio=" ";
	public SuperTesto(Gruppo gruppo,double x,double y,double larghezza,double altezza,CharSequence testo,int colore,final int sfondo)
	{
		super(gruppo,x,y,larghezza,altezza);
		typeface=gruppo.schermo().typeface();
		this.testo=testo;
		this.colore=colore;
		colore(sfondo);
		aggiorna();
	}
	public SuperTesto(Gruppo gruppo,double x,double y,double larghezza,double altezza,CharSequence testo)
	{
		this(gruppo,x,y,larghezza,altezza,testo,Color.WHITE,Color.TRANSPARENT);
	}
	public SuperTesto scrivi(CharSequence testo,int colore,final int sfondo)
	{
		this.testo=testo;
		this.colore=colore;
		aggiorna();
		return this;
	}
	public SuperTesto scrivi(CharSequence testo,int colore)
	{
		CharSequence vecchio=this.testo;
		this.testo=testo;
		this.colore=colore;
		if(vecchio!=null&&!vecchio.equals(testo))aggiorna();
		return this;
	}
	public SuperTesto scrivi(CharSequence testo)
	{
		CharSequence vecchio=this.testo;
		this.testo=testo;
		if(vecchio!=null&&!vecchio.equals(testo))aggiorna();
		return this;
	}
	public SuperTesto scrivi(int colore,final int sfondo)
	{
		this.colore=colore;
		colore(sfondo);
		schermo().runOnUiThread(invalida);
		return this;
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		schermo().paint.reset();
		schermo().paint.setTextAlign(textAlign);
		schermo().paint.setColor(colore);
		if(typeface!=null)schermo().paint.setTypeface(typeface);
		schermo().paint.setTextSize((float)(tall()*unitaY()*carattere));
		schermo().paint.setTextScaleX((float)(unitaX()/unitaY()*larghezzaX));
		int index=0;
		int punto=0;
		for(int a=0;a<finale.length();a++)if(finale.charAt(a)==linea)
		{
			canvas.drawText(finale,index,a,(float)(canvas.getWidth()*textX),(punto+1)*schermo().paint.getTextSize(),schermo().paint);
			index=a+1;
			punto++;
		}
	}
	public CharSequence testo()
	{
		return testo;
	}
	public SuperTesto allineamento(Paint.Align allineamento)
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
	public SuperTesto larghezzaX(double larghezzaX)
	{
		this.larghezzaX=larghezzaX;
		return this;
	}
	public SuperTesto carattere(double carattere)
	{
		this.carattere=carattere;
		return this;
	}
	public int coloreTesto()
	{
		return colore;
	}
	public SuperTesto coloreTesto(int colore)
	{
		this.colore=colore;
		schermo().runOnUiThread(invalida);
		return this;
	}
	private Runnable aggiorna=new Runnable()
	{
		public void run()
		{
			finale.setLength(0);
			aggiunta.setLength(0);
			cache.setLength(0);
			testosb.setLength(0);
			testosb.append(SuperTesto.this.testo).append(spazio);
			schermo().paint.setTextSize((float)(tall()*unitaY()*carattere));
			schermo().paint.setTextScaleX((float)(unitaX()/unitaY()*larghezzaX));
			int a=0;
			while(a<testosb.length())
			{
				float somma=schermo().paint.measureText(aggiunta,0,aggiunta.length());
				while(a<testosb.length())
				{
					cache.setLength(0);
					while(true)
					{
						char c=testosb.charAt(a);
						a++;
						if(c==spazio.charAt(0))break;
						else cache.append(c);
					}
					cache.append(spazio);
					somma+=schermo().paint.measureText(cache,0,cache.length());
					if(somma>=large()*unitaX()||!(a<testosb.length()))
					{
						boolean esiste=false;
						int lunghezza=0;
						for(int b=0;b<aggiunta.length();b++)if(aggiunta.charAt(b)==linea)
							{
								esiste=true;
								lunghezza=b;
								riga.setLength(0);
								riga.append(aggiunta);
								riga.delete(lunghezza,riga.length());
								finale.append(riga).append(linea);
								aggiunta.delete(0,riga.length()+1).append(cache);
								b=0;
								break;
							}
						if(esiste)break;
						else
						{
							finale.append(aggiunta).append(linea);
							aggiunta.setLength(0);
							aggiunta.append(cache);
							break;
						}
					}
					else aggiunta.append(cache);
				}
			}
			finale.append(aggiunta).append(linea);
			invalidate();
		}
	};
	public void aggiorna()
	{
		schermo().runOnUiThread(aggiorna);
	}
	public void font(Typeface typeface,int type)
	{
		this.typeface=Typeface.create(typeface,type);
		schermo().runOnUiThread(invalida);
	}
	public Typeface typeface()
	{
		return typeface;
	}
	public static String riga()
	{
		return"&";
	}
}
