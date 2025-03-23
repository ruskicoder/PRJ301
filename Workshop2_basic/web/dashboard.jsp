<%-- dashboard.jsp --%>
<%@page import="dto.UserDTO"%>
<%@page import="java.util.List"%>
<%@page import="dto.ExamCategoryDTO"%>
<%@page import="utils.AuthUtils"%>
<%@page import="dao.ExamCategoryDAO"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exam Dashboard</title>
    </head>
    <body>
        <%
            UserDTO user = (UserDTO) session.getAttribute("user");
            if (user != null) {
        %>
        <h1>Welcome, <%= user.getName()%></h1>


        <h2>Exam Categories</h2>
        <%
            ExamCategoryDAO categoryDAO = new ExamCategoryDAO();
            List<ExamCategoryDTO> categories = categoryDAO.readAll();
            if (categories != null && !categories.isEmpty()) {
        %>
        <table border="1">
            <thead>
                <tr>
                    <th>Category Name</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (ExamCategoryDTO category : categories) {
                %>
                <tr>
                    <td><%= category.getCategoryName()%></td>
                    <td><%= category.getDescription() == null ? "" : category.getDescription()%></td>
                    <td>
                        <form action="controller" method="POST">

                            <input type="hidden" name="categoryId" value="<%= category.getCategoryId()%>">
                            <input type="hidden" name="action" value="searchExams">
                            <input type="submit" value="View Exams">
                        </form>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
        <% } else { %>
        <p>No categories found.</p>
        <%
            }
        %>

        <% if (AuthUtils.isInstructor(session)) { %>
        

        <% } %>
        <form action="controller" method="POST">

            <input type="hidden" name="action" value="searchExams">
            <input type="submit" value="View All Exams">

        </form>

        <form action="controller" method="POST">
            <input type="hidden" name="action" value="logout">
            <input type="submit" value="Logout">
        </form>
        <%
        } else {
        %>
        Error 403: You do not have permission.
        <a href="login.jsp">
            Back to login
        </a>
        <%
            }
        %>
    </body>
</html>