/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * 
 */
public class CategoryDetail {
    private int categoryDetailID;
    private String atrributeName;
    private int categoryDetailsGroupID;
    private boolean isActive;

    public CategoryDetail() {
    }

    public CategoryDetail(int categoryDetailID, String atrributeName, int categoryDetailsGroupID, boolean isActive) {
        this.categoryDetailID = categoryDetailID;
        this.atrributeName = atrributeName;
        this.categoryDetailsGroupID = categoryDetailsGroupID;
        this.isActive = isActive;
    }

    public int getCategoryDetailID() {
        return categoryDetailID;
    }

    public void setCategoryDetailID(int categoryDetailID) {
        this.categoryDetailID = categoryDetailID;
    }

    public String getAtrributeName() {
        return atrributeName;
    }

    public void setAtrributeName(String atrributeName) {
        this.atrributeName = atrributeName;
    }

    public int getCategoryDetailsGroupID() {
        return categoryDetailsGroupID;
    }

    public void setCategoryDetailsGroupID(int categoryDetailsGroupID) {
        this.categoryDetailsGroupID = categoryDetailsGroupID;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

   

  
    
}
