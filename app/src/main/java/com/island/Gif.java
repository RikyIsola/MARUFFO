package com.island;
import android.graphics.*;
public class Gif extends Oggetto
{
	private Movie gif;
	private long tempo;
	private Bitmap bitmap;
	private Canvas c;
	private Rect rect=new Rect();
	public Gif(Gruppo g,double x,double y,double larghezza,double altezza,int gif)
	{
		super(g,x,y,larghezza,altezza);
		this.gif=Movie.decodeStream(schermo().getResources().openRawResource(gif));
		tempo=System.currentTimeMillis();
		bitmap=Bitmap.createBitmap(this.gif.width(),this.gif.height(),Bitmap.Config.ARGB_8888);
		c=new Canvas(bitmap);
	}
	public Gif sempre()
	{
		long tempo=System.currentTimeMillis()-this.tempo;
		if(tempo>=gif.duration())
		{
			this.tempo=System.currentTimeMillis();
			tempo=0;
		}
		gif.setTime((int)tempo);
		bitmap.eraseColor(Color.TRANSPARENT);
		gif.draw(c,0,0);
		schermo().runOnUiThread(invalida);
		return this;
	}
	public void onDraw(Canvas c)
	{
		schermo().paint.reset();
		schermo().paint.setAlpha(alpha());
		rect.set(0,0,c.getWidth(),c.getHeight());
		if(bitmap!=null&&!bitmap.isRecycled())c.drawBitmap(bitmap,null,rect,schermo().paint);
	}
}
