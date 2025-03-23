/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author PC
 */
public class UserDAO implements IDAO<UserDTO, String> {

    @Override
    public boolean create(UserDTO entity) {
        String sql = "INSERT INTO tblUsers (Username, Name, Password, Role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getPassword()); 
            ps.setString(4, entity.getRole());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        }catch (Exception e) {
            System.out.println(e.toString()); 
        }
        return false;
    }

    @Override
    public List<UserDTO> readAll() {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT Username, Name, Password, Role FROM tblUsers"; 
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserDTO user = new UserDTO(rs.getString("Username"),
                        rs.getString("Name"),
                        rs.getString("Password"),  
                        rs.getString("Role"));
                list.add(user);
            }
        } catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return list;
    }

    @Override
    public UserDTO readById(String id) {
        String sql = "SELECT Username, Name, Password, Role FROM tblUsers WHERE Username = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserDTO user = new UserDTO(rs.getString("Username"),
                            rs.getString("Name"),
                            rs.getString("Password"), // Consider *not* selecting password
                            rs.getString("Role"));
                    return user;
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return null;
    }
 @Override
    public boolean update(UserDTO entity) {
        String sql = "UPDATE tblUsers SET Name = ?, Password = ?, Role = ? WHERE Username = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());  // You should hash this!
            ps.setString(3, entity.getRole());
            ps.setString(4, entity.getUsername());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        }catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return false;
    }

    @Override
    public List<UserDTO> search(String searchTerm) {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT Username, Name, Password, Role FROM tblUsers WHERE Username LIKE ? OR Name LIKE ?"; // Use LIKE for partial matching
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + searchTerm + "%"); // Add wildcards for partial match
            ps.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserDTO user = new UserDTO(rs.getString("Username"),
                            rs.getString("Name"),
                            rs.getString("Password"),  // Consider *not* selecting the password
                            rs.getString("Role"));
                    list.add(user);
                }
            }
        }catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return list;
    }
    
      public boolean delete(String username) {
        String sql = "DELETE FROM tblUsers WHERE Username = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return false;
    }
    
        public UserDTO login(String username, String password) {
        String sql = "SELECT Username, Name, Role FROM tblUsers WHERE Username = ? AND Password = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password); 

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    
                    return new UserDTO(rs.getString("Username"),
                            rs.getString("Name"),
                            rs.getString("Password"),
                            rs.getString("Role"));
                }
            }

        }catch (Exception e) {
            System.out.println(e.toString()); 
        }
        return null;
    }

}