<%-- 
    Document   : UpdateProject
    Created on : Mar 6, 2025, 12:06:39 AM
    Author     : PC
--%>
<%@page import="utils.AuthUtils"%>
<%@page import="dto.ProjectDTO"%>
<%@page import="dao.ProjectDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Project</title>
    </head>
    <body>
        <%if (AuthUtils.isFounder(session)) {
        %>
        <h1>Update Project</h1>

        <%
            String projectID = request.getParameter("projectID");
            ProjectDAO projectDAO = new ProjectDAO();
            ProjectDTO project = projectDAO.readById(projectID);

            if (project != null) {
        %>
        <form action="controller" method="POST">
            <input type="hidden" name="action" value="updateProject"/>
            <input type="hidden" name="projectID" value="<%= project.getProjectID()%>"/>

            Project Name: <input type="text" name="projectName" value="<%= project.getProjectName()%>" required/><br/><br/>
            Description: <textarea name="description" rows="4" cols="50"><%= project.getDescription()%></textarea><br/><br/>
            Status:
            <select name="status" required>
                <option value="Ideation" <%= project.getStatus().equals("Ideation") ? "selected" : ""%>>Ideation</option>
                <option value="Development" <%= project.getStatus().equals("Development") ? "selected" : ""%>>Development</option>
                <option value="Launch" <%= project.getStatus().equals("Launch") ? "selected" : ""%>>Launch</option>
                <option value="Scaling" <%= project.getStatus().equals("Scaling") ? "selected" : ""%>>Scaling</option>
            </select><br/><br/>
            Estimated Launch Date: <input type="date" name="estimatedLaunch" value="<%= project.getDate()%>" required/><br/><br/>

            <input type="submit" value="Update Project"/>
        </form>
        <%
        } else {
        %>
        <p>Project not found.</p>
        <%
            }
        %>
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