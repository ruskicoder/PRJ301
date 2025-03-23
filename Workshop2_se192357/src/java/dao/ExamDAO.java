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

import dto.ExamDTO;
import dto.QuestionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ExamDAO implements IDAO<ExamDTO, Integer> {

    @Override
    public boolean create(ExamDTO entity) {
        String sql = "INSERT INTO tblExams (exam_id, exam_title, Subject, category_id, total_marks, Duration) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getExamId());
            ps.setString(2, entity.getExamTitle());
            ps.setString(3, entity.getSubject());
            ps.setInt(4, entity.getCategoryId());
            ps.setInt(5, entity.getTotalMarks());
            ps.setInt(6, entity.getDuration());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    @Override
    public List<ExamDTO> readAll() {
        List<ExamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tblExams";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ExamDTO exam = new ExamDTO();
                exam.setExamId(rs.getInt("exam_id"));
                exam.setExamTitle(rs.getString("exam_title"));
                exam.setSubject(rs.getString("Subject"));
                exam.setCategoryId(rs.getInt("category_id"));
                exam.setTotalMarks(rs.getInt("total_marks"));
                exam.setDuration(rs.getInt("Duration"));
                list.add(exam);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

        @Override
    public ExamDTO readById(Integer id) {
        String sql = "SELECT * FROM tblExams WHERE exam_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ExamDTO exam = new ExamDTO();
                exam.setExamId(rs.getInt("exam_id"));
                exam.setExamTitle(rs.getString("exam_title"));
                exam.setSubject(rs.getString("Subject"));
                exam.setCategoryId(rs.getInt("category_id"));
                exam.setTotalMarks(rs.getInt("total_marks"));
                exam.setDuration(rs.getInt("Duration"));
                return exam;
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(ExamDTO entity) {
        String sql = "UPDATE tblExams SET exam_title = ?, Subject = ?, category_id = ?, total_marks = ?, Duration = ? WHERE exam_id = ?";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, entity.getExamTitle());
            ps.setString(2, entity.getSubject());
            ps.setInt(3, entity.getCategoryId());
            ps.setInt(4, entity.getTotalMarks());
            ps.setInt(5, entity.getDuration());
            ps.setInt(6, entity.getExamId());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }
        public boolean delete(int id) {
        String sql = "DELETE FROM tblExams WHERE exam_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ExamDTO> search(String searchTerm) {
        List<ExamDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM tblExams WHERE exam_title LIKE ? OR Subject LIKE ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ExamDTO exam = new ExamDTO();
                exam.setExamId(rs.getInt("exam_id"));
                exam.setExamTitle(rs.getString("exam_title"));
                exam.setSubject(rs.getString("Subject"));
                exam.setCategoryId(rs.getInt("category_id"));
                exam.setTotalMarks(rs.getInt("total_marks"));
                exam.setDuration(rs.getInt("Duration"));
                result.add(exam);
            }
        } catch (Exception e) {
              e.printStackTrace();
        }
        return result;
    }
        public List<ExamDTO> searchByCategoryId(int categoryId) {
        List<ExamDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM tblExams WHERE category_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ExamDTO exam = new ExamDTO();
                exam.setExamId(rs.getInt("exam_id"));
                exam.setExamTitle(rs.getString("exam_title"));
                exam.setSubject(rs.getString("Subject"));
                exam.setCategoryId(rs.getInt("category_id"));
                exam.setTotalMarks(rs.getInt("total_marks"));
                exam.setDuration(rs.getInt("Duration"));
                result.add(exam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
        
        public int getNextId() {
        String sql = "SELECT MAX(exam_id) as max_id FROM tblExams";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1; 
    }
    public List<ExamDTO> readAllWithQuestions() {
    List<ExamDTO> examsWithQuestions = new ArrayList<>();
    String sql = "SELECT e.* FROM tblExams e " +
                 "INNER JOIN tblQuestions q ON e.exam_id = q.exam_id " +
                 "GROUP BY e.exam_id, e.exam_title, e.Subject, e.category_id, e.total_marks, e.Duration " +
                 "HAVING COUNT(q.question_id) > 0"; // Only exams with questions

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            ExamDTO exam = new ExamDTO();
            exam.setExamId(rs.getInt("exam_id"));
            exam.setExamTitle(rs.getString("exam_title"));
            exam.setSubject(rs.getString("Subject"));
            exam.setCategoryId(rs.getInt("category_id"));
            exam.setTotalMarks(rs.getInt("total_marks"));
            exam.setDuration(rs.getInt("Duration"));
            examsWithQuestions.add(exam);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return examsWithQuestions;
}
    public List<ExamDTO> searchByCategoryIdWithQuestions(int categoryId) {
     List<ExamDTO> result = new ArrayList<>();
    String sql = "SELECT e.* FROM tblExams e " +
                 "INNER JOIN tblQuestions q ON e.exam_id = q.exam_id " +
                 "WHERE e.category_id = ? " +
                 "GROUP BY e.exam_id, e.exam_title, e.Subject, e.category_id, e.total_marks, e.Duration " +
                 "HAVING COUNT(q.question_id) > 0";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, categoryId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ExamDTO exam = new ExamDTO();
            exam.setExamId(rs.getInt("exam_id"));
            exam.setExamTitle(rs.getString("exam_title"));
            exam.setSubject(rs.getString("Subject"));
            exam.setCategoryId(rs.getInt("category_id"));
            exam.setTotalMarks(rs.getInt("total_marks"));
            exam.setDuration(rs.getInt("Duration"));
            result.add(exam);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
public List<ExamDTO> searchWithQuestions(String searchTerm) {
    List<ExamDTO> result = new ArrayList<>();
    String sql = "SELECT e.* FROM tblExams e " +
                 "INNER JOIN tblQuestions q ON e.exam_id = q.exam_id " +
                 "WHERE (e.exam_title LIKE ? OR e.Subject LIKE ?) " +
                 "GROUP BY e.exam_id, e.exam_title, e.Subject, e.category_id, e.total_marks, e.Duration " +
                 "HAVING COUNT(q.question_id) > 0";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + searchTerm + "%");
        ps.setString(2, "%" + searchTerm + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ExamDTO exam = new ExamDTO();
            exam.setExamId(rs.getInt("exam_id"));
            exam.setExamTitle(rs.getString("exam_title"));
            exam.setSubject(rs.getString("Subject"));
            exam.setCategoryId(rs.getInt("category_id"));
            exam.setTotalMarks(rs.getInt("total_marks"));
            exam.setDuration(rs.getInt("Duration"));
            result.add(exam);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}


}