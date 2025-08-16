package dao;

import model.Staff;
import java.sql.*;
import utils.DBContext;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO extends DBContext {

    public StaffDAO() {
        super();
    }

    // Lấy tất cả Staff
    public List<Staff> getAllStaff() {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffID(rs.getInt("StaffID"));
                staff.setEmail(rs.getString("Email"));
                staff.setPasswordHash(rs.getString("PasswordHash"));
                staff.setFullName(rs.getString("FullName"));
                staff.setPhoneNumber(rs.getString("PhoneNumber"));
                staff.setBirthDate(rs.getDate("BirthDate"));
                staff.setGender(rs.getString("Gender"));
                staff.setRole(rs.getString("Role")); // Admin/Staff
                staff.setActive(rs.getBoolean("IsActive"));
                staff.setHiredDate(rs.getDate("HiredDate"));
                staff.setCreatedAt(rs.getDate("CreatedAt"));
                list.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy Staff theo ID
    public Staff getStaffById(int staffId) {
        String sql = "SELECT * FROM Staff WHERE StaffID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Staff staff = new Staff();
                    staff.setStaffID(rs.getInt("StaffID"));
                    staff.setEmail(rs.getString("Email"));
                    staff.setPasswordHash(rs.getString("PasswordHash"));
                    staff.setFullName(rs.getString("FullName"));
                    staff.setPhoneNumber(rs.getString("PhoneNumber"));
                    staff.setBirthDate(rs.getDate("BirthDate"));
                    staff.setGender(rs.getString("Gender"));
                    staff.setRole(rs.getString("Role"));
                    staff.setActive(rs.getBoolean("IsActive"));
                    staff.setHiredDate(rs.getDate("HiredDate"));
                    staff.setCreatedAt(rs.getDate("CreatedAt"));
                    return staff;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm mới Staff
    public boolean createStaff(Staff staff) {
        String sql = "INSERT INTO Staff (Email, PasswordHash, FullName, PhoneNumber, BirthDate, Gender, Role, IsActive, HiredDate, CreatedAt) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getEmail());
            ps.setString(2, staff.getPasswordHash());
            ps.setString(3, staff.getFullName());
            ps.setString(4, staff.getPhoneNumber());
            ps.setDate(5, new java.sql.Date(staff.getBirthDate().getTime()));
            ps.setString(6, staff.getGender());
            ps.setString(7, staff.getRole());
            ps.setBoolean(8, staff.isActive());
            ps.setDate(9, new java.sql.Date(staff.getHiredDate().getTime()));
            ps.setDate(10, new java.sql.Date(staff.getCreatedAt().getTime()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật Staff
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE Staff "
                   + "SET Email = ?, PasswordHash = ?, FullName = ?, PhoneNumber = ?, BirthDate = ?, Gender = ?, Role = ?, IsActive = ?, HiredDate = ?, CreatedAt = ? "
                   + "WHERE StaffID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, staff.getEmail());
            ps.setString(2, staff.getPasswordHash());
            ps.setString(3, staff.getFullName());
            ps.setString(4, staff.getPhoneNumber());
            ps.setDate(5, new java.sql.Date(staff.getBirthDate().getTime()));
            ps.setString(6, staff.getGender());
            ps.setString(7, staff.getRole());
            ps.setBoolean(8, staff.isActive());
            ps.setDate(9, new java.sql.Date(staff.getHiredDate().getTime()));
            ps.setDate(10, new java.sql.Date(staff.getCreatedAt().getTime()));
            ps.setInt(11, staff.getStaffID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa Staff
    public boolean deleteStaff(int staffId) {
        String sql = "DELETE FROM Staff WHERE StaffID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm kiếm Staff theo tên hoặc email
    public List<Staff> searchStaff(String keyword) {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM Staff WHERE LOWER(FullName) LIKE ? OR LOWER(Email) LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Staff staff = new Staff();
                    staff.setStaffID(rs.getInt("StaffID"));
                    staff.setEmail(rs.getString("Email"));
                    staff.setPasswordHash(rs.getString("PasswordHash"));
                    staff.setFullName(rs.getString("FullName"));
                    staff.setPhoneNumber(rs.getString("PhoneNumber"));
                    staff.setBirthDate(rs.getDate("BirthDate"));
                    staff.setGender(rs.getString("Gender"));
                    staff.setRole(rs.getString("Role"));
                    staff.setActive(rs.getBoolean("IsActive"));
                    staff.setHiredDate(rs.getDate("HiredDate"));
                    staff.setCreatedAt(rs.getDate("CreatedAt"));
                    list.add(staff);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
