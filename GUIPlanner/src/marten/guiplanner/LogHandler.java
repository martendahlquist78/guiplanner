package marten.guiplanner;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.xml.DOMConfigurator;

public class LogHandler 
{
	  private static Logger log = Logger.getLogger("marten.guiplanner");
	  private static String LOG_PROPERTIES_FILE = "";
	  private static final String LOG_DIR = "c:/logs";
	  private static final String LOG_FILENAME = "guiplanner_log.txt";
	  private static final String FILE_SEPARATOR_PROP = "file.separator";
	  private static final int MAX_LOG_BACKUP_FILES = 200;
	  private static final String MAX_LOG_FILE_SIZE = "1024KB"; 
	  private static final int LOG_IO_BUFFER_SIZE_BYTES = 1024;
	  private static final String LOG_PATTERN = "%5p | %d | %F | Line:%L | %m%n";
	  
	  public static void init(String propertyPath)
	  {
		Properties logProperties = new Properties();
	    try
	    {
	    	final File logDir = new File( LOG_DIR );
	        if(!logDir.exists())
	        	logDir.mkdirs();
	        final File logFile = new File(logDir,String.format("%s%s",System.getProperty(FILE_SEPARATOR_PROP),LOG_FILENAME) );
	        LOG_PROPERTIES_FILE = propertyPath + "\\lib\\log4j.xml";
	        DOMConfigurator.configure(LOG_PROPERTIES_FILE);
	        PropertyConfigurator.configure(logProperties);
	        final PatternLayout pl = new PatternLayout( LOG_PATTERN );
	        final RollingFileAppender rfp = new RollingFileAppender(pl,logFile.getCanonicalPath(),true );
	        rfp.setImmediateFlush( true );
	        rfp.setBufferedIO( false );
	        rfp.setBufferSize( LOG_IO_BUFFER_SIZE_BYTES );
	        rfp.setMaxBackupIndex( MAX_LOG_BACKUP_FILES );
	        rfp.setMaxFileSize( MAX_LOG_FILE_SIZE );
	        log.addAppender( rfp );
	        
	    }
	    catch(Exception e)
	    {
	    	throw new RuntimeException("Unable to load logging property " + LOG_PROPERTIES_FILE, e);
	    }
	    finally
	    {
	    	log.info("Log4j is initated");
	    }
	  }
	  
	  public static void stopLogHandler()
	  {
		  LogManager.shutdown();
	  }
	  
}
