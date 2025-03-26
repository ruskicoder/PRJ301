<%-- 
    Document   : licenseForm
    Created on : Mar 26, 2025, 9:32:48 PM
    Author     : PC
--%>

<%-- /licenseForm.jsp --%>
<%@page import="dao.UserDAO"%>
<%@page import="dto.UserDTO"%>
<%@page import="java.util.ArrayList"%> <%-- Import ArrayList --%>
<%@page import="java.util.stream.Collectors"%> <%-- Keep this just in case, though not used in the loop --%>
<%@page import="dto.LicenseDTO"%>
<%@page import="dao.LicenseDAO"%>
<%@page import="utils.AuthUtils"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>License Form</title>
        <link rel="stylesheet" type="text/css" href="assets/css/licenseForm.css"> <%-- Link to new CSS --%>
    </head>
    <body>
        <%@include file="header.jsp" %>

        <%
            // Authentication: Must be logged in AND be an Admin
            if (!AuthUtils.isLoggedIn(session) || !AuthUtils.isAdmin(session)) {
                // Set an error message attribute for the error page
                request.setAttribute("errorMessage", "Access Denied: You must be an administrator to access this page.");
                // Use RequestDispatcher to forward to error.jsp
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                rd.forward(request, response);
                return; // Stop processing this JSP
            }

            // Determine if adding or editing
            String formAction = request.getParameter("formAction"); // Should be "add" or "edit"
            boolean isEdit = "edit".equals(formAction);

            LicenseDTO license = null;
            String licenseID = request.getParameter("licenseID"); // Get ID if editing

            if (isEdit && licenseID != null && !licenseID.isEmpty()) {
                LicenseDAO licenseDAO = new LicenseDAO();
                license = licenseDAO.readById(licenseID);
                if (license == null) {
                    request.setAttribute("errorMessage", "License with ID " + licenseID + " not found.");
                    RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                    rd.forward(request, response);
                    return;
                }
            } else if (isEdit) {
                // If isEdit is true but licenseID is missing
                request.setAttribute("errorMessage", "License ID is missing for edit action.");
                RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
                rd.forward(request, response);
                return;
            }

            // Fetch users for the dropdown (excluding guests/system accounts) - Traditional Loop
            UserDAO userDAO = new UserDAO();
            List<UserDTO> allUsers = userDAO.readAll();
            List<UserDTO> eligibleUsers = new ArrayList<>(); // Initialize the list
            if (allUsers != null) { // Check if the list is not null
                for (UserDTO user : allUsers) {
                    if (user.getCitizenID() != -1) { // Filter condition
                        eligibleUsers.add(user); // Add eligible users to the new list
                    }
                }
            }

        %>

        <div class="license-form-container">
            <h1><%= isEdit ? "Edit License" : "Add New License"%></h1>
            <form action="main" method="post">
                <input type="hidden" name="action" value="<%= isEdit ? "editLicense" : "addLicense"%>" />
                <% if (isEdit) {%>
                <input type="hidden" name="licenseID" value="<%= license.getLicenseID()%>" />
                <% }%>

                <div class="form-group">
                    <label for="licenseIDInput">License ID:</label>
                    <%-- Use a different name than the hidden field if editing --%>
                    <input type="text" id="licenseIDInput" name="licenseIDInput" value="<%= isEdit ? license.getLicenseID() : ""%>" <%= isEdit ? "readonly" : "required"%> />
                </div>

                <div class="form-group">
                    <label for="fullName">Full Name (Owner):</label>
                    <select id="fullName" name="fullName" required>
                        <option value="">-- Select User --</option>
                        <% for (UserDTO user : eligibleUsers) { // Iterate over the filtered list
                                boolean selected = isEdit && license != null && license.getFullName().equals(user.getFullName());
                        %>
                        <option value="<%= user.getFullName()%>" <%= selected ? "selected" : ""%>>
                            <%= user.getFullName()%> (<%= user.getUserName()%>)
                        </option>
                        <% }%>
                    </select>
                </div>

                <div class="form-group">
                    <label for="licenseType">License Type:</label>
                    <input type="text" id="licenseType" name="licenseType" value="<%= isEdit && license != null ? (license.getLicenseType() != null ? license.getLicenseType() : "") : ""%>" required />
                </div>

                <div class="form-group">
                    <label for="lRegDate">Registration Date:</label>
                    <input type="date" id="lRegDate" name="lRegDate" value="<%= isEdit && license != null && license.getlRegDate() != null ? license.getlRegDate().toString() : (request.getAttribute("lRegDateValue") != null ? request.getAttribute("lRegDateValue") : "")%>" required />
                </div>

                <div class="form-group">
                    <label for="expDate">Expiry Date:</label>
                    <input type="date" id="expDate" name="expDate" value="<%= isEdit && license != null && license.getExpDate() != null ? license.getExpDate().toString() : (request.getAttribute("expDateValue") != null ? request.getAttribute("expDateValue") : "")%>" required />
                </div>

                <div>
                    <button type="submit" class="submit-btn"><%= isEdit ? "Update License" : "Add License"%></button>
                </div>
                <p class="message">
                    <%
                        String formMessage = (String) request.getAttribute("message");
                        if (formMessage != null) {
                            out.println(formMessage);
                        }
                    %>
                </p>
            </form>

            <%-- Back to Manage Licenses Button --%>
            <form action="main" method="post">
                <input type="hidden" name="action" value="direct" />
                <input type="hidden" name="direct" value="manageLicenses.jsp"/>
                <button type="submit" class="back-btn">Back to Manage Licenses</button>
            </form>
        </div>

        <%@include file="footer.jsp" %>
    </body>
</html>