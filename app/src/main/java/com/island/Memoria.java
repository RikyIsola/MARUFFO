package com.island;
import android.content.*;
import java.io.*;
import java.util.*;
public class Memoria
{
	public static void salva(Schermo schermo,String dove,int cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putInt(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,String cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putString(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,boolean cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putBoolean(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,float cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putFloat(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static void salva(Schermo schermo,String dove,long cosa)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putLong(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public static int leggi(Schermo schermo,String dove,int altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getInt(dove,altrimenti);
	}
	public static boolean leggi(Schermo schermo,String dove,boolean altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getBoolean(dove,altrimenti);
	}
	public static float leggi(Schermo schermo,String dove,float altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getFloat(dove,altrimenti);
	}
	public static long leggi(Schermo schermo,String dove,long altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getLong(dove,altrimenti);
	}
	public static String leggi(Schermo schermo,String dove,String altrimenti)
	{
		return schermo.getSharedPreferences(dove,0).getString(dove,altrimenti);
	}
	public static void salva(String dove,CharSequence cosa)
	{
		try
		{
			File file=new File(dove);
			File cartella=file.getParentFile();
			if(file.exists())file.delete();
			cartella.mkdirs();
			file.createNewFile();
			FileOutputStream output=new FileOutputStream(file);
			int length=cosa.length();
			for(int a=0;a<length;a++)output.write(cosa.charAt(a));
			output.flush();
			output.close();
		}
		catch(IOException e)
		{
			Lista.debug(e);
		}
	}
	public static boolean esiste(String dove)
	{
		return new File(dove).exists();
	}
	public static StringBuilder leggi(String dove)
	{
		StringBuilder ritorno=new StringBuilder();
		try
		{
			FileInputStream input=new FileInputStream(new File(dove));
			int letto;
			byte[]bites=new byte[1024*64];
			while((letto=input.read(bites))>=0)
			{
				ritorno.ensureCapacity(ritorno.length()+letto);
				for(int a=0;a<letto;a++)ritorno.append((char)bites[a]);
			}
			input.close();
		}
		catch(IOException e)
		{
			Lista.debug(e);
		}
		return ritorno;
	}
	public static void cancella(String dove)
	{
		File file=new File(dove);
		if(file.isDirectory())cancellaDirectory(dove);
		else file.delete();
	}
	private static void cancellaDirectory(String dove)
	{
		String[]file=file(dove);
		if(file!=null)for(int a=0;a<file.length;a++)cancella(dove+File.separator+file[a]);
		new File(dove).delete();
	}
	public static String[]file(String dove)
	{
		final String[]ritorno=new File(dove).list();
		if(ritorno==null)return new String[0];
		else return ritorno;
	}
	public static void cancella(Schermo schermo,String dove)
	{
		SharedPreferences.Editor sharedPreferencesEditor=schermo.getSharedPreferences(dove,0).edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
	}
	public static long dimensioni(String file)
	{
		File f=new File(file);
		long ritorno=0;
		if(f.isDirectory())
		{
			String[]lista=f.list();
			if(lista!=null)for(int a=0;a<lista.length;a++)ritorno+=dimensioni(file+File.separator+lista[a]);
		}
		else ritorno=f.length();
		return ritorno;
	}
	public static String nome(String file)
	{
		return new File(file).getName();
	}
	public static void copia(String dove,String arrivo)
	{
		String[]files=file(dove);
		for(String string:files)
		{
			String nuovo=dove+string;
			if(directory(nuovo))copia(nuovo+File.separator,arrivo+string+File.separator);
			else salva(arrivo+string,leggi(nuovo));
		}
	}
	public static void taglia(String dove,String arrivo)
	{
		copia(dove,arrivo);
		cancella(dove);
	}
	public static String sopra(String file)
	{
		return new File(file).getParent()+File.separator;
	}
	public static String casuale(String dove)
	{
		String nome;
		while(Memoria.esiste(nome=dove.concat(String.valueOf(new Random().nextInt()))));
		return nome;
	}
	public static boolean directory(String dove)
	{
		return new File(dove).isDirectory();
	}
	public static void creaDir(String dove)
	{
		new File(dove).mkdirs();
	}
	/*public Memoria(String dove)
	{
		this((Schermo)null,dove);
	}
	public Memoria(Memoria base,String aggiunta)
	{
		file=new File(base.file,aggiunta);
		sopra=file;
	}
	public Memoria(Schermo schermo,String dove)
	{
		if(schermo!=null)
		{
			sharedPreferences=schermo.getSharedPreferences(dove,0);
			sharedPreferencesEditor=sharedPreferences.edit();
			this.dove=dove;
		}
		else
		{
			file=new File(dove);
			sopra=file.getParentFile();
		}
	}
	private Memoria(File f)
	{
		file=f;
		sopra=file.getParentFile();
	}
	private String dove;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor sharedPreferencesEditor;
	private File file,sopra;
	public void salva(int cosa)
	{
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putInt(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public void salvaDentro(String cosa)
	{
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putString(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public void salva(boolean cosa)
	{
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putBoolean(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public void salva(float cosa)
	{
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putFloat(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public void salva(long cosa)
	{
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
		sharedPreferencesEditor.putLong(dove,cosa);
		sharedPreferencesEditor.commit();
	}
	public int leggi(int altrimenti)
	{
		return sharedPreferences.getInt(dove,altrimenti);
	}
	public boolean leggi(boolean altrimenti)
	{
		return sharedPreferences.getBoolean(dove,altrimenti);
	}
	public float leggi(float altrimenti)
	{
		return sharedPreferences.getFloat(dove,altrimenti);
	}
	public long leggi(long altrimenti)
	{
		return sharedPreferences.getLong(dove,altrimenti);
	}
	public String leggiDentro(String altrimenti)
	{
		return sharedPreferences.getString(dove,altrimenti);
	}
	public void salva(CharSequence cosa)
	{
		try
		{
			if(file.exists())file.delete();
			sopra.mkdirs();
			file.createNewFile();
			FileOutputStream output=new FileOutputStream(file);
			int length=cosa.length();
			for(int a=0;a<length;a++)output.write(cosa.charAt(a));
			output.flush();
			output.close();
		}
		catch(IOException e)
		{
			Lista.debug(e);
		}
	}
	public boolean esiste()
	{
		return file.exists();
	}
	public StringBuilder leggi()
	{
		StringBuilder ritorno=new StringBuilder();
		try
		{
			FileInputStream input=new FileInputStream(new File(dove));
			int letto;
			while(true)
			{
				letto=input.read();
				if(letto==-1)break;
				else ritorno.append((char)letto);
			}
			input.close();
		}
		catch(IOException e)
		{
			Lista.debug(e);
		}
		return ritorno;
	}
	public void cancella()
	{
		if(file.isDirectory())cancellaDirectory();
		else file.delete();
	}
	private void cancellaDirectory()
	{
		String[]file=file();
		if(file!=null)for(int a=0;a<file.length;a++)cancella(dove+File.separator+file[a]);
		this.file.delete();
	}
	public String[]file()
	{
		final String[]ritorno=file.list();
		if(ritorno==null)return new String[0];
		else return ritorno;
	}
	public void cancellaDentro()
	{
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();
	}
	public long dimensioni()
	{
		long ritorno=0;
		if(file.isDirectory())
		{
			String[]lista=file.list();
			if(lista!=null)for(int a=0;a<lista.length;a++)ritorno+=dimensioni(file+File.separator+lista[a]);
		}
		else ritorno=file.length();
		return ritorno;
	}
	public String nome()
	{
		return file.getName();
	}
	public void copia(String arrivo)
	{
		String[]files=file(dove);
		for(String string:files)
		{
			String nuovo=dove+string;
			if(directory())copia(nuovo+File.separator,arrivo+string+File.separator);
			else salva(arrivo+string,leggi(nuovo));
		}
	}
	public void taglia(String arrivo)
	{
		copia(arrivo);
		cancella();
	}
	public Memoria sopra()
	{
		return new Memoria(file.getParent());
	}
	public boolean directory()
	{
		return file.isDirectory();
	}
	public void creaDir()
	{
		file.mkdirs();
	}*/
}
