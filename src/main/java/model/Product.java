    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 */
public class Product {

    private int productID;
    private String productName;
    private String description;
    private Date AddedAt;
    private BigDecimal price;
    private int supplierID;
    private int categoryID;
    private int brandID;
    private int warrantyPeriod;
    private boolean isActive;
    private int quantity;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;

    private double discount = 0.0;
    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public Product() {
    }

    public Product(int productID, String productName, String description, Date AddedAt, BigDecimal price, int supplierID, int categoryID, int brandID, int warrantyPeriod, boolean isActive, int quantity, String imageUrl1, String imageUrl2, String imageUrl3, String imageUrl4) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.AddedAt = AddedAt;
        this.price = price;
        this.supplierID = supplierID;
        this.categoryID = categoryID;
        this.brandID = brandID;
        this.warrantyPeriod = warrantyPeriod;
        this.isActive = isActive;
        this.quantity = quantity;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.imageUrl3 = imageUrl3;
        this.imageUrl4 = imageUrl4;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAddedAt() {
        return AddedAt;
    }

    public void setAddedAt(Date AddedAt) {
        this.AddedAt = AddedAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    @Override
    public String toString() {
        return "Product{" + "productID=" + productID + ", productName=" + productName + ", description=" + description + ", AddedAt=" + AddedAt + ", price=" + price + ", supplierID=" + supplierID + ", categoryID=" + categoryID + ", brandID=" + brandID + ", warrantyPeriod=" + warrantyPeriod + ", isActive=" + isActive + ", quantity=" + quantity + ", imageUrl1=" + imageUrl1 + ", imageUrl2=" + imageUrl2 + ", imageUrl3=" + imageUrl3 + ", imageUrl4=" + imageUrl4 + '}';
    }

   
}
