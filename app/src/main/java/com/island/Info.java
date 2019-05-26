package com.island;
import android.view.*;
public class Info extends ViewGroup.LayoutParams
{
	double x;
	double y;
	double larghezza;
	double altezza;
	double unitaX;
	double unitaY;
	double transX;
	double transY;
	boolean antiTransX=false;
	boolean antiTransY=false;
	public Info(double x,double y,double larghezza,double altezza,double unitaX,double unitaY)
	{
		super((int)((larghezza-x)*unitaX),(int)((altezza-y)*unitaY));
		this.x=x;
		this.y=y;
		this.larghezza=larghezza;
		this.altezza=altezza;
		this.unitaX=unitaX;
		this.unitaY=unitaY;
		aggiorna();
	}
	void schermo(double unitaX,double unitaY)
	{
		this.unitaX=unitaX;
		this.unitaY=unitaY;
		aggiorna();
	}
	private void aggiorna()
	{
		width=(int)((larghezza-x)*unitaX);
		height=(int)((altezza-y)*unitaY);
	}
	void trans(double transX,double transY)
	{
		this.transX=transX;
		this.transY=transY;
	}
	void antiTrans(boolean TransX,boolean transY)
	{
		antiTransX=TransX;
		antiTransY=transY;
	}
}
