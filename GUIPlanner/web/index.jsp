<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="marten.guiplanner.Log"%>
<%@page import="marten.guiplanner.db.DBUtil"%>
<%@page import="marten.guiplanner.Activity"%>
<%@ page errorPage="errorPage.jsp" %>
<%
Activity[] activities = DBUtil.allActivities();
boolean isAuthorized = false;
if(session.getAttribute("pass") != null)
{
	isAuthorized = true;
}
%>
<html> 
<head> 
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>GUI Planner</title> 
	<link rel="stylesheet" href="style/jquery.ui.all.css"/>
	<link rel="stylesheet" href="style/jquery.ui.theme.css"/>
	<link rel="stylesheet" href="style/main.css"/>
	<script src="scripts/jquery.js"></script> 
	<script src="scripts/draganddrop.js"></script>  
	<script>
	function redirect()
	{
		window.location.href="login.jsp"
	}
	function deleteActivity(index)
	{
		handleWorkingDiv('visible');
		document.getElementById('data').value = index;
		document.getElementById('action').value = 'delete';
		document.forms['actionForm'].submit();
	}
<%
if(isAuthorized)
{
	for(int i=0;i<activities.length;i++)
	{
		Activity activity = activities[i];
%>
		$().ready(function()
		{
			$('#dragDiv_<%=activities[i].getIndex()%>').Drags({
				onMove: function(e){},
		        onDrop:function(e){
					var offset = $('#dragDiv_<%=activity.getIndex()%>').offset();
			        var index = <%=activities[i].getIndex()%>;
			        $.ajax({
			            	type: "post",
			            	url: "storedata.jsp",
			            	data: "&left="+offset.left+"&top="+offset.top+"&index="+index
			        	});
			}
			});
		});

<%
	}
}
%>	
	function setText(id, text)
	{
		var obj = document.getElementById('draggable_text_'+id);
		var value = prompt("Ange aktivtetstext", obj.innerHTML);
		if(value != null)
		{
			obj.innerHTML = value;
			$.ajax({
            	type: "post",
            	url: "storedata.jsp",
            	data: "action=setText&index="+id+"&text="+value
        	});
		}
	}
	
	function setInvoiceable(id, invoicable)
	{
		var obj = document.getElementById('invoiceable_check_'+id);
			$.ajax({
            	type: "post",
            	url: "storedata.jsp",
            	data: "action=setInvoiceable&index="+id+"&invoiceable="+invoicable
        	});
	}
	function handleWorkingDiv(mode)
	{
		document.getElementById('working').style.visibility=mode;
	}
	</script> 
</head>
<body onload="handleWorkingDiv('hidden')">
<div id="working" style="visibility: hidden;position: absolute;top:10px;left:500px;z-index:3"><img src="images/working.gif"/></div>
<%
	for(int i=0;i<activities.length;i++)
	{
		Activity activity = activities[i];
%>
 <div style="height:10%;width:10%;cursor:move;position:absolute;top:<%=activity.getTop()%>px;left:<%=activity.getLeft()%>px" id="dragDiv_<%=activity.getIndex()%>" class="ui-widget-content"> 
		<table>
<%
if(isAuthorized)
{
%>		
			<tr><td valign="top"><img title="Editera" src="images/pencil.png" style="cursor:pointer" onclick="setText(<%=activity.getIndex()%>)"/></td><td valign="top"><img title="Ta bort" src="images/delete.png" style="cursor:pointer" onclick="deleteActivity('<%=activity.getIndex()%>')"/></td></tr>
<%
}
%>			
			<tr><td style="width:100%" colspan="2"><span class="content_<%=activity.getIndex()%>" style="width: 100px" id="draggable_text_<%=activity.getIndex()%>"><%=activity.getText().replaceAll("å","&aring;").replaceAll("ä","&auml;").replaceAll("ö","&ouml;").replaceAll("Ö","&Ouml;")%></span></td></tr>
		</table>
</div>
<%
	}
if(isAuthorized)
{
%>
<form method="post" action="storedata.jsp" name="actionForm">
<input type="hidden" id="action" name="action" value="add" />
<input type="hidden" id="data" name="data" value=""/>
<input type="submit" value="Ny aktivitet" class="buttons" onclick="handleWorkingDiv('visible');"/>
</form>
<%
}
else
{
%>
<form method="post" action="storedata.jsp" name="actionForm">
	<input name="Submit" type="button" onclick="redirect()" value="Logga in" class="buttons" />
</form>
<%
}
%>
 	<div id="container">
 		<div id="ongoing" class="column"><p style="text-align: center" class="bold_14"><img src="images/Warning.png"></p></div> 
 		<div id="todo" class="column"><p style="text-align: center" class="bold_14"><img src="images/X.png"></p></div>
 		<div id="done" class="column"><p style="text-align: center" class="bold_14"><img src="images/Checkmark.png"></p></div> 
 	</div>
 	<div id="container2">
 	 		<div id="wait" class="column"><p style="text-align: center" class="bold_14"><img src="images/wait.png"></p></div>
 	</div>
 </body> 
</html>