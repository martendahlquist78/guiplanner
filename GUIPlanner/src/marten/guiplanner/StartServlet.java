package marten.guiplanner;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import marten.guiplanner.db.DBHandler;

public class StartServlet extends HttpServlet
{
	protected static Logger Log = Logger.getLogger("marten.guiplanner");
    private static final long serialVersionUID = 1L;
    public static ServletConfig servletConfig = null;
    private DBHandler db = null;
    
	public void init(ServletConfig servletConfig) throws ServletException
    {
		try
		{
			System.out.println("**** Starting GUIPlanner ****");
			super.init(servletConfig);
			LogHandler.init(servletConfig.getServletContext().getRealPath("WEB-INF"));
			DBHandler db = new DBHandler();
			if(db.connect())
			{
				Log.info("Connection to db was established");
			}
			else
			{
				Log.info("Unable to connect to db");
			}
		}
		catch(Exception e)
		{
			Log.error("An error occured in StartServlet init ",e);
		}
    }
	
	

	public void destroy()
	{
		try
		{
			Log.info("**** Stopping GUIPlanner ****");
			System.out.println("**** Stopping GUIPlanner ****");
			if(db != null)
				db.disconnect();
			LogHandler.stopLogHandler();
		}
		catch(Exception e)
		{
			Log.error("An error occured in StartServlet destroy ",e);
		}
	}
	
    public void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {}
}
