package utils;

import dao.UserDAO;
import dto.UserDTO;
import javax.servlet.http.HttpSession;

public class AuthUtils {

    private static final String FOUNDER_ROLE = "Founder";
    private static final String TEAM_MEMBER_ROLE = "Team Member";

    public static UserDTO getUser(String username) {
        UserDAO udao = new UserDAO();
        UserDTO user = udao.readById(username);
        return user;
    }

    public static boolean isValidLogin(String username, String password) {
        UserDAO userDao = new UserDAO();
        UserDTO user = userDao.login(username, password);
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

    public static boolean isFounder(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return user.getRole().equals(FOUNDER_ROLE);
    }
     public static boolean isTeamMember(HttpSession session) {
        if (!isLoggedIn(session)) {
            return false;
        }
        UserDTO user = getUser(session);
        return user.getRole().equals(TEAM_MEMBER_ROLE);
    }
}