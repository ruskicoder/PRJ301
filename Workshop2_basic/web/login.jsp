<%-- 
    Document   : login
    Created on : Feb 28, 2025, 11:34:02 PM
    Author     : PC
--%>

<%-- login.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h1>Login</h1>
        
        <form action="controller" method="POST">
            Username: <input type="text" name="username" required><br>
            Password: <input type="password" name="password" required><br>
            <input type="hidden" name="action" value="login">
            <input type="submit" value="Login">
        </form>
        
         <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <p style="color:red;"><%= error%></p>
        <%
            }
        %>
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
        <p style="color:green;"><%= message%></p>
        <%
            }
        %>
    </body>
</html>