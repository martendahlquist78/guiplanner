<%@page import="marten.guiplanner.Log"%>
<%@ page isErrorPage="true" %>
Följande fel uppstod:
<%= Log.error("An error occurred: ",exception) %>