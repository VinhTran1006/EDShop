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
    private long unitPrice;
    private int productID;

    public ImportStockDetail() {
    }

    public ImportStockDetail(int importStockDetailsID, int importID, int stock, long unitPrice, int productID) {
        this.importStockDetailsID = importStockDetailsID;
        this.importID = importID;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.productID = productID;
    }

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
  
}
