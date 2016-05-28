package marten.guiplanner.db;



import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import marten.guiplanner.Log;

public class DBHandler extends Log
{
	private Properties dbProperties = new Properties();
	private final String DBNAME = "ACTIVITIES";
	private final String DBNAMEUSR = "USERS";
    public Connection dbConnection;
    private boolean isConnected;
    protected static PreparedStatement stmtSaveNewRecord;
    protected static PreparedStatement stmtUpdateExistingRecord;
    protected static PreparedStatement stmtGetActivities;
    protected static PreparedStatement stmtGetActivity;
    protected static PreparedStatement stmtDeleteActivity;
    protected static PreparedStatement stmtGetUser;
    protected static PreparedStatement stmtCreateUser;
    
	public DBHandler()
	{
		try
		{
			Log.info("Initializing database");
		    String systemDir = setDBSystemDir();
		    dbProperties = loadDBProperties();
		    String driverName = dbProperties.getProperty("derby.driver");
		    loadDatabaseDriver(driverName);
		    Log.info("Check if database exists...");
		     if(!dbExists())
		     {
		    	 Log.info("Found no database, creating new in "+systemDir);
		    	 createDatabase();
		     }
		     else
		     {
		    	 Log.info("Found database");
		     }
		}
		catch(Exception e)
		{
			Log.error("An error occurred in initDatabase", e);
		}
	}
	
	private boolean dbExists()
    {
        String dbLocation = getDatabaseLocation();
        File dbFileDir = new File(dbLocation);
        if (dbFileDir.exists())
        {
            return true;
        }
        return false;
    }

	private Properties loadDBProperties()throws Exception
	{
	    InputStream dbPropInputStream = null;
	    dbPropInputStream = DBHandler.class.getResourceAsStream("/Configuration.properties");
	    dbProperties.load(dbPropInputStream);
	    return dbProperties;
	}

	private void loadDatabaseDriver(String driverName) throws Exception
	{
	    Class.forName(driverName);
	}
	
	private boolean tableExist(String tableName)
	{
		 String[] names = { "TABLE" };
		 ResultSet result = null;
	     DatabaseMetaData metadata = null;
	     try 
	     {
	    	 metadata = dbConnection.getMetaData();
	         result = metadata.getTables(null, null, "%", names);
	         while(result.next())
	         {	
	        	 if(result.getString(3).equals(tableName))
	        	 return true;
	         }
	     }
	     catch(java.sql.SQLException e)
	     {
	    	 Log.error("An error occurred in tableExist", e);
	     }
	     return false;
	}
	
    public boolean connect()
    {
    	try
    	{
    		Log.info("Connecting to db");
    		String dbUrl = getDatabaseUrl();
    		dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
    		isConnected = dbConnection != null;
    		if(isConnected)
    		{
    			if(!tableExist(DBNAMEUSR))
    				createTable(dbConnection);
    			prepereStatements();
    				
    		}		
    	}
    	catch(Exception e)
    	{
    		Log.error("An error occurred in connect", e);
    	}
        return isConnected;
    }
    
    private void prepereStatements()
    {
    	try
    	{
	    	Log.info("Prepering stmtSaveNewRecord");
	    	stmtSaveNewRecord = dbConnection.prepareStatement(
	    		    "INSERT INTO PLANNER.ACTIVITIES " +
	    		    "(TEXT, TOP_VAL, LEFT_VAL) " +
	    		    "VALUES (?, ?, ?)",
	    		    Statement.RETURN_GENERATED_KEYS);
	    	
	    	Log.info("Prepering stmtUpdateExistingRecord");
	    	stmtUpdateExistingRecord = dbConnection.prepareStatement(
	    			"UPDATE PLANNER.ACTIVITIES " +
	    			"SET TEXT = ?, " +
	    			"TOP_VAL = ?, " +
	    			"LEFT_VAL = ? " +
	    			"WHERE ID = ?");
	    	
	    	Log.info("Prepering stmtGetActivity");
	    	stmtGetActivity = dbConnection.prepareStatement(
	    		    "SELECT * FROM PLANNER.ACTIVITIES " +
	                "WHERE ID = ?");
	    	
	    	Log.info("Prepering stmtGetActivities");
	    	stmtGetActivities = dbConnection.prepareStatement(
	    			"SELECT ID, TEXT, TOP_VAL, LEFT_VAL FROM PLANNER.ACTIVITIES "  +
	    			"ORDER BY ID ASC");
	    	
	    	Log.info("Prepering stmtDeleteActivity");
	    	stmtDeleteActivity = dbConnection.prepareStatement(
	    			 "DELETE FROM PLANNER.ACTIVITIES " +
	    	            "WHERE ID = ?");
	    	
	    	Log.info("Prepering stmtGetUser");
	    	stmtGetUser = dbConnection.prepareStatement(
	    			"SELECT * FROM PLANNER.USERS " +
	    			"WHERE USERNAME = ? AND PWD = ? ");
	    	
    	}
    	catch(Exception e)
    	{
      		Log.error("An error occurred in prepereStatements", e);
    	}
    }
    
    public void disconnect() throws Exception
    {
        if(isConnected)
        {
        	Log.info("Disconnecting db");
            dbProperties.put("shutdown", "true");
            isConnected = false;
        }
    }
	
	private boolean createDatabase() throws Exception
	{
		boolean bCreated = false;
        Connection dbConnection = null;
	    String dbUrl = getDatabaseUrl();
	    dbProperties.put("create", "true");
	    dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
	    bCreated = createTables(dbConnection);
        dbProperties.remove("create");
	    return bCreated;
	}
	
    public String getDatabaseUrl()
    {
        String dbUrl = dbProperties.getProperty("derby.url") + DBNAME;
        return dbUrl;
    }
    
    public String getDatabaseLocation()
    {
        String dbLocation = System.getProperty("derby.system.home") + "/" + DBNAME;
        return dbLocation;
    }
	
    private boolean createTables(Connection dbConnection) throws Exception
    {
        boolean bCreatedTables = false;
        Statement statement = dbConnection.createStatement();
        Log.info(strCreateActivityTable);
        statement.execute(strCreateActivityTable);
        bCreatedTables = true;
        return bCreatedTables;
    }
    
    private boolean createTable(Connection dbConnection) throws Exception
    {
        boolean bCreatedTables = false;
        Statement statement = dbConnection.createStatement();
        Log.info(strCreateUserTable);
        statement.execute(strCreateUserTable);
        bCreatedTables = true;
        return bCreatedTables;
    }
	
	private String setDBSystemDir()
	{
	    String userHomeDir = System.getProperty("user.home", ".");
	    String systemDir = userHomeDir + "/.guiplanner";
	    System.setProperty("derby.system.home", systemDir);
	    File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir();
        return systemDir;
	}
	
    private final String strCreateActivityTable =
        "create table PLANNER."+DBNAME+"(" +
        "ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
        "TEXT VARCHAR(255)," +
        "TOP_VAL INTEGER NOT NULL, " +
        "LEFT_VAL INTEGER NOT NULL" +
        ")";
    
    private final String strCreateUserTable =
        "create table PLANNER."+DBNAMEUSR+"(" +
        "USERNAME VARCHAR(255)," +
        "PWD VARCHAR(255) " +
        ")";

}