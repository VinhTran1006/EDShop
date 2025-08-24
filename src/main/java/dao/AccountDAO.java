package dao;

import model.Customer;
import model.Staff;
import java.security.MessageDigest;
import java.sql.*;
import utils.DBContext;

public class AccountDAO extends DBContext {

    public AccountDAO() {
        super();
    }

    // Hash MD5
    public String hashMD5(String pass) {
        try {
            MessageDigest mes = MessageDigest.getInstance("MD5");
            byte[] mesMD5 = mes.digest(pass.getBytes());
            StringBuilder str = new StringBuilder();
            for (byte b : mesMD5) {
                str.append(String.format("%02X", b));
            }
            return str.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ----------------------
    // LOGIN / VERIFY
    // ----------------------
    public Customer verifyCustomer(String email, String pass) {

        String sql = "SELECT * FROM Customers WHERE Email = ? AND PasswordHash = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hashMD5(pass));
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("Email"),
                            rs.getString("PasswordHash"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getDate("BirthDate"),
                            rs.getString("Gender"),
                            rs.getBoolean("IsActive"),
                            rs.getBoolean("EmailVerified"),
                            rs.getTimestamp("CreatedAt")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Staff verifyStaff(String email, String pass) {
        String sql = "SELECT * FROM Staffs WHERE Email = ? AND PasswordHash = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hashMD5(pass));
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Staff(
                            rs.getInt("StaffID"),
                            rs.getString("Email"),
                            rs.getString("PasswordHash"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getDate("BirthDate"),
                            rs.getString("Gender"),
                            rs.getString("Role"),
                            rs.getDate("HiredDate"),
                            rs.getBoolean("IsActive"),
                            rs.getTimestamp("CreatedAt") // nên dùng getTimestamp cho DATETIME
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ----------------------
    // CHECK EMAIL EXIST
    // ----------------------
    public boolean checkEmailExisted(String email) {
        String sqlCustomer = "SELECT 1 FROM Customers WHERE Email = ?";
        String sqlStaff = "SELECT 1 FROM Staff WHERE Email = ?";
        try ( PreparedStatement ps1 = conn.prepareStatement(sqlCustomer)) {
            ps1.setString(1, email);
            try ( ResultSet rs = ps1.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try ( PreparedStatement ps2 = conn.prepareStatement(sqlStaff)) {
            ps2.setString(1, email);
            try ( ResultSet rs = ps2.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------------
    // CHANGE PASSWORD
    // ----------------------
    public boolean changePassword(int customerId, String oldPassword, String newPassword) {
        String sqlCheck = "SELECT PasswordHash FROM Customers WHERE CustomerID = ?";
        String sqlUpdate = "UPDATE Customers SET PasswordHash = ? WHERE CustomerID = ?";

        try ( PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, customerId);
            try ( ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    String currentHash = rs.getString("PasswordHash");

                    // hash oldPassword để so sánh
                    String oldPasswordHash = hashMD5(oldPassword);

                    if (!currentHash.equalsIgnoreCase(oldPasswordHash)) {
                        return false; // sai mật khẩu cũ
                    }
                } else {
                    return false; // không tìm thấy customer
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // cập nhật mật khẩu mới
        try ( PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
            String newPasswordHash = hashMD5(newPassword);
            psUpdate.setString(1, newPasswordHash);
            psUpdate.setInt(2, customerId);

            return psUpdate.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean changeStaffPassword(int staffId, String oldPass, String newPass) {
        String sqlCheck = "SELECT PasswordHash FROM Staff WHERE StaffID = ?";
        String sqlUpdate = "UPDATE Staff SET PasswordHash = ? WHERE StaffID = ?";
        try ( PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, staffId);
            try ( ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    String currentHash = rs.getString("PasswordHash");
                    if (!currentHash.equals(hashMD5(oldPass))) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try ( PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
            psUpdate.setString(1, hashMD5(newPass));
            psUpdate.setInt(2, staffId);
            return psUpdate.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------------
    // ADD NEW CUSTOMER
    // ----------------------
    public boolean addNewCustomer(String email, String passwordHash, String fullName, String phone) {
        String sql = "INSERT INTO Customers (Email, PasswordHash, FullName, PhoneNumber, IsActive, EmailVerified, CreatedAt) "
                + "VALUES (?, ?, ?, ?, 1, 0, GETDATE())";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            ps.setString(3, fullName);
            ps.setString(4, phone);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------------
    // ADD GOOGLE ACCOUNT
    // ----------------------
    public boolean addNewAccountGoogle(String email, String fullName, String phone) {
        String randomPassword = generateRandomPassword(10);
        String passwordHash = hashMD5(randomPassword);
        return addNewCustomer(email, passwordHash, fullName, phone);
    }

    // ----------------------
    // RESET PASSWORD (ADMIN)
    // ----------------------
    public boolean adminResetCustomerPassword(int customerId, String newPass) {
        String sql = "UPDATE Customers SET PasswordHash = ? WHERE CustomerID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashMD5(newPass));
            ps.setInt(2, customerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean adminResetStaffPassword(int staffId, String newPass) {
        String sql = "UPDATE Staff SET PasswordHash = ? WHERE StaffID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashMD5(newPass));
            ps.setInt(2, staffId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ----------------------
    // GET ROLE
    // ----------------------
    public String getRoleByEmail(String email) {
        String sqlCustomer = "SELECT 'Customer' as Role FROM Customers WHERE Email = ?";
        String sqlStaff = "SELECT Role FROM Staff WHERE Email = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sqlCustomer)) {
            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Role");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try ( PreparedStatement ps = conn.prepareStatement(sqlStaff)) {
            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Role");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ----------------------
    // RANDOM PASSWORD
    // ----------------------
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
