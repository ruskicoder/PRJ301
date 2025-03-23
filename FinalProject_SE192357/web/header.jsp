<%-- 
    Document   : header
    Created on : Mar 8, 2025, 9:47:32 PM
    Author     : PC
--%>
<%-- 
    Document   : header
    Created on : Mar 8, 2025, 9:47:32 PM
    Author     : PC
--%>
<%@page import="utils.AuthUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.UserDTO"%>


<link rel="stylesheet" type="text/css" href="assets/css/header.css">


<header class="wiki-header">
    <div class="header-container">
        <div class="logo-section">
            <img src="assets/images/Emblem_of_Vietnam.png" class="logo-image" id="wiki-logo" name="wiki-logo">
            <ul class="titlegroup" style="list-style-type:none;">
                <li><span class="logo-text" id="logo-small" name="logo-text">BỘ GIAO THÔNG VẬN TẢI</span></li>
                <li><span class="logo-text" id="logo-large" name="logo-text">CỔNG THÔNG TIN GIAO THÔNG</span></li>
            </ul>
        </div>

        <nav class="nav-menu" id="main-nav" name="main-nav">
            <ul>
                <li><a href="homepage.jsp" id="nav-home" name="navbar"><img src="assets/images/home_icon.png"></a></li>
                <li><a href="#" id="nav-articles" name="navbar">BẢN TIN</a></li>
                <li><a href="#" id="nav-categories" name="navbar">ĐĂNG KIỂM</a></li>
                <li><a href="#" id="nav-community" name="navbar">PHẠT NGUỘI</a></li>
                <li><a href="#" id="nav-contribute" name="navbar">LỖI VI PHẠM</a></li>
                <li><a href="#" id="nav-help" name="navbar">TRỢ GIÚP</a></li>
                <li><a href="https://mt.gov.vn/vn/Pages/Trangchu.aspx" id="nav-about" name="navbar">VỀ BTGTVT</a></li>
                <li><a href="#" id="nav-contact" name="navbar">LIÊN HỆ</a></li>
            </ul>
        </nav>


        <% if (AuthUtils.isLoggedIn(session) && !AuthUtils.isGuest(session)) {
                UserDTO userHeader = AuthUtils.getUser(session);
        %>
        <div class="user-inline">
            <div class="user-profile">
                <div class="avatar-container">
                    <img src="<%= userHeader.getUserImage() != null ? userHeader.getUserImage() : ""%>" alt="Avatar" class="avatar">
                </div>
                <div class="user-info">
                    <span class="welcome-text">Xin chào,</span>
                    <span class="user-name"><%= userHeader.getFullName()%></span>
                </div>
            </div>
            <div class="user-actions">
                <form action="main" method="post" >
                    <input type="hidden" name="action" value="direct" />
                    <input type="hidden" name="direct" value="dashboard.jsp" />
                    <button type="submit" class="dashboard-btn" id="dashboard-icon">
                        <img src="assets/images/dashboard_icon.png" alt="Dashboard" />
                    </button>
                </form>
                <form action="main" method="post">
                    <input type="hidden" name="action" value="logout"/>
                    <button type="submit" class="logout-btn" id="logout-icon">
                        <img src="assets/images/logout_icon.png" alt="Logout"/>
                    </button>
                </form>
            </div>
        </div>
        <%
        } else if (AuthUtils.isGuest(session)) {
            UserDTO userHeader = AuthUtils.getUser(session);
        %>
        <div class="user-section">
            <span class="welcome-text">Bạn đang ở chế độ <span class="user-name"><%=userHeader.getFullName()%></span>!</span><br/>
            <div class="mini-login">
                <form action="main" method="post">
                    <input type="hidden" name="action" value="login" />
                    <input type="hidden" name="type" value="header" />

                    <div class="form-group">
                        <label for="userId">Tên đăng nhập</label>
                        <input type="text" id="userId" name="username" required />
                    </div>

                    <div class="form-group">
                        <label for="password">Mật khẩu</label>
                        <input type="password" id="password" name="password" required />
                    </div>
                    <span class="mini-login-optn">
                        <button type="submit" class="submit-btn">Đăng nhập</button>
                        <%
                            String loginMessage = request.getAttribute("loginMessage") + "";

                        %>
                        <p class="message"><%=loginMessage.equals("null") ? "" : loginMessage%><p/>

                    </span>
                </form>
                <span class="guest-operations">
                    <form action="main" method="post" >
                        <input type="hidden" name="action" value="direct" />
                        <input type="hidden" name="direct" value="dashboard.jsp" />
                        <button type="submit" class="dashboard-btn" id="dashboard-icon">
                            <img src="assets/images/dashboard_icon.png" alt="Dashboard"/>
                        </button>
                    </form>
                    <form action="main" method="post" >
                        <input type="hidden" name="action" value="logout"/>
                        <button type="submit" class="logout-btn" id="logout-icon">
                            <img src="assets/images/logout_icon.png" alt="Logout"/>
                        </button>
                    </form>
                </span>

                <%
                } else if (!AuthUtils.isLoggedIn(session) && !AuthUtils.isLoginPage(request)) {
                %>
                <div class="user-section">
                    <span class="welcome-text">Bạn chưa đăng nhập!</span><br/>
                    <div class="login-option">
                        <span>
                            <form action="main" method="post">
                                <input type="hidden" name="action" value="login" />
                                <input type="hidden" id="username" name="username" value="guest" />
                                <input type="hidden" id="password" name="password" value="" />
                                <button type="submit" class="submit-btn" id="guest-login">Đăng nhập với chế độ khách</button>
                            </form>
                            <%
                                if (!AuthUtils.isRegisterPage(request)) {
                            %>
                            <form action="main" method="post">
                                <input type="hidden" name="action" value="direct" />
                                <input type="hidden" name="direct" value="register.jsp" />
                                <button type="submit" class="submit-btn">Đăng ký</button>
                            </form>
                            <%
                                }
                            %>
                            <form action="main" method="post">
                                <input type="hidden" name="action" value="direct" />
                                <input type="hidden" name="direct" value="login.jsp" />
                                <button type="submit" class="submit-btn">Đăng nhập</button>
                            </form>
                        </span>
                        <%
                        } else {
                        %>
                        <form action="main" method="post">
                            <input type="hidden" name="action" value="login" />
                            <input type="hidden" id="username" name="username" value="guest" />
                            <input type="hidden" id="password" name="password" value="" />
                            <button type="submit" id="guest-login" class="submit-btn">Đăng nhập với chế độ khách</button>
                        </form>
                        <%
                            if (!AuthUtils.isRegisterPage(request)) {
                        %>
                        <form action="main" method="post">
                            <input type="hidden" name="action" value="direct" />
                            <input type="hidden" name="direct" value="register.jsp" />
                            <button type="submit" class="submit-btn">Đăng ký</button>
                        </form>
                        <%
                            }
                        %>
                        <%
                            }%>
                    </div>
                    </header>