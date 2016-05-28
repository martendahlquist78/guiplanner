package marten.guiplanner.db;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import marten.guiplanner.Activity;
import marten.guiplanner.User;

public class DBUtil extends DBHandler
{
	public static void updateActivity(Activity activity)
	{
	    try 
	    {
	        stmtUpdateExistingRecord.clearParameters();
	        stmtUpdateExistingRecord.setString(1, activity.getText());
	        stmtUpdateExistingRecord.setInt(2, activity.getTop());
	        stmtUpdateExistingRecord.setInt(3, activity.getLeft());
	        stmtUpdateExistingRecord.setInt(4, activity.getIndex());
	        stmtUpdateExistingRecord.executeUpdate();
	    } 
	    catch(SQLException sqle) 
	    {
	        Log.error("An error occurred in updateActivity" , sqle);
	    }
	}
	

	
	public static int addActivity(Activity activity)
	{
	    int id = -1;
	    try 
	    {
	        stmtSaveNewRecord.clearParameters();
	        stmtSaveNewRecord.setString(1, activity.getText());
	        stmtSaveNewRecord.setInt(2, activity.getTop());
	        stmtSaveNewRecord.setInt(3, activity.getLeft());
	        stmtSaveNewRecord.executeUpdate();
	        ResultSet results = stmtSaveNewRecord.getGeneratedKeys();
	        if (results.next())
	        {
	            id = results.getInt(1);
	        }
	    } 
	    catch(SQLException sqle) 
	    {
	        Log.error("An error occurred in addActivity" , sqle);
	    }
	    return id;
	} 
	
	public static boolean deleteActivity(Activity activity)
	{
		boolean bDeleted = false;
        try
        {
        	stmtDeleteActivity.clearParameters();
        	stmtDeleteActivity.setInt(1, activity.getIndex());
        	stmtDeleteActivity.executeUpdate();
            bDeleted = true;
        }
        catch (SQLException sqle) 
        {
        	 Log.error("An error occurred in deleteActivity" , sqle);
        }
        return bDeleted;
	}
	
	public static boolean login(String user, String pwd)
	{
		return userByUserNameAndPwd(user,pwd)!=null;
	}
	
	public static Activity activityByIndex(int index)
	{
		Activity activity = null;
        try 
        {
        	stmtGetActivity.clearParameters();
        	stmtGetActivity.setInt(1, index);
            ResultSet result = stmtGetActivity.executeQuery();
            if (result.next())
            {
            	int indx = result.getInt("ID");
                String text = result.getString("TEXT");
                int top = result.getInt("TOP_VAL");
                int left = result.getInt("LEFT_VAL");
                activity = new Activity(indx, text, left, top);
            }
        }
        catch(SQLException sqle) 
        {
        	Log.error("An error occurred in activityByIndex" , sqle);
        }
        return activity;
	}
	
	private static User userByUserNameAndPwd(String userName, String pwd)
	{
		User user = null;
        try 
        {
        	stmtGetUser.clearParameters();
        	stmtGetUser.setString(1, userName);
        	stmtGetUser.setString(2, pwd);
            ResultSet result = stmtGetUser.executeQuery();
            if (result.next())
            {
                String username = result.getString("USERNAME");
                String pswd = result.getString("PWD");
                user = new User(username, pswd);
            }
        }
        catch(SQLException sqle) 
        {
        	Log.error("An error occurred in userByUserNameAndPwd" , sqle);
        }
        return user;
	}
		
	public static Activity[] allActivities()
	{
		Vector<Activity> activities = new Vector<Activity>(0);
        ResultSet results = null;
        try 
        {
            results = stmtGetActivities.executeQuery();
            while(results.next()) 
            {
            	int index = results.getInt(1);
                String text = results.getString(2);
                int top = results.getInt(3);
                int left = results.getInt(4);
                Activity newActivity = new Activity(index, text, left, top);
                activities.add(newActivity);
            }
        } 
        catch (SQLException sqle) 
        {
        	Log.error("An error occurred in allActivities" , sqle);
        }            
        return activities.toArray(new Activity[activities.size()]);
	}
}
