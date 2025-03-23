<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng nhập</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; /* More modern font */
                background-color: #f0f2f5; /* Light gray background */
                margin: 0;
                padding: 0;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }

            .login-container {
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 40px 20px; /* More padding */
                background-image: url(assets/images/background-highway.jif);
                background-size: cover;
                background-position: center; /* Center the background image */
                background-repeat: no-repeat;
                position: relative; /* For positioning the blurred overlay */
            }

            .background-blur {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(255, 255, 255, 0.2); /* Semi-transparent white overlay */
                backdrop-filter: blur(8px); /* Increased blur */
                z-index: 1; /* Place it behind the form */
            }
            .login-form {
                position: relative;
                background-color: rgba(255, 255, 255, 0.95);
                padding: 50px 40px;
                border-radius: 12px;
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* More pronounced shadow */
                width: 100%;
                max-width: 450px;
                z-index: 2;
            }

            .form-title {
                text-align: center;
                color: #2c3e50; /* Darker blue */
                margin-bottom: 40px;
                font-size: 1.8em; /* Larger font size */
                font-weight: 600; /* Semi-bold */
            }

            .form-group {
                margin-bottom: 25px;
            }

            .form-group label {
                display: block;
                margin-bottom: 8px;
                color: #444;
                font-weight: 500;
            }

            .form-group input[type="text"],
            .form-group input[type="password"] {
                width: calc(100% - 22px);
                padding: 12px;
                border: 1px solid #ced4da; /* Lighter border */
                border-radius: 6px;
                outline: none;
                transition: border-color 0.3s ease, box-shadow 0.3s ease; /* Smooth transition */
                font-size: 1rem;
            }

            .form-group input[type="text"]:focus,
            .form-group input[type="password"]:focus {
                border-color: #007bff;
                box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25); /* Focus glow */
            }

            .submit-btn {
                width: 100%;
                padding: 12px;
                background-color: #007bff;
                color: #fff;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 1.1em; /* Larger font size */
                font-weight: 500;
                transition: background-color 0.3s ease, transform 0.2s ease; /* Smooth transition */
                box-shadow: 0 4px 8px rgba(0, 123, 255, 0.3); /* Button shadow */
            }

            .submit-btn:hover {
                background-color: #0056b3;
                transform: translateY(-2px); /* Slight lift on hover */
            }

            .message {
                color: #e74c3c; /* Red color for errors */
                text-shadow: 0px 0px 5px rgba(255,255,255,0.7);
                background-color: transparent;
                padding: 8px 12px;
                border-radius: 6px;
                text-align: center;
                font-size: 0.9em;
                font-weight: 500;
                margin-top: 15px; /* Space above the message */
            }

            .header-container {
                width: 100%;
            }
        </style>
    </head>
    <body>
        <div>
            <%@include file="header.jsp" %> 
        </div>
        <div class="login-container">
            <div class="background-blur"></div>
            <div class="login-form">
                <h2 class="form-title">ĐĂNG NHẬP</h2>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="login" />

                    <div class="form-group">
                        <label for="userId">Username</label>
                        <input type="text" id="userId" name="username" required />
                    </div>

                    <div class="form-group">
                        <label for="password">Mật khẩu</label>
                        <input type="password" id="password" name="password" required />
                    </div>

                    <span>
                        <button type="submit" class="submit-btn">Đăng nhập</button>
                        <%
                            String loginMessage = request.getAttribute("loginMessage") + "";

                        %>
                        <p class="message"><%=loginMessage.equals("null") ? "" : loginMessage%><p/>
                        <span/>
                </form>
            </div>
        </div>

    </body>
</html>