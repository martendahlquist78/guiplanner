package marten.guiplanner;

import org.apache.log4j.Logger;

public abstract class Log 
{
	protected static Logger Log = Logger.getLogger("marten.guiplanner");
	
	public static void info(String msg)
	{
		Log.info(msg);
	}

	public static void error(String msg, Throwable e)
	{
		Log.error(msg, e);
	}

}
