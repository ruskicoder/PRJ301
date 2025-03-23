package utils;

import dao.UserDAO;
import dto.UserDTO;
import javax.servlet.http.HttpSession;

public class AuthUtils {

    public static UserDTO getUser(String username) {
        UserDAO udao = new UserDAO();
        UserDTO user = udao.readById(username);
        return user;
    }
     public static boolean isValidLogin(String username, String password) {
        UserDAO userDao = new UserDAO();
        UserDTO user = userDao.login(username, password); //check DAO
        return user != null;
    }


    public static boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public static UserDTO getUser(HttpSession session) {
        if (!isLoggedIn(session)) {
            return null;
        }
        return (UserDTO) session.getAttribute("user");
    }

    public static boolean isInstructor(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return "Instructor".equals(user.getRole());
    }
     public static boolean isStudent(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return "Student".equals(user.getRole());
    }
}