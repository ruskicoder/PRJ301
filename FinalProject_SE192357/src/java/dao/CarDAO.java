package dao;

import dto.CarDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CarDAO implements IDAO<CarDTO, String> {

    @Override
    public boolean create(CarDTO car) {
        String sql = "INSERT INTO tblCars (CarID, CarName, CarImage, CarType) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getCarID());
            pstmt.setString(2, car.getCarName());
            pstmt.setString(3, car.getCarImage());
            pstmt.setString(4, car.getCarType());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Proper error handling/logging
            return false;
        }
    }
    @Override
    public CarDTO readById(String carId) {
        String sql = "SELECT CarID, CarName, CarImage, CarType FROM tblCars WHERE CarID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CarDTO(
                            rs.getString("CarID"),
                            rs.getString("CarName"),
                            rs.getString("CarImage"),
                            rs.getString("CarType")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Proper error handling/logging
        }
        return null;
    }
    @Override
    public List<CarDTO> readAll() {
        List<CarDTO> cars = new ArrayList<>();
        String sql = "SELECT CarID, CarName, CarImage, CarType FROM tblCars";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                cars.add(new CarDTO(
                        rs.getString("CarID"),
                        rs.getString("CarName"),
                        rs.getString("CarImage"),
                        rs.getString("CarType")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Proper error handling/logging
            return null; // Or an empty list, depending on your needs
        }
        return cars;
    }
    @Override
    public boolean update(CarDTO car) {
        String sql = "UPDATE tblCars SET CarName = ?, CarImage = ?, CarType = ? WHERE CarID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getCarName());
            pstmt.setString(2, car.getCarImage());
            pstmt.setString(3, car.getCarType());
            pstmt.setString(4, car.getCarID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Proper error handling/logging
             return false;
        }
    }

    @Override
    public boolean delete(String carId) {
        String sql = "DELETE FROM tblCars WHERE CarID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, carId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
           e.printStackTrace(); // Proper error handling/logging
            return false;
        }
    }
    @Override
    public List<CarDTO> search(String searchTerm) {
        List<CarDTO> result = new ArrayList<>();
        String sql = "SELECT CarID, CarName, CarImage, CarType FROM tblCars WHERE CarName LIKE ?"; // Example: search by name
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%"); // Use % for wildcard search
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new CarDTO(
                            rs.getString("CarID"),
                            rs.getString("CarName"),
                            rs.getString("CarImage"),
                            rs.getString("CarType")
                    ));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace(); // Proper error handling/logging
            return null; // Or an empty list
        }
        return result;
    }
}