<%-- 
    Document   : takeExam
    Created on : Mar 15, 2025, 4:28:51 PM
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
        <title>Take Exam</title>
        <style>
            #timer {
                font-weight: bold;
                color: red; /* Make it stand out */
                font-size: 1.2em;
            }
        </style>
    </head>
    <body>
        <%
            ExamDTO exam = (ExamDTO) request.getAttribute("exam");
            List<QuestionDTO> questions = (List<QuestionDTO>) request.getAttribute("questions");
            int duration = exam.getDuration(); // Get exam duration in minutes
            int questionNumber = 1; // Initialize question number
%>

        <h1><%= exam.getExamTitle()%></h1>
        <p>Subject: <%= exam.getSubject()%></p>
        <p>Total Marks: <%= exam.getTotalMarks()%></p>
        <p>Duration: <%= exam.getDuration()%> minutes</p>

        <p>Time Remaining: <span id="timer"></span></p>

        <form action="controller" method="POST" id="examForm">
            <%
                if (questions != null) {
                    for (QuestionDTO question : questions) {
            %>
            <div style="margin-bottom: 20px;">
                <p><%= questionNumber%>. <%= question.getQuestionText()%></p>  <%-- Use the local variable --%>
                <input type="radio" name="question_<%= question.getQuestionId()%>" value="A"> <%= question.getOptionA()%><br>
                <input type="radio" name="question_<%= question.getQuestionId()%>" value="B"> <%= question.getOptionB()%><br>
                <input type="radio" name="question_<%= question.getQuestionId()%>" value="C"> <%= question.getOptionC()%><br>
                <input type="radio" name="question_<%= question.getQuestionId()%>" value="D"> <%= question.getOptionD()%><br>
            </div>
            <%
                        questionNumber++; // Increment question number for the next question
                    }
                }
            %>
            <input type="hidden" name="action" value="submitExam">
            <input type="submit" value="Submit Exam">
        </form>

        <form action="controller" method="POST">
            <input type="hidden" name="action" value="searchExams">
            <input type="submit" value="Back to Exam List">
        </form>

        <script>
            function startTimer(duration, display) {
                let timer = duration * 60; // Convert minutes to seconds
                let minutes, seconds;

                let countdown = setInterval(function () {
                    minutes = parseInt(timer / 60, 10);
                    seconds = parseInt(timer % 60, 10);

                    minutes = minutes < 10 ? "0" + minutes : minutes;
                    seconds = seconds < 10 ? "0" + seconds : seconds;

                    display.textContent = minutes + ":" + seconds;

                    if (--timer < 0) {
                        // Timer expired, submit the form
                        clearInterval(countdown);
                        display.textContent = "Time's up!";
                        document.getElementById("examForm").submit();
                    }
                }, 1000);
            }

            window.onload = function () {
                const examDuration = <%= duration%>; // Get duration from JSP
                const display = document.querySelector('#timer'); // Get the timer display element
                startTimer(examDuration, display); // Start the timer
            };
        </script>
    </body>
</html>