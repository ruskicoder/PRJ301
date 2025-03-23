/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.sql.Date;

/**
 *
 * @author PC
 */
public class ProjectDTO {
    private int projectID;
    private String projectName;
    private String description;
    private String status;
    private Date date;

    public ProjectDTO(int projectID, String projectName, String description, String status, Date date) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" + "projectID=" + projectID + ", projectName=" + projectName + ", description=" + description + ", status=" + status + ", date=" + date + '}';
    }
    
    
}
