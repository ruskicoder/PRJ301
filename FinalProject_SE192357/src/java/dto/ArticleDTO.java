/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author PC
 */
import java.sql.Date; // Import java.sql.Date

public class ArticleDTO {

    private String articleID;
    private String title;
    private String subtitle;
    private String author;
    private String content;
    private String thumbnail;
    private String articleType;
    private Date publishDate; // New field

    // Constructor with publishDate
    public ArticleDTO(String articleID, String title, String subtitle, String author, String content, String thumbnail, String articleType, Date publishDate) {
        this.articleID = articleID;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.content = content;
        this.thumbnail = thumbnail;
        this.articleType = articleType;
        this.publishDate = publishDate;
    }

    //Keep old constructor, added default publishDate
    public ArticleDTO(String articleID, String title, String subtitle, String author, String content, String thumbnail, String articleType) {
        this.articleID = articleID;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.content = content;
        this.thumbnail = thumbnail;
        this.articleType = articleType;
        //this.publishDate = new Date(System.currentTimeMillis()); //set default publish date //Remove this
    }

    // Getters and setters (including for publishDate)
    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public Date getPublishDate() { // Getter for publishDate
        return publishDate;
    }

    public void setPublishDate(Date publishDate) { // Setter for publishDate
        this.publishDate = publishDate;
    }
}
