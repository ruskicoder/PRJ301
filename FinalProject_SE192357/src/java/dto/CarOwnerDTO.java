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

import java.sql.Date;

public class CarOwnerDTO {
    private String plate;
    private String carID;
    private String plateType;
    private String owner;
    private Date regDate;
    private Date firstRegDate;
    private Date mfdDate;

    public CarOwnerDTO(String plate, String carID, String plateType, String owner,
                        Date regDate, Date firstRegDate, Date mfdDate) {
        this.plate = plate;
        this.carID = carID;
        this.plateType = plateType;
        this.owner = owner;
        this.regDate = regDate;
        this.firstRegDate = firstRegDate;
        this.mfdDate = mfdDate;
    }

    // Getters
    public String getPlate() {
        return plate;
    }

    public String getCarID() {
        return carID;
    }
    public String getPlateType() {
        return plateType;
    }
    public String getOwner() {
        return owner;
    }
    public Date getRegDate() {
        return regDate;
    }

    public Date getFirstRegDate() {
        return firstRegDate;
    }
    public Date getMfdDate(){
        return mfdDate;
    }

    // Setters
    public void setPlate(String plate) {
        this.plate = plate;
    }
    public void setCarID(String carID) {
        this.carID = carID;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    public void setFirstRegDate(Date firstRegDate) {
        this.firstRegDate = firstRegDate;
    }
    public void setMfdDate(Date mfdDate){
        this.mfdDate = mfdDate;
    }
}