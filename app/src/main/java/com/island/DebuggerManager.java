package com.island;
import java.lang.reflect.*;
public class DebuggerManager
{
	Processo processo=new Processo()
	{
		public void esegui()
		{
			setPriority(1);
			loop();
		}
	};
	private String Boolean="boolean";
	private String Byte="byte";
	private String Char="char";
	private String Long="long";
	private String Double="double";
	private String Float="float";
	private String Short="short";
	private String Int="int";
	private String risultati="DEBUGGER MANAGER RESULTS:";
	private String vel="VEL:";
	private String vel1=" ms MED ";
	private String vel2=" ms MIN ";
	private String vel3=" ms MAX ";
	private String memoria=" ms RAM:";
	private String memoria1=" kb MED ";
	private String memoria2=" kb MIN ";
	private String memoria3=" kb MAX ";
	private String unita=" kb ";
	private String unita2=" b";
	private String at="at ";
	private String tonda="(";
	private String tonda2=")";
	private String punti=":";
	private String punto=".";
	private Lista<Lista<StackTraceElement>>elementi=new Lista<Lista<StackTraceElement>>();
	private Lista<Thread>thread=new Lista<Thread>();
	private Lista<Lista<Debugger>>debug=new Lista<Lista<Debugger>>();
	private Lista<String>nomi=new Lista<String>();
	private Lista<Long>dimensioni=new Lista<Long>();
	private Lista<Lista<Debugger>>inutilizzati=new Lista<Lista<Debugger>>();
	private Lista<Lista<String>>metodi=new Lista<Lista<String>>();
	private Lista<Lista<String>>files=new Lista<Lista<String>>();
	private Lista<Lista<String>>nlinee=new Lista<Lista<String>>();
	private Lista<Lista<Long>>tempimax=new Lista<Lista<Long>>();
	private Lista<Lista<Long>>ramsmax=new Lista<Lista<Long>>();
	private Lista<Lista<Long>>tempimin=new Lista<Lista<Long>>();
	private Lista<Lista<Long>>ramsmin=new Lista<Lista<Long>>();
	private Lista<Lista<Long>>tempi=new Lista<Lista<Long>>();
	private Lista<Lista<Long>>rams=new Lista<Lista<Long>>();
	Runtime r=Runtime.getRuntime();
	public void salva(final StackTraceElement e,final long tempo,final long ram)
	{
		processo.handler(true).post(new Runnable()
		{
			public void run()
			{
				String nome=e.getClassName();
				String nlinea=String.valueOf(e.getLineNumber());
				String metodo=e.getMethodName();
				String file=e.getFileName();
				Lista<String>stringa;
				Lista<String>f;
				Lista<String>n;
				int a=nomi.indexOf(nome);
				if(a!=-1)
				{
					stringa=metodi.get(a);
					f=files.get(a);
					n=nlinee.get(a);
					int b=n.indexOf(nlinea);
					if(b!=-1)
					{
						Lista<Long>tempomax=tempimax.get(a);
						Lista<Long>tempomin=tempimin.get(a);
						Lista<Long>rammax=ramsmax.get(a);
						Lista<Long>rammin=ramsmin.get(a);
						Lista<Long>tempomed=tempi.get(a);
						Lista<Long>rammed=rams.get(a);
						if(tempo<tempomin.get(b))tempomin.set(b,tempo);
						else if(tempo>tempomax.get(b))tempomax.set(b,tempo);
						if(ram<rammin.get(b))rammin.set(b,ram);
						else if(ram<rammax.get(b))rammax.set(b,ram);
						tempomed.set(b,(tempomed.get(b)+tempo)/2);
						rammed.set(b,(rammed.get(b)+ram)/2);
					}
					else
					{
						stringa.add(metodo);
						f.add(file);
						n.add(nlinea);
						tempimax.get(a).add(tempo);
						ramsmax.get(a).add(ram);
						tempimin.get(a).add(tempo);
						ramsmin.get(a).add(ram);
						tempi.get(a).add(tempo);
						rams.get(a).add(ram);
					}
				}
				else
				{
					long size;
					try
					{
						size=sizeOf(Class.forName(nome));
					}
					catch(ClassNotFoundException e)
					{
						size=0;
						Lista.debug(e);
					}
					dimensioni.add(size);
					nomi.add(nome);
					Lista<String>nuovo=new Lista<String>();
					nuovo.add(metodo);
					metodi.add(nuovo);
					nuovo=new Lista<String>();
					nuovo.add(file);
					files.add(nuovo);
					nuovo=new Lista<String>();
					nuovo.add(nlinea);
					nlinee.add(nuovo);
					Lista<Long>nuovol=new Lista<Long>();
					nuovol.add(tempo);
					tempimax.add(nuovol);
					nuovol=new Lista<Long>();
					nuovol.add(ram);
					ramsmax.add(nuovol);
					nuovol=new Lista<Long>();
					nuovol.add(tempo);
					tempimin.add(nuovol);
					nuovol=new Lista<Long>();
					nuovol.add(ram);
					ramsmin.add(nuovol);
					nuovol=new Lista<Long>();
					nuovol.add(tempo);
					tempi.add(nuovol);
					nuovol=new Lista<Long>();
					nuovol.add(ram);
					rams.add(nuovol);
				}
			}
		});
	}
	public long sizeOf(Class c)
	{
		long ritorno;
		Field[]fields=c.getDeclaredFields();
		ritorno=12;
		for(Field f:fields)if(!Modifier.isStatic(f.getModifiers()))
			{
				Class cl=f.getType();
				String s=cl.getName();
				if(cl.isPrimitive())
				{
					if(s.equals(Boolean))ritorno+=4;
					else if(s.equals(Byte))ritorno+=4;
					else if(s.equals(Char))ritorno+=4;
					else if(s.equals(Short))ritorno+=4;
					else if(s.equals(Int))ritorno+=4;
					else if(s.equals(Float))ritorno+=4;
					else if(s.equals(Long))ritorno+=8;
					else if(s.equals(Double))ritorno+=8;
				}
				else ritorno+=4;
			}
		Class sopra=c.getSuperclass();
		if(sopra!=null)ritorno+=sizeOf(sopra);
		long resto=ritorno%8;
		if(resto>0)ritorno+=8-resto;
		return ritorno;
	}
	public void finito()
	{
		processo.handler().post(new Runnable()
		{
			public void run()
			{
				StringBuilder sb=new StringBuilder(nomi.size()*100);
				String linea=System.lineSeparator();
				sb.append(risultati).append(linea);
				int length=metodi.size();
				for(int a=0;a<length;a++)
				{
					String classe=nomi.get(a);
					sb.append(classe).append(punti).append(dimensioni.get(a)).append(unita2).append(linea);
					Lista<String>pezzo=metodi.get(a);
					Lista<String>file=files.get(a);
					Lista<String>nlinea=nlinee.get(a);
					Lista<Long>tempomin=tempimin.get(a);
					Lista<Long>tempomax=tempimax.get(a);
					Lista<Long>rammin=ramsmin.get(a);
					Lista<Long>rammax=ramsmax.get(a);
					Lista<Long>rammed=rams.get(a);
					Lista<Long>tempomed=tempi.get(a);
					int length2=pezzo.size();
					for(int b=0;b<length2;b++)
					{
						long tmin=tempomin.get(b);
						long tmax=tempomax.get(b);
						long tmed=tempomed.get(b);
						long rmin=rammin.get(b);
						long rmax=rammax.get(b);
						long rmed=rammed.get(b);
						sb.append(at).append(classe).append(punto).append(pezzo.get(b)).append(tonda).append(vel).append((tmax+tmin+tmed)/3).append(vel1).append(tmed).append(vel2).append(tmin).append(vel3).append(tmax).append(memoria).append((rmax+rmin+rmed)/3).append(memoria1).append(rmed).append(memoria2).append(rmin).append(memoria3).append(rmax).append(unita).append(file.get(b)).append(punti).append(nlinea.get(b)).append(tonda2).append(linea);
					}
				}
				System.out.print(sb);
				processo.chiudi();
			}
		});
	}
	public void inizio(int n)
	{
		Thread t=Thread.currentThread();
		Debugger d;
		int index=thread.indexOf(t);
		if(index!=-1)
		{
			Lista<Debugger>pezzo=inutilizzati.get(index);
			if(pezzo.size()>0)pezzo.remove(d=pezzo.get(0));
			else d=new Debugger(this,t);
			debug.get(index).add(d);
		}
		else
		{
			thread.add(t);
			Lista<Debugger>de=new Lista<Debugger>();
			d=new Debugger(this,t);
			de.add(d);
			debug.add(de);
			inutilizzati.add(new Lista<Debugger>());
			index=elementi.size();
			elementi.add(new Lista<StackTraceElement>());
		}
		Lista<StackTraceElement>pezzo=elementi.get(index);
		while(n>=pezzo.size())pezzo.add(null);
		StackTraceElement e=pezzo.get(n);
		if(e==null)
		{
			e=t.getStackTrace()[4];
			pezzo.set(n,e);
		}
		d.inizio(e,n);
	}
	public void fine(int n)
	{
		Thread t=Thread.currentThread();
		int index=thread.indexOf(t);
		Lista<Debugger>pezzo=debug.get(index);
		try
		{
			Debugger d=pezzo.get(pezzo.size()-1).fine(n);
			pezzo.remove(d);
			inutilizzati.get(index).add(d);
		}
		catch(ArrayIndexOutOfBoundsException e){}
	}
}
