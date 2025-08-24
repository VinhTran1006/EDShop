/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 *
 */
public class ImportStockDetail {

    private int importStockDetailsID;
    private int importID;
    private int stock;
    private int stockLeft;
    private long unitPrice;
    private int productID;

    // Join
    private Product product; // set sau khi JOIN

    public ImportStockDetail() {
    }

    public ImportStockDetail(int importStockDetailsID, int importID, int stock, int stockLeft, long unitPrice, int productID) {
        this.importStockDetailsID = importStockDetailsID;
        this.importID = importID;
        this.stock = stock;
        this.stockLeft = stockLeft;
        this.unitPrice = unitPrice;
        this.productID = productID;

    }

    // getters & setters
    public int getImportStockDetailsID() {
        return importStockDetailsID;
    }

    public void setImportStockDetailsID(int importStockDetailsID) {
        this.importStockDetailsID = importStockDetailsID;
    }

    public int getImportID() {
        return importID;
    }

    public void setImportID(int importID) {
        this.importID = importID;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public int getStockLeft() {
        return stockLeft;
    }

    public void setStockLeft(int stockLeft) {
        this.stockLeft = stockLeft;
    }
}
