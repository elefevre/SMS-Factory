<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>SMS Factory</title>
  </head>

  <body>
    <h1>SMS Factory</h1>

    <table>
      <tr>
        <td><a href="_ah/admin">Admin console</a></td>
      </tr>
    </table>
    <hr/>
<p><%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<%= user.getNickname() %>&nbsp;|&nbsp;<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Sign out</a>
<%
    } else {
%>
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
<%
    }
%></p>
  </body>
</html>
