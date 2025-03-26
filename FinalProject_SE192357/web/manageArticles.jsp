<%-- 
    Document   : manageArticles
    Created on : Mar 20, 2025, 11:30:01 PM
    Author     : PC
--%>

<%@page import="dto.ArticleDTO"%>
<%@page import="java.util.List"%>
<%@page import="dao.ArticleDAO"%>
<%@page import="utils.AuthUtils"%>
<%@page import="dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản Lý Bài Viết</title>
        <link rel="stylesheet" type="text/css" href="assets/css/manageArticles.css">
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="manage-articles-container">
            <h1>Quản Lý Bài Viết</h1>

            <div class="search-section">
                <form action="main" method="post">
                    <input type="hidden" name="action" value="searchArticles" />

                    <input type="text" name="searchTerm" placeholder="Search articles..." value="${param.searchTerm}" />

                    <select name="filterBy">
                        <option value="all" ${param.filterBy == 'all' ? 'selected' : ''}>All Articles</option>
                        <option value="myArticles" ${param.filterBy == 'myArticles' ? 'selected' : ''}>My Articles</option>
                        <option value="byAuthor" ${param.filterBy == 'byAuthor' ? 'selected' : ''}>By Author</option>
                        <option value="Forum" ${param.filterBy == 'Forum' ? 'selected' : ''}>Forum</option>
                        <option value="News" ${param.filterBy == 'News' ? 'selected' : ''}>News</option>
                        <option value="Other" ${param.filterBy == 'Other' ? 'selected' : ''}>Other</option>
                    </select>
                    <select name="filterDate">
                        <option value="Newest" ${param.filterDate == 'Newest' ? 'selected' : ''}>Newest</option>
                        <option value="Oldest" ${param.filterDate == 'Oldest' ? 'selected' : ''}>Oldest</option>
                    </select>

                    <%-- Show input field for author search only when "By Author" is selected --%>
                    <input type="text" name="authorName" placeholder="Enter author name" value="${param.authorName}" style="${param.filterBy == 'byAuthor' ? 'display:inline-block;' : 'display:none;'}" />
                    <button type="submit" class="search-btn">Search</button>
                </form>
                <% if (!AuthUtils.isGuest(session)) {%>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="direct"/>
                    <input type="hidden" name="direct" value="articleForm.jsp"/>
                    <button type="submit" class="add-article-btn">Thêm Bài Viết</button>
                </form>
                <% }%>
            </div>
            <p class="message">
                <%
                    String filterMessage = (String) request.getAttribute("filterMessage");
                    if (filterMessage != null && !filterMessage.isEmpty()) {
                        out.println(filterMessage);
                    }
                %>
            </p>
            <%--  Notification Area  --%>
            <div id="notification" class="notification"></div>
            <input type="hidden" id="message" value="${requestScope.message}" />

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Thumbnail</th>
                            <th>ID</th>
                            <th>Tiêu Đề</th>
                            <th>Tác Giả</th>
                            <th>Loại</th>
                            <th>Ngày Đăng</th>
                            <th>Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<ArticleDTO> articles = (List<ArticleDTO>) request.getAttribute("articles");
                            if (articles != null && !articles.isEmpty()) {
                                for (ArticleDTO article : articles) {
                        %>
                        <tr>
                            <td><img src="<%= article.getThumbnail()%>" alt="Thumbnail" class="thumbnail"></td>
                            <td><%= article.getArticleID()%></td>
                            <td><%= article.getTitle()%></td>
                            <td><%= article.getAuthor()%></td>
                            <td><%= article.getArticleType()%></td>
                            <td><%= article.getPublishDate()%></td>
                            <td>
                                <form action="main" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="direct" />
                                    <input type="hidden" name="direct" value="details.jsp"/>
                                    <input type="hidden" name="id" value="<%= article.getArticleID()%>"/>
                                    <input type="hidden" name="source" value="manageArticles"/>
                                    <button type="submit" class="view-btn image-button">
                                        <img src="assets/images/view_icon.png" alt="View">
                                    </button>
                                </form>
                                <% if (!AuthUtils.isGuest(session)) {%>
                                <% if (AuthUtils.isAdmin(session) || (AuthUtils.isUser(session) && article.getAuthor().equals(AuthUtils.getUser(session).getFullName()))) {%>
                                <form action="main" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="direct" />
                                    <input type="hidden" name="direct" value="articleForm.jsp" />
                                    <input type="hidden" name="articleID" value="<%= article.getArticleID()%>" />
                                    <button type="submit" class="edit-btn image-button">
                                        <img src="assets/images/edit_icon.png" alt="Edit">
                                    </button>
                                </form>
                                <form action="main" method="post" style="display: inline;" onsubmit="return confirmDelete();">
                                    <input type="hidden" name="action" value="deleteArticle" />
                                    <input type="hidden" name="articleID" value="<%= article.getArticleID()%>" />
                                    <button type="submit" class="delete-btn image-button">
                                        <img src="assets/images/delete_icon.png" alt="Delete">
                                    </button>
                                </form>
                                <% }
                                    }%>
                            </td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="7">No articles found.</td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct" />
                <input type="hidden" name="direct" value="dashboard.jsp"/>
                <button type="submit" class="back-btn">Back to dashboard</button>
            </form>
        </div>

        <script src="assets/js/manageArticles.js"></script>

        <%@include file="footer.jsp" %>
    </body>
</html>