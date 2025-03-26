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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.AuthUtils;
import utils.Base64Image;
import utils.Hash;
import utils.TextConvert;
import dao.LicenseDAO;
import dto.LicenseDTO;
import java.util.ArrayList;
import java.util.Collections; // If needed for empty list

/**
 *
 * @author PC
 */
@WebServlet(name = "main", urlPatterns = {"/main"})
public class main extends HttpServlet {

    private static final String DASHBOARD_PAGE = "dashboard.jsp";
    private static final String HOMEPAGE = "homepage.jsp";
    private static final String FORUMS_PAGE = "forums.jsp";
    private static final String LOGIN_PAGE = "login.jsp";
    private static final String REGISTER_PAGE = "register.jsp";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String MANAGE_ARTICLES_PAGE = "manageArticles.jsp";
    private static final String ARTICLE_FORM_PAGE = "articleForm.jsp";
    private static final String EDIT_PROFILE_PAGE = "editProfile.jsp";
    private static final String ADMIN_EDIT_PROFILE_PAGE = "adminEditProfile.jsp";
    private static final String CHANGE_PASSWORD_PAGE = "changePassword.jsp";
    private static final String DETAILS_PAGE = "details.jsp";
    private static final String ERROR_404 = "Error 404: Page not found!";
    private static final String ERROR_403 = "Error 403: Forbidden request!";
    private static final String MANAGE_LICENSES_PAGE = "manageLicenses.jsp";
    private static final String LICENSE_FORM_PAGE = "licenseForm.jsp";

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
                url = LOGIN_PAGE;
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
        HttpSession session = request.getSession(false); // Get session once

        if (direct != null && !direct.isEmpty()) {
            url = direct; // Assume valid direct initially

            // --- Session/Auth Checks First ---
            if (session == null || !AuthUtils.isLoggedIn(session)) {
                // Redirect non-logged-in users trying to access protected pages
                if (direct.equals(MANAGE_ARTICLES_PAGE) || direct.equals(ARTICLE_FORM_PAGE)
                        || direct.equals(EDIT_PROFILE_PAGE) || direct.equals(ADMIN_EDIT_PROFILE_PAGE)
                        || direct.equals(CHANGE_PASSWORD_PAGE) || direct.equals(MANAGE_LICENSES_PAGE)
                        || direct.equals(LICENSE_FORM_PAGE) || direct.equals(DASHBOARD_PAGE)) {
                    request.setAttribute("errorMessage", "You need to login first.");
                    return ERROR_PAGE; // Or LOGIN_PAGE
                }
            } else { // User is logged in, perform role-specific checks
                if (AuthUtils.isGuest(session)) {
                    // Guests restricted from certain pages
                    if (direct.equals(ARTICLE_FORM_PAGE) || direct.equals(CHANGE_PASSWORD_PAGE)
                            || direct.equals(EDIT_PROFILE_PAGE) || direct.equals(MANAGE_LICENSES_PAGE)
                            || direct.equals(LICENSE_FORM_PAGE)) {
                        request.setAttribute("errorMessage", "Guests do not have permission to access this page.");
                        return ERROR_PAGE; // Or dashboard with a message
                    }
                }
                if (!AuthUtils.isAdmin(session)) {
                    // Non-admins restricted from admin pages
                    if (direct.equals(ADMIN_EDIT_PROFILE_PAGE) || direct.equals(LICENSE_FORM_PAGE)) {
                        request.setAttribute("errorMessage", "Access Denied: Administrators only.");
                        return ERROR_PAGE; // Or dashboard
                    }
                }
            }

            // --- Load Data Based on Target Page ---
            if (direct.equals(MANAGE_ARTICLES_PAGE)) {
                ArticleDAO articleDAO = new ArticleDAO();
                List<ArticleDTO> articles = articleDAO.readAll(); // Load initial articles
                if (articles != null) { // Sort by newest by default when navigating directly
                    articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate()));
                }
                request.setAttribute("articles", articles);
            } else if (direct.equals(EDIT_PROFILE_PAGE) && session != null && AuthUtils.isLoggedIn(session)) {
                // Reload user data, including list for admins
                UserDTO user = AuthUtils.getUser(session);
                request.setAttribute("user", user);
                if (AuthUtils.isAdmin(session)) {
                    UserDAO userDAO = new UserDAO();
                    List<UserDTO> users = userDAO.readAll(); // Load all users
                    request.setAttribute("users", users);
                }
            } else if (direct.equals(DETAILS_PAGE)) {
                // Logic for loading article details (seems mostly correct)
                String articleId = request.getParameter("id");
                if (articleId != null && !articleId.isEmpty()) {
                    ArticleDAO dao = new ArticleDAO();
                    ArticleDTO article = dao.readById(articleId);
                    if (article != null) {
                        request.setAttribute("article", article);
                    } else {
                        request.setAttribute("errorMessage", "Article not found.");
                        url = ERROR_PAGE;
                    }
                } else {
                    request.setAttribute("errorMessage", "Article ID is required.");
                    url = ERROR_PAGE;
                }
                // Keep track of source for back button
                String source = request.getParameter("source");
                if ("manageArticles".equals(source)) {
                    request.setAttribute("fromManageArticles", true);
                }
            } // --- New Logic for License Pages ---
            else if (direct.equals(MANAGE_LICENSES_PAGE) && session != null && AuthUtils.isLoggedIn(session) && !AuthUtils.isGuest(session)) {
                LicenseDAO licenseDAO = new LicenseDAO();
                List<LicenseDTO> licenses;
                UserDTO currentUser = AuthUtils.getUser(session);
                if (AuthUtils.isAdmin(session)) {
                    licenses = licenseDAO.readAll(); // Admin sees all
                } else {
                    // User sees only their own license - search by full name
                    licenses = licenseDAO.searchByFullName(currentUser.getFullName());
                }
                request.setAttribute("licenses", licenses);
            } else if (direct.equals(LICENSE_FORM_PAGE) && session != null && AuthUtils.isAdmin(session)) {
                String formAction = request.getParameter("formAction");
                String licenseID = request.getParameter("licenseID");

                // Load users for dropdown
                UserDAO userDAO = new UserDAO();
                List<UserDTO> allUsers = userDAO.readAll();
                List<UserDTO> eligibleUsers = allUsers.stream()
                        .filter(u -> u.getCitizenID() != -1)
                        .collect(Collectors.toList());
                request.setAttribute("eligibleUsers", eligibleUsers); // Set for JSP

                if ("edit".equals(formAction) && licenseID != null && !licenseID.isEmpty()) {
                    LicenseDAO licenseDAO = new LicenseDAO();
                    LicenseDTO license = licenseDAO.readById(licenseID);
                    if (license != null) {
                        request.setAttribute("license", license); // Set license data for pre-filling form
                    } else {
                        request.setAttribute("errorMessage", "License not found for editing.");
                        url = processDirect_Data(request, response); // Redirect back to manage licenses
                    }
                } else if ("edit".equals(formAction)) {
                    request.setAttribute("errorMessage", "License ID required for edit.");
                    url = processDirect_Data(request, response); // Redirect back to manage licenses
                }
                // If action is 'add', just load users and proceed to form
                request.setAttribute("formAction", formAction); // Pass action type to JSP
            }
            // --- End License Logic ---

        } else {
            // If direct is null or empty, handle based on login status
            if (session != null && AuthUtils.isLoggedIn(session)) {
                // If logged in, default to dashboard or homepage
                url = DASHBOARD_PAGE; // Or HOMEPAGE via processSortArticles
                // If going to dashboard, you might want to load initial dashboard data here
            } else {
                url = LOGIN_PAGE; // Default to login if not logged in and no destination
            }
            request.setAttribute("errorMessage", ERROR_404 + " No destination specified."); // More specific error
        }
        return url;
    }

    //In main.java
    private String processRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String fullname = request.getParameter("fullname");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String citizenIDStr = request.getParameter("citizenID");
        String dobStr = request.getParameter("dob");
        String userImageData = request.getParameter("userImageData"); // Get Base64 data

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

        // Check password format for both password and confirmPassword
        if (!AuthUtils.checkPasswordFormat(password, request)) {
            return REGISTER_PAGE; // Error message set by checkPasswordFormat
        }
        if (!AuthUtils.checkPasswordFormat(confirmPassword, request)) {
            return REGISTER_PAGE;// Error message set by checkPasswordFormat
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

        // Handle default image if no image is uploaded
        if (userImageData == null || userImageData.trim().isEmpty()) {
            userImageData = Base64Image.imageToBase64("/assets/images/no_image.jpg", getServletContext());
        }
        if (userImageData == null) {
            request.setAttribute("errorMessage", "Error with default user image.");
            return REGISTER_PAGE;
        }

        // Hash the password BEFORE creating the UserDTO
        String hashedPassword = Hash.toSHA256(password);
        UserDTO newUser = new UserDTO(username, fullname, hashedPassword, "US", citizenID, dob); // Role = US, use hashed password
        newUser.setUserImage(userImageData);  // Set the image data
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
                            articles = articleDAO.searchBy(user.getFullName(), "Author");
                            break;
                        case "all":
                            articles = articleDAO.readAll();
                            break;
                        case "byAuthor":
                            articles = articleDAO.searchBy(authorName, "ArticleType");
                            break;
                        case "Forum":
                            articles = articleDAO.searchBy(filterBy, "ArticleType");
                            break;
                        case "News":
                            articles = articleDAO.searchBy(filterBy, "ArticleType");
                            break;
                        case "Other":
                            articles = articleDAO.searchBy(filterBy, "ArticleType");
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

    private String processSortArticles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArticleDAO articleDAO = new ArticleDAO();
        String filterDate = request.getParameter("filterDate");

        List<ArticleDTO> articles = articleDAO.searchBy("News", "ArticleType");

        if (articles != null && !articles.isEmpty()) {
            // Sort based on filterDate parameter
            if ("Oldest".equals(filterDate)) {
                articles.sort((a1, a2) -> a1.getPublishDate().compareTo(a2.getPublishDate())); // Sort oldest first
            } else { // Default to Newest or if filterDate is "Newest" or null/invalid
                articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate())); // Sort newest first
            }

        }

        request.setAttribute("articles", articles);
        request.setAttribute("filterDate", filterDate == null ? "Newest" : filterDate);

        return HOMEPAGE;
    }

    private String processSearchUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "You must be logged in as an admin to manage users.");
            return ERROR_PAGE; // Or redirect to login/dashboard
        }

        String searchTerm = request.getParameter("searchTerm");
        String filterBy = request.getParameter("filterBy");
        UserDAO userDAO = new UserDAO();
        List<UserDTO> users; // Declare outside the switch

        if (filterBy == null || filterBy.isEmpty() || filterBy.equals("all")) {
            users = userDAO.readAll();
        } else {
            switch (filterBy) {
                case "admins":
                    users = userDAO.searchBy("AD", "Role"); // Search specifically by role
                    break;
                case "users":
                    users = userDAO.searchBy("US", "Role");  // Search specifically by role
                    break;
                default: // Should not happen, but handle it
                    users = userDAO.readAll();
            }
        }

        // Further filter by search term if provided
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            // Filter by username or full name
            users = users.stream()
                    .filter(u -> u.getUserName().toLowerCase().contains(searchTerm.toLowerCase())
                    || u.getFullName().toLowerCase().contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
        }

        request.setAttribute("users", users);
        request.setAttribute("user", AuthUtils.getUser(session)); // Make user data available to the JSP
        return EDIT_PROFILE_PAGE;
    }

    private String processEditUserProfileForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "You must be logged in as an admin to edit user profiles.");
            return ERROR_PAGE; // Or redirect to login/dashboard as appropriate
        }

        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username is required to edit a user.");
            return ERROR_PAGE;  // Or perhaps back to the user list with an error
        }

        UserDAO userDAO = new UserDAO();
        UserDTO userToEdit = userDAO.readById(username);

        if (userToEdit == null) {
            request.setAttribute("errorMessage", "User not found.");
            return processSearchUsers(request, response); // Go back to user search
        }

        request.setAttribute("userToEdit", userToEdit);
        request.setAttribute("user", AuthUtils.getUser(session));
        return EDIT_PROFILE_PAGE; // Go to the edit form
    }

    private String processChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session)) {
            request.setAttribute("errorMessage", "You need to login first.");
            return ERROR_PAGE; // Or login page.
        }
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("errorMessage", "You need to login first.");
            return ERROR_PAGE;
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (oldPassword == null || newPassword == null || confirmPassword == null) {
            request.setAttribute("message", "Password cannot be null!");
            return CHANGE_PASSWORD_PAGE;
        }

        if (!AuthUtils.checkPasswordFormat(newPassword, request)) {
            return CHANGE_PASSWORD_PAGE;
        }
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("message", "New passwords do not match.");
            return CHANGE_PASSWORD_PAGE;
        }

        UserDAO dao = new UserDAO();
        UserDTO userFromDB = dao.checkLogin(user.getUserName(), oldPassword); // Use checkLogin
        if (userFromDB == null) {
            request.setAttribute("message", "Incorrect old password.");
            return CHANGE_PASSWORD_PAGE;
        }

        String hashedNewPassword = Hash.toSHA256(newPassword);
        if (hashedNewPassword == null) {
            request.setAttribute("message", "Password change failed: Internal Error.");
            return CHANGE_PASSWORD_PAGE;
        }

        user.setPassword(hashedNewPassword);

        if (dao.update(user)) {
            request.setAttribute("message", "Password changed successfully!");
            return CHANGE_PASSWORD_PAGE;
        } else {
            request.setAttribute("message", "Password change failed.");
            return CHANGE_PASSWORD_PAGE;
        }
    }

    private String processEditUserProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session)) {
            request.setAttribute("errorMessage", "You must be logged in to edit your profile.");
            return LOGIN_PAGE;
        }

        UserDTO loggedInUser = AuthUtils.getUser(session);
        if (loggedInUser == null) {
            request.setAttribute("errorMessage", "User session is invalid.");
            return LOGIN_PAGE;
        }

        String fullname = request.getParameter("fullname");

        String citizenIDStr = request.getParameter("citizenID");
        String userImageData = request.getParameter("userImageData");

        // Validate inputs (add more as needed)
        if (fullname == null || fullname.trim().isEmpty()) {
            request.setAttribute("message", "Full name is required.");
            return EDIT_PROFILE_PAGE; // Return to the edit form with error
        }
        try {
            if (citizenIDStr == null) {
                request.setAttribute("message", "CitizenID is null.");
                return EDIT_PROFILE_PAGE;
            }
            long citizenID = Long.parseLong(citizenIDStr);
            loggedInUser.setCitizenID(citizenID); // Update

        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid Citizen ID format.");
            return EDIT_PROFILE_PAGE;
        }

        // Update other fields
        loggedInUser.setFullName(fullname);
        if (userImageData != null) {
            loggedInUser.setUserImage(userImageData);
        }

        // Save changes to the database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.update(loggedInUser);

        if (success) {
            // Update the session attribute with the updated user object
            request.getSession().setAttribute("user", loggedInUser);
            request.setAttribute("message", "Profile updated successfully!");
        } else {
            request.setAttribute("message", "Profile update failed.");
        }
        request.setAttribute("user", loggedInUser);
        return EDIT_PROFILE_PAGE; // Return to the edit form (with success/error message)
    }

    private String processEditAdminProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "You must be logged in as an admin to edit admin profiles.");
            return LOGIN_PAGE; // Or an appropriate error page
        }
        UserDTO loggedInUser = AuthUtils.getUser(session);
        if (loggedInUser == null) {
            request.setAttribute("errorMessage", "User session is invalid.");
            return LOGIN_PAGE;
        }

        String fullname = request.getParameter("fullname");
        String userImageData = request.getParameter("userImageData");

        // Validate inputs (add more as needed)
        if (fullname == null || fullname.trim().isEmpty()) {
            request.setAttribute("message", "Full name is required.");
            return EDIT_PROFILE_PAGE; // Return to the edit form with error
        }

        // Update other fields
        loggedInUser.setFullName(fullname);
        if (userImageData != null && !userImageData.isEmpty()) {
            loggedInUser.setUserImage(userImageData); // Update
        }

        // Save changes to the database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.update(loggedInUser);

        if (success) {
            // Update the session attribute with the updated user object
            request.getSession().setAttribute("user", loggedInUser); // Important to keep session updated!
            request.setAttribute("message", "Profile updated successfully!");
        } else {
            request.setAttribute("message", "Profile update failed.");
        }
        request.setAttribute("user", loggedInUser);
        return EDIT_PROFILE_PAGE; // Return to the edit form (with success/error message)
    }

    private String processEditOtherUserProfileForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "You must be logged in as an admin to edit user profiles.");
            return ERROR_PAGE;
        }

        String username = request.getParameter("username"); // Get the username from the request
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username is required.");
            return processSearchUsers(request, response); // Go back to user management
        }

        UserDAO userDAO = new UserDAO();
        UserDTO userToEdit = userDAO.readById(username);

        if (userToEdit == null) {
            request.setAttribute("errorMessage", "User not found.");
            return processSearchUsers(request, response); // Go back to user search
        }

        request.setAttribute("userToEdit", userToEdit); // Pass the user data to the JSP
        return ADMIN_EDIT_PROFILE_PAGE; // Go to the admin edit profile page
    }

    private String processEditOtherUserProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "You must be logged in as an admin to edit user profiles.");
            return ERROR_PAGE;
        }

        // Get the username of the user being edited from the request parameters.  IMPORTANT: use a different
        // parameter name than the *admin's* username, to avoid confusion!
        String usernameToEdit = request.getParameter("username"); // Get the username of user to be edited
        if (usernameToEdit == null || usernameToEdit.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username is required to edit a user.");
            return processSearchUsers(request, response); // Go back to user search with error
        }
        //Retrieve all updated parameters
        String fullname = request.getParameter("fullname");
        String role = request.getParameter("role");
        String citizenIDStr = request.getParameter("citizenID");
        String userImageData = request.getParameter("userImageData");

        // Validate inputs
        if (fullname == null || fullname.trim().isEmpty()
                || role == null || role.trim().isEmpty()
                || citizenIDStr == null || citizenIDStr.trim().isEmpty()) {
            request.setAttribute("message", "All fields are required.");
            request.setAttribute("userToEdit", new UserDAO().readById(usernameToEdit)); // Keep form data for redisplay
            return ADMIN_EDIT_PROFILE_PAGE; // Return to the *admin edit* page
        }

        long citizenID = 0;
        try {
            citizenID = Long.parseLong(citizenIDStr);
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Invalid Citizen ID format.");
            request.setAttribute("userToEdit", new UserDAO().readById(usernameToEdit)); //Keep data
            return ADMIN_EDIT_PROFILE_PAGE;
        }
        // Fetch the user to edit from the database
        UserDAO userDAO = new UserDAO();
        UserDTO userToEdit = userDAO.readById(usernameToEdit);  // Use the correct username!

        if (userToEdit == null) {
            request.setAttribute("errorMessage", "User not found.");
            return processSearchUsers(request, response); // Go back to user search with error
        }
        // Update user object
        userToEdit.setFullName(fullname);
        userToEdit.setRole(role);
        userToEdit.setCitizenID(citizenID);
        if (userImageData != null && !userImageData.isEmpty()) {
            userToEdit.setUserImage(userImageData); // Update
        }

        // Save changes to the database
        boolean success = userDAO.update(userToEdit);

        if (success) {
            request.setAttribute("message", "User profile updated successfully!");
        } else {
            request.setAttribute("message", "User profile update failed.");
        }
        // Always return to the user management page, even on failure
        return processSearchUsers(request, response);
    }

    private String processSearchHomepageNews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        String filterDate = request.getParameter("filterDate");

        ArticleDAO articleDAO = new ArticleDAO();
        List<ArticleDTO> articles;

        // Start by getting articles of type "News"
        articles = articleDAO.searchBy("News", "ArticleType");

        // If there's a search term, filter further
        if (articles != null && searchTerm != null && !searchTerm.trim().isEmpty()) {
            String lowerCaseSearchTerm = searchTerm.toLowerCase().trim();
            articles = articles.stream()
                    .filter(article -> (article.getTitle() != null && article.getTitle().toLowerCase().contains(lowerCaseSearchTerm))
                    || (article.getSubtitle() != null && article.getSubtitle().toLowerCase().contains(lowerCaseSearchTerm))
                    || (article.getContent() != null && article.getContent().toLowerCase().contains(lowerCaseSearchTerm)))
                    .collect(Collectors.toList());
        }

        // Sort the filtered results based on Date
        if (articles != null && !articles.isEmpty()) {
            if ("Oldest".equals(filterDate)) {
                articles.sort((a1, a2) -> a1.getPublishDate().compareTo(a2.getPublishDate())); // Sort oldest first
            } else { // Default to Newest or if filterDate is "Newest" or null/invalid
                articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate())); // Sort newest first
            }

        }

        request.setAttribute("articles", articles);
        // Keep search parameters in request scope for the form fields
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("filterDate", filterDate == null ? "Newest" : filterDate); // Default to Newest if null

        return HOMEPAGE; // Return to homepage with filtered/sorted news
    }

    // Add new method processSortForums
    private String processSortForums(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Authorization Check: Ensure user is logged in and not a guest
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || AuthUtils.isGuest(session)) {
            request.setAttribute("errorMessage", "You must be logged in as a User or Admin to access the forums.");
            return ERROR_PAGE;
        }

        ArticleDAO articleDAO = new ArticleDAO();
        String filterDate = request.getParameter("filterDate"); // Get filter date parameter

        // Fetch only Forum articles
        List<ArticleDTO> articles = articleDAO.searchBy("Forum", "ArticleType");

        if (articles != null && !articles.isEmpty()) {
            // Sort based on filterDate parameter
            if ("Oldest".equals(filterDate)) {
                articles.sort((a1, a2) -> a1.getPublishDate().compareTo(a2.getPublishDate())); // Sort oldest first
            } else { // Default to Newest
                articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate())); // Sort newest first
            }
        }

        request.setAttribute("articles", articles); // Pass the full list
        request.setAttribute("filterDate", filterDate == null ? "Newest" : filterDate);

        return FORUMS_PAGE;
    }

// Add new method processSearchForums
    private String processSearchForums(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Authorization Check: Ensure user is logged in and not a guest
        HttpSession session = request.getSession(false);
        if (session == null || !AuthUtils.isLoggedIn(session) || AuthUtils.isGuest(session)) {
            request.setAttribute("errorMessage", "You must be logged in as a User or Admin to access the forums.");
            return ERROR_PAGE;
        }

        String searchTerm = request.getParameter("searchTerm");
        String filterDate = request.getParameter("filterDate");

        ArticleDAO articleDAO = new ArticleDAO();
        List<ArticleDTO> articles;

        // Start by getting articles of type "Forum"
        articles = articleDAO.searchBy("Forum", "ArticleType");

        // If there's a search term, filter further
        if (articles != null && searchTerm != null && !searchTerm.trim().isEmpty()) {
            String lowerCaseSearchTerm = searchTerm.toLowerCase().trim();
            articles = articles.stream()
                    .filter(article -> (article.getTitle() != null && article.getTitle().toLowerCase().contains(lowerCaseSearchTerm))
                    || (article.getSubtitle() != null && article.getSubtitle().toLowerCase().contains(lowerCaseSearchTerm))
                    || (article.getContent() != null && article.getContent().toLowerCase().contains(lowerCaseSearchTerm))
                    || (article.getAuthor() != null && article.getAuthor().toLowerCase().contains(lowerCaseSearchTerm))) // Also search by author for forums
                    .collect(Collectors.toList());
        }

        // Sort the filtered results based on Date
        if (articles != null && !articles.isEmpty()) {
            if ("Oldest".equals(filterDate)) {
                articles.sort((a1, a2) -> a1.getPublishDate().compareTo(a2.getPublishDate())); // Sort oldest first
            } else { // Default to Newest
                articles.sort((a1, a2) -> a2.getPublishDate().compareTo(a1.getPublishDate())); // Sort newest first
            }
            // No sublist limit here for pagination
        }

        request.setAttribute("articles", articles); // Pass the full list
        // Keep search parameters in request scope for the form fields
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("filterDate", filterDate == null ? "Newest" : filterDate);

        return FORUMS_PAGE;
    }

    private String processSearchLicenses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Auth check
        if (session == null || !AuthUtils.isLoggedIn(session) || AuthUtils.isGuest(session)) {
            request.setAttribute("errorMessage", "You must be logged in to search licenses.");
            return ERROR_PAGE; // Or LOGIN_PAGE
        }

        String searchTerm = request.getParameter("searchTerm");
        LicenseDAO licenseDAO = new LicenseDAO();
        List<LicenseDTO> licenses = new ArrayList<>(); // Initialize to avoid null pointer
        UserDTO currentUser = AuthUtils.getUser(session);

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            LicenseDTO foundLicense = licenseDAO.readById(searchTerm.trim());
            if (foundLicense != null) {
                if (AuthUtils.isAdmin(session)) {
                    // Admin sees any license found by ID
                    licenses.add(foundLicense);
                } else {
                    // User only sees the license if the FullName matches their own
                    if (foundLicense.getFullName().equals(currentUser.getFullName())) {
                        licenses.add(foundLicense);
                    } else {
                        request.setAttribute("message", "License found, but it does not belong to you.");
                        // Optionally load their own license anyway if you want to show it alongside message
                        // licenses = licenseDAO.searchByFullName(currentUser.getFullName());
                    }
                }
            } else {
                request.setAttribute("message", "No license found with that ID.");
                // Optionally load their own license if User, or all if Admin
                if (AuthUtils.isAdmin(session)) {
                    licenses = licenseDAO.readAll();
                } else {
                    licenses = licenseDAO.searchByFullName(currentUser.getFullName());
                }
            }
        } else {
            // If search term is empty, load default view
            if (AuthUtils.isAdmin(session)) {
                licenses = licenseDAO.readAll(); // Admin sees all
            } else {
                licenses = licenseDAO.searchByFullName(currentUser.getFullName()); // User sees their own
            }
            request.setAttribute("message", "Please enter a License ID to search.");
        }

        request.setAttribute("licenses", licenses);
        return MANAGE_LICENSES_PAGE;
    }

    private String processAddLicense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Auth check - Admin Only
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "Access Denied: Administrators only.");
            return ERROR_PAGE;
        }

        String licenseID = request.getParameter("licenseIDInput"); // Name from the input field
        String fullName = request.getParameter("fullName");
        String licenseType = request.getParameter("licenseType");
        String lRegDateStr = request.getParameter("lRegDate");
        String expDateStr = request.getParameter("expDate");

        // Basic Validation
        if (licenseID == null || licenseID.trim().isEmpty()
                || fullName == null || fullName.trim().isEmpty()
                || licenseType == null || licenseType.trim().isEmpty()
                || lRegDateStr == null || lRegDateStr.trim().isEmpty()
                || expDateStr == null || expDateStr.trim().isEmpty()) {
            request.setAttribute("message", "All fields are required.");
            // Reload users for dropdown and return to form
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            request.setAttribute("formAction", "add"); // Keep form state
            return LICENSE_FORM_PAGE;
        }

        LicenseDAO licenseDAO = new LicenseDAO();

        // Check if License ID already exists
        if (licenseDAO.readById(licenseID) != null) {
            request.setAttribute("message", "License ID already exists.");
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            request.setAttribute("formAction", "add"); // Keep form state
            // Pre-fill entered data back into the form
            request.setAttribute("license", new LicenseDTO(licenseID, fullName, licenseType, null, null)); // Create temporary DTO
            try {
                request.setAttribute("lRegDateValue", lRegDateStr); // Pass string dates back
                request.setAttribute("expDateValue", expDateStr);
            } catch (Exception e) {/* Ignore parsing error here */
            }

            return LICENSE_FORM_PAGE;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date lRegDate = new Date(sdf.parse(lRegDateStr).getTime());
            Date expDate = new Date(sdf.parse(expDateStr).getTime());

            // Basic date validation: expiry date should be after registration date
            if (expDate.before(lRegDate)) {
                request.setAttribute("message", "Expiry date must be after registration date.");
                UserDAO userDAO = new UserDAO();
                request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
                request.setAttribute("formAction", "add");
                // Pre-fill data
                request.setAttribute("license", new LicenseDTO(licenseID, fullName, licenseType, null, null));
                request.setAttribute("lRegDateValue", lRegDateStr);
                request.setAttribute("expDateValue", expDateStr);
                return LICENSE_FORM_PAGE;
            }

            LicenseDTO newLicense = new LicenseDTO(licenseID, fullName, licenseType, lRegDate, expDate);
            boolean success = licenseDAO.create(newLicense);

            if (success) {
                request.setAttribute("message", "License added successfully!");
            } else {
                request.setAttribute("message", "Failed to add license.");
            }
        } catch (ParseException e) {
            request.setAttribute("message", "Invalid date format. Please use YYYY-MM-DD.");
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            request.setAttribute("formAction", "add");
            // Pre-fill data
            request.setAttribute("license", new LicenseDTO(licenseID, fullName, licenseType, null, null));
            request.setAttribute("lRegDateValue", lRegDateStr);
            request.setAttribute("expDateValue", expDateStr);
            return LICENSE_FORM_PAGE; // Go back to form on date error
        } catch (Exception e) { // Catch broader errors
            request.setAttribute("message", "An error occurred: " + e.getMessage());
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            request.setAttribute("formAction", "add");
            // Pre-fill data
            request.setAttribute("license", new LicenseDTO(licenseID, fullName, licenseType, null, null));
            request.setAttribute("lRegDateValue", lRegDateStr);
            request.setAttribute("expDateValue", expDateStr);
            return LICENSE_FORM_PAGE;
        }

        // Redirect back to manage licenses page after add/fail
        request.setAttribute("direct", MANAGE_LICENSES_PAGE); // Set target for processDirect_Data
        return processDirect_Data(request, response); // Reload the manage page
    }

    private String processEditLicense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Auth check - Admin Only
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "Access Denied: Administrators only.");
            return ERROR_PAGE;
        }

        String licenseID = request.getParameter("licenseID"); // This comes from the hidden input
        String fullName = request.getParameter("fullName");
        String licenseType = request.getParameter("licenseType");
        String lRegDateStr = request.getParameter("lRegDate");
        String expDateStr = request.getParameter("expDate");

        // Basic Validation
        if (licenseID == null || licenseID.trim().isEmpty()
                || // ID is crucial for update
                fullName == null || fullName.trim().isEmpty()
                || licenseType == null || licenseType.trim().isEmpty()
                || lRegDateStr == null || lRegDateStr.trim().isEmpty()
                || expDateStr == null || expDateStr.trim().isEmpty()) {
            request.setAttribute("message", "All fields are required.");
            // Reload users and existing license data for the form
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            LicenseDAO licenseDAO = new LicenseDAO();
            request.setAttribute("license", licenseDAO.readById(licenseID)); // Reload original data if possible
            request.setAttribute("formAction", "edit");
            return LICENSE_FORM_PAGE;
        }

        LicenseDAO licenseDAO = new LicenseDAO();
        LicenseDTO existingLicense = licenseDAO.readById(licenseID);

        if (existingLicense == null) {
            request.setAttribute("message", "License to update not found.");
            request.setAttribute("direct", MANAGE_LICENSES_PAGE);
            return processDirect_Data(request, response); // Go back to list
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date lRegDate = new Date(sdf.parse(lRegDateStr).getTime());
            Date expDate = new Date(sdf.parse(expDateStr).getTime());

            // Date validation
            if (expDate.before(lRegDate)) {
                request.setAttribute("message", "Expiry date must be after registration date.");
                UserDAO userDAO = new UserDAO();
                request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
                request.setAttribute("license", existingLicense); // Pass original license back
                request.setAttribute("formAction", "edit");
                return LICENSE_FORM_PAGE;
            }

            // Update the existing DTO
            existingLicense.setFullName(fullName);
            existingLicense.setLicenseType(licenseType);
            existingLicense.setlRegDate(lRegDate);
            existingLicense.setExpDate(expDate);

            boolean success = licenseDAO.update(existingLicense);

            if (success) {
                request.setAttribute("message", "License updated successfully!");
            } else {
                request.setAttribute("message", "Failed to update license.");
            }
        } catch (ParseException e) {
            request.setAttribute("message", "Invalid date format. Please use YYYY-MM-DD.");
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            request.setAttribute("license", existingLicense); // Pass original license back
            request.setAttribute("formAction", "edit");
            return LICENSE_FORM_PAGE; // Go back to form on date error
        } catch (Exception e) { // Catch broader errors
            request.setAttribute("message", "An error occurred during update: " + e.getMessage());
            UserDAO userDAO = new UserDAO();
            request.setAttribute("eligibleUsers", userDAO.readAll().stream().filter(u -> u.getCitizenID() != -1).collect(Collectors.toList()));
            request.setAttribute("license", existingLicense); // Pass original license back
            request.setAttribute("formAction", "edit");
            return LICENSE_FORM_PAGE;
        }

        // Redirect back to manage licenses page after update/fail
        request.setAttribute("direct", MANAGE_LICENSES_PAGE);
        return processDirect_Data(request, response);
    }

    private String processDeleteLicense(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        // Auth check - Admin Only
        if (session == null || !AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
            request.setAttribute("errorMessage", "Access Denied: Administrators only.");
            return ERROR_PAGE;
        }

        String licenseID = request.getParameter("licenseID");

        if (licenseID == null || licenseID.trim().isEmpty()) {
            request.setAttribute("message", "License ID is required for deletion.");
        } else {
            LicenseDAO licenseDAO = new LicenseDAO();
            boolean success = licenseDAO.delete(licenseID);
            if (success) {
                request.setAttribute("message", "License deleted successfully!");
            } else {
                request.setAttribute("message", "Failed to delete license. It might not exist or another error occurred.");
            }
        }

        // Redirect back to manage licenses page
        request.setAttribute("direct", MANAGE_LICENSES_PAGE);
        return processDirect_Data(request, response);
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
                    case "editUserProfile":
                        url = processEditUserProfile(request, response);
                        break;
                    case "searchUsers":
                        url = processSearchUsers(request, response);
                        break;
                    case "editAdminProfile":
                        url = processEditAdminProfile(request, response);
                        break;
                    case "editUserProfileForm":
                        url = processEditUserProfileForm(request, response);
                        break;
                    case "editOtherUserProfile":
                        url = processEditOtherUserProfile(request, response);
                        break;
                    case "editOtherUserProfileForm":
                        url = processEditOtherUserProfileForm(request, response);
                        break;
                    case "changePassword":
                        url = processChangePassword(request, response);
                        break;
                    case "sortArticles":
                        url = processSortArticles(request, response);
                        break;
                    case "searchHomepageNews":
                        url = processSearchHomepageNews(request, response);
                        break;
                    case "sortForums":
                        url = processSortForums(request, response);
                        break;
                    case "searchForums":
                        url = processSearchForums(request, response);
                        break;
                    case "searchLicenses":
                        url = processSearchLicenses(request, response);
                        break;
                    case "addLicense":
                        url = processAddLicense(request, response);
                        break;
                    case "editLicense":
                        url = processEditLicense(request, response);
                        break;
                    case "deleteLicense":
                        url = processDeleteLicense(request, response);
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
