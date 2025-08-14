/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * 
 */
public class ProductDetail {
    private int productDetailID;
    private int productID;
    private int categoryDetailID;
    private String attributeValue;
    private Boolean isActive;

    public ProductDetail() {
    }

    public ProductDetail(int productDetailID, int productID, int categoryDetailID, String attributeValue, Boolean isActive) {
        this.productDetailID = productDetailID;
        this.productID = productID;
        this.categoryDetailID = categoryDetailID;
        this.attributeValue = attributeValue;
        this.isActive = isActive;
    }

    public int getProductDetailID() {
        return productDetailID;
    }

    public void setProductDetailID(int productDetailID) {
        this.productDetailID = productDetailID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryDetailID() {
        return categoryDetailID;
    }

    public void setCategoryDetailID(int categoryDetailID) {
        this.categoryDetailID = categoryDetailID;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


   
}
