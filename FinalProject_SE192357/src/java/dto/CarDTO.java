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
    private String carID;
    private String carName;
    private String carImage;
    private String carType;

    public CarDTO(String carID, String carName, String carImage, String carType) {
        this.carID = carID;
        this.carName = carName;
        this.carImage = carImage;
        this.carType = carType;
    }

    // Getters
    public String getCarID() {
        return carID;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarImage() {
        return carImage;
    }

    public String getCarType() {
        return carType;
    }

    // Setters
    public void setCarID(String carID) {
        this.carID = carID;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}