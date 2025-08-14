/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 *
 */
public class Customer {
    private int customerID;
    private String Email;
    private String Password;
    private String FullName;
    private String PhoneNumber;
    private boolean isActive;
    private String birthDay;
    private String gender;
    private Date createAt;

 
    public Customer() {
    }

    public Customer(int customerID, String Email, String Password, String FullName, String PhoneNumber, boolean isActive, String birthDay, String gender, Date createAt) {
        this.customerID = customerID;
        this.Email = Email;
        this.Password = Password;
        this.FullName = FullName;
        this.PhoneNumber = PhoneNumber;
        this.isActive = isActive;
        this.birthDay = birthDay;
        this.gender = gender;
        this.createAt = createAt;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    
}
