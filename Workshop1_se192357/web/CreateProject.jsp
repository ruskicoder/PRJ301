<%-- 
    Document   : CreateDashboard
    Created on : Mar 5, 2025, 7:53:57 PM
    Author     : PC
--%>

<%@page import="utils.AuthUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Project</title>
    </head>
    <body>
        <%if (AuthUtils.isFounder(session)) {
        %>
        <h1>Create New Project</h1>
        <form action="controller" method="POST">
            <input type="hidden" name="action" value="createProject"/>

            Project ID: <input type="number" name="projectID" required/><br/><br/>
            Project Name: <input type="text" name="projectName" required/><br/><br/>
            Description: <textarea name="description" rows="4" cols="50"></textarea><br/><br/>
            Status:
            <select name="status" required>
                <option value="Ideation">Ideation</option>
                <option value="Development">Development</option>
                <option value="Launch">Launch</option>
                <option value="Scaling">Scaling</option>
            </select><br/><br/>
            Estimated Launch Date: <input type="date" name="estimatedLaunch" required/><br/><br/>

            <input type="submit" value="Create Project"/>
        </form>

        <a href="dashboard.jsp">Back to Dashboard</a>
        <%
        } else {
        %>
        Error 403: You do not have permission.
        <a href="login.jsp">
            Back to login
        </a><%
                }%>
    </body>
</html>