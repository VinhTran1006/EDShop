/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.Date;
import model.Customer;
import utils.DBContext;

/**
 *
 * @author pc
 */
public class ProfileDAO extends DBContext {

    public ProfileDAO() {
        super();
    }

    public Customer getCustomerByID(int customerID) {
        Customer cus = null;
        String sql = "SELECT c.CustomerID, a.Email, a.PasswordHash, c.FullName, c.PhoneNumber, "
                   + "c.BirthDate, c.Gender, a.IsActive, a.EmailVerified, a.CreatedAt "
                   + "FROM Accounts a JOIN Customers c ON c.AccountID = a.AccountID "
                   + "WHERE a.AccountID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cus = mapResultSetToCustomer(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cus;
    }

    public Customer getCustomerByCustomerID(int customerID) {
        Customer cus = null;
        String sql = "SELECT c.CustomerID, a.Email, a.PasswordHash, c.FullName, c.PhoneNumber, "
                   + "c.BirthDate, c.Gender, a.IsActive, a.EmailVerified, a.CreatedAt "
                   + "FROM Accounts a JOIN Customers c ON c.AccountID = a.AccountID "
                   + "WHERE c.CustomerID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cus = mapResultSetToCustomer(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cus;
    }

    public int getAccountIDByCustomerID(int customerID) {
        String sql = "SELECT AccountID FROM Customers WHERE CustomerID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("AccountID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // không tìm thấy
    }

    public boolean updateProfileCustomer(int customerID, String fullName, String phone, Date birthDate, String gender) {
        String sql = "UPDATE Customers SET FullName = ?, PhoneNumber = ?, BirthDate = ?, Gender = ? WHERE CustomerID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setDate(3, new java.sql.Date(birthDate.getTime()));
            ps.setString(4, gender);
            ps.setInt(5, customerID);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------- Helper Method -----------------
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        int id = rs.getInt("CustomerID");
        String email = rs.getString("Email");
        String passwordHash = rs.getString("PasswordHash");
        String fullName = rs.getString("FullName");
        String phone = rs.getString("PhoneNumber");
        Date birthDate = rs.getDate("BirthDate");
        String gender = rs.getString("Gender");
        boolean Active = rs.getBoolean("IsActive");
        boolean emailVerified = rs.getBoolean("EmailVerified");
        Date createdAt = rs.getTimestamp("CreatedAt");

        Customer cus = new Customer();
        cus.setCustomerID(id);
        cus.setEmail(email);
        cus.setPasswordHash(passwordHash);
        cus.setFullName(fullName);
        cus.setPhoneNumber(phone);
        cus.setBirthDate(birthDate);
        cus.setGender(gender);
        cus.setActive(Active);
        cus.setEmailVerified(emailVerified);
        cus.setCreatedAt(createdAt);

        return cus;
    }
}

