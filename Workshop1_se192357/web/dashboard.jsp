<%@page import="utils.AuthUtils"%>
<%@page import="dao.ProjectDAO"%>
<%@page import="dto.ProjectDTO"%>
<%@page import="java.util.List"%>
<%@page import="dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Project Dashboard</title>
    </head>
    <body>
        <span>
            <%
                if (AuthUtils.isLoggedIn(session)) {
                    UserDTO user = (UserDTO) session.getAttribute("user");
            %>
            <h1> Welcome <%= user.getName()%> </h1>

            <form action="controller">
                <input type="hidden" name="action" value="logout"/>
                <input type="submit" value="Logout"/>
            </form>

            <hr/>

            <% if (user.getRole().equals("Founder")) { %>
            <form action="CreateProject.jsp" method="GET">
                <input type="submit" value="Add New Project"/>
            </form>

            <% } %>
            <br/>
            <% if (AuthUtils.isFounder(session)) {%>
            <form action="controller">
                <input type="hidden" name="action" value="search"/>
                Search Project <input type="text" name="searchTerm" />
                <input type="submit" value="Search"/>
            </form>
            <% } %>
            <br/>
            <br/>
            <%
                if (request.getAttribute("projects") != null) {
                    List<ProjectDTO> projects = (List<ProjectDTO>) request.getAttribute("projects");
                    if (projects != null && !projects.isEmpty()) {
            %>
            <table border="1">
                <tr>
                    <td>ProjectID</td>
                    <td>Name</td>
                    <td>Description</td>
                    <td>Status</td>
                    <td>Publish Date</td>
                    <% if (user.getRole().equals("Founder")) { %>
                    <td>Action</td>
                    <% } %>
                </tr>
                <%
                    for (ProjectDTO b : projects) {
                %>
                <tr>
                    <td><%=b.getProjectID()%></td>
                    <td><%=b.getProjectName()%></td>
                    <td><%=b.getDescription()%></td>
                    <td><%=b.getStatus()%></td>
                    <td><%=b.getDate()%></td>
                    <% if (AuthUtils.isFounder(session)) {%>
                    <td>
                        <form action="UpdateProject.jsp" method="GET">
                            <input type="hidden" name="projectID" value="<%= b.getProjectID()%>"/>
                            <input type="submit" value="Update"/>
                        </form>
                    </td>
                    <% } %>
                </tr>
                <%
                    }
                %>
            </table>
            <%      } else {
            %>
            <p>No projects found.</p>
            <%
                    }
                }
            } else {
            %>
            Error 403: You do not have permission.
            <a href="login.jsp">
                Back to login
            </a>
            <%
                }
            %>
        </span>
    </body>
</html>