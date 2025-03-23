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
import dto.QuestionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class QuestionDAO implements IDAO<QuestionDTO, Integer> {

    @Override
    public boolean create(QuestionDTO entity) {
        String sql = "INSERT INTO tblQuestions (question_id, exam_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getQuestionId());
            ps.setInt(2, entity.getExamId());
            ps.setString(3, entity.getQuestionText());
            ps.setString(4, entity.getOptionA());
            ps.setString(5, entity.getOptionB());
            ps.setString(6, entity.getOptionC());
            ps.setString(7, entity.getOptionD());
            ps.setString(8, entity.getCorrectOption());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    @Override
    public List<QuestionDTO> readAll() {
        List<QuestionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tblQuestions";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                QuestionDTO question = new QuestionDTO();
                question.setQuestionId(rs.getInt("question_id"));
                question.setExamId(rs.getInt("exam_id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c"));
                question.setOptionD(rs.getString("option_d"));
                question.setCorrectOption(rs.getString("correct_option"));
                list.add(question);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public List<QuestionDTO> readAllByExamId(int examId) {
        List<QuestionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tblQuestions WHERE exam_id = ?";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, examId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                QuestionDTO question = new QuestionDTO();
                question.setQuestionId(rs.getInt("question_id"));
                question.setExamId(rs.getInt("exam_id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c"));
                question.setOptionD(rs.getString("option_d"));
                question.setCorrectOption(rs.getString("correct_option"));
                list.add(question);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    @Override
    public QuestionDTO readById(Integer id) {
        String sql = "SELECT * FROM tblQuestions WHERE question_id = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                QuestionDTO question = new QuestionDTO();
                question.setQuestionId(rs.getInt("question_id"));
                question.setExamId(rs.getInt("exam_id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c"));
                question.setOptionD(rs.getString("option_d"));
                question.setCorrectOption(rs.getString("correct_option"));
                return question;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(QuestionDTO entity) {
        String sql = "UPDATE tblQuestions SET exam_id = ?, question_text = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ? WHERE question_id = ?";
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getExamId());
            ps.setString(2, entity.getQuestionText());
            ps.setString(3, entity.getOptionA());
            ps.setString(4, entity.getOptionB());
            ps.setString(5, entity.getOptionC());
            ps.setString(6, entity.getOptionD());
            ps.setString(7, entity.getCorrectOption());
            ps.setInt(8, entity.getQuestionId());
            int i = ps.executeUpdate();
            return i > 0;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM tblQuestions WHERE question_id = ?";
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
    public List<QuestionDTO> search(String searchTerm) {
        List<QuestionDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM tblQuestions WHERE question_text LIKE ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                QuestionDTO question = new QuestionDTO();
                question.setQuestionId(rs.getInt("question_id"));
                question.setExamId(rs.getInt("exam_id"));
                question.setQuestionText(rs.getString("question_text"));
                question.setOptionA(rs.getString("option_a"));
                question.setOptionB(rs.getString("option_b"));
                question.setOptionC(rs.getString("option_c"));
                question.setOptionD(rs.getString("option_d"));
                question.setCorrectOption(rs.getString("correct_option"));
                result.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getNextId() {
        String sql = "SELECT MAX(question_id) as max_id FROM tblQuestions";
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

}
