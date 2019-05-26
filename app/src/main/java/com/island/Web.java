package com.island;
import android.graphics.*;
import android.view.accessibility.*;
import android.webkit.*;
public class Web extends WebView
{
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event)
	{
		return true;
	}
	private Info info;
	public static final String blank="about:blank";
	public Web(final Gruppo gruppo,final double x,final double y,final double larghezza,final double altezza,String url)
	{
		super(gruppo.getContext());
		setBackgroundColor(Color.TRANSPARENT);
		info=new Info(x,y,larghezza,altezza,gruppo.unitaX(),gruppo.unitaY());
		schermo().runOnUiThread(new Runnable()
			{
				public void run()
				{
					gruppo.addView(Web.this);
					setLayoutParams(info);
				}
			}
		);
		loadUrl(url);
		WebSettings opt=getSettings();
		opt.setJavaScriptEnabled(true);
		if(cambioSito())setWebViewClient(new WebViewClient()
			{
				public void onLoadResource(WebView w,String s)
				{
				}
			});
	}
	public boolean cambioSito()
	{
		return true;
	}
	public double tall()
	{
		return altezza()-y();
	}
	public double large()
	{
		return larghezza()-x();
	}
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}
	public Web y(double y)
	{
		info.altezza=y+info.altezza-info.y;
		info.y=y;
		return this;
	}
	public Web x(double x)
	{
		info.larghezza=x+info.larghezza-info.x;
		info.x=x;
		return this;
	}
	public Web trans(double transX,double transY)
	{
		info.trans(transX,transY);
		return this;
	}
	public double transX()
	{
		return info.transX;
	}
	public double transY()
	{
		return info.transY;
	}
	public Web larghezza(double larghezza)
	{
		info.larghezza=larghezza;
		return this;
	}
	public Web altezza(double altezza)
	{
		info.altezza=altezza;
		return this;
	}
	public Web large(double large)
	{
		info.larghezza=x()+large;
		return this;
	}
	public Web tall(double tall)
	{
		info.altezza=y()+tall;
		return this;
	}
	public Web antiTrans(boolean transX,boolean transY)
	{
		info.antiTrans(transX,transY);
		return this;
	}
	public double x()
	{
		return info.x;
	}
	public double y()
	{
		return info.y;
	}
	public double larghezza()
	{
		return info.larghezza;
	}
	public double altezza()
	{
		return info.altezza;
	}
	public double unitaX()
	{
		return info.unitaX;
	}
	public double unitaY()
	{
		return info.unitaY;
	}
	public Schermo schermo()
	{
		return Schermo.schermo;
	}
	public void riprendi()
	{
		resumeTimers();
	}
	public void pausa()
	{
		pauseTimers();
	}
	public void distruggi()
	{
		clearHistory();
		clearCache(true);
		loadUrl(blank);
		pauseTimers();
		destroy();
	}
	public double largeMeta()
	{
		return large()/2;
	}
	public double tallMeta()
	{
		return tall()/2;
	}
	public double centroX()
	{
		return x()+largeMeta();
	}
	public double centroY()
	{
		return y()+tallMeta();
	}
	public void largeMeta(double largeMeta)
	{
		large(largeMeta*2);
	}
	public void tallMeta(double tallMeta)
	{
		tall(tallMeta*2);
	}
	public void centroX(double centroX)
	{
		x(centroX-largeMeta());
	}
	public void centroY(double centroY)
	{
		y(centroY-tallMeta());
	}
}
