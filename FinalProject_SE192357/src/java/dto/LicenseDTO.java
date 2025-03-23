package dto;

import java.sql.Date;

/**
 *
 * @author PC
 */

public class LicenseDTO {

    private String licenseID;
    private String fullName;
    private String licenseType;
    private Date lRegDate;
    private Date expDate;

    public LicenseDTO(String licenseID, String fullName, String licenseType, Date lRegDate, Date expDate) {
        this.licenseID = licenseID;
        this.fullName = fullName;
        this.licenseType = licenseType;
        this.lRegDate = lRegDate;
        this.expDate = expDate;
    }

    public String getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public Date getlRegDate() {
        return lRegDate;
    }

    public void setlRegDate(Date lRegDate) {
        this.lRegDate = lRegDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
}
