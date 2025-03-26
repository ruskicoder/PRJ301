<%-- 
    Document   : details
    Created on : Mar 13, 2025, 1:54:21 PM
    Author     : PC
--%>

<%@page import="utils.TextConvert"%>
<%@page import="java.sql.Date"%>
<%@page import="dto.ArticleDTO"%>
<%@page import="dao.ArticleDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Article Details</title>
        <link rel="stylesheet" type="text/css" href="assets/css/details.css">
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="container">

            <div class="side-placeholder"></div>
            <div class="article-content">
                <%
                    String articleId = request.getParameter("id");
                    ArticleDTO article = null;
                    if (articleId != null && !articleId.isEmpty()) {
                        ArticleDAO dao = new ArticleDAO();
                        article = dao.readById(articleId);
                    }

                    // Check if request is from ManageArticles after fetching the article
                    String source = request.getParameter("source"); // Get source from parameter
                    boolean fromManageArticles = "manageArticles".equals(source);
                    if (fromManageArticles) {
                %>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="manageArticles.jsp"/>
                    <button type="submit" class="back-btn">Back to Manage Articles</button>
                </form> <br/>
                <% } else { %>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="sortArticles" /> 
                    <input type="hidden" name="filterDate" value="Newest" /> 
                    <button type="submit" class="back-btn">Back to Homepage</button>
                </form> <br/>
                <% } %>

                <%
                    if (article != null) {
                %>
                <div class="article-thumbnail-container">
                    <img src="<%= article.getThumbnail() != null && !article.getThumbnail().isEmpty() ? article.getThumbnail() : "assets/images/no_image.jpg"%>" alt="<%= article.getTitle()%>" class="article-thumbnail">
                </div>
                <h1 class="article-title"><%= article.getTitle()%></h1>
                <h2 class="article-subtitle"><%= article.getSubtitle() != null ? article.getSubtitle() : ""%></h2>
                <p class="article-author">By <%= article.getAuthor()%></p>
                <% if (article.getPublishDate() != null) {%>
                <p class="article-date">Published on: <%= article.getPublishDate()%></p>
                <% }%>
                <%-- Display Article Type --%>
                <p class="article-type">Type: <%= article.getArticleType()%></p> 

                <div class="article-body">
                    <%= TextConvert.convertToHTMLFormat(article.getContent())%>
                </div>
                <% } else { %>
                <p>Article not found.</p>
                <% }%>
            </div>
            <div class="side-placeholder"></div>
        </div>

    </body>
</html>