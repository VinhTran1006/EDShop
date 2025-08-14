/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * 
 */

public class Address {

    private int addressId;
    private int customerId;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String addressDetails;
    private boolean isDefault;
    private boolean isActive;
    

    public Address() {
    }

    public Address(int addressId, int customerId, String provinceName, String districtName, String wardName, String addressDetails, boolean isDefault, boolean isActive) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.wardName = wardName;
        this.addressDetails = addressDetails;
        this.isDefault = isDefault;
        this.isActive = isActive;
    }

  

    // Getters & Setters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
}
