package com.island.maruffo;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.island.*;
import java.util.*;
import android.view.View.*;
import java.net.*;
import java.io.*;
import android.net.*;
import android.graphics.drawable.*;
public class MainActivity extends Schermo 
{
	//8560 496
	public void display(Point p)
	{
		super.display(p);
		//s4Denso(p);
		//s5mini(p);
		//displayPiccolo(p);
		//s4(p);
		//s5miniDenso(p);
	}
	public void s5miniDenso(Point p)
	{
		p.x=720;
		p.y=1280;
	}
	public void s4Denso(Point p)
	{
		p.x=1080;
		p.y=1920;
	}
	public void s5mini(Point p)
	{
		displayPiccolo(p,720,1280,326);
	}
	public void s4(Point p)
	{
		displayPiccolo(p,1080,1920,442);
	}
	public boolean debug()
	{
		return true;
	}
	public boolean immagini()
	{
		return true;
	}
	public boolean suoni()
	{
		return true;
	}
	public boolean scimmia()
	{
		return false;
	}
	public int orientamento()
	{
		return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}
	private Oggetto grandepipozu,grandeastrati,videocamera,pipo,astra,titolo;
	private Gruppo g;
	private Testo ram,velocita,frame,velocitaSuono;
	private Random r=new Random();
	private Gif sole;
	public void distruggi()
	{
		grandepipozu=null;
		r=null;
		grandeastrati=null;
		videocamera=null;
		pipo=null;
		astra=null;
		g=null;
		ram=null;
		velocita=null;
		velocitaSuono=null;
		frame=null;
		pipozu=null;
		astrati=null;
		camera=null;
		storia=null;
		razzoClick=null;
		titolo=null;
		island=null;
		tetto=null;
	}
    public void crea()
    {
		System.out.println("inizio");
		g=new Gruppo(this,640,1140).immagine(R.drawable.torre);
		sole=new Gif(g,50,50,250,250,R.raw.sole);
		new Oggetto(g,65,50,250,250).immagine(R.drawable.island).setOnClickListener(island);
		pipo=new Bottone(g,93,947,195,1029,Color.TRANSPARENT,Color.TRANSPARENT);
		pipo.immagine(R.drawable.pipozu).setOnTouchListener(pipozu);
		grandepipozu=new Bottone(g,43,897,245,1079,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.pipozu);
		grandepipozu.setVisibility(View.INVISIBLE);
		astra=new Bottone(g,221,917,305,991,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.astrati);
		astra.setOnTouchListener(astrati);
		grandeastrati=new Bottone(g,171,867,355,1041,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.astrati);
		grandeastrati.setVisibility(View.INVISIBLE);
		videocamera=new Bottone(g,348.5,147,430.5,212,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.videocamera);
		videocamera.setOnClickListener(tetto);
		new Bottone(g,338,498,395,554,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.camera).setOnClickListener(camera);
		new Bottone(g,313,880,385,950,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.storia).setOnClickListener(storia);
		titolo=new Oggetto(g,475,0,640,1140);
		titolo.setOnClickListener(razzoClick);
		new Bottone(g,311,985,375,1070,Color.TRANSPARENT,Color.TRANSPARENT).immagine(R.drawable.maps).setOnClickListener(mappa);
		if(debug())
		{
			Toast.makeText(this,dimensioni()[0]+" "+dimensioni()[1]+" "+dpi(),0).show();
			ram=new Testo(g,400,0,640,50,"RAM:"+ram());
			velocita=new Testo(g,250,0,390,50,"VEL:"+velocita());
			frame=new Testo(g,250,50,390,100,"FPS:"+frame());
			velocitaSuono=new Testo(g,400,50,640,100,"SUONO:"+velocitaSuono());
		}
		grandeastrati.bringToFront();
		g.aggiorna();
    }
	private View.OnTouchListener pipozu=new View.OnTouchListener()
	{
		public boolean onTouch(View p1,MotionEvent p2)
		{
			Oggetto o=(Oggetto)p1;
			if(p2.getAction()==MotionEvent.ACTION_UP)
			{
				grandepipozu.setVisibility(View.INVISIBLE);
				if(libero()&&p2.getX()<o.large()*o.unitaX()&&p2.getY()<o.tall()*o.unitaY()&&p2.getX()>0&&p2.getY()>0)new Sito(schermo(),"https://m.facebook.com/pizzornoporcariarchitettiassociati/?ref=bookmarks");
			}
			else if(p2.getAction()==MotionEvent.ACTION_DOWN)
			{
				grandepipozu.setVisibility(View.VISIBLE);
				g.aggiorna();
			}
			return true;
		}
	};
	private View.OnTouchListener astrati=new View.OnTouchListener()
	{
		public boolean onTouch(View p1,MotionEvent p2)
		{
			Oggetto o=(Oggetto)p1;
			if(p2.getAction()==MotionEvent.ACTION_UP)
			{
				grandeastrati.setVisibility(View.INVISIBLE);
				if(libero()&&p2.getX()<o.large()*o.unitaX()&&p2.getY()<o.tall()*o.unitaY()&&p2.getX()>0&&p2.getY()>0)new Sito(schermo(),"https://m.facebook.com/profile.php?id=1126285857383566");
			}
			else if(p2.getAction()==MotionEvent.ACTION_DOWN)
			{
				grandeastrati.setVisibility(View.VISIBLE);
				g.aggiorna();
			}
			return true;
		}
	};
	private View.OnClickListener camera=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			if(libero())new Camera(MainActivity.this,new int[]{R.drawable.camera1,R.drawable.camera2,R.drawable.camera3},true);
		}
	};
	private View.OnClickListener tetto=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			if(libero())new Camera(MainActivity.this,new int[]{R.drawable.tetto1,R.drawable.tetto2,R.drawable.tetto3,R.drawable.tetto4,R.drawable.tetto5,R.drawable.tetto6,R.drawable.tetto7,R.drawable.tetto8},true);
		}
	};
	private View.OnClickListener island=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			Intent i=new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("https://rikyisola.github.io"));
			startActivity(i);
			//new Sito(schermo(),"https://rikyisola.github.io");
		}
	};
	private View.OnClickListener mappa=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			Intent i=new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("https://www.google.it/maps/place/Via+di+Canneto+Il+Lungo,+23,+16123+Genova/@44.40725,8.93112,17z/data=!4m2!3m1!1s0x12d343ddf418f977:0xc05e661c7d27dadf"));
			startActivity(i);
			//new Sito(schermo(),"https://www.google.it/maps/place/Via+di+Canneto+Il+Lungo,+23,+16123+Genova/@44.40725,8.93112,17z/data=!4m2!3m1!1s0x12d343ddf418f977:0xc05e661c7d27dadf");
		}
	};
	private View.OnClickListener storia=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			if(libero())new Camera(MainActivity.this,new int[]{R.drawable.storia3,R.drawable.storia4,R.drawable.storia5,R.drawable.storia6,R.drawable.storia1,R.drawable.storia2},true);
		}
	};
	private View.OnClickListener razzoClick=new View.OnClickListener()
	{
		public void onClick(View v)
		{
			/*razzo.setVisibility(View.VISIBLE);
			razzoinv.setVisibility(View.VISIBLE);
			titolo.setVisibility(View.INVISIBLE);
			videocamera.setVisibility(View.INVISIBLE);
			astra.setVisibility(View.INVISIBLE);
			pipo.setVisibility(View.INVISIBLE);
			g.immagine(R.drawable.gioco);
			gioco=true;
			sottofondo=new Suono(MainActivity.this,R.raw.sottofondo).infinito(true).start();
			testoPunteggio.setVisibility(View.VISIBLE);*/
			if(libero())new Livelli(schermo());
		}
	};
	public void sempreGrafico()
	{
		if(debug())
		{
			ram.scrivi("RAM:"+ram()+"/"+ramGiusta());
			frame.scrivi("FPS:"+frame());
			velocita.scrivi("VEL:"+velocita());
			velocitaSuono.scrivi("SUONO:"+velocitaSuono());
			//getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
		}
		if(libero())sole.sempre();
		//g.aggiorna();
	}
	protected void onRestoreInstanceState(Bundle b)
	{
		super.onRestoreInstanceState(b);
		if(b.getBoolean("LIVELLI",false))new Livelli(this);
		if(b.getBoolean("CAMERA",false))new Camera(this,b.getIntArray("CAMERA IMMAGINI"),b.getBoolean("CAMERA SPAZIO"));
		if(b.getBoolean("SITO",false))new Sito(this,b.getString("SITO URL"));
		if(b.getBoolean("ARCHITETTI",false))new Architetti(this);
		if(b.getBoolean("ASTRATI",false))new Astrati(this);
		if(b.getBoolean("MEDIOEVO",false))new Medioevo(this);
		if(b.getBoolean("SPAZIO",false))new Spazio(this);
	}
}
