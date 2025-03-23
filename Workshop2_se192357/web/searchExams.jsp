<%-- 
    Document   : searchExams
    Created on : Mar 15, 2025, 4:24:01 PM
    Author     : PC
--%>
<%@page import="java.util.List"%>
<%@page import="dto.ExamCategoryDTO"%>
<%@page import="dto.ExamDTO"%>
<%@page import="utils.AuthUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Exams</title>
    </head>
    <body>
        <%
            Object categoryIdObj = request.getAttribute("categoryId");
            String categoryId = null;
            if (categoryIdObj != null) {
                if (categoryIdObj instanceof Integer) {
                    categoryId = String.valueOf((Integer) categoryIdObj);
                } else if (categoryIdObj instanceof String) {
                    categoryId = (String) categoryIdObj;
                }
            }

            HttpSession currentSession = request.getSession(false); // Use false to get session
            boolean isStudent = (currentSession != null) && AuthUtils.isStudent(currentSession);
            boolean isLoggedin = (currentSession != null) && AuthUtils.isLoggedIn(currentSession);

        %>

        <h1>Search Exams</h1>

        <form action="controller" method="POST">
            <input type="text" name="search" placeholder="Search Exams">
            <input type="hidden" name="action" value="search">

            <input type="submit" value="Search">

        </form>
        <% if (isLoggedin) {%>
        <form action="controller" method="POST">

            <select name="categoryId" onchange="this.form.submit()">
                <option value="">Select Category</option>
                <%
                    List<ExamCategoryDTO> categories = (List<ExamCategoryDTO>) request.getAttribute("EXAM_CATEGORIES");
                    if (categories != null) {
                        for (ExamCategoryDTO category : categories) {
                %>
                <option value="<%= category.getCategoryId()%>" <%= categoryId != null && categoryId.equals(String.valueOf(category.getCategoryId())) ? "selected" : ""%>><%= category.getCategoryName()%></option>
                <%
                        }
                    }
                %>
            </select>
            <input type="hidden" name="action" value="searchExams">

        </form>
        <% }%>

        <table border="1">
            <thead>
                <tr>
                    <th>Exam Title</th>
                    <th>Subject</th>
                    <th>Total Marks</th>
                    <th>Duration (minutes)</th>
                        <% if (!isStudent) { %>
                    <th>Category ID</th>
                    <th>Actions</th>
                        <% } else { %>
                    <th>Action</th>
                        <% }%>

                </tr>
            </thead>
            <tbody>
                <%  List<ExamDTO> exams = (List<ExamDTO>) request.getAttribute("EXAM_LIST");
                    if (exams != null && !exams.isEmpty()) {
                        for (ExamDTO exam : exams) {
                %>
                <tr>
                    <td><%= exam.getExamTitle()%></td>
                    <td><%= exam.getSubject()%></td>
                    <td><%= exam.getTotalMarks()%></td>
                    <td><%= exam.getDuration()%></td>
                    <% if (!isStudent) {%>
                    <td><%= exam.getCategoryId()%></td>
                    <td>

                        <form action="controller" method="POST">
                            <input type="hidden" name="action" value="viewExamDetails">
                            <input type="hidden" name="examId" value="<%= exam.getExamId()%>">
                            <input type="submit" value="View Details">
                        </form>

                        <form action="controller" method="POST">
                            <input type="hidden" name="action" value="deleteExam">
                            <input type="hidden" name="examId" value="<%= exam.getExamId()%>">
                            <input type="submit" value="Delete">
                        </form>
                        <form action="controller" method = "POST">
                            <input type="hidden" name="action" value="editExam">
                            <input type="hidden" name="examId" value="<%= exam.getExamId()%>">
                            <input type="submit" value="Edit">
                        </form>
                    </td>
                    <% } else {%>

                    <td>
                        <form action="controller" method="POST">
                            <input type="hidden" name="action" value="takeExam">
                            <input type="hidden" name="examId" value="<%= exam.getExamId()%>">
                            <input type="submit" value="Take Exam">
                        </form>
                    </td>
                    <% }%>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5">No exams found.</td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

        <form action="controller" method="GET">
            <input type="hidden" name="action" value="direct">
            <input type="hidden" name="direct" value="dashboard.jsp">
            <input type="submit" value="Back to Dashboard">
        </form>
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
        <p style="color:green;"><%= message%></p>
        <%
            }
        %>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <p style="color:red;"><%= errorMessage%></p>
        <%
            }
        %>
    </body>
</html>