<%-- 
    Document   : addExams
    Created on : Mar 15, 2025, 4:25:10 PM
    Author     : PC
--%>

<%@page import="utils.AuthUtils"%>
<%@page import="dto.ExamCategoryDTO"%>
<%@page import="java.util.List"%>
<%@page import="dao.ExamCategoryDAO"%>
<%@page import="dto.ExamDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add/Edit Exam</title>
    </head>
    <body>
        <%
            ExamDTO exam = (ExamDTO) request.getAttribute("exam");
            boolean isUpdate = (exam != null);

            String examId = "";
            String examTitle = "";
            String subject = "";
            String categoryId = "";
            String totalMarks = "";
            String duration = "";

            if (isUpdate) {
                examId = String.valueOf(exam.getExamId());
                examTitle = exam.getExamTitle();
                subject = exam.getSubject();
                categoryId = String.valueOf(exam.getCategoryId());
                totalMarks = String.valueOf(exam.getTotalMarks());
                duration = String.valueOf(exam.getDuration());
            }
        %>

        <h1><%= isUpdate ? "Edit Exam" : "Add New Exam"%></h1>

        <form action="controller" method="POST">
            <% if (isUpdate) {%>
            Exam ID: <input type="text" name="examId" value="<%= examId%>" readonly><br>
            <% }%>
            Exam Title: <input type="text" name="examTitle" value="<%= examTitle%>" required><br>
            Subject: <input type="text" name="subject" value="<%= subject%>" required><br>
            Category:
            <select name="categoryId" required>

                <%
                    ExamCategoryDAO categoryDAO = new ExamCategoryDAO();
                    List<ExamCategoryDTO> categories = categoryDAO.readAll();
                    if (categories != null) {
                        for (ExamCategoryDTO category : categories) {
                %>
                <option value="<%= category.getCategoryId()%>" <%= categoryId.equals(String.valueOf(category.getCategoryId())) ? "selected" : ""%>><%= category.getCategoryName()%></option>
                <%
                        }
                    }
                %>
            </select><br>
            Total Marks: <input type="number" name="totalMarks" value="<%= totalMarks%>" required><br>
            Duration (minutes): <input type="number" name="duration" value="<%= duration%>" required><br>

            <% if (isUpdate) { %>
            <input type="hidden" name="action" value="updateExam">
            <input type="submit" value="Update Exam">
            <% } else { %>
            <input type="hidden" name="action" value="addExam">
            <input type="submit" value="Add Exam">
            <% } %>

        </form>
        <form action="controller" method="POST">
            <input type="hidden" name="action" value="searchExams">
            <input type="submit" value="Back to Exam List">
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