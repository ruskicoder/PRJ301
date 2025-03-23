<%-- 
    Document   : examDetails
    Created on : Mar 15, 2025, 4:26:08 PM
    Author     : PC
--%>

<%@page import="dto.QuestionDTO"%>
<%@page import="java.util.List"%>
<%@page import="dto.ExamDTO"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Exam Details</title>
    </head>
    <body>
        <%

            ExamDTO exam = (ExamDTO) request.getAttribute("EXAM");
            String examId = String.valueOf(exam.getExamId());

        %>

        <h1>Exam Details: <%= exam.getExamTitle()%></h1>


        <form action="controller" method="POST">
            <input type="text" name="search" placeholder="Search Questions">
            <input type="hidden" name="action" value="search">

            <input type="submit" value="Search">
        </form>

        <span>
            <form action="controller" method="GET">
                <input type="hidden" name="action" value="direct">
                <input type="hidden" name="direct" value="addQuestion.jsp">
                <input type="hidden" name="examId" value="<%= examId%>">
                <input type="hidden" name="questionId" value="">

                <input type="submit" value="Add new Question">
            </form>
            <form action="controller" method="POST">
                <input type="hidden" name="action" value="searchExams">

                <input type="submit" value="Back to Exam List">
            </form>
        </span>
        <h2>Questions</h2>

        <table border="1">
            <thead>
                <tr>
                    <th>Question Text</th>
                    <th>Option A</th>
                    <th>Option B</th>
                    <th>Option C</th>
                    <th>Option D</th>
                    <th>Correct Option</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%                    List<QuestionDTO> questions = (List<QuestionDTO>) request.getAttribute("QUESTION_LIST");
                    if (questions != null && !questions.isEmpty()) {
                        int questionNumber = 1;
                        for (QuestionDTO question : questions) {
                %>
                <tr>
                    <td><%= question.getQuestionText()%></td>
                    <td><%= question.getOptionA()%></td>
                    <td><%= question.getOptionB()%></td>
                    <td><%= question.getOptionC()%></td>
                    <td><%= question.getOptionD()%></td>
                    <td><%= question.getCorrectOption()%></td>
                    <td>
                        <form action="controller" method="POST">
                            <input type="hidden" name="action" value="editQuestion">
                            <input type="hidden" name="questionId" value="<%= question.getQuestionId()%>">
                            <input type="submit" value="Edit">
                        </form>

                        <form action="controller" method="POST">
                            <input type="hidden" name="action" value="deleteQuestion">
                            <input type="hidden" name="questionId" value="<%= question.getQuestionId()%>">
                            <input type="hidden" name="examId" value="<%= question.getExamId()%>">
                            <input type="submit" value="Delete">
                        </form>
                    </td>
                </tr>
                <%
                        questionNumber++;
                    }
                } else {
                %>
                <tr>
                    <td colspan="7">No questions found for this exam.</td>>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>

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