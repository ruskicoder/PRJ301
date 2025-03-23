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
public class UserDTO {
    private String userName;
    private String fullName;
    private String password;
    private String role;
    private int money;
    private String userImage;
    private long citizenID;
    private Date dob;

    public UserDTO(String userName, String fullName, String password, String role, int money, String userImage, long citizenID, Date dob) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.money = money;
        this.userImage = userImage;
        this.citizenID = citizenID;
        this.dob = dob;
    }
    
    public UserDTO(String userName, String fullName, String password, String role,  long citizenID, Date dob) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.citizenID = citizenID;
        this.dob = dob;
        this.money = 0;
        this.userImage = null;
    }
    
    public UserDTO(String userName, String fullName, String password, String role) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.userImage = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public long getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(long citizenID) {
        this.citizenID = citizenID;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}