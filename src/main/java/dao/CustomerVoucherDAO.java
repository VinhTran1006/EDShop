package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBContext;

public class CustomerVoucherDAO extends DBContext {

    public boolean createCustomerVoucher(int customerId, int voucherId) {
        String sql = "INSERT INTO CustomerVouchers (customerID, voucherID) VALUES (?, ?)";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, voucherId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra khách hàng đã sử dụng voucher này chưa
    public boolean hasCustomerUsedVoucher(int customerId, int voucherId) {
        String sql = "SELECT COUNT(*) FROM CustomerVouchers WHERE customerID = ? AND voucherID = ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setInt(2, voucherId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
