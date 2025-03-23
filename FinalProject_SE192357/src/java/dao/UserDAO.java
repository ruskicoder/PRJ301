package dao;

import dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;
import utils.Hash;
import java.sql.Date;

public class UserDAO implements IDAO<UserDTO, String> {

    @Override
    public boolean create(UserDTO entity) {
        String sql = "INSERT INTO tblUsers (Username, Fullname, Password, Role, Money, UserImage, CitizenID, DOB) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            String hash = Hash.toSHA256(entity.getPassword());
            ps.setString(1, entity.getUserName());
            ps.setString(2, entity.getFullName());
            ps.setString(3, hash);
            ps.setString(4, entity.getRole());
            ps.setInt(5, entity.getMoney());
            ps.setString(6, entity.getUserImage());
            ps.setLong(7, entity.getCitizenID());
            ps.setDate(8, entity.getDob());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<UserDTO> readAll() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT Username, Fullname, Role, Money, UserImage, CitizenID, DOB FROM tblUsers";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                UserDTO user = new UserDTO(
                        rs.getString("Username"),
                        rs.getString("Fullname"),
                        "",
                        rs.getString("Role"),
                        rs.getInt("Money"),
                        rs.getString("UserImage"),
                        rs.getLong("CitizenID"),
                        rs.getDate("DOB")
                );
                list.add(user);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }

    @Override
    public UserDTO readById(String id) {
        String sql = "SELECT Username, Fullname, Role, Money, UserImage, CitizenID, DOB FROM tblUsers WHERE Username = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserDTO(
                            rs.getString("Username"),
                            rs.getString("Fullname"),
                            "",
                            rs.getString("Role"),
                            rs.getInt("Money"),
                            rs.getString("UserImage"),
                            rs.getLong("CitizenID"),
                            rs.getDate("DOB")
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean update(UserDTO entity) {
        String sql = "UPDATE tblUsers SET Fullname = ?, Password = ?, Role = ?, Money = ?, UserImage = ?, CitizenID = ?, DOB = ? WHERE Username = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            String hash = Hash.toSHA256(entity.getPassword());
            ps.setString(1, entity.getFullName());
            ps.setString(2, hash);
            ps.setString(3, entity.getRole());
            ps.setInt(4, entity.getMoney());
            ps.setString(5, entity.getUserImage());
            ps.setLong(6, entity.getCitizenID());
            ps.setDate(7, entity.getDob());
            ps.setString(8, entity.getUserName());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM tblUsers WHERE Username = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<UserDTO> search(String searchTerm) {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT Username, Fullname, Role, Money, UserImage, CitizenID, DOB FROM tblUsers WHERE Fullname LIKE ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserDTO user = new UserDTO(
                            rs.getString("Username"),
                            rs.getString("Fullname"),
                            "",
                            rs.getString("Role"),
                            rs.getInt("Money"),
                            rs.getString("UserImage"),
                            rs.getLong("CitizenID"),
                            rs.getDate("DOB")
                    );
                    list.add(user);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }

    public UserDTO checkLogin(String username, String password) {
        String sql = "SELECT Username, Fullname, Role, Money, UserImage, CitizenID, DOB FROM tblUsers WHERE Username = ? AND Password = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, Hash.toSHA256(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserDTO(
                            rs.getString("Username"),
                            rs.getString("Fullname"),
                            "",
                            rs.getString("Role"),
                            rs.getInt("Money"),
                            rs.getString("UserImage"),
                            rs.getLong("CitizenID"),
                            rs.getDate("DOB")
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}