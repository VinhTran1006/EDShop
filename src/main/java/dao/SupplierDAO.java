package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Suppliers;
import utils.DBContext;

public class SupplierDAO extends DBContext {

    // Lấy toàn bộ danh sách Suppliers
    public List<Suppliers> getAllSuppliers() {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT SupplierID, TaxID, Name, Email, PhoneNumber, Address, ContactPerson, Description, IsActive = 'true' FROM Suppliers";
        try ( PreparedStatement st = conn.prepareStatement(sql);  ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Suppliers s = new Suppliers(
                        rs.getInt("SupplierID"),
                        rs.getString("TaxID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("ContactPerson"),
                        rs.getString("Description"),
                        rs.getBoolean("IsActive")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy Supplier theo ID
    public Suppliers getSupplierById(int id) {
        String sql = "SELECT SupplierID, TaxID, Name, Email, PhoneNumber, Address, ContactPerson, Description, IsActive "
                + "FROM Suppliers WHERE SupplierID = ?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try ( ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Suppliers(
                            rs.getInt("SupplierID"),
                            rs.getString("TaxID"),
                            rs.getString("Name"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("ContactPerson"),
                            rs.getString("Description"),
                            rs.getBoolean("IsActive")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm Supplier
    public int insertSupplier(Suppliers s) {
        String sql = "INSERT INTO Suppliers (TaxID, Name, Email, PhoneNumber, Address, ContactPerson, Description, IsActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, s.getTaxID());
            st.setString(2, s.getName());
            st.setString(3, s.getEmail());
            st.setString(4, s.getPhoneNumber());
            st.setString(5, s.getAddress());
            st.setString(6, s.getContactPerson());
            st.setString(7, s.getDescription());
            st.setBoolean(8, s.getIsActive());
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Cập nhật Supplier
    public boolean updateSupplier(Suppliers s) {
        String sql = "UPDATE Suppliers SET TaxID=?, Name=?, Email=?, PhoneNumber=?, Address=?, ContactPerson=?, Description=?, IsActive=? "
                + "WHERE SupplierID=?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, s.getTaxID());
            st.setString(2, s.getName());
            st.setString(3, s.getEmail());
            st.setString(4, s.getPhoneNumber());
            st.setString(5, s.getAddress());
            st.setString(6, s.getContactPerson());
            st.setString(7, s.getDescription());
            st.setBoolean(8, s.getIsActive());
            st.setInt(9, s.getSupplierID());
            return st.executeUpdate() > 0; // true nếu update thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa Supplier
    public int deleteSupplier(int id) {
        String sql = "DELETE FROM Suppliers WHERE SupplierID=?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Kiểm tra TaxID hoặc Email trùng
    public boolean isSupplierExist(String taxId, String email) {
        String sql = "SELECT 1 FROM Suppliers WHERE TaxID = ? OR Email = ?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, taxId);
            st.setString(2, email);
            try ( ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra Name trùng
    public boolean isSupplierNameExist(String name) {
        String sql = "SELECT 1 FROM Suppliers WHERE Name = ?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, name);
            try ( ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSupplierByID(int supplierID) {
        String sql = "DELETE FROM Suppliers WHERE SupplierID = ?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, supplierID);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0; // true nếu xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Suppliers> findSuppliersByName(String name) {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers WHERE Name LIKE ?";
        try ( PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, "%" + name + "%"); // tìm gần đúng
            try ( ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Suppliers s = new Suppliers();
                    s.setSupplierID(rs.getInt("SupplierID"));
                    s.setTaxID(rs.getString("TaxID"));
                    s.setName(rs.getString("Name"));
                    s.setEmail(rs.getString("Email"));
                    s.setPhoneNumber(rs.getString("PhoneNumber"));
                    s.setAddress(rs.getString("Address"));
                    s.setContactPerson(rs.getString("ContactPerson"));
                    s.setDescription(rs.getString("Description"));
                    s.setIsActive(rs.getBoolean("IsActive"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // Lấy tất cả supplier đang active

    public List<Suppliers> getAllActiveSuppliers() {
        List<Suppliers> list = new ArrayList<>();
        String sql = "SELECT supplierID, taxID, name, email, phoneNumber, address, contactPerson, description, isActive "
                + "FROM Suppliers WHERE isActive = 1";

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
            e.printStackTrace(); // hoặc dùng logger
        }

        return list;
    }


}
