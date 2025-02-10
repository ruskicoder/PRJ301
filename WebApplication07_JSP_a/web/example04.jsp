<%-- 
    Document   : example04
    Created on : Feb 10, 2025, 1:36:26 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <!-- In bang cuu chuong -->
        <% for (int i = 2; i <= 9; i++) {
                for (int k = 1; k <= 10; k++) {
                    int r = i * k;
        %> 

        <%=i%> * <%=k%> = <%=r%> <br/>

        <%
            }
        %>
        <hr>
        <%
            }%>




    </body>
</html>
