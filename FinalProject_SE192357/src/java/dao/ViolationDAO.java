package dao;

import dto.ViolationDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;

public class ViolationDAO implements IDAO<ViolationDTO, String> {

    @Override
    public boolean create(ViolationDTO entity) {
        String sql = "INSERT INTO tblViolation (ViolationID, Plate, Violation, VImage, Status, VDate, Fine) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getViolationID());
            ps.setString(2, entity.getPlate());
            ps.setString(3, entity.getViolation());
            ps.setString(4, entity.getVImage());
            ps.setString(5, entity.getStatus());
            ps.setDate(6, entity.getvDate());
            ps.setInt(7, entity.getFine());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViolationDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<ViolationDTO> readAll() {
        List<ViolationDTO> list = new ArrayList<>();
        String sql = "SELECT ViolationID, Plate, Violation, VImage, Status, VDate, Fine FROM tblViolation";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ViolationDTO violation = new ViolationDTO(
                        rs.getString("ViolationID"),
                        rs.getString("Plate"),
                        rs.getString("Violation"),
                        rs.getString("VImage"),
                        rs.getString("Status"),
                        rs.getDate("VDate"),
                        rs.getInt("Fine")
                );
                list.add(violation);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViolationDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }
 @Override
    public ViolationDTO readById(String id) {
        String sql = "SELECT ViolationID, Plate, Violation, VImage, Status, VDate, Fine FROM tblViolation WHERE ViolationID = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ViolationDTO(
                            rs.getString("ViolationID"),
                            rs.getString("Plate"),
                            rs.getString("Violation"),
                            rs.getString("VImage"),
                            rs.getString("Status"),
                            rs.getDate("VDate"),
                            rs.getInt("Fine")
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViolationDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }


    @Override
    public boolean update(ViolationDTO entity) {
        String sql = "UPDATE tblViolation SET Plate = ?, Violation = ?, VImage = ?, Status = ?, VDate = ?, Fine = ? WHERE ViolationID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getPlate());
            ps.setString(2, entity.getViolation());
            ps.setString(3, entity.getVImage());
            ps.setString(4, entity.getStatus());
            ps.setDate(5, entity.getvDate());
            ps.setInt(6, entity.getFine());
            ps.setString(7, entity.getViolationID());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViolationDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM tblViolation WHERE ViolationID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViolationDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<ViolationDTO> search(String searchTerm) {
        List<ViolationDTO> list = new ArrayList<>();
        String sql = "SELECT ViolationID, Plate, Violation, VImage, Status, VDate, Fine FROM tblViolation WHERE Violation LIKE ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ViolationDTO violation = new ViolationDTO(
                            rs.getString("ViolationID"),
                            rs.getString("Plate"),
                            rs.getString("Violation"),
                            rs.getString("VImage"),
                            rs.getString("Status"),
                            rs.getDate("VDate"),
                            rs.getInt("Fine")
                    );
                    list.add(violation);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ViolationDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }
}