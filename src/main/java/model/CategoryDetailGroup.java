/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 *
 */
public class CategoryDetailGroup {
    private int categoryDetailsGroupID;
    private String nameCategoryDetailsGroup;
    private int categoryID;
    private boolean isActive;

    public CategoryDetailGroup(int categoryDetailsGroupID, String nameCategoryDetailsGroup, int categoryID, boolean isActive) {
        this.categoryDetailsGroupID = categoryDetailsGroupID;
        this.nameCategoryDetailsGroup = nameCategoryDetailsGroup;
        this.categoryID = categoryID;
        this.isActive = isActive;
    }

   
    public CategoryDetailGroup() {
    }

    public int getCategoryDetailsGroupID() {
        return categoryDetailsGroupID;
    }

    public void setCategoryDetailsGroupID(int categoryDetailsGroupID) {
        this.categoryDetailsGroupID = categoryDetailsGroupID;
    }

    public String getNameCategoryDetailsGroup() {
        return nameCategoryDetailsGroup;
    }

    public void setNameCategoryDetailsGroup(String nameCategoryDetailsGroup) {
        this.nameCategoryDetailsGroup = nameCategoryDetailsGroup;
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
