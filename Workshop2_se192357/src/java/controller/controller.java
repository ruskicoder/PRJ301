/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PC
 */
package controller;

import dao.ExamCategoryDAO;
import dao.ExamDAO;
import dao.QuestionDAO;
import dto.UserDTO;
import dto.ExamCategoryDTO;
import dto.ExamDTO;
import dto.QuestionDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.AuthUtils;

@WebServlet(name = "controller", urlPatterns = {"/controller"})
public class controller extends HttpServlet {

    private static final String LOGIN_PAGE = "login.jsp";
    private static final String DASHBOARD_PAGE = "dashboard.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String ERROR_404 = "404 Not Found";
    private static final String SEARCH_EXAMS_PAGE = "searchExams.jsp";
    private static final String ADD_EXAMS_PAGE = "addExams.jsp";
    private static final String EXAM_DETAILS_PAGE = "examDetails.jsp";
    private static final String TAKE_EXAM_PAGE = "takeExam.jsp";
    private static final String EXAM_RESULTS_PAGE = "examResults.jsp";
    private static final String ADD_QUESTION_PAGE = "addQuestion.jsp";

    private String processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = LOGIN_PAGE;
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null && password != null) {
            if (AuthUtils.isValidLogin(username, password)) {
                UserDTO user = AuthUtils.getUser(username);
                session.setAttribute("user", user);
                url = DASHBOARD_PAGE;
            } else {
                request.setAttribute("error", "Incorrect Username or Password");
                url = LOGIN_PAGE;
            }
        }
        return url;
    }

    private String processSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = LOGIN_PAGE; // Default
        HttpSession session = request.getSession();
        if (AuthUtils.isLoggedIn(session)) {
            String action = request.getParameter("action");
            String searchTerm = request.getParameter("search");
            String categoryIdStr = request.getParameter("categoryId");
            String examIdStr = request.getParameter("examId");

            if (searchTerm == null) {
                searchTerm = "";
            }

            if (action == null) {
                action = "searchExams"; // Default
            }

            if (action.equals("search") || action.equals("searchExams") || action.equals("viewExamCategories")) {
                url = SEARCH_EXAMS_PAGE;
                ExamDAO examDAO = new ExamDAO();
                List<ExamDTO> exams;

                if (AuthUtils.isStudent(session)) { // Student
                    if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                        int categoryId = Integer.parseInt(categoryIdStr);
                        exams = examDAO.searchByCategoryIdWithQuestions(categoryId); // Only with questions
                        request.setAttribute("categoryId", categoryId);
                    } else if (searchTerm != null && !searchTerm.isEmpty()) {
                        exams = examDAO.searchWithQuestions(searchTerm);  // Only with questions
                    } else {
                        exams = examDAO.readAllWithQuestions(); // Only with questions
                    }

                } else { // Instructor (all exams)
                    if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                        int categoryId = Integer.parseInt(categoryIdStr);
                        exams = examDAO.searchByCategoryId(categoryId); // all
                        request.setAttribute("categoryId", categoryId);
                    } else if (searchTerm != null && !searchTerm.isEmpty()) {
                        exams = examDAO.search(searchTerm);  // all
                    } else {
                        exams = examDAO.readAll(); // all
                    }

                }
                request.setAttribute("EXAM_LIST", exams);

                ExamCategoryDAO categoryDAO = new ExamCategoryDAO(); // category dropdown
                List<ExamCategoryDTO> categories = categoryDAO.readAll();
                request.setAttribute("EXAM_CATEGORIES", categories);

            } else if (action.equals("viewExamDetails")) {
                url = EXAM_DETAILS_PAGE;

                QuestionDAO questionDAO = new QuestionDAO();
                List<QuestionDTO> questions;

                if (searchTerm != null && !searchTerm.isEmpty()) {
                    questions = questionDAO.search(searchTerm); //search question
                } else {
                    int examId = Integer.parseInt(examIdStr);
                    questions = questionDAO.readAllByExamId(examId); // load question
                }
                ExamDAO examDAO = new ExamDAO();
                ExamDTO exam = examDAO.readById(Integer.parseInt(examIdStr));
                request.setAttribute("EXAM", exam);
                request.setAttribute("QUESTION_LIST", questions);
            } else {
                url = DASHBOARD_PAGE; // default
            }
        }
        return url;
    }

    private String processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        request.setAttribute("message", "You have been logged out.");
        return LOGIN_PAGE;
    }

    private String processAddExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ADD_EXAMS_PAGE; // default
        try {
            HttpSession session = request.getSession();
            if (!AuthUtils.isInstructor(session)) {
                request.setAttribute("errorMessage", "Only instructors can add exams.");
                url = DASHBOARD_PAGE;  // redirect
                return url;
            }

            // Check POST
            if (request.getMethod().equalsIgnoreCase("POST")) {
                // process
                String examTitle = request.getParameter("examTitle");
                String subject = request.getParameter("subject");
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                int totalMarks = Integer.parseInt(request.getParameter("totalMarks"));
                int duration = Integer.parseInt(request.getParameter("duration"));
                ExamDAO examDAO = new ExamDAO();
                int examId = examDAO.getNextId();

                ExamDTO exam = new ExamDTO(examId, examTitle, subject, categoryId, totalMarks, duration);

                if (examDAO.create(exam)) {
                    request.setAttribute("message", "Exam added successfully!");
                    // redirect
                    request.setAttribute("action", "searchExams");
                    url = processSearch(request, response); // show
                    return url;
                } else {
                    request.setAttribute("errorMessage", "Failed to add exam.");
                    url = ADD_EXAMS_PAGE;
                    // stay
                }
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format.");
            // stay
        }
        return url;
    }

    private String processDeleteExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = SEARCH_EXAMS_PAGE;  // Default
        try {
            int examId = Integer.parseInt(request.getParameter("examId"));
            ExamDAO examDAO = new ExamDAO();
            if (examDAO.delete(examId)) {
                request.setAttribute("message", "Exam deleted successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to delete exam.");
            }
            request.setAttribute("action", "searchExams");
            url = processSearch(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid exam ID.");
        }
        return url;
    }

    private String processEditExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ADD_EXAMS_PAGE;
        try {
            HttpSession session = request.getSession();
            if (!AuthUtils.isInstructor(session)) {
                request.setAttribute("errorMessage", "Only instructors can edit exams.");
                url = DASHBOARD_PAGE;
                return url;
            }
            int examId = Integer.parseInt(request.getParameter("examId"));
            ExamDAO examDAO = new ExamDAO();
            ExamDTO exam = examDAO.readById(examId);
            if (exam != null) {
                request.setAttribute("exam", exam); // Set the exam data for the form

            } else {
                request.setAttribute("errorMessage", "Exam not found.");
                url = SEARCH_EXAMS_PAGE; // Redirect back to search if exam not found

            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format for updating exam.");

        }

        return url;
    }

    private String processUpdateExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ADD_EXAMS_PAGE; // default
        try {
            int examId = Integer.parseInt(request.getParameter("examId"));
            String examTitle = request.getParameter("examTitle");
            String subject = request.getParameter("subject");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            int totalMarks = Integer.parseInt(request.getParameter("totalMarks"));
            int duration = Integer.parseInt(request.getParameter("duration"));

            ExamDTO exam = new ExamDTO(examId, examTitle, subject, categoryId, totalMarks, duration);
            ExamDAO examDAO = new ExamDAO();

            if (request.getMethod().equalsIgnoreCase("POST")) {

                if (examDAO.update(exam)) {
                    request.setAttribute("message", "Exam updated successfully!");
                    request.setAttribute("action", "searchExams"); // redirect
                    url = processSearch(request, response); // Use
                    return url; // Return
                } else {
                    request.setAttribute("errorMessage", "Failed to update exam.");
                    // Stay, repopulate
                    request.setAttribute("exam", exam); // send

                }

            } else { //first time
                examDAO = new ExamDAO();
                exam = examDAO.readById(examId); //fetch
                request.setAttribute("exam", exam);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format for updating exam.");

        }

        return url; //return addExams.jsp
    }

    private String processViewExamDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = EXAM_DETAILS_PAGE; // default
        try {
            int examId = Integer.parseInt(request.getParameter("examId"));
            QuestionDAO questionDAO = new QuestionDAO();
            List<QuestionDTO> questions = questionDAO.readAllByExamId(examId);
            ExamDAO examDAO = new ExamDAO();
            ExamDTO exam = examDAO.readById(examId);
            request.setAttribute("examId", examId);
            request.setAttribute("EXAM", exam);
            request.setAttribute("QUESTION_LIST", questions);
            request.setAttribute("action", "viewExamDetails");

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid exam ID.");
            url = ERROR_PAGE; // redirect
        }
        return url;
    }

    private String processAddQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ADD_QUESTION_PAGE;  // default
        try {
            HttpSession session = request.getSession();
            if (!AuthUtils.isInstructor(session)) {
                request.setAttribute("errorMessage", "Only instructors can add questions.");
                url = DASHBOARD_PAGE; // redirect
                return url;
            }
            int examId = Integer.parseInt(request.getParameter("examId")); // Get

            // Check POST
            if (request.getMethod().equalsIgnoreCase("POST")) {
                // process
                QuestionDAO questionDAO = new QuestionDAO();
                int questionId = questionDAO.getNextId();

                String questionText = request.getParameter("questionText");
                String optionA = request.getParameter("optionA");
                String optionB = request.getParameter("optionB");
                String optionC = request.getParameter("optionC");
                String optionD = request.getParameter("optionD");
                String correctOption = request.getParameter("correctOption");

                QuestionDTO question = new QuestionDTO(questionId, examId, questionText, optionA, optionB, optionC, optionD, correctOption);

                if (questionDAO.create(question)) {
                    request.setAttribute("message", "Question added successfully!");
                    // redirect
                    url = EXAM_DETAILS_PAGE;
                    request.setAttribute("examId", examId);
                    request.setAttribute("action", "viewExamDetails"); // Set

                    url = processViewExamDetails(request, response); // show
                    return url;
                } else {
                    request.setAttribute("errorMessage", "Failed to add question.");
                    // stay
                }
            } else {
                // Initial GET, set
                request.setAttribute("examId", examId);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format.");
            // Stay

        }
        return url; // Return
    }

    private String processEditQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ADD_QUESTION_PAGE;
        try {
            HttpSession session = request.getSession();
            if (!AuthUtils.isInstructor(session)) {
                request.setAttribute("errorMessage", "Only instructors can edit exams.");
                url = DASHBOARD_PAGE;
                return url;
            }
            int questionId = Integer.parseInt(request.getParameter("questionId"));
            QuestionDAO questionDAO = new QuestionDAO();
            QuestionDTO question = questionDAO.readById(questionId);
            if (question != null) {
                request.setAttribute("question", question); // Set the question data for the form
                request.setAttribute("examId", question.getExamId()); //keep examID
            } else {
                request.setAttribute("errorMessage", "Question not found.");
                url = EXAM_DETAILS_PAGE; // Redirect back to search if exam not found

            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format for updating question.");

        }
        return url;
    }

    private String processUpdateQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ADD_QUESTION_PAGE;
        try {
            int questionId = Integer.parseInt(request.getParameter("questionId"));
            int examId = Integer.parseInt(request.getParameter("examId"));
            String questionText = request.getParameter("questionText");
            String optionA = request.getParameter("optionA");
            String optionB = request.getParameter("optionB");
            String optionC = request.getParameter("optionC");
            String optionD = request.getParameter("optionD");
            String correctOption = request.getParameter("correctOption");

            QuestionDTO question = new QuestionDTO(questionId, examId, questionText, optionA, optionB, optionC, optionD, correctOption);
            QuestionDAO questionDAO = new QuestionDAO();
            if (request.getMethod().equalsIgnoreCase("POST")) {
                if (questionDAO.update(question)) {
                    request.setAttribute("message", "Question updated successfully!");
                    url = EXAM_DETAILS_PAGE;
                    request.setAttribute("examId", examId);
                    request.setAttribute("action", "viewExamDetails");

                    url = processViewExamDetails(request, response);
                    return url;
                } else {
                    request.setAttribute("errorMessage", "Failed to update question.");
                    url = EXAM_DETAILS_PAGE;
                    request.setAttribute("examId", examId);
                    request.setAttribute("action", "viewExamDetails");

                    url = processViewExamDetails(request, response);
                    return url; //stay
                }
            } else { //first time
                QuestionDAO qdao = new QuestionDAO();
                question = qdao.readById(questionId);
                request.setAttribute("question", question); //pass

            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid input format.");
            url = EXAM_DETAILS_PAGE;
            request.setAttribute("examId", request.getParameter("examId"));
            request.setAttribute("action", "viewExamDetails");

            url = processViewExamDetails(request, response);
            return url;
        }
        return url;
    }

    private String processDeleteQuestion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = EXAM_DETAILS_PAGE;  // Default
        try {
            int questionId = Integer.parseInt(request.getParameter("questionId"));
            int examId = Integer.parseInt(request.getParameter("examId")); // Get
            QuestionDAO questionDAO = new QuestionDAO();

            if (questionDAO.delete(questionId)) {
                request.setAttribute("message", "Question deleted successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to delete question.");
            }
            request.setAttribute("examId", examId); // Set
            request.setAttribute("action", "viewExamDetails"); // Set

            url = processViewExamDetails(request, response); // Refresh
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid question ID.");
            url = EXAM_DETAILS_PAGE;
            request.setAttribute("examId", request.getParameter("examId")); //keep
            request.setAttribute("action", "viewExamDetails");

            url = processViewExamDetails(request, response);
        }
        return url;
    }

    private String processTakeExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = TAKE_EXAM_PAGE;
        try {
            int examId = Integer.parseInt(request.getParameter("examId"));
            ExamDAO examDAO = new ExamDAO();
            ExamDTO exam = examDAO.readById(examId);
            QuestionDAO questionDAO = new QuestionDAO();
            List<QuestionDTO> questions = questionDAO.readAllByExamId(examId);
            request.setAttribute("exam", exam);
            request.setAttribute("questions", questions);

            HttpSession session = request.getSession();
            UserDTO user = (UserDTO) session.getAttribute("user");

            session.setAttribute("currentExamId", examId);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid exam ID.");
            url = ERROR_PAGE;
        }
        return url;
    }

    private String processSubmitExam(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = EXAM_RESULTS_PAGE;
        try {
            HttpSession session = request.getSession();
            int examId = (int) session.getAttribute("currentExamId");
            ExamDAO examDAO = new ExamDAO();
            ExamDTO exam = examDAO.readById(examId);
            QuestionDAO questionDAO = new QuestionDAO();
            List<QuestionDTO> questions = questionDAO.readAllByExamId(examId);

            int score = 0;
            for (QuestionDTO question : questions) {
                String selectedOption = request.getParameter("question_" + question.getQuestionId());
                if (selectedOption != null && selectedOption.equals(question.getCorrectOption())) {
                    score++;
                }
            }
            UserDTO user = (UserDTO) session.getAttribute("user");
            request.setAttribute("user", user);
            request.setAttribute("score", score);
            request.setAttribute("totalQuestions", questions.size());
            request.setAttribute("exam", exam);

            session.removeAttribute("currentExamId");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing exam submission.");
            url = ERROR_PAGE;
        }
        return url;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "login"; // Default
            }

            switch (action) {
                case "login":
                    url = processLogin(request, response);
                    break;
                case "logout":
                    url = processLogout(request, response);
                    break;
                case "searchExams":  // exam list, category, search
                case "search": // search question and exams
                    url = processSearch(request, response);
                    break;
                case "addExam":
                    url = processAddExam(request, response);
                    break;
                case "viewExamCategories": //view
                    request.setAttribute("action", "searchExams");
                    url = processSearch(request, response);
                    break;
                case "editExam":
                    url = processEditExam(request, response);
                    break;
                case "deleteExam":
                    url = processDeleteExam(request, response);
                    break;
                case "updateExam":
                    url = processUpdateExam(request, response);
                    break;
                case "viewExamDetails": // question
                    url = processViewExamDetails(request, response);
                    break;
                case "addQuestion":
                    url = processAddQuestion(request, response);
                    break;
                case "editQuestion":
                    url = processEditQuestion(request, response);
                    break;
                case "updateQuestion":
                    url = processUpdateQuestion(request, response);
                    break;
                case "deleteQuestion":
                    url = processDeleteQuestion(request, response);
                    break;
                case "takeExam":
                    url = processTakeExam(request, response);
                    break;
                case "submitExam":
                    url = processSubmitExam(request, response);
                    break;
                case "direct": // button
                    url = request.getParameter("direct");
                    break;
                default:
                    url = LOGIN_PAGE; // If not
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            url = ERROR_PAGE;
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
