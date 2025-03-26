/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author PC
 */

import dto.CarOwnerDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;
import java.sql.Date;

public class CarOwnerDAO implements IDAO<CarOwnerDTO, String> {

     @Override
    public boolean create(CarOwnerDTO owner) {
        String sql = "INSERT INTO tblCarOwner (Plate, CarID, PlateType, Owner, RegDate, FirstRegDate, MfdDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, owner.getPlate());
            pstmt.setString(2, owner.getCarID());
            pstmt.setString(3, owner.getPlateType());
            pstmt.setString(4, owner.getOwner());
            pstmt.setDate(5, owner.getRegDate());
            pstmt.setDate(6, owner.getFirstRegDate());
            pstmt.setDate(7, owner.getMfdDate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace();
            return false;
        }
    }

    @Override
    public CarOwnerDTO readById(String plate) {
        String sql = "SELECT Plate, CarID, PlateType, Owner, RegDate, FirstRegDate, MfdDate FROM tblCarOwner WHERE Plate = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new CarOwnerDTO(
                            rs.getString("Plate"),
                            rs.getString("CarID"),
                            rs.getString("PlateType"),
                            rs.getString("Owner"),
                            rs.getDate("RegDate"),
                            rs.getDate("FirstRegDate"),
                            rs.getDate("MfdDate")
                    );
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CarOwnerDTO> readAll() {
        List<CarOwnerDTO> owners = new ArrayList<>();
        String sql = "SELECT Plate, CarID, PlateType, Owner, RegDate, FirstRegDate, MfdDate FROM tblCarOwner";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                owners.add(new CarOwnerDTO(
                        rs.getString("Plate"),
                        rs.getString("CarID"),
                        rs.getString("PlateType"),
                        rs.getString("Owner"),
                        rs.getDate("RegDate"),
                        rs.getDate("FirstRegDate"),
                        rs.getDate("MfdDate")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace();
            return null; // Or an empty list
        }
        return owners;
    }

    @Override
    public boolean update(CarOwnerDTO owner) {
        String sql = "UPDATE tblCarOwner SET CarID = ?,  PlateType = ?, Owner = ?, RegDate = ?, FirstRegDate = ?, MfdDate = ? WHERE Plate = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, owner.getCarID());
            pstmt.setString(2, owner.getPlateType());
            pstmt.setString(3, owner.getOwner());
            pstmt.setDate(4, owner.getRegDate());
            pstmt.setDate(5, owner.getFirstRegDate());
            pstmt.setDate(6, owner.getMfdDate());
            pstmt.setString(7, owner.getPlate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String plate) {
        String sql = "DELETE FROM tblCarOwner WHERE Plate = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CarOwnerDTO> search(String searchTerm) {
        List<CarOwnerDTO> result = new ArrayList<>();
        // Example: search by owner name
        String sql = "SELECT Plate, CarID, PlateType, Owner, RegDate, FirstRegDate, MfdDate FROM tblCarOwner WHERE Owner LIKE ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new CarOwnerDTO(
                            rs.getString("Plate"),
                            rs.getString("CarID"),
                            rs.getString("PlateType"),
                            rs.getString("Owner"),
                            rs.getDate("RegDate"),
                            rs.getDate("FirstRegDate"),
                            rs.getDate("MfdDate")
                    ));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
             e.printStackTrace();
            return null; // Or an empty list
        }
        return result;
    }
}
