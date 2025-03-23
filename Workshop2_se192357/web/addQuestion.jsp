<%-- 
    Document   : addQuestion
    Created on : Mar 15, 2025, 4:37:25 PM
    Author     : PC
--%>

<%@page import="dto.QuestionDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add/Edit Question</title>
    </head>
    <body>
        <%
            QuestionDTO question = (QuestionDTO) request.getAttribute("question");
            boolean isUpdate = (question != null);

            String questionId = "";
            String examId = request.getParameter("examId"); // Always get examId from parameter
            String questionText = "";
            String optionA = "";
            String optionB = "";
            String optionC = "";
            String optionD = "";
            String correctOption = "";

            if (isUpdate) {
                questionId = String.valueOf(question.getQuestionId());
                examId = String.valueOf(question.getExamId()); // Ensure examId is consistent
                questionText = question.getQuestionText();
                optionA = question.getOptionA();
                optionB = question.getOptionB();
                optionC = question.getOptionC();
                optionD = question.getOptionD();
                correctOption = question.getCorrectOption();
            }
            // Ensure examId is never null
            if (examId == null) {
                examId = "";
            }
        %>
        <h1><%= isUpdate ? "Edit Question" : "Add New Question"%></h1>
        <form action="controller" method="POST">
            <% if (isUpdate) {%>
            Question ID: <input type="number" name="questionId" value="<%= questionId%>" readonly><br>
            <% }%>
            Exam ID: <input type="number" name="examId" value="<%= examId%>" required readonly><br>
            Question Text: <textarea name="questionText" required><%= questionText%></textarea><br>
            Option A: <input type="text" name="optionA" value="<%= optionA%>" required><br>
            Option B: <input type="text" name="optionB" value="<%= optionB%>" required><br>
            Option C: <input type="text" name="optionC" value="<%= optionC%>" required><br>
            Option D: <input type="text" name="optionD" value="<%= optionD%>" required><br>
            Correct Option:
            <select name="correctOption" required>
                <option value="A" <%= correctOption.equals("A") ? "selected" : ""%>>A</option>
                <option value="B" <%= correctOption.equals("B") ? "selected" : ""%>>B</option>
                <option value="C" <%= correctOption.equals("C") ? "selected" : ""%>>C</option>
                <option value="D" <%= correctOption.equals("D") ? "selected" : ""%>>D</option>
            </select><br>


            <% if (isUpdate) {%>
            <input type="hidden" name="action" value="updateQuestion">
            <input type="submit" value="Update Question">
            <% } else {%>
            <input type="hidden" name="action" value="addQuestion">
            <input type="submit" value="Add Question">
            <% }%>
        </form>
        <form action="controller" method="POST">
            <input type="hidden" name="action" value="viewExamDetails">
            <input type="hidden" name="examId" value="<%= examId%>">
            <input type="submit" value="Back to Exam Detail">
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