/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Customer;
import utils.DBContext;

/**
 *
 * @author pc
 */
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DBContext {

    public CustomerDAO() {
        super();
    }

    // Lấy danh sách tất cả khách hàng
    public List<Customer> getCustomerList() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT CustomerID, Email, PasswordHash, FullName, PhoneNumber, "
                   + "BirthDate, Gender, CreatedAt, IsActive, EmailVerified "
                   + "FROM Customers";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer cus = new Customer(
                    rs.getInt("CustomerID"),
                    rs.getString("Email"),
                    rs.getString("PasswordHash"),
                    rs.getString("FullName"),
                    rs.getString("PhoneNumber"),
                    rs.getDate("BirthDate"),
                    rs.getString("Gender"),
                    rs.getBoolean("IsActive"),
                    rs.getBoolean("EmailVerified"),
                    rs.getDate("CreatedAt")
                );
                list.add(cus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        Customer cus = null;
        String sql = "SELECT CustomerID, Email, PasswordHash, FullName, PhoneNumber, "
                   + "BirthDate, Gender, CreatedAt, IsActive, EmailVerified "
                   + "FROM Customers WHERE CustomerID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cus = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("FullName"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("BirthDate"),
                        rs.getString("Gender"),
                        rs.getBoolean("IsActive"),
                        rs.getBoolean("EmailVerified"),
                        rs.getDate("CreatedAt")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cus;
    }

    // Cập nhật trạng thái active/inactive của khách hàng
    public boolean updateStatus(int customerId) {
        String sqlUpdate = "UPDATE Customers "
                         + "SET IsActive = CASE WHEN IsActive = 1 THEN 0 ELSE 1 END "
                         + "WHERE CustomerID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
            ps.setInt(1, customerId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm kiếm khách hàng theo tên
    public List<Customer> searchCustomerByName(String keyword) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT CustomerID, Email, PasswordHash, FullName, PhoneNumber, "
                   + "BirthDate, Gender, CreatedAt, IsActive, EmailVerified "
                   + "FROM Customers WHERE LOWER(FullName) LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer cus = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("Email"),
                        rs.getString("PasswordHash"),
                        rs.getString("FullName"),
                        rs.getString("PhoneNumber"),
                        rs.getDate("BirthDate"),
                        rs.getString("Gender"),
                        rs.getBoolean("IsActive"),
                        rs.getBoolean("EmailVerified"),
                        rs.getDate("CreatedAt")
                    );
                    list.add(cus);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đếm tổng số khách hàng
    public int countTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM Customers WHERE IsActive != 0";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

