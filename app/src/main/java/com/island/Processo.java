package com.island;
import android.os.*;
public abstract class Processo extends Thread
{
	private Handler handler;
	private Looper looper;
	private int frame,frameS;
	private long precedente;
	public Processo()
	{
		start();
	}
	public final void run()
	{
		setPriority(Thread.MAX_PRIORITY);
		esegui();
	}
	public abstract void esegui()
	public Processo loop()
	{
		Looper.prepare();
		handler=new Handler();
		looper=Looper.myLooper();
		Looper.loop();
		return this;
	}
	public Processo pausa()
	{
		handler.removeCallbacks(r);
		return this;
	}
	public Processo riprendi()
	{
		try
		{
			Thread.sleep(10);
		}
		catch(InterruptedException e)
		{
			Lista.debug(e);
		}
		while(handler==null);
		handler.postDelayed(r,50);
		return this;
	}
	public Handler handler()
	{
		return handler(false);
	}
	public Handler handler(boolean blocca)
	{
		while(handler==null&&blocca);
		return handler;
	}
	public void chiudi()
	{
		pausa();
		while(looper==null);
		looper.quit();
	}
	public void sempre(){}
	public int velocita()
	{
		return frameS*5;
	}
	private Runnable r=new Runnable()
	{
		public void run()
		{
			handler.postDelayed(this,50);
			if(System.currentTimeMillis()>=precedente)
			{
				precedente=System.currentTimeMillis()+1000;
				frameS=frame;
				frame=0;
			}
			frame++;
			sempre();
		}
	};
}
