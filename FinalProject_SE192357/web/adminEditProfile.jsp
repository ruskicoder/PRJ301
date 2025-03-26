<%-- 
    Document   : adminEditProfile
    Created on : Mar 24, 2025, 11:49:56 PM
    Author     : PC
--%>

<%@page import="dto.UserDTO"%>
<%@page import="utils.AuthUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit User Profile (Admin)</title>
        <link rel="stylesheet" type="text/css" href="assets/css/adminEditProfile.css"> 
    </head>
    <body>
        <%@include file="header.jsp" %>

        <%
            if (!AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
                response.sendRedirect("login.jsp"); // Or a dedicated error page
                return;
            }

            UserDTO userToEdit = (UserDTO) request.getAttribute("userToEdit");
            if (userToEdit == null) {
                // Handle case where user data isn't available (shouldn't normally happen)
        %>
        <p>Error: User data not found.</p>
        <%
                return; // Stop processing
            }
        %>

        <div class="admin-edit-profile-container">
            <h2>Edit User (<%= userToEdit.getUserName()%>)</h2>

            <form action="main" method="post">
                <input type="hidden" name="action" value="editOtherUserProfile" />
                <input type="hidden" name="username" value="<%= userToEdit.getUserName()%>" />
                <div class="form-group">
                    <label for="fullname">Full Name:</label>
                    <input type="text" id="fullname" name="fullname" value="<%= userToEdit.getFullName()%>" required />
                </div>
                <div class="form-group">
                    <label for="role">Role:</label>
                    <select id="role" name="role" required>
                        <option value="AD" <%= userToEdit.getRole().equals("AD") ? "selected" : ""%>>Admin</option>
                        <option value="US" <%= userToEdit.getRole().equals("US") ? "selected" : ""%>>User</option>
                        <option value="GU" <%= userToEdit.getRole().equals("GU") ? "selected" : ""%>>Guest</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="citizenID">Citizen ID:</label>
                    <input type="text" id="citizenID" name="citizenID" value="<%= userToEdit.getCitizenID()%>" required />
                </div>
                <div class="form-group">
                    <label for="userImage">Profile Image:</label>
                    <div id="drop-area">
                        <p>Drag and drop image here, or click to select file</p>
                        <input type="file" id="userImage" name="userImage" accept="image/*"  onchange="handleFiles(this.files)"/>
                        <img id="preview" src="<%= userToEdit.getUserImage() != null ? userToEdit.getUserImage() : ""%>"  style="<%= userToEdit.getUserImage() != null ? "display:block;" : ""%>"/>
                    </div>
                    <input type="hidden" id="userImageData" name="userImageData" value="<%= userToEdit.getUserImage() != null ? userToEdit.getUserImage() : ""%>"/>
                </div>
                <button type="submit" class="update-btn">Update User</button>
            </form>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct"/>
                <input type="hidden" name="direct" value="editProfile.jsp"/>
                <button type= "submit" class="back-btn">Back</button>
            </form>
        </div>
        <script src="assets/js/adminEditProfile.js"></script>
    </body>
</html>
