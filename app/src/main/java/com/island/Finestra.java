package com.island;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
public class Finestra extends ViewGroup
{
	private String nome=getClass().getName();
	private Runnable show=new Runnable()
	{
		public void run()
		{
			Schermo schermo=Finestra.this.schermo();
			Finestra attuale;
			if(!schermo.libero())attuale=schermo.finestra();
			else attuale=null;
			if(attuale==null)
			{
				View view=schermo.view;
				if(view!=null)view.setVisibility(View.INVISIBLE);
			}
			else
			{
				attuale.setVisibility(View.INVISIBLE);
			}
			schermo.finestre.add(Finestra.this);
		}
	};
	private Runnable hide=new Runnable()
	{
		public void run()
		{
			Schermo schermo=Finestra.this.schermo();
			ViewManager vm=(ViewManager)getParent();
			if(vm!=null)vm.removeView(Finestra.this);
			schermo.finestre.remove(Finestra.this);
			if(schermo.libero())
			{
				View view=schermo.view;
				if(view!=null)view.setVisibility(View.VISIBLE);
			}
			else schermo.finestra().setVisibility(View.VISIBLE);
		}
	};
	public Finestra(final Schermo schermo)
	{
		super(schermo);
		dimensioni=schermo.dimensioni;
		for(Finestra f:schermo.finestre)if(f.nome.equals(nome))return;
		schermo.runOnUiThread(new Runnable()
		{
			public void run()
			{
				Schermo.schermo.addContentView(Finestra.this,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
				show();
			}
		});
	}
	private int[]dimensioni;
	Gruppo view;
	public void setContentView(final Gruppo view)
	{
		schermo().runOnUiThread(new Runnable()
		{
			public void run()
			{
				if(view!=null)removeView(view);
				addView(view);
				Finestra.this.view=view;
			}
		});
	}
	public void show()
	{
		schermo().runOnUiThread(show);
	}
	public void hide()
	{
		schermo().runOnUiThread(hide);
	}
	public int[]dimensioni()
	{
		return dimensioni;
	}
	public Schermo schermo()
	{
		return Schermo.schermo;
	}
	public void sempre(){}
	public void sempreGrafico(){}
	public void onStop()
	{
		
	}
	public void pausa(){}
	public void riprendi(){}
	public boolean debug()
	{
		return schermo().debug();
	}
	public long ram()
	{
		return schermo().ram();
	}
	public int velocita()
	{
		return schermo().velocita();
	}
	public int frame()
	{
		return schermo().frame();
	}
	public int velocitaSuono()
	{
		return schermo().velocitaSuono();
	}
	public int ramGiusta()
	{
		return schermo().ramGiusta();
	}
	public boolean libero()
	{
		int length=schermo().finestre.size();
		for(int a=0;a<length;a++)if(schermo().finestre.get(a)==this&&a==schermo().finestre.size()-1)return true;
		return false;
	}
	public Handler grafica()
	{
		return schermo().grafica();
	}
	public Handler thread()
	{
		return schermo().thread();
	}
	public Finestra toast(final String testo)
	{
		schermo().toast(testo);
		return this;
	}
	protected void onLayout(boolean p1,int p2,int p3,int p4,int p5)
	{
		view.layout(p2+(p4-dimensioni[0])/2,p3+(p5-dimensioni[1])/2,p4-(p4-dimensioni[0])/2,p5-(p5-dimensioni[1])/2);
	}
	public void cancel()
	{
		hide();
		onStop();
	}
	public void onBackPressed()
	{
		cancel();
	}
}
