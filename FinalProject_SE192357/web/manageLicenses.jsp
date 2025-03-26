<%-- 
    Document   : manageLicenses
    Created on : Mar 26, 2025, 9:26:12 PM
    Author     : PC
--%>

<%-- /manageLicenses.jsp --%>
<%@page import="dto.LicenseDTO"%>
<%@page import="java.util.List"%>
<%@page import="utils.AuthUtils"%>
<%@page import="dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quản Lý Giấy Phép Lái Xe</title>
        <link rel="stylesheet" type="text/css" href="assets/css/manageLicenses.css"> <%-- Link to new CSS --%>
    </head>
    <body>
        <%@include file="header.jsp" %>

        <%
            // Authentication: Must be logged in, cannot be Guest
            if (!AuthUtils.isLoggedIn(session)) {
                response.sendRedirect("login.jsp");
                return;
            }
            if (AuthUtils.isGuest(session)) {
                // Guests cannot access this page
        %>
        <div class="error-container manage-licenses-container"> <%-- Added container class --%>
            <h1>Access Denied</h1>
            <p>Guests cannot manage licenses.</p>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct" />
                <input type="hidden" name="direct" value="dashboard.jsp"/>
                <button type="submit" class="back-btn">Back to Dashboard</button>
            </form>
        </div>
        <%
                return; // Stop processing
            }
            UserDTO currentUser = AuthUtils.getUser(session); // Get current user
        %>

        <div class="manage-licenses-container">
            <h1>Quản Lý Giấy Phép Lái Xe</h1>

            <div class="search-section">
                <form action="main" method="post">
                    <input type="hidden" name="action" value="searchLicenses" />
                    <input type="text" name="searchTerm" placeholder="Search by License ID..." value="${param.searchTerm}" />
                    <button type="submit" class="search-btn">Search</button>
                </form>

                <%-- Add License Button (Admin Only) --%>
                <% if (AuthUtils.isAdmin(session)) { %>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="direct"/>
                    <input type="hidden" name="direct" value="licenseForm.jsp"/>
                    <%-- Pass an indicator that this is for adding --%>
                    <input type="hidden" name="formAction" value="add"/>
                    <button type="submit" class="add-license-btn">Thêm GPLX</button>
                </form>
                <% } %>
            </div>
            <p class="message">
                <%
                    String message = (String) request.getAttribute("message");
                    if (message != null && !message.isEmpty()) {
                        out.println(message);
                    }
                %>
            </p>

            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>License ID</th>
                            <th>Full Name</th>
                            <th>License Type</th>
                            <th>Registration Date</th>
                            <th>Expiry Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<LicenseDTO> licenses = (List<LicenseDTO>) request.getAttribute("licenses");
                            if (licenses != null && !licenses.isEmpty()) {
                                for (LicenseDTO license : licenses) {
                                    boolean canManage = AuthUtils.isAdmin(session); // Only admins can edit/delete
                        %>
                        <tr>
                            <td><%= license.getLicenseID()%></td>
                            <td><%= license.getFullName()%></td>
                            <td><%= license.getLicenseType()%></td>
                            <td><%= license.getlRegDate()%></td>
                            <td><%= license.getExpDate()%></td>
                            <td>
                                <% if (canManage) {%>
                                <%-- Edit Button --%>
                                <form action="main" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="direct" />
                                    <input type="hidden" name="direct" value="licenseForm.jsp" />
                                    <input type="hidden" name="licenseID" value="<%= license.getLicenseID()%>" />
                                    <%-- Pass an indicator that this is for editing --%>
                                    <input type="hidden" name="formAction" value="edit"/>
                                    <button type="submit" class="edit-btn image-button">
                                        <img src="assets/images/edit_icon.png" alt="Edit" class="icon">
                                    </button>
                                </form>
                                <%-- Delete Button --%>
                                <form action="main" method="post" style="display: inline;" onsubmit="return confirmDeleteLicense();">
                                    <input type="hidden" name="action" value="deleteLicense" />
                                    <input type="hidden" name="licenseID" value="<%= license.getLicenseID()%>" />
                                    <button type="submit" class="delete-btn image-button">
                                        <img src="assets/images/delete_icon.png" alt="Delete" class="icon">
                                    </button>
                                </form>
                                <% } else { %>
                                <span>No actions available</span>
                                <% } %>
                            </td>
                        </tr>
                        <%
                            } // end for
                        } else {
                        %>
                        <tr>
                            <td colspan="6">No licenses found.</td>
                        </tr>
                        <%
                            } // end if else
                        %>
                    </tbody>
                </table>
            </div>
            <%-- Back to Dashboard Button --%>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct" />
                <input type="hidden" name="direct" value="dashboard.jsp"/>
                <button type="submit" class="back-btn">Back to Dashboard</button>
            </form>
        </div>

        <script src="assets/js/manageLicenses.js"></script>
        <%@include file="footer.jsp" %>
    </body>
</html>