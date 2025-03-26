<%-- 
    Document   : editProfile
    Created on : Mar 24, 2025, 1:03:44 PM
    Author     : PC
--%>

<%@page import="dto.UserDTO"%>
<%@page import="dao.UserDAO"%>
<%@page import="utils.AuthUtils"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Profile</title>
        <link rel="stylesheet" type="text/css" href="assets/css/editProfile.css">
    </head>
    <body>
        <%@include file="header.jsp" %>

        <%
            if (!AuthUtils.isLoggedIn(session)) {
                response.sendRedirect("login.jsp");
                return;
            }

            UserDTO loggedInUser = AuthUtils.getUser(session);
            if (loggedInUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            if (AuthUtils.isGuest(session)) {
        %>
        <div class="error-container">
            <h1>Error</h1>
            <p>Guests cannot edit profiles.</p>
            <a href="dashboard.jsp">Back to Dashboard</a>
        </div>
        <%      return; // Stop processing the rest of the JSP
            }
        %>

        <div class="edit-profile-container">
            <% if (AuthUtils.isAdmin(session)) {%>
            <div class="admin-sidebar">
                <h3>Edit Admin Profile</h3>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="editAdminProfile" />
                    <div class="form-group">
                        <label for="adminFullname">Full Name:</label>
                        <input type="text" id="adminFullname" name="fullname" value="<%= loggedInUser.getFullName()%>" required />
                    </div>

                    <div class="form-group">
                        <label for="adminUserImage">Profile Image:</label>
                        <div id="drop-area-admin">
                            <p>Drag and drop image here, or click to select file</p>
                            <input type="file" id="adminUserImage" name="userImage" accept="image/*"  onchange="handleFilesAdmin(this.files)"/>
                            <img id="preview-admin" src="<%= loggedInUser.getUserImage() != null ? loggedInUser.getUserImage() : ""%>"  style="<%= loggedInUser.getUserImage() != null ? "display:block;" : ""%>"/>
                        </div>
                        <input type="hidden" id="adminUserImageData" name="userImageData" value="<%= loggedInUser.getUserImage() != null ? loggedInUser.getUserImage() : ""%>"/>
                    </div>
                    <button type="submit" class="update-btn">Update Profile</button>
                </form>


                <form action="main" method="post">
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="changePassword.jsp" />
                    <button type="submit" class="change-password-btn">Change Password</button>
                </form>
            </div>
            <% }%>

            <div class="main-content <%= AuthUtils.isAdmin(session) ? "admin-view" : ""%>">
                <% if (AuthUtils.isAdmin(session)) { %>
                <h2>Manage Users (Admin)</h2>
                <div class="search-section">
                    <form action="main" method="post">
                        <input type="hidden" name="action" value="searchUsers" />
                        <input type="text" name="searchTerm" placeholder="Search users..." value="${param.searchTerm}" />
                        <select name="filterBy">
                            <option value="all" ${param.filterBy == 'all' ? 'selected' : ''}>All Users</option>
                            <option value="admins" ${param.filterBy == 'admins' ? 'selected' : ''}>Admins</option>
                            <option value="users" ${param.filterBy == 'users' ? 'selected' : ''}>Users</option>
                        </select>
                        <button type="submit" class="search-btn">Search</button>
                    </form>
                </div>

                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>Full Name</th>
                                <th>Role</th>
                                <th>CitizenID</th>
                                <th>Image</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<UserDTO> users = (List<UserDTO>) request.getAttribute("users");
                                if (users != null && !users.isEmpty()) {
                                    for (UserDTO user : users) {
                                        // Add this conditional check to filter out users with citizenID -1
                                        if (user.getCitizenID() != -1) {
                            %>
                            <tr>
                                <td><%= user.getUserName()%></td>
                                <td><%= user.getFullName()%></td>
                                <td><%= user.getRole()%></td>
                                <td><%= user.getCitizenID()%></td>
                                <td>
                                    <% if (user.getUserImage() != null && !user.getUserImage().isEmpty()) {%>
                                    <img src="<%= user.getUserImage()%>" alt="User Image" class = "user-image">
                                    <% } else { %>
                                    No Image
                                    <% }%>
                                </td>
                                <td>
                                    <form action="main" method="post">
                                        <input type="hidden" name="action" value="editOtherUserProfileForm" />
                                        <input type="hidden" name="username" value="<%= user.getUserName()%>" />
                                        <button type="submit" class="edit-btn">Edit</button>
                                    </form>
                                </td>
                            </tr>
                            <%          } // End of the if statement
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="6">No users found.</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>

                <% } else {%>
                <h2>Edit Your Profile</h2>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="editUserProfile" />

                    <div class="form-group">
                        <label for="fullname">Full Name:</label>
                        <input type="text" id="fullname" name="fullname" value="<%= loggedInUser.getFullName()%>" required/>
                    </div>

                    <div class="form-group">
                        <label for="citizenID">Citizen ID:</label>
                        <input type="text" id="citizenID" name="citizenID" value="<%= loggedInUser.getCitizenID()%>" required />
                    </div>
                    <div class="form-group">
                        <label for="userImage">Profile Image:</label>
                        <div id="drop-area">
                            <p>Drag and drop image here, or click to select file</p>
                            <input type="file" id="userImage" name="userImage" accept="image/*"  onchange="handleFiles(this.files)"/>
                            <img id="preview" src="<%= loggedInUser.getUserImage() != null ? loggedInUser.getUserImage() : ""%>"  style="<%= loggedInUser.getUserImage() != null ? "display:block;" : ""%>"/>
                        </div>
                        <input type="hidden" id="userImageData" name="userImageData" value="<%= loggedInUser.getUserImage() != null ? loggedInUser.getUserImage() : ""%>"/>
                    </div>

                    <button type="submit" class="update-btn">Update Profile</button>
                </form>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="changePassword.jsp" />
                    <button type="submit" class="change-password-btn">Change Password</button>
                </form>
                <%-- Add Back to Dashboard Button for Non-Admins --%>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="dashboard.jsp" />
                    <button type="submit" class="back-btn">Back to Dashboard</button>
                </form>

                <% } %>
                <p class="message">
                    <%
                        String message = (String) request.getAttribute("message");
                        if (message != null) {
                            out.println(message);
                        }
                    %>
                </p>
            </div>

        </div>


        <script src="assets/js/editProfile.js"></script>
        <%@include file="footer.jsp" %>
    </body>
</html>