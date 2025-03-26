<%-- 
    Document   : dashboard
    Created on : Mar 9, 2025
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO"%>
<%@page import="utils.AuthUtils" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link rel="stylesheet" type="text/css" href="assets/css/dashboard.css">
    </head>
    <body>
        <%@include file="header.jsp" %>

        <div class = "content-container">
            <div class="sidebar">
                <div class="user-info">
                    <%
                        if (AuthUtils.isLoggedIn(session)) {
                            UserDTO loggedInUser = AuthUtils.getUser(session);
                    %>
                    <img src="<%= loggedInUser.getUserImage() != null ? loggedInUser.getUserImage() : ""%>" alt="Avatar" class="avatar">
                    <h2 class="username-info"><%= loggedInUser.getUserName()%></h2>

                    <% if (!AuthUtils.isGuest(session)) { %>
                    <form action="main" method="post">
                        <input type="hidden" name="action" value="direct" />
                        <input type="hidden" name="direct" value="editProfile.jsp" />
                        <button type="submit" class="edit-profile-btn">Edit Profile</button>
                    </form>
                    <% }%>

                    <p><strong>Họ và Tên:</strong> <%= loggedInUser.getFullName()%></p>

                    <% if (AuthUtils.isAdmin(session) || AuthUtils.isGuest(session)) {%>
                    <p><strong>Role:</strong> <%= loggedInUser.getRole()%></p>
                    <% } else if (!AuthUtils.isAdmin(session)) {%>
                    <!-- For regular users, show Citizen ID and DOB -->
                    <p><strong>CCCD/CMND/Mã định danh:</strong> <%= loggedInUser.getCitizenID()%></p>
                    <p><strong>Ngày tháng năm sinh:</strong><%= loggedInUser.getDob()%></p>
                    <% } %>

                    <%
                    } else {
                    %>
                    <p>No user logged in.</p>
                    <%
                        }
                    %>
                </div>
            </div>
            <div class="main-content">
                <% if (AuthUtils.isLoggedIn(session)) { %>
                <form action="main" method="post" style="display: inline-block; margin-right: 10px;"> <%-- Use inline-block for side-by-side --%>
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="manageArticles.jsp" />
                    <button type="submit" class="manage-articles-btn">Quản Lý Bài Viết</button>
                </form>
                <%-- Add Manage Licenses Button (visible to Admins and Users, not Guests) --%>
                <% if (!AuthUtils.isGuest(session)) { %>
                <form action="main" method="post" style="display: inline-block;"> <%-- Use inline-block for side-by-side --%>
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="manageLicenses.jsp" />
                    <button type="submit" class="manage-articles-btn">Quản Lý GPLX</button> <%-- Reusing style for consistency --%>
                </form>
                <% } %>
                <% }%>
            </div>
        </div>

        <%@include file="footer.jsp" %>
    </body>
</html>