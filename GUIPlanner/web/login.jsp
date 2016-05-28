<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="marten.guiplanner.db.DBUtil"%>
<%
if(request.getParameter("user")!=null &&
	request.getParameter("psw")!=null)
{
	if(DBUtil.login(request.getParameter("user"),request.getParameter("psw")))
	{
		session.setAttribute("pass","e43fgdgsd3634");
		response.sendRedirect("index.jsp");
	}
	else
	{
%>
		<span style="font-family:Verdana;font-size: 90%;color:red">Felaktiga uppgifter angavs. Försök igen.</span>
<%
	}
}
%>
<html>
<head>
<title>GUI Planner - Logga in</title>
<link rel="stylesheet" href="style/main.css"/>
</head>
<body>
<div id="container2">
  <div id="top">
  </div>
  <div id="leftSide">
  <fieldset>
<legend>GUI Planner</legend>
<form action="login.jsp" method="post" class="form">
  <label for="username">Användarnamn</label>
    <div class="div_texbox">
    <input name="user" type="text" onfocus="this.value=''" class="username" id="username" />
	</div>
	 <label for="password">Lösenord</label>

    <div class="div_texbox">
    <input name="psw" type="password" onfocus="this.value=''" class="password" id="password" />
	</div>
	<div class="button_div">
	<input name="Submit" type="submit" value="Logga in" class="buttons" />
	</div>
</form>
</fieldset>
</div>
</div>
</body>
</html>