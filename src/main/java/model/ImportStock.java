/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

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
    private long totalAmount;

    // Join
    private String fullName;    // staff name
    private Suppliers supplier; // supplier info
    private List<ImportStockDetail> importStockDetails;

    public ImportStock() {
    }

    public ImportStock(int importID, int staffID, int supplierID, Timestamp importDate, long totalAmount) {
        this.importID = importID;
        this.staffID = staffID;
        this.supplierID = supplierID;
        this.importDate = importDate;
        this.totalAmount = totalAmount;
        ;
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

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
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
