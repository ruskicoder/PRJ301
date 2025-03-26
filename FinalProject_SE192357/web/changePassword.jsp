<%-- 
    Document   : changePassword
    Created on : Mar 24, 2025, 11:25:54 PM
    Author     : PC
--%>

<%@page import="utils.AuthUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
        <link rel="stylesheet" type="text/css" href="assets/css/changePassword.css">
    </head>
    <body>
        <%@include file="header.jsp" %>

        <%
            if (!AuthUtils.isLoggedIn(session)) {
                response.sendRedirect("login.jsp");
                return;
            }
            if (AuthUtils.isGuest(session)) {
        %>
        <div class="error-container">
            <h1>Error</h1>
            <p>Guests cannot change password.</p>
            <a href="dashboard.jsp">Back to Dashboard</a>
        </div>
        <%
                return;
            }
        %>

        <div class="change-password-container">
            <h2>Change Password</h2>
            <form action="main" method="post">
                <input type="hidden" name="action" value="changePassword" />

                <div class="form-group">
                    <label for="oldPassword">Old Password:</label>
                    <input type="password" id="oldPassword" name="oldPassword" required>
                </div>
                <div class="form-group">
                    <label for="newPassword">New Password:</label>
                    <input type="password" id="newPassword" name="newPassword" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm New Password:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>

                <button type="submit" class="change-password-btn">Change Password</button>
            </form>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct" />
                <input type="hidden" name="direct" value="editProfile.jsp"/>
                <button type="submit" class="back-btn">Back to profile</button>
            </form>
            <p class="message">
                <%
                    String message = (String) request.getAttribute("message");
                    if (message != null) {
                        out.println(message);
                    }
                %>
            </p>
        </div>

        <%@include file="footer.jsp" %>
    </body>
</html>