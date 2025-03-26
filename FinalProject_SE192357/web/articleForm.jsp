<%-- 
    Document   : articleForm
    Created on : Mar 20, 2025, 11:32:13 PM
    Author     : PC
--%>

<%@page import="dto.ArticleDTO"%>
<%@page import="dao.ArticleDAO"%>
<%@page import="utils.AuthUtils"%>
<%@page import="dto.UserDTO" %>
<%@page import="utils.Base64Image" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Article Form</title>
        <link rel="stylesheet" type="text/css" href="assets/css/articleForm.css">
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="article-form-container">
            <%
                // Check if the user is logged in.  Redirect to login if not.
                if (!AuthUtils.isLoggedIn(session)) {
                    response.sendRedirect("login.jsp");
                    return; // Important: Stop further processing of this JSP.
                }

                String articleID = request.getParameter("articleID");
                ArticleDTO article = null;
                if (articleID != null && !articleID.isEmpty()) {
                    ArticleDAO dao = new ArticleDAO();
                    article = dao.readById(articleID);
                }
                boolean isEdit = (article != null);
                UserDTO user = AuthUtils.getUser(session);

                //This check is technically redundant now because of the isLoggedIn check above,
                //but it's good practice to keep it for potential future changes.
                if (user == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }
            %>
            <h1><%= isEdit ? "Edit Article" : "Add New Article"%></h1>
            <form action="main" method="post">
                <input type="hidden" name="action" value="<%= isEdit ? "editArticle" : "addArticle"%>" />
                <% if (isEdit) {%>
                <input type="hidden" name="articleID" value="<%= article.getArticleID()%>" />
                <% }%>

                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" value="<%= isEdit ? article.getTitle() : ""%>" required />
                </div>

                <div class="form-group">
                    <label for="subtitle">Subtitle:</label>
                    <input type="text" id="subtitle" name="subtitle" value="<%= isEdit ? (article.getSubtitle() != null ? article.getSubtitle() : "") : ""%>" />
                </div>

                <div class="form-group">
                    <label for="author">Author:</label>
                    <%
                        if (AuthUtils.isAdmin(session)) {
                    %>
                    <input type="text" id="author" name="author" value="<%= isEdit ? article.getAuthor() : user.getFullName()%>" required/>
                    <%
                    } else {
                    %>
                    <input type="text" id="author" name="author" value="<%= isEdit ? article.getAuthor() : user.getFullName()%>" required readonly/>
                    <%
                        }
                    %>
                </div>

                <div class="form-group">
                    <label for="content">Content:</label>
                    <textarea id="content" name="content" rows="10" required><%= isEdit ? article.getContent() : ""%></textarea>
                </div>

                <div class="form-group">
                    <label for="thumbnail">Thumbnail:</label>
                    <div id="drop-area">
                        <p>Drag and drop image here, or click to select file</p>
                        <input type="file" id="thumbnail" name="thumbnail" accept="image/*"  onchange="handleFiles(this.files)"/>
                        <img id="preview" src="<%= isEdit ? article.getThumbnail() : ""%>"  style="<%= isEdit ? "display:block;" : ""%>"/>
                    </div>
                    <input type="hidden" id="thumbnail-data" name="thumbnail-data" value="<%= isEdit ? article.getThumbnail() : ""%>"/>
                </div>

                <div class="form-group">
                    <label for="articleType">Article Type:</label>
                    <select id="articleType" name="articleType" required>
                        <% if (AuthUtils.isAdmin(session)) {%>
                        <option value="News" <%= isEdit && article.getArticleType().equals("News") ? "selected" : ""%>>News</option>
                        <option value="Other" <%= isEdit && article.getArticleType().equals("Other") ? "selected" : ""%>>Other</option>
                        <option value="Forum" <%= isEdit && article.getArticleType().equals("Forum") ? "selected" : ""%>>Forum</option>
                        <%} else {%>

                        <option value="Forum" <%= isEdit && article.getArticleType().equals("Forum") ? "selected" : ""%>>Forum</option>
                        <% }%>
                    </select>
                </div>
                <div>
                    <button type="submit" class="submit-btn"><%= isEdit ? "Update Article" : "Add Article"%></button>
                </div>
            </form>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct" />
                <input type="hidden" name="direct" value="manageArticles.jsp"/>
                <button type="submit" class="back-btn">Back to manage articles</button>
            </form>
        </div>
        <script src="assets/js/articleForm.js"></script>
    </body>
</html>