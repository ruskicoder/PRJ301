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

public class CarDTO {
    private String plate;
    private String carName;
    private String carImage;
    private String ownerName;
    private String carType;
    private String plateType;
    private Date regDate;
    private Date firstRegDate;
    private Date mfdDate;

    public CarDTO(String plate, String carName, String carImage, String ownerName, String carType, String plateType, Date regDate, Date firstRegDate, Date mfdDate) {
        this.plate = plate;
        this.carName = carName;
        this.carImage = carImage;
        this.ownerName = ownerName;
        this.carType = carType;
        this.plateType = plateType;
        this.regDate = regDate;
        this.firstRegDate = firstRegDate;
        this.mfdDate = mfdDate;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getFirstRegDate() {
        return firstRegDate;
    }

    public void setFirstRegDate(Date firstRegDate) {
        this.firstRegDate = firstRegDate;
    }

    public Date getMfdDate() {
        return mfdDate;
    }

    public void setMfdDate(Date mfdDate) {
        this.mfdDate = mfdDate;
    }   
}
