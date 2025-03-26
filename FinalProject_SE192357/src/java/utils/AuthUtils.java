/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dao.UserDAO;
import dto.UserDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author PC
 */
public class AuthUtils {

    private static final String ADMIN_ROLE = "AD";
    private static final String USER_ROLE = "US";
    private static final String GUEST_ROLE = "GU";

    public static UserDTO getUser(String strUserID) {
        UserDAO udao = new UserDAO();
        UserDTO user = udao.readById(strUserID);
        return user;
    }

    public static boolean isValidLogin(String strUserID, String strPassword) {
        UserDAO udao = new UserDAO();
        return udao.checkLogin(strUserID, strPassword) != null;
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public static boolean isLoginPage(HttpServletRequest request) {
        String url = request.getAttribute("url") + "";
        return url.equals("login.jsp");
    }

    public static boolean isRegisterPage(HttpServletRequest request) {
        String url = request.getAttribute("url") + "";
        return url.equals("register.jsp");
    }

    public static UserDTO getUser(HttpSession session) {
        if (!isLoggedIn(session)) {
            return null;
        }
        return (UserDTO) session.getAttribute("user");
    }

    public static boolean isAdmin(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return user.getRole().equals(ADMIN_ROLE);
    }

    public static boolean isUser(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return user.getRole().equals(USER_ROLE);
    }

    public static boolean isGuest(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return user.getRole().equals(GUEST_ROLE);
    }

    public static boolean checkPasswordFormat(String password, HttpServletRequest request) {
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("message", "Password cannot be empty or contain only spaces.");
            return false;
        }
        if (password.length() < 4) {
            request.setAttribute("message", "Password must be at least 4 characters long.");
            return false;
        }
        if (Character.isDigit(password.charAt(0))) {
            request.setAttribute("message", "Password must not begin with a number.");
            return false;
        }
        if (password.matches(".*\\s.*")) {
            request.setAttribute("message", "Password must not contain spaces.");
            return false;
        }

        // Check for at least one uppercase letter and one number, allowing only "_" as a special character
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9_]+$");
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            request.setAttribute("message", "Password must contain at least one uppercase letter, one number, and only allow '_' as a special character.");
            return false;
        }

        return true;
    }

     public static boolean AuthUpdatePassword(UserDTO user, String oldPassword, String newPassword, String confirmPassword, HttpServletRequest request) {
        if (!checkPasswordFormat(newPassword, request)) {
            return false; // checkPasswordFormat already sets the error message.
        }
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("message", "New passwords do not match.");
            return false;
        }

        UserDAO userDao = new UserDAO();
        UserDTO userFromDB = userDao.checkLogin(user.getUserName(), oldPassword);

        if (userFromDB == null) {
            request.setAttribute("message", "Incorrect old password."); 
            return false;
        }
        
        return true; 
    }
}
