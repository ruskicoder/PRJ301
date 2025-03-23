<%-- 
    Document   : examResults
    Created on : Mar 15, 2025, 4:29:18 PM
    Author     : PC
--%>

<%@page import="dto.UserDTO"%>
<%@page import="dto.ExamDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exam Results</title>
    </head>
    <body>
        <%
            ExamDTO exam = (ExamDTO) request.getAttribute("exam");

        %>
        <h1>Exam Results: <%= exam.getExamTitle()%></h1>
        <%
            UserDTO user = (UserDTO) request.getAttribute("user");
            Integer score = (Integer) request.getAttribute("score");
            Integer totalQuestions = (Integer) request.getAttribute("totalQuestions");
        %>

        <h2>Student: <%= user.getName()%></h2>
        <p>Score: <%= score%> / <%= totalQuestions%></p>

        <form action="controller" method="GET">
            <input type="hidden" name="action" value="direct">
            <input type="hidden" name="direct" value="dashboard.jsp">
            <input type="submit" value="Back to Dashboard">
        </form>
    </body>
</html>