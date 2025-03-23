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

public class ExamDTO {
    private int examId;
    private String examTitle;
    private String subject;
    private int categoryId;
    private int totalMarks;
    private int duration;

    public ExamDTO() {
    }

    public ExamDTO(int examId, String examTitle, String subject, int categoryId, int totalMarks, int duration) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.subject = subject;
        this.categoryId = categoryId;
        this.totalMarks = totalMarks;
        this.duration = duration;
    }

    // Getters and setters
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
