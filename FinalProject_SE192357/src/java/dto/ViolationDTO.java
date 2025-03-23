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
public class ViolationDTO {

    private String violationID;
    private String plate;
    private String violation;
    private String VImage;
    private String status;
    private Date vDate;
    private int fine;

    public ViolationDTO(String violationID, String plate, String violation, String VImage, String status, Date vDate, int fine) {
        this.violationID = violationID;
        this.plate = plate;
        this.violation = violation;
        this.VImage = VImage;
        this.status = status;
        this.vDate = vDate;
        this.fine = fine;
    }


    public String getViolationID() {
        return violationID;
    }

    public void setViolationID(String violationID) {
        this.violationID = violationID;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getVImage() {
        return VImage;
    }

    public void setVImage(String VImage) {
        this.VImage = VImage;
    }
    

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getvDate() {
        return vDate;
    }

    public void setvDate(Date vDate) {
        this.vDate = vDate;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

}
