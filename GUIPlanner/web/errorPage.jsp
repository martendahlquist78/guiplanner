<%@page import="marten.guiplanner.Log"%>
<%@ page isErrorPage="true" %>
F�ljande fel uppstod:
<%= Log.error("An error occurred: ",exception) %>