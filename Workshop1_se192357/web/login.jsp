<%-- 
    Document   : login
    Created on : Feb 28, 2025, 11:34:02 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <form action="controller" method="post">
            <input type="hidden" name="action" value="login"/>
            Username: <input type="text" name="username"/> <br/>
            Password: <input type="password" name="password"/> <br/>
            <input type="submit" value="Submit"/>
             <%
                        String message = request.getAttribute("error")+"";
                    %>
                    <%=message.equals("null")?"":message%>
        </form>
    </body>
</html>
