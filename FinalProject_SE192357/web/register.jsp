<%-- 
    Document   : register
    Created on : Mar 13, 2025, 12:28:15 PM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Register</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        .register-container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
            margin-top: 8vh;
            background: linear-gradient(135deg, #7f7fd5, #86a8e7, #91eae4); /* Gradient background */

        }

        .register-form {
            background-color: rgba(255, 255, 255, 0.95);
            padding: 10px;
            padding-left: 30px;
            padding-right: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 500px; /* Wider form */
        }

        .form-title {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
            font-size: 1.8em;
            font-weight: 600;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #444;
            font-weight: 500;
        }

       .form-group input[type="text"],
        .form-group input[type="password"],
        .form-group input[type="date"],
        .form-group input[type="number"]
         {
            width: calc(100% - 22px);
            padding: 10px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            outline: none;
            font-size: 1rem;
              transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .form-group input[type="text"]:focus,
        .form-group input[type="password"]:focus,
        .form-group input[type="date"]:focus,
        .form-group input[type="number"]:focus{
            border-color: #007bff;
            box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
        }

        .submit-btn {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50; /* Green button */
            color: #fff;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: 500;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 8px rgba(76, 175, 80, 0.3); /* Shadow for green button */
        }

        .submit-btn:hover {
            background-color: #388e3c; /* Darker green */
            transform: translateY(-2px);
        }

        .message {
             color: #e74c3c; /* Red color for errors */
            text-shadow: 0px 0px 5px rgba(255,255,255,0.7);
            padding: 8px 12px;
            border-radius: 6px;
            text-align: center;
            font-size: 0.9em;
            font-weight: 500;
            margin-top: 15px;
        }
         .header-container{
                width: 100%;
            }
    </style>
</head>
<body>
    <div class = "header-container">
        <%@include file="header.jsp" %>
    </div>

    <div class="register-container">
        <div class="register-form">
            <h2 class="form-title">ĐĂNG KÝ</h2>
            <form action="main" method="post">
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
</body>
</html>
