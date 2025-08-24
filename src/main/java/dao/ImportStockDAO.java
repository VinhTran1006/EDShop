package dao;

import java.sql.*;
import model.ImportStock;
import model.ImportStockDetail;
import model.Product;
import model.Suppliers;
import java.util.ArrayList;
import java.util.List;
import utils.DBContext;

public class ImportStockDAO extends DBContext {

    // Lấy danh sách tất cả suppliers active
    public List<Suppliers> getAllActiveSuppliers() {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers WHERE isActive = 1 ORDER BY name";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Suppliers s = new Suppliers();
                s.setSupplierID(rs.getInt("supplierID"));
                s.setTaxID(rs.getString("taxID"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                s.setPhoneNumber(rs.getString("phoneNumber"));
                s.setAddress(rs.getString("address"));
                s.setContactPerson(rs.getString("contactPerson"));
                s.setDescription(rs.getString("description"));
                s.setIsActive(rs.getBoolean("isActive"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy lịch sử nhập kho theo filter
    public List<ImportStock> getImportHistoryFiltered(Timestamp from, Timestamp to, Integer supplierId) {
        List<ImportStock> list = new ArrayList<>();
        String sql = "SELECT i.importID, i.staffID, i.supplierID, i.importDate, i.totalAmount, "
                + "s.fullName, sup.supplierID, sup.name, sup.isActive "
                + "FROM ImportStocks i "
                + "JOIN Staffs s ON i.staffID = s.staffID "
                + "JOIN Suppliers sup ON i.supplierID = sup.supplierID "
                + "WHERE i.importDate BETWEEN ? AND ? "
                + (supplierId != null && supplierId != 0 ? "AND i.supplierID = ? " : "")
                + "ORDER BY i.importDate DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
            if (supplierId != null && supplierId != 0) {
                ps.setInt(3, supplierId);
            }

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportStock stock = new ImportStock();
                    stock.setImportID(rs.getInt("importID"));
                    stock.setStaffID(rs.getInt("staffID"));
                    stock.setSupplierID(rs.getInt("supplierID"));
                    stock.setImportDate(rs.getTimestamp("importDate"));
                    stock.setTotalAmount(rs.getBigDecimal("totalAmount"));
                    stock.setFullName(rs.getString("fullName"));

                    Suppliers supplier = new Suppliers();
                    supplier.setSupplierID(rs.getInt("supplierID"));
                    supplier.setName(rs.getString("name"));
                    supplier.setIsActive(rs.getBoolean("isActive"));

                    stock.setSupplier(supplier);
                    list.add(stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public ImportStock getImportStockDetailsByID(int importID) {
        ImportStock stock = null;
        try {
            // Lấy ImportStock
            String sql = "SELECT i.importID, i.staffID, i.supplierID, i.importDate, i.totalAmount, "
                    + "s.fullName, sup.supplierID, sup.taxID, sup.name, sup.email, sup.phoneNumber, "
                    + "sup.address, sup.contactPerson, sup.description, sup.isActive "
                    + "FROM ImportStocks i "
                    + "JOIN Staffs s ON i.staffID = s.staffID "
                    + "JOIN Suppliers sup ON i.supplierID = sup.supplierID "
                    + "WHERE i.importID = ?";
            try ( PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, importID);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stock = new ImportStock();
                        stock.setImportID(rs.getInt("importID"));
                        stock.setStaffID(rs.getInt("staffID"));
                        stock.setSupplierID(rs.getInt("supplierID"));
                        stock.setImportDate(rs.getTimestamp("importDate"));
                        stock.setTotalAmount(rs.getBigDecimal("totalAmount"));
                        stock.setFullName(rs.getString("fullName"));

                        Suppliers supplier = new Suppliers();
                        supplier.setSupplierID(rs.getInt("supplierID"));
                        supplier.setTaxID(rs.getString("taxID"));
                        supplier.setName(rs.getString("name"));
                        supplier.setEmail(rs.getString("email"));
                        supplier.setPhoneNumber(rs.getString("phoneNumber"));
                        supplier.setAddress(rs.getString("address"));
                        supplier.setContactPerson(rs.getString("contactPerson"));
                        supplier.setDescription(rs.getString("description"));
                        supplier.setIsActive(rs.getBoolean("isActive"));
                        stock.setSupplier(supplier);
                    }
                }
            }

            if (stock != null) {
                // Lấy chi tiết
                String detailSql = "SELECT d.ImportStockDetailsID, d.ImportID, d.ProductID, d.Stock, d.StockLeft, d.UnitPrice, "
                        + "p.ProductID, p.ProductName "
                        + "FROM ImportStockDetails d "
                        + "JOIN Products p ON d.ProductID = p.ProductID "
                        + "WHERE d.ImportID = ?";
                try ( PreparedStatement ps = conn.prepareStatement(detailSql)) {
                    ps.setInt(1, importID);
                    try ( ResultSet rs = ps.executeQuery()) {
                        List<ImportStockDetail> details = new ArrayList<>();
                        while (rs.next()) {
                            ImportStockDetail detail = new ImportStockDetail();
                            detail.setImportStockDetailsID(rs.getInt("ImportStockDetailsID"));
                            detail.setImportID(rs.getInt("ImportID"));
                            detail.setProductID(rs.getInt("ProductID"));
                            detail.setStock(rs.getInt("Stock"));
                            detail.setStockLeft(rs.getInt("StockLeft"));
                            detail.setUnitPrice(rs.getBigDecimal("UnitPrice"));

                            Product product = new Product();
                            product.setProductID(rs.getInt("ProductID"));
                            product.setProductName(rs.getString("ProductName"));
                            detail.setProduct(product);

                            details.add(detail);
                        }
                        stock.setImportStockDetails(details);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public int createImportStock(ImportStock stock) {
        String sql = "INSERT INTO ImportStocks(staffID, supplierID, importDate, totalAmount) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, stock.getStaffID());
            ps.setInt(2, stock.getSupplierID());
            ps.setTimestamp(3, stock.getImportDate());
            ps.setBigDecimal(4, stock.getTotalAmount());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // importID vừa tạo
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean importStock(int importID) {
        String sql = "UPDATE ImportStocks SET isImported = 1 WHERE importID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, importID);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
