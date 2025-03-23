/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dao.UserDAO;
import dto.UserDTO;
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
        return udao.checkLogin(strUserID, strPassword)!=null;
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
    
    public static boolean isLoginPage(HttpServletRequest request){
        String url = request.getAttribute("url") + "";
        return url.equals("login.jsp");
    }
    
    public static boolean isRegisterPage(HttpServletRequest request){
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
}
