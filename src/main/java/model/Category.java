/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * 
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private String imgUrlLogo;
    private Boolean isActive;

    public Category() {
    }

    public Category(int categoryId, String categoryName, String imgUrlLogo, Boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.imgUrlLogo = imgUrlLogo;
        this.isActive = isActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImgUrlLogo() {
        return imgUrlLogo;
    }

    public void setImgUrlLogo(String imgUrlLogo) {
        this.imgUrlLogo = imgUrlLogo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Category{" + "categoryId=" + categoryId + ", categoryName=" + categoryName + ", imgUrlLogo=" + imgUrlLogo + ", isActive=" + isActive + '}';
    }

    
}
