/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author
 */
public class ImportStock {

    private int importID;
    private int staffID;
    private int supplierID;
    private Timestamp importDate;
    private BigDecimal totalAmount;

    // Join
    private String fullName;    // staff name
    private Suppliers supplier; // supplier info
    private List<ImportStockDetail> importStockDetails;

    public ImportStock() {
    }

    public ImportStock( int staffID, int supplierID,  BigDecimal totalAmount) {
        
        this.staffID = staffID;
        this.supplierID = supplierID;
        this.importDate = new Timestamp(System.currentTimeMillis());
        this.totalAmount = totalAmount;
        
    }

    // getters & setters
    public int getImportID() {
        return importID;
    }

    public void setImportID(int importID) {
        this.importID = importID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public Timestamp getImportDate() {
        return importDate;
    }

    public void setImportDate(Timestamp importDate) {
        this.importDate = importDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Suppliers getSupplier() {
        return supplier;
    }

    public void setSupplier(Suppliers supplier) {
        this.supplier = supplier;
    }

    public List<ImportStockDetail> getImportStockDetails() {
        return importStockDetails;
    }

    public void setImportStockDetails(List<ImportStockDetail> importStockDetails) {
        this.importStockDetails = importStockDetails;
    }
}
