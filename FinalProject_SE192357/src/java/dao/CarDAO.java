package dao;

import dto.CarDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;

public class CarDAO implements IDAO<CarDTO, String> {

    @Override
    public boolean create(CarDTO entity) {
        String sql = "INSERT INTO tblCars (Plate, CarName, CarImage, OwnerName, CarType, PlateType, RegDate, FirstRegDate, MfdDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getPlate());
            ps.setString(2, entity.getCarName());
            ps.setString(3, entity.getCarImage());
            ps.setString(4, entity.getOwnerName());
            ps.setString(5, entity.getCarType());
            ps.setString(6, entity.getPlateType());
            ps.setDate(7, entity.getRegDate());
            ps.setDate(8, entity.getFirstRegDate());
            ps.setDate(9, entity.getMfdDate());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<CarDTO> readAll() {
        List<CarDTO> list = new ArrayList<>();
        String sql = "SELECT Plate, CarName, CarImage, OwnerName, CarType, PlateType, RegDate, FirstRegDate, MfdDate FROM tblCars";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CarDTO car = new CarDTO(
                        rs.getString("Plate"),
                        rs.getString("CarName"),
                        rs.getString("CarImage"),
                        rs.getString("OwnerName"),
                        rs.getString("CarType"),
                        rs.getString("PlateType"),
                        rs.getDate("RegDate"),
                        rs.getDate("FirstRegDate"),
                        rs.getDate("MfdDate")
                );
                list.add(car);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }

    @Override
    public CarDTO readById(String id) {
        String sql = "SELECT Plate, CarName, CarImage, OwnerName, CarType, PlateType, RegDate, FirstRegDate, MfdDate FROM tblCars WHERE Plate = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CarDTO(
                            rs.getString("Plate"),
                            rs.getString("CarName"),
                            rs.getString("CarImage"),
                            rs.getString("OwnerName"),
                            rs.getString("CarType"),
                            rs.getString("PlateType"),
                            rs.getDate("RegDate"),
                            rs.getDate("FirstRegDate"),
                            rs.getDate("MfdDate")
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean update(CarDTO entity) {
        String sql = "UPDATE tblCars SET CarName = ?, CarImage = ?, OwnerName = ?, CarType = ?, PlateType = ?, RegDate = ?, FirstRegDate = ?, MfdDate = ? WHERE Plate = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCarName());
            ps.setString(2, entity.getCarImage());
            ps.setString(3, entity.getOwnerName());
            ps.setString(4, entity.getCarType());
            ps.setString(5, entity.getPlateType());
            ps.setDate(6, entity.getRegDate());
            ps.setDate(7, entity.getFirstRegDate());
            ps.setDate(8, entity.getMfdDate());
            ps.setString(9, entity.getPlate());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM tblCars WHERE Plate = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<CarDTO> search(String searchTerm) {
        List<CarDTO> list = new ArrayList<>();
        String sql = "SELECT Plate, CarName, CarImage, OwnerName, CarType, PlateType, RegDate, FirstRegDate, MfdDate FROM tblCars WHERE CarName LIKE ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarDTO car = new CarDTO(
                            rs.getString("Plate"),
                            rs.getString("CarName"),
                            rs.getString("CarImage"),
                            rs.getString("OwnerName"),
                            rs.getString("CarType"),
                            rs.getString("PlateType"),
                            rs.getDate("RegDate"),
                            rs.getDate("FirstRegDate"),
                            rs.getDate("MfdDate")
                    );
                    list.add(car);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CarDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }
}