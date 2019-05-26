package com.island;
import java.lang.reflect.*;
import java.util.*;
public class Lista<T>extends ArrayList<T>
{
	public static double toDouble(CharSequence cs)
	{
		char puntoc=46;
		char menoc=45;
		char zeroc=48;
		char unoc=49;
		char duec=50;
		char trec=51;
		char quattroc=52;
		char cinquec=53;
		char seic=54;
		char settec=55;
		char ottoc=56;
		char novec=57;
		int length=cs.length();
		boolean negativo=false;
		boolean puntino=false;
		double ritorno=0;
		int index=1;
		for(int a=0;a<length;a++)
		{
			char c=cs.charAt(a);
			if(c==menoc)negativo=true;
			else
			{
				if(c==zeroc)ritorno+=0.0/index;
				else if(c==unoc)ritorno+=1.0/index;
				else if(c==duec)ritorno+=2.0/index;
				else if(c==trec)ritorno+=3.0/index;
				else if(c==quattroc)ritorno+=4.0/index;
				else if(c==cinquec)ritorno+=5.0/index;
				else if(c==seic)ritorno+=6.0/index;
				else if(c==settec)ritorno+=7.0/index;
				else if(c==ottoc)ritorno+=8.0/index;
				else if(c==novec)ritorno+=9.0/index;
				else if(c==puntoc)
				{
					ritorno/=10;
					puntino=true;
				}
				else throw new NumberFormatException(cs.toString());
				if(!puntino)ritorno*=10;
				else index*=10;
			}
		}
		if(negativo)ritorno*=-1;
		if(!puntino)ritorno/=10;
		return ritorno;
	}
	public static int toInt(CharSequence cs)
	{
		return (int)toLong(cs);
	}
	public static long toLong(CharSequence cs)
	{
		char menoc=45;
		char zeroc=48;
		char unoc=49;
		char duec=50;
		char trec=51;
		char quattroc=52;
		char cinquec=53;
		char seic=54;
		char settec=55;
		char ottoc=56;
		char novec=57;
		int length=cs.length();
		boolean negativo=false;
		long ritorno=0;
		int index=1;
		for(int a=0;a<length;a++)
		{
			char c=cs.charAt(a);
			if(c==menoc)negativo=true;
			else
			{
				if(c==zeroc)ritorno+=0.0/index;
				else if(c==unoc)ritorno+=1.0/index;
				else if(c==duec)ritorno+=2.0/index;
				else if(c==trec)ritorno+=3.0/index;
				else if(c==quattroc)ritorno+=4.0/index;
				else if(c==cinquec)ritorno+=5.0/index;
				else if(c==seic)ritorno+=6.0/index;
				else if(c==settec)ritorno+=7.0/index;
				else if(c==ottoc)ritorno+=8.0/index;
				else if(c==novec)ritorno+=9.0/index;
				else throw new NumberFormatException(cs.toString());
				ritorno*=10;
			}
		}
		if(negativo)ritorno*=-1;
		ritorno/=10;
		return ritorno;
	}
	public static boolean uguali(CharSequence uno,CharSequence due)
	{
		if(uno.length()!=due.length())return false;
		else
		{
			int length=uno.length();
			for(int a=0;a<length;a++)if(uno.charAt(a)!=due.charAt(a))return false;
			return true;
		}
	}
	public static void debug(Throwable t)
	{
		String punto=".";
		String at="at ";
		String tonda="(";
		String tonda2=")";
		String punti=":";
		String nativo="Native Method";
		String linea="\n";
		StackTraceElement[]ste=t.getStackTrace();
		StringBuilder sb=new StringBuilder((ste.length+1)*42);
		sb.append(t);
		for(StackTraceElement el:ste)
		{
			sb.append(linea).append(at).append(el.getClassName()).append(punto).append(el.getMethodName()).append(tonda);
			int n=el.getLineNumber();
			if(n==-2)sb.append(nativo);
			else sb.append(el.getFileName()).append(punti).append(n);
			sb.append(tonda2);
		}
		Throwable causa=t.getCause();
		if(causa!=null)sb.append(linea).append("Cause by:");
		System.err.println(sb);
		if(causa!=null)debug(causa);
	}
	public static void breakpoint(long tempo,Object valori)
	{
		try
		{
			Thread.sleep(tempo);
			System.out.println(valori);
			if(valori!=null&&valori.getClass().isArray())
			{
				Object[]array=(Object[])valori;
				for(Object o:array)System.out.println(o);
			}
		}
		catch(InterruptedException e)
		{
			debug(e);
		}
	}
	public static<T>Lista<T>aggiungi(Lista<T>input,T aggiungi)
	{
		input.add(aggiungi);
		return input;
	}
	public static<T>Lista<T>rimuovi(Lista<T>input,T rimuovi)
	{
		input.remove(rimuovi);
		return input;
	}
	public static<T>Lista<T>rimuovi(Lista<T>input,int index)
	{
		input.remove(index);
		return input;
	}
	public static double arrotonda(double valore,int cifre)
	{
		double potenza=(int)Math.pow(10,cifre);
		return Math.round(valore*potenza)/potenza;
	}
	public Lista(){}
	public Lista(int n){super(n);}
	public Lista(Collection<? extends T>n){super(n);}
	public Iterator iterator()
	{
		return new Iterator()
		{
			int n=size();
			int index;
			public boolean hasNext()
			{
				int size=size();
				return(index=size-n)<size;
			}
			public Object next()
			{
				if(index<0)index=0;
				Object o=get(index);
				n--;
				return o;
			}
			public void remove(){}
		};
	}
}
