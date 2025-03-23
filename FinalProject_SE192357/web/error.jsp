<%-- 
    Document   : error
    Created on : Mar 10, 2025, 11:29:53 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <link rel="stylesheet" type="text/css" href="assets/css/error.css">
    </head>
    <body>
        <div class = "header-container">
            <%@include file="header.jsp" %>
        </div>

        <div class="error-container">
            <h1>Error!</h1>
            <p>
                <%
                            String errorMessage = (String) request.getAttribute("errorMessage");
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                out.println(errorMessage);
                            }
                        %>
            </p>
             <a href="login.jsp" class="back-link">Back to Login</a>
           
        </div>
    </body>
</html>