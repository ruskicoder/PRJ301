<%-- 
    Document   : output
    Created on : Feb 10, 2025, 2:18:45 PM
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
        <%
        int n = (int)request.getAttribute("n");
        %>
        
        
              <!-- In bang cuu chuong -->
       
        <h3>Bang cuu chuong <%=n%> <br/></h3>
            <%

                for (int k = 1; k <= 10; k++) {
                    int r = n * k;
            %> 

        <%=n%> * <%=k%> = <%=r%> <br/>

        <%
            }
        %>
        <hr>
       
    </body>
</html>
