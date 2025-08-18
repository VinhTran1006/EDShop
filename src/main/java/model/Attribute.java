/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * 
 */
public class Attribute {
    private int attributeID;
    private String atrributeName;
    private int categoryID;
    private boolean isActive;

    public Attribute() {
    }

    public Attribute(int attributeID, String atrributeName, int categoryID, boolean isActive) {
        this.attributeID = attributeID;
        this.atrributeName = atrributeName;
        this.categoryID = categoryID;
        this.isActive = isActive;
    }

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public String getAtrributeName() {
        return atrributeName;
    }

    public void setAtrributeName(String atrributeName) {
        this.atrributeName = atrributeName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    
    
}
