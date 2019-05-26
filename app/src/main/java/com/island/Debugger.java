package com.island;
import java.io.*;
public class Debugger
{
	private long tempo,ram;
	private Thread t;
	private DebuggerManager dm;
	private StackTraceElement e;
	private int id;
	public Debugger(DebuggerManager dm,Thread t)
	{
		this.dm=dm;
		this.t=t;
	}
	public Debugger inizio(StackTraceElement e,int n)
	{
		Runtime r=dm.r;
		id=n;
		this.e=e;
		ram=r.totalMemory()-r.freeMemory();
		tempo=System.currentTimeMillis();
		return this;
	}
	public Debugger fine(final int id)
	{
		int tid=this.id;
		Runtime r=dm.r;
		if(id!=tid)
		{
			final int fid=tid;
			throw new RuntimeException("DEBUG NON CHIUSO ID DOVEVA ESSERE:"+id+" INVECE È:"+fid);
		}
		long delta=System.currentTimeMillis()-tempo;
		long deltar=(r.totalMemory()-r.freeMemory()-ram)/1024;
		if(deltar<0)deltar=0;
		dm.salva(e,delta,deltar);
		return this;
	}
}
