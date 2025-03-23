/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.ProjectDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PC
 */
public class ProjectDAO implements IDAO<ProjectDTO, String> {

    @Override
    public boolean create(ProjectDTO entity) {
        String sql = "INSERT INTO tblStartupProjects VALUES (?,?,?,?,?)";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getProjectID());
            ps.setString(2, entity.getProjectName());
            ps.setString(3, entity.getDescription());
            ps.setString(4, entity.getStatus());
            ps.setDate(5, entity.getDate());

            int i = ps.executeUpdate();
            return i > 0;

        } catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }

        return false;
    }


    @Override
    public List<ProjectDTO> readAll() {
        List<ProjectDTO> projects = new ArrayList<>();
        String sql = "SELECT project_id, project_name, description, Status, estimated_launch FROM tblStartupProjects";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int projectId = rs.getInt("project_id");
                String projectName = rs.getString("project_name");
                String description = rs.getString("description");
                String status = rs.getString("Status");
                Date estimatedLaunch = rs.getDate("estimated_launch");

                ProjectDTO project = new ProjectDTO(projectId, projectName, description, status, estimatedLaunch);
                projects.add(project);
            }
        }catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return projects;
    }

    @Override
    public ProjectDTO readById(String id) {
        String sql = "SELECT project_id, project_name, description, Status, estimated_launch FROM tblStartupProjects WHERE project_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id); // Important: Set the parameter for the WHERE clause
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int projectId = rs.getInt("project_id");
                    String projectName = rs.getString("project_name");
                    String description = rs.getString("description");
                    String status = rs.getString("Status");
                    Date estimatedLaunch = rs.getDate("estimated_launch");
                    return new ProjectDTO(projectId, projectName, description, status, estimatedLaunch);
                }
            }
        }catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return null; // Return null if no project is found with the given ID
    }


    @Override
    public boolean update(ProjectDTO entity) {
        String sql = "UPDATE tblStartupProjects SET project_name = ?, description = ?, Status = ?, estimated_launch = ? WHERE project_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getProjectName());
            ps.setString(2, entity.getDescription());
            ps.setString(3, entity.getStatus());
            ps.setDate(4, entity.getDate());
            ps.setInt(5, entity.getProjectID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        }catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return false;
    }


    @Override
    public List<ProjectDTO> search(String searchTerm) {
        List<ProjectDTO> projects = new ArrayList<>();
       
        String sql = "SELECT project_id, project_name, description, Status, estimated_launch FROM tblStartupProjects WHERE project_name LIKE ? OR description LIKE ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");  // Search term in project_name
            ps.setString(2, "%" + searchTerm + "%");  // Search term in description

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int projectId = rs.getInt("project_id");
                    String projectName = rs.getString("project_name");
                    String description = rs.getString("description");
                    String status = rs.getString("Status");
                    Date estimatedLaunch = rs.getDate("estimated_launch");

                    ProjectDTO project = new ProjectDTO(projectId, projectName, description, status, estimatedLaunch);
                    projects.add(project);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return projects;
    }
    
     public boolean delete(int projectId) {
        String sql = "DELETE FROM tblStartupProjects WHERE project_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println(e.toString()); //  Better to log the exception
        }
        return false;
    }
}