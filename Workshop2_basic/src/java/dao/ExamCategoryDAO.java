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

import dto.ExamCategoryDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ExamCategoryDAO implements IDAO<ExamCategoryDTO, Integer> {

    @Override
    public boolean create(ExamCategoryDTO entity) {
        String sql = "INSERT INTO tblExamCategories (category_id, category_name, description) VALUES (?, ?, ?)";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getCategoryId());
            ps.setString(2, entity.getCategoryName());
            ps.setString(3, entity.getDescription());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
        return false;
    }

    @Override
    public List<ExamCategoryDTO> readAll() {
        List<ExamCategoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tblExamCategories";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ExamCategoryDTO category = new ExamCategoryDTO();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                list.add(category);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
        return list;
    }
    @Override
    public ExamCategoryDTO readById(Integer id) {
          String sql = "SELECT * FROM tblExamCategories WHERE category_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ExamCategoryDTO category = new ExamCategoryDTO();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                return category;
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public boolean update(ExamCategoryDTO entity) {
        String sql = "UPDATE tblExamCategories SET category_name = ?, description = ? WHERE category_id = ?";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getCategoryName());
            ps.setString(2, entity.getDescription());
            ps.setInt(3, entity.getCategoryId());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.toString());
        }
        return false;
    }

    @Override
    public List<ExamCategoryDTO> search(String searchTerm) {
        List<ExamCategoryDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM tblExamCategories WHERE category_name LIKE ? OR description LIKE ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ExamCategoryDTO category = new ExamCategoryDTO();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                result.add(category);
            }
        } catch (Exception e) {
        }
        return result;
    }

}