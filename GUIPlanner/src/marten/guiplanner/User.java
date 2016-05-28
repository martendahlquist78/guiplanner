package marten.guiplanner;

public class User extends Log
{
	public String username = "";
	public String pwd = "";

	public User(String username, String pwd)
	{
		this.username = username;
		this.pwd = pwd;
	}
	public String getUsername() {
		return username;
	}
	public String getPwd() {
		return username;
	}
}
