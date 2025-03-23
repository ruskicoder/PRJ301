package dao;

import dto.ArticleDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBUtils;
import java.sql.Date;

public class ArticleDAO implements IDAO<ArticleDTO, String> {

    @Override
    public boolean create(ArticleDTO entity) {
        String sql = "INSERT INTO tblArticles (ArticleID, Title, Subtitle, Author, Content, Thumbnail, ArticleType) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getArticleID());
            ps.setString(2, entity.getTitle());
            ps.setString(3, entity.getSubtitle());
            ps.setString(4, entity.getAuthor());
            if (entity.getContent() != null && entity.getContent().length() > 4000) {
                entity.setContent(entity.getContent().substring(0, 4000)); //Example
            }
            ps.setString(5, entity.getContent());
            ps.setString(6, entity.getThumbnail());
            ps.setString(7, entity.getArticleType());
            // No need to set publishDate, as it has a DEFAULT constraint in the database
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<ArticleDTO> readAll() {
        List<ArticleDTO> list = new ArrayList<>();
        String sql = "SELECT ArticleID, Title, Subtitle, Author, Content, Thumbnail, ArticleType, PublishDate FROM tblArticles";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ArticleDTO article = new ArticleDTO(
                        rs.getString("ArticleID"),
                        rs.getString("Title"),
                        rs.getString("Subtitle"),
                        rs.getString("Author"),
                        rs.getString("Content"),
                        rs.getString("Thumbnail"),
                        rs.getString("ArticleType"),
                        rs.getDate("PublishDate") // Retrieve publishDate
                );
                list.add(article);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }

    @Override
    public ArticleDTO readById(String id) {
        String sql = "SELECT ArticleID, Title, Subtitle, Author, Content, Thumbnail, ArticleType, PublishDate FROM tblArticles WHERE ArticleID = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ArticleDTO(
                            rs.getString("ArticleID"),
                            rs.getString("Title"),
                            rs.getString("Subtitle"),
                            rs.getString("Author"),
                            rs.getString("Content"),
                            rs.getString("Thumbnail"),
                            rs.getString("ArticleType"),
                            rs.getDate("PublishDate") // Retrieve publishDate
                    );
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean update(ArticleDTO entity) {
        String sql = "UPDATE tblArticles SET Title = ?, Subtitle = ?, Author = ?, Content = ?, Thumbnail = ?, ArticleType = ?, PublishDate = ? WHERE ArticleID = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getTitle());
            ps.setString(2, entity.getSubtitle());
            ps.setString(3, entity.getAuthor());
            if (entity.getContent() != null && entity.getContent().length() > 4000) {
                entity.setContent(entity.getContent().substring(0, 4000));
            }
            ps.setString(4, entity.getContent());
            ps.setString(5, entity.getThumbnail());
            ps.setString(6, entity.getArticleType());
            ps.setDate(7, entity.getPublishDate()); // Set publishDate on update
            ps.setString(8, entity.getArticleID());
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM tblArticles WHERE ArticleID = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int n = ps.executeUpdate();
            return n > 0;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public List<ArticleDTO> search(String searchTerm) {
        List<ArticleDTO> list = new ArrayList<>();
        String sql = "SELECT ArticleID, Title, Subtitle, Author, Content, Thumbnail, ArticleType, PublishDate FROM tblArticles WHERE Title LIKE ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArticleDTO article = new ArticleDTO(
                            rs.getString("ArticleID"),
                            rs.getString("Title"),
                            rs.getString("Subtitle"),
                            rs.getString("Author"),
                            rs.getString("Content"),
                            rs.getString("Thumbnail"),
                            rs.getString("ArticleType"),
                            rs.getDate("PublishDate") // Retrieve publishDate
                    );
                    list.add(article);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list;
    }
}
