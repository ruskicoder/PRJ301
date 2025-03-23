/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ArticleDAO;
import dao.UserDAO;
import dto.ArticleDTO;
import dto.UserDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.AuthUtils;
import utils.TextConvert;

/**
 *
 * @author PC
 */
@WebServlet(name = "main", urlPatterns = {"/main"})
public class main extends HttpServlet {

    private static final String DASHBOARD_PAGE = "dashboard.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String REGISTER_PAGE = "register.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String MANAGE_ARTICLES_PAGE = "manageArticles.jsp";
    private static final String ARTICLE_FORM_PAGE = "articleForm.jsp";
    private static final String DETAILS_PAGE = "details.jsp";
    private static final String ERROR_404 = "Error 404: Page not found!";
    private static final String ERROR_403 = "Error 403: Forbidden request!";

    private String processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = LOGIN_PAGE;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String type = request.getParameter("type");
        if (AuthUtils.isValidLogin(username, password)) {
            url = DASHBOARD_PAGE;
            UserDTO user = AuthUtils.getUser(username);
            request.getSession().setAttribute("user", user);
            // search
            url = DASHBOARD_PAGE;
//            processSearch(request, response);
        } else {
            request.setAttribute("loginMessage", "Username hoặc Mật Khẩu không đúng!");
            if (type != null && !type.equals("header")) {
                url = LOGIN_PAGE;
                request.setAttribute("url", url);
            } else {
                url = "header.jsp";
                request.setAttribute("url", url);
            }
        }
        return url;
    }

    private String processLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = LOGIN_PAGE;
        HttpSession session = request.getSession();
        if (AuthUtils.isLoggedIn(session)) {
            request.getSession().invalidate(); // Hủy session
            url = LOGIN_PAGE;
        }
        return url;
    }
        
    //Use to retain data
    private String processDirect_Data(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = ERROR_PAGE;
        String direct = request.getParameter("direct");

        if (direct != null && !direct.isEmpty()) {
            // Check for articleForm.jsp and session
            if ("articleForm.jsp".equals(direct)) {
                HttpSession session = request.getSession(false);
                if (session == null || !AuthUtils.isLoggedIn(session)) {
                    request.setAttribute("errorMessage", "You need to login first.");
                    return ERROR_PAGE;  // Or LOGIN_PAGE, depending on your preference
                }
            }

            // Check for manageArticles.jsp and load articles
            if ("manageArticles.jsp".equals(direct)) {
                ArticleDAO articleDAO = new ArticleDAO();
                List<ArticleDTO> articles = articleDAO.readAll();
                request.setAttribute("articles", articles);
            }

            url = direct;
            // Check if the request is for details.jsp from manageArticles.jsp
            if ("details.jsp".equals(direct)) {
                String source = request.getParameter("source");
                if ("manageArticles".equals(source)) {
                    // Set an attribute to indicate that details.jsp was accessed from manageArticles.jsp
                    request.setAttribute("fromManageArticles", true);
                }
                String articleId = request.getParameter("id");
                if (articleId != null && !articleId.isEmpty()) {
                    ArticleDAO dao = new ArticleDAO();
                    ArticleDTO article = dao.readById(articleId);
                    if (article != null) {
                        request.setAttribute("article", article); // Set the article as a request attribute
                    } else {
                        //If Article not found, redirect
                        request.setAttribute("errorMessage", "Article not found.");
                        url = ERROR_PAGE;
                    }
                } else {
                    //If no Id, redirect
                    request.setAttribute("errorMessage", "Article ID is required.");
                    url = ERROR_PAGE;
                }
            }

        } else {
            request.setAttribute("errorMessage", ERROR_404);
        }
        return url;
    }

    private String processRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String citizenIDStr = request.getParameter("citizenID");
        String dobStr = request.getParameter("dob");

        long citizenID = 0;
        try {
            citizenID = Long.parseLong(citizenIDStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Citizen ID format.");
            return REGISTER_PAGE;
        }

        Date dob = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsed = sdf.parse(dobStr);
            dob = new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            request.setAttribute("errorMessage", "Invalid date format for Date of Birth");
            return REGISTER_PAGE; // Return to registration page with error
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            return REGISTER_PAGE;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.readById(username) != null) {
            request.setAttribute("errorMessage", "Username Existed");
            return REGISTER_PAGE;
        }

        UserDTO newUser = new UserDTO(username, fullname, password, "US", citizenID, dob); // Role = US
        boolean success = userDAO.create(newUser);

        if (success) {
            request.setAttribute("successMessage", "Registered Successfully");
            return LOGIN_PAGE;
        } else {
            request.setAttribute("errorMessage", "Registration failed.");
            return REGISTER_PAGE;
        }
    }

    private String processSearchArticles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String filterBy = request.getParameter("filterBy");
        String authorName = request.getParameter("authorName");
        String filterDate = request.getParameter("filterDate");

        ArticleDAO articleDAO = new ArticleDAO();
        List<ArticleDTO> articles = null;
        HttpSession session = request.getSession(false);

        if (session != null) {
            UserDTO user = (UserDTO) session.getAttribute("user");

            //For Guest
            if (user != null && "GU".equals(user.getRole())) {
                if (null != filterBy) {
                    switch (filterBy) {
                        case "Forum":
                            request.setAttribute("filterMessage", "Login to use Forum");
                            articles = articleDAO.readAll();
                            break;
                        //For other roles
                        case "all":
                            // Guest can search all articles
                            articles = articleDAO.search(searchTerm);
                            break;
                        case "byAuthor":
                            // Search by author's name
                            articles = articleDAO.search(authorName);
                            break;
                        case "News":
                            articles = articleDAO.search(filterBy);
                            break;
                        case "Other":
                            articles = articleDAO.search(filterBy);
                            break;
                        default:
                            break;
                    }
                }

            } else if (user != null) {
                if (null != filterBy) {
                    switch (filterBy) {
                        case "myArticles":
                            articles = articleDAO.search(user.getFullName());
                            break;
                        case "all":
                            articles = articleDAO.readAll();
                            break;
                        case "byAuthor":
                            articles = articleDAO.search(authorName);
                            break;
                        case "Forum":
                            articles = articleDAO.search(filterBy);
                            break;
                        case "News":
                            articles = articleDAO.search(filterBy);
                            break;
                        case "Other":
                            articles = articleDAO.search(filterBy);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                //If no user logged in, redirect
                request.setAttribute("errorMessage", "You need to login first.");
                return ERROR_PAGE;
            }
        } else {
            request.setAttribute("errorMessage", "You need to login first.");
            return ERROR_PAGE;
        }

        //Filter by Date
        if ("Newest".equals(filterDate)) {
            if (articles != null && !articles.isEmpty()) {
                articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate())); // Sort newest
            }
        } else if ("Oldest".equals(filterDate)) {
            if (articles != null && !articles.isEmpty()) {
                articles.sort((a1, a2) -> a1.getPublishDate().compareTo(a2.getPublishDate())); // Sort oldest
            }
        }
        request.setAttribute("articles", articles);
        return MANAGE_ARTICLES_PAGE;
    }

    private String processAddArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Retrieve data, check for null
        String title = request.getParameter("title");
        String subtitle = request.getParameter("subtitle");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        String thumbnailData = request.getParameter("thumbnail-data"); // Base64 data
        String articleType = request.getParameter("articleType");

        //If null, redirect
        if (title == null || author == null || content == null || articleType == null || thumbnailData == null) {
            request.setAttribute("errorMessage", "Missing required fields.");
            return ERROR_PAGE;
        }
        if (session == null || !AuthUtils.isLoggedIn(session)) {
            request.setAttribute("errorMessage", "You need to login first.");
            return ERROR_PAGE;
        }

        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("errorMessage", "You need to login first.");
            return ERROR_PAGE;
        }
        
        //convert to database text
        String dbContent = TextConvert.convertToDatabaseFormat(content);
        //Generate ArticleID
        String articleID = generateArticleId(articleType); // Pass articleType
        //New article
        ArticleDTO newArticle = new ArticleDTO(articleID, title, subtitle, author, dbContent, thumbnailData, articleType);

        ArticleDAO articleDAO = new ArticleDAO();
        //Add
        boolean success = articleDAO.create(newArticle);
        String message = "";
        if (success) {
            message = "Article added successfully!";
        } else {
            message = "Failed to add article.";
        }

        // Instead of calling processSearchArticles, set attributes and return manageArticles.jsp
        request.setAttribute("message", message);
        request.setAttribute("articles", articleDAO.readAll()); // Re-fetch all articles
        return MANAGE_ARTICLES_PAGE;
    }

    private String processEditArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve data, check for null
        String articleID = request.getParameter("articleID");
        String title = request.getParameter("title");
        String subtitle = request.getParameter("subtitle");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        String thumbnailData = request.getParameter("thumbnail-data"); // Base64 data
        String articleType = request.getParameter("articleType");

        // If null, redirect
        if (articleID == null || title == null || author == null || content == null || articleType == null) {
            request.setAttribute("errorMessage", "Missing required fields.");
            return ERROR_PAGE;
        }

        // Check session
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session)) {
            request.setAttribute("errorMessage", "You need to login first.");
            return ERROR_PAGE;
        }

        // Find Article
        ArticleDAO articleDAO = new ArticleDAO();
        ArticleDTO article = articleDAO.readById(articleID);
        if (article == null) {
            request.setAttribute("errorMessage", "Article not found.");
            return ERROR_PAGE;
        }
        // Logged-in user
        UserDTO loggedInUser = AuthUtils.getUser(session);

        // Authorization check: Only admin or the article's author can edit.
        if (!AuthUtils.isAdmin(session) && !loggedInUser.getUserName().equals(article.getAuthor())) {
            request.setAttribute("errorMessage", "You are not authorized to edit this article.");
            return ERROR_PAGE; // Or perhaps a different "forbidden" page.
        }
        
        // Convert content to database format (replace newlines with \n)
        String dbContent = TextConvert.convertToDatabaseFormat(content);
        
        // Update article with new data
        article.setTitle(title);
        article.setSubtitle(subtitle);
        article.setAuthor(author);
        article.setContent(dbContent);
        article.setThumbnail(thumbnailData); // Set Base64 data
        article.setArticleType(articleType);
        article.setPublishDate(new Date(System.currentTimeMillis()));

        // Update in DB
        boolean success = articleDAO.update(article);
        String message = "";
        if (success) {
            message = "Article updated successfully!";
        } else {
            message = "Failed to update article.";
        }

        // Instead of calling processSearchArticles, set attributes and return manageArticles.jsp
        request.setAttribute("message", message);
        request.setAttribute("articles", articleDAO.readAll());  // Re-fetch all articles.
        return MANAGE_ARTICLES_PAGE;
    }

    private String processDeleteArticle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String articleID = request.getParameter("articleID");
        //If null, redirect
        if (articleID == null) {
            request.setAttribute("errorMessage", "Article id cannot be null.");
            return ERROR_PAGE;
        }
        ArticleDAO articleDAO = new ArticleDAO();
        boolean success = articleDAO.delete(articleID);

        if (success) {
            request.setAttribute("message", "Article deleted successfully!");
        } else {
            request.setAttribute("message", "Failed to delete article.");
        }
        return processSearchArticles(request, response);
    }

    // Generate ArticleID
    private String generateArticleId(String articleType) {
        ArticleDAO dao = new ArticleDAO();
        List<ArticleDTO> articles = dao.readAll();  // Get all articles

        String prefix = "";
        switch (articleType) {
            case "News":
                prefix = "NW";
                break;
            case "Forum":
                prefix = "FR";
                break;
            case "Other":
                prefix = "OT"; //Changed to OT, since O is one character
                break;
            default:
                prefix = "OT"; // Default case, though articleType should be validated
        }

        int maxId = 0;
        // Iterate through all articles to find the highest existing ID for the given type.
        for (ArticleDTO article : articles) {
            if (article.getArticleID().startsWith(prefix)) {
                try {
                    // Extract the numeric part of the ID and convert to integer
                    int idNumber = Integer.parseInt(article.getArticleID().substring(2));
                    if (idNumber > maxId) {
                        maxId = idNumber;
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the ID doesn't follow the expected format
                    // Log this, or potentially skip this article.  For now, we'll just skip.
                    continue;
                }
            }
        }

        // Increment the max ID and format the new ID.
        return String.format("%s%04d", prefix, maxId + 1);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String url = LOGIN_PAGE;
        try {
            String action = request.getParameter("action");
            if (action == null) {
                url = LOGIN_PAGE;
            } else {
                switch (action) {
                    case "login":
                        url = processLogin(request, response);
                        break;
                    case "logout":
                        url = processLogout(request, response);
                        break;
                    case "direct":
                        url = processDirect_Data(request, response);
                        break;
                    case "register":
                        url = processRegister(request, response);
                        break;
                    case "searchArticles":
                        url = processSearchArticles(request, response);
                        break;
                    case "addArticle":
                        url = processAddArticle(request, response);
                        break;
                    case "editArticle":
                        url = processEditArticle(request, response);
                        break;
                    case "deleteArticle":
                        url = processDeleteArticle(request, response);
                        break;
                    default:
                        url = ERROR_PAGE;
                        request.setAttribute("errorMessage", ERROR_404);
                        break;
                }
            }
        } catch (IOException | ServletException e) {
            log("Error in main servlet: " + e.toString());
            url = ERROR_PAGE;
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
