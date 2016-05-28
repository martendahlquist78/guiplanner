<%@page import="marten.guiplanner.Log"%>
<%@page import="marten.guiplanner.db.DBUtil"%>
<%@page import="marten.guiplanner.Activity"%>
<%@ page errorPage="errorPage.jsp" %>
<%
String action = request.getParameter("action"); 
if(action!= null)
{
	if(action.equals("add"))
	{
		DBUtil.addActivity(new Activity("Ny aktivitet",0,0));
		response.sendRedirect("index.jsp");
	}
	else if(action.equals("delete"))
	{
		if(request.getParameter("data")!=null)
		{
			Activity activityToDelete = DBUtil.activityByIndex(Integer.parseInt(request.getParameter("data")));
			if(activityToDelete != null)
				DBUtil.deleteActivity(activityToDelete);
		}
		response.sendRedirect("index.jsp");
	}
	else if(action.equals("setText"))
	{
		if(request.getParameter("index")!=null)
		{
			Activity activityToEdit = DBUtil.activityByIndex(Integer.parseInt(request.getParameter("index")));
			if(activityToEdit != null)
			{
				activityToEdit.setText(request.getParameter("text"));
				DBUtil.updateActivity(activityToEdit);			
			}
		}
	}
	else if(action.equals("setInvoiceable"))
	{
		if(request.getParameter("index")!=null)
		{
			Activity activityToEdit = DBUtil.activityByIndex(Integer.parseInt(request.getParameter("index")));
			if(activityToEdit != null)
			{
				activityToEdit.setInvoiceable(request.getParameter("invoiceable"));
				DBUtil.updateActivity(activityToEdit);			
			}
		}		
	}
}
else if(request.getParameter("index")!= null && request.getParameter("left") != null && request.getParameter("top") !=null)
{
	Activity activity = DBUtil.activityByIndex(Integer.parseInt(request.getParameter("index")));
	if(activity != null)
	{
		activity.setLeft(Integer.parseInt(request.getParameter("left")));
		activity.setTop(Integer.parseInt(request.getParameter("top")));
		DBUtil.updateActivity(activity);
	}
}
%>