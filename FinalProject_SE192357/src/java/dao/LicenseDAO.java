package dao;

import dto.LicenseDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;

public class LicenseDAO implements IDAO<LicenseDTO, String>{

    @Override
    public boolean create(LicenseDTO entity) {
        String sql = "INSERT INTO tblLicense (LicenseID, FullName, LicenseType, LRegDate, ExpDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getLicenseID());
            ps.setString(2, entity.getFullName());
            ps.setString(3, entity.getLicenseType());
            ps.setDate(4, entity.getlRegDate());
            ps.setDate(5, entity.getExpDate());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LicenseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<LicenseDTO> readAll() {
        List<LicenseDTO> list = new ArrayList<>();
        String sql = "SELECT LicenseID, FullName, LicenseType, LRegDate, ExpDate FROM tblLicense";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LicenseDTO license = new LicenseDTO(
                        rs.getString("LicenseID"),
                        rs.getString("FullName"),
                        rs.getString("LicenseType"),
                        rs.getDate("LRegDate"),
                        rs.getDate("ExpDate")
                );
                list.add(license);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LicenseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }

    @Override
    public LicenseDTO readById(String id) {
        String sql = "SELECT LicenseID, FullName, LicenseType, LRegDate, ExpDate FROM tblLicense WHERE LicenseID = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LicenseDTO(
                            rs.getString("LicenseID"),
                            rs.getString("FullName"),
                            rs.getString("LicenseType"),
                            rs.getDate("LRegDate"),
                            rs.getDate("ExpDate")
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LicenseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    @Override
    public boolean update(LicenseDTO entity) {
        String sql = "UPDATE tblLicense SET FullName = ?, LicenseType = ?, LRegDate = ?, ExpDate = ? WHERE LicenseID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getFullName());
            ps.setString(2, entity.getLicenseType());
            ps.setDate(3, entity.getlRegDate());
            ps.setDate(4, entity.getExpDate());
            ps.setString(5, entity.getLicenseID());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LicenseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM tblLicense WHERE LicenseID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LicenseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public List<LicenseDTO> search(String searchTerm) {
        List<LicenseDTO> list = new ArrayList<>();
        String sql = "SELECT LicenseID, FullName, LicenseType, LRegDate, ExpDate FROM tblLicense WHERE FullName LIKE ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");  // Use % for wildcard search
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LicenseDTO license = new LicenseDTO(
                        rs.getString("LicenseID"),
                        rs.getString("FullName"),
                        rs.getString("LicenseType"),
                        rs.getDate("LRegDate"),
                        rs.getDate("ExpDate")
                    );
                    list.add(license);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LicenseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }
}