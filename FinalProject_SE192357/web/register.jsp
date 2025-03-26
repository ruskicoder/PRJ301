<%-- 
    Document   : register
    Created on : Mar 13, 2025, 12:28:15 PM
    Author     : PC
--%>

<%@page import="utils.Base64Image"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" type="text/css" href="assets/css/register.css">
    </head>
    <body>
        <div class = "header-container">
            <%@include file="header.jsp" %>
        </div>

        <div class="register-container">
            <div class="register-form">
                <h2 class="form-title">ĐĂNG KÝ</h2>
                <form action="main" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="register"/> 
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="fullname">Họ và tên:</label>
                        <input type="text" id="fullname" name="fullname" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="form-group">
                        <label for="confirmPassword">Nhập lại mật khẩu:</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                    </div>
                    <div class="form-group">
                        <label for="citizenID">Số CCCD/CMND/Mã định danh:</label>
                        <input type="number" id="citizenID" name="citizenID" required>
                    </div>
                    <div class="form-group">
                        <label for="dob">Ngày tháng năm sinh:</label>
                        <input type="date" id="dob" name="dob" required>
                    </div>
                    <div class="form-group">
                        <label for="userImage">User Image:</label>
                        <div id="drop-area">
                            <p>Drag and drop image here, or click to select file</p>
                            <input type="file" id="userImage" name="userImage" accept="image/*"  onchange="handleFiles(this.files)"/>
                            <img id="preview" src=""  />
                        </div>
                        <input type="hidden" id="userImageData" name="userImageData" value=""/>
                    </div>
                    <div>
                        <button type="submit" class="submit-btn">ĐĂNG KÝ</button>
                    </div>

                    <p class="message">
                        <%
                            String errorMessage = (String) request.getAttribute("errorMessage");
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                out.println(errorMessage);
                            }
                            String successMessage = (String) request.getAttribute("successMessage");
                            if (successMessage != null && !successMessage.isEmpty()) {
                                out.println(successMessage);
                            }
                        %>
                    </p>
                </form>
            </div>
        </div>

        <script src="assets/js/register.js"></script>
        <%@include file="footer.jsp" %>
    </body>
</html>