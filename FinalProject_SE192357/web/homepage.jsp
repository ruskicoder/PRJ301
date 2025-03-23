<%-- 
    Document   : homepage
    Created on : Mar 13, 2025, 11:16:56 AM
    Author     : PC
--%>
<%@page import="dto.ArticleDTO"%>
<%@page import="java.util.List"%>
<%@page import="dao.ArticleDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
        <link rel="stylesheet" type="text/css" href="assets/css/homepage.css">
    </head>
    <body>
        <%@include file="header.jsp" %>

        <div class="page-container">
            <div class="articles-container">
                <%
                    ArticleDAO articleDAO = new ArticleDAO();
                    List<ArticleDTO> articles = articleDAO.readAll(); // Fetch all articles
                    if (articles != null && !articles.isEmpty()) {
                        for (ArticleDTO article : articles) {
                %>
                <a href="details.jsp?id=<%= article.getArticleID()%>" style="text-decoration: none;">            
                    <div class="article-card">
                        <img src="<%= article.getThumbnail()%>" alt="<%= article.getTitle()%>" class="article-thumbnail">
                        <div class="article-info">
                            <h2 class="article-title"><%= article.getTitle()%></h2>
                            <p class="article-subtitle"><%= article.getSubtitle() != null ? article.getSubtitle() : ""%></p>
                        </div>
                    </div>
                </a>
                <%
                    }
                } else {
                %>
                <p>No articles found.</p>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>