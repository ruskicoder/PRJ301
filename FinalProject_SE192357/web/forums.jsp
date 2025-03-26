<%-- 
    Document   : forums
    Created on : Mar 26, 2025, 6:24:43 PM
    Author     : PC
--%>

<%-- /forums.jsp --%>
<%@page import="utils.AuthUtils"%>
<%@page import="dto.ArticleDTO"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forums</title>
        <%-- Use homepage CSS or create a dedicated forums.css if needed --%>
        <link rel="stylesheet" type="text/css" href="assets/css/homepage.css">
        <link rel="stylesheet" type="text/css" href="assets/css/forums.css">
        <link rel="stylesheet" type="text/css" href="assets/css/pagination.css">
    </head>
    <body>
        <%@include file="header.jsp" %>

        <%-- Authorization Check --%>
        <%
            if (!AuthUtils.isLoggedIn(session) || AuthUtils.isGuest(session)) {
                // Option 1: Redirect to login
                // response.sendRedirect("login.jsp?message=Please+login+to+view+forums");

                // Option 2: Show error message and link
                request.setAttribute("errorMessage", "You must be logged in as a User or Admin to access the forums.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return; // Stop processing
            }
        %>

        <div class="page-container">
            <!-- Search and Add Section -->
            <div class="search-section search-add-container">
                <%-- Search Form Part --%>
                <div class="search-form-part">
                    <form action="main" method="post" style="display: flex; flex-grow: 1; gap: 10px; justify-content: center; align-items: center; flex-wrap: wrap;">
                        <input type="hidden" name="action" value="searchForums" /> <%-- New Action --%>
                        <input type="text" name="searchTerm" placeholder="Search forum posts..." value="${requestScope.searchTerm}" />
                        <select name="filterDate">
                            <option value="Newest" ${empty requestScope.filterDate || requestScope.filterDate == 'Newest' ? 'selected' : ''}>Newest</option>
                            <option value="Oldest" ${requestScope.filterDate == 'Oldest' ? 'selected' : ''}>Oldest</option>
                        </select>
                        <button type="submit" class="search-btn">Search</button>
                    </form>
                </div>

                <%-- Add Forum Post Button --%>
                <div>
                    <form action="main" method="post">
                        <input type="hidden" name="action" value="direct" />
                        <input type="hidden" name="direct" value="articleForm.jsp"/>
                        <%-- Optionally pre-select 'Forum' type if desired, handled in articleForm.jsp is better --%>
                        <%-- <input type="hidden" name="preselectType" value="Forum"/> --%>
                        <button type="submit" class="add-forum-btn">Add Forum Post</button>
                    </form>
                </div>
            </div>

            <%-- Pagination Controls (Top) --%>
            <div id="pagination-controls-top" class="pagination-controls"></div>

            <div id="articles-container" class="articles-container">
                <%
                    List<ArticleDTO> articles = (List<ArticleDTO>) request.getAttribute("articles");
                    boolean articlesFound = false;
                    if (articles != null && !articles.isEmpty()) {
                        articlesFound = true;
                        for (ArticleDTO article : articles) {
                            // Articles are pre-filtered by servlet
%>
                <div class="article-card">
                    <a href="details.jsp?id=<%= article.getArticleID()%>">
                        <img src="<%= article.getThumbnail() != null && !article.getThumbnail().isEmpty() ? article.getThumbnail() : "assets/images/no_image.jpg"%>" alt="<%= article.getTitle()%>" class="article-thumbnail">
                        <div class="article-info">
                            <h2 class="article-title"><%= article.getTitle()%></h2>
                            <p class="article-subtitle"><%= article.getSubtitle() != null ? article.getSubtitle() : ""%></p>
                            <%-- Display Author and Date --%>
                            <p style="font-size: 0.9em; color: #777; margin-top: auto; padding-top: 10px;">
                                By: <%= article.getAuthor()%> | <%= article.getPublishDate()%>
                            </p>
                        </div>
                    </a>
                </div>
                <%
                        } // End for loop
                    }

                    if (!articlesFound) {
                %>
                <p class="no-articles" style="grid-column: 1 / -1;">No forum posts found.</p>
                <%
                    }
                %>
            </div>

            <%-- Pagination Controls (Bottom) --%>
            <div id="pagination-controls-bottom" class="pagination-controls"></div>

        </div>

        <script src="assets/js/pagination.js"></script>

        <%@include file="footer.jsp" %>
    </body>
</html>
