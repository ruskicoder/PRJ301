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
            <!-- Search Section -->
            <div class="search-section">
                <form action="main" method="post" style="display: flex; flex-grow: 1; gap: 10px; justify-content: center; align-items: center; flex-wrap: wrap;">
                    <input type="hidden" name="action" value="searchHomepageNews" />
                    <input type="text" name="searchTerm" placeholder="Search news articles..." value="${requestScope.searchTerm}" />
                    <select name="filterDate">
                        <option value="Newest" ${empty requestScope.filterDate || requestScope.filterDate == 'Newest' ? 'selected' : ''}>Newest</option>
                        <option value="Oldest" ${requestScope.filterDate == 'Oldest' ? 'selected' : ''}>Oldest</option>
                    </select>
                    <button type="submit" class="search-btn">Search</button>
                </form>
            </div>


            <div class="articles-container">
                <%
                    List<ArticleDTO> articles = (List<ArticleDTO>) request.getAttribute("articles");
                    boolean articlesFound = false; // Flag to check if any news articles are found
                    if (articles != null && !articles.isEmpty()) {
                        for (ArticleDTO article : articles) {
                            // Filter for "News" type articles only
                            if ("News".equals(article.getArticleType())) {
                                articlesFound = true; // Found at least one news article
                %>
                <div class="article-card">
                    <a href="details.jsp?id=<%= article.getArticleID()%>">
                        <img src="<%= article.getThumbnail() != null && !article.getThumbnail().isEmpty() ? article.getThumbnail() : "assets/images/no_image.jpg" %>" alt="<%= article.getTitle()%>" class="article-thumbnail">
                        <div class="article-info">
                            <h2 class="article-title"><%= article.getTitle()%></h2>
                            <p class="article-subtitle"><%= article.getSubtitle() != null ? article.getSubtitle() : ""%></p>
                        </div>
                    </a>
                </div>
                <%
                            } // End if "News"
                        } // End for loop
                    }

                    // Display message if no articles or no news articles were found
                    if (!articlesFound) {
                %>
                <p class="no-articles">No news articles found.</p>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>