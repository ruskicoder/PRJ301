<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng nhập</title>
        <link rel="stylesheet" type="text/css" href="assets/css/login.css">
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