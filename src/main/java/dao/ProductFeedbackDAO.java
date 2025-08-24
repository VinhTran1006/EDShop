/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.google.api.client.util.DateTime;
import utils.DBContext;
import model.Product;
import model.ProductFeedback;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author VinhNTCE181630
 */
public class ProductFeedbackDAO extends DBContext {

    // Lấy feedback theo product
    public List<ProductFeedback> getFeedbacksByProduct(int productId) {
        List<ProductFeedback> list = new ArrayList<>();
        String sql = "SELECT f.*, c.FullName FROM ProductFeedbacks f "
                + "JOIN Customers c ON f.CustomerID = c.CustomerID "
                + "WHERE f.ProductID = ? ORDER BY f.CreatedDate DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductFeedback fb = new ProductFeedback();
                fb.setFeedbackID(rs.getInt("FeedbackID"));
                fb.setCustomerID(rs.getInt("CustomerID"));
                fb.setProductID(rs.getInt("ProductID"));
                fb.setOrderID(rs.getInt("OrderID"));
                fb.setCreatedDate(rs.getTimestamp("CreatedDate"));
                fb.setStar(rs.getInt("Star"));
                fb.setComment(rs.getString("Comment"));
                fb.setIsActive(rs.getBoolean("IsActive"));
                fb.setIsRead(rs.getBoolean("IsRead"));
                fb.setReply(rs.getString("Reply"));
                fb.setStaffID(rs.getInt("StaffID"));
                java.sql.Timestamp replyDate = rs.getTimestamp("ReplyDate");
                if (replyDate != null) {
                    fb.setReplyDate(new java.util.Date(replyDate.getTime()));
                }

                list.add(fb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ProductFeedback getProductFeedback(int feedbackID) {
        ProductFeedback fb = null;
        String sql = "SELECT f.*, c.FullName "
                + "FROM ProductFeedbacks f "
                + "JOIN Customers c ON f.CustomerID = c.CustomerID "
                + "WHERE f.FeedbackID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fb = new ProductFeedback();
                fb.setFeedbackID(rs.getInt("FeedbackID"));
                fb.setCustomerID(rs.getInt("CustomerID"));
                fb.setProductID(rs.getInt("ProductID"));
                fb.setOrderID(rs.getInt("OrderID"));
                fb.setCreatedDate(rs.getDate("CreatedDate"));
                fb.setStar(rs.getInt("Star"));
                fb.setComment(rs.getString("Comment"));
                fb.setIsActive(rs.getBoolean("IsActive"));
                fb.setIsRead(rs.getBoolean("IsRead"));
                fb.setReply(rs.getString("Reply"));
                fb.setStaffID(rs.getInt("StaffID"));

                java.sql.Timestamp replyDate = rs.getTimestamp("ReplyDate");
                if (replyDate != null) {
                    fb.setReplyDate(new java.util.Date(replyDate.getTime()));
                }

                fb.setFullName(rs.getString("FullName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fb;
    }

    // Thêm feedback
    public boolean addFeedback(ProductFeedback fb) {
        String sql = "INSERT INTO ProductFeedbacks "
                + "(CustomerID, ProductID, OrderID, Star, Comment, CreatedDate, IsActive, IsRead) "
                + "VALUES (?, ?, ?, ?, ?, GETDATE(), 1, 0)";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fb.getCustomerID());
            ps.setInt(2, fb.getProductID());
            ps.setInt(3, fb.getOrderID());
            ps.setInt(4, fb.getStar());
            ps.setString(5, fb.getComment());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Staff reply feedback
    public boolean replyFeedback(int feedbackID, int staffID, String reply) {
        String sql = "UPDATE ProductFeedbacks SET Reply = ?, StaffID = ?, ReplyDate = GETDATE(), IsRead = 0 WHERE FeedbackID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reply);
            ps.setInt(2, staffID);
            ps.setInt(3, feedbackID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductFeedback> getNewFeedback() {
        List<ProductFeedback> list = new ArrayList<>();
        String query = "SELECT f.*, c.FullName "
                + "FROM ProductFeedbacks f "
                + "JOIN Customers c ON f.CustomerID = c.CustomerID "
                + "ORDER BY f.IsRead ASC";

        try ( PreparedStatement ps = conn.prepareStatement(query);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductFeedback fb = new ProductFeedback();
                fb.setFeedbackID(rs.getInt("FeedbackID"));
                fb.setCustomerID(rs.getInt("CustomerID"));
                fb.setProductID(rs.getInt("ProductID"));
                fb.setOrderID(rs.getInt("OrderID"));
                fb.setCreatedDate(rs.getTimestamp("CreatedDate"));
                fb.setStar(rs.getInt("Star"));
                fb.setComment(rs.getString("Comment"));
                fb.setIsActive(rs.getBoolean("IsActive"));
                fb.setIsRead(rs.getBoolean("IsRead"));
                fb.setReply(rs.getString("Reply"));
                fb.setStaffID(rs.getInt("StaffID"));
                java.sql.Timestamp replyDate = rs.getTimestamp("ReplyDate");
                if (replyDate != null) {
                    fb.setReplyDate(new java.util.Date(replyDate.getTime()));
                }

                // có thể set thêm FullName nếu bạn mở rộng model
                fb.setFullName(rs.getString("FullName"));

                list.add(fb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ProductFeedback> getAllFeedbackRepliesByFeedbackID(int feedbackID) {
        List<ProductFeedback> list = new ArrayList<>();
        String sql = "select * from ProductFeedbacks where FeedbackID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductFeedback fb = new ProductFeedback();
                fb.setFeedbackID(rs.getInt("FeedbackID"));
                fb.setCustomerID(rs.getInt("CustomerID"));
                fb.setProductID(rs.getInt("ProductID"));
                fb.setOrderID(rs.getInt("OrderID"));
                fb.setCreatedDate(rs.getDate("CreatedDate"));
                fb.setStar(rs.getInt("Star"));
                fb.setComment(rs.getString("Comment"));
                fb.setIsActive(rs.getBoolean("IsActive"));
                fb.setIsRead(rs.getBoolean("IsRead"));
                fb.setReply(rs.getString("Reply"));
                fb.setStaffID(rs.getInt("StaffID"));

                java.sql.Timestamp replyDate = rs.getTimestamp("ReplyDate");
                if (replyDate != null) {
                    fb.setReplyDate(new java.util.Date(replyDate.getTime()));
                }

                list.add(fb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insertFeedback(int customerID, int productID, int orderID, int star, String comment) {
        int count = 0;
        String sql = "INSERT INTO ProductFeedbacks (CustomerID, ProductID, OrderID, CreatedDate, Star, Comment, isDeleted, isRead) "
                + "VALUES (?, ?, ?, GETDATE(), ?, ?, 0, 0)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setInt(2, productID);
            ps.setInt(3, orderID);
            ps.setInt(4, star);
            ps.setString(5, comment);
            count = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean hasRatedProduct(int customerID, int productID, int orderID) {
        String sql = "SELECT COUNT(*) FROM ProductFeedbacks WHERE CustomerID = ? AND ProductID = ? AND OrderID = ?";
        try (
                 PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ps.setInt(2, productID);
            ps.setInt(3, orderID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int addProductRating(int customerId, int productId, int orderId, int star, String comment) {
        int count = 0;
        String query = "INSERT INTO ProductFeedbacks "
                + "(CustomerID, ProductID, OrderID, CreatedDate, Star, Comment, IsActive, isRead) "
                + "VALUES (?, ?, ?, GETDATE(), ?, ?, 1, 0)";
        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setInt(1, customerId);
            pre.setInt(2, productId);
            pre.setInt(3, orderId);
            pre.setInt(4, star);
            pre.setString(5, comment);
            count = pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean updateStatusComment(int feedbackID, int isActive) {
        boolean isOk = false;
        String query = "Update ProductFeedbacks SET IsActive = ? WHERE FeedbackID =?";
        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setInt(1, isActive);
            pre.setInt(2, feedbackID);
            pre.executeUpdate();
            isOk = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }

    public boolean addReply(int staffId, int feedbackId, String reply) {
        String sql = "UPDATE ProductFeedbacks SET Reply = ?, StaffID = ?, ReplyDate = GETDATE(), IsRead = 0 WHERE FeedbackID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, reply);
            ps.setInt(2, staffId);
            ps.setInt(3, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateisReadComment(int feedbackID) {
        String query = "Update ProductRatings SET IsRead = 1  WHERE RateID =?";
        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setInt(1, feedbackID);
            pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProductFeedback getReplyByFeedbackID(int replyID) {
        List<ProductFeedback> list = new ArrayList<>();
        ProductFeedback r = null;
//        String query = "SELECT r.* FROM RatingReplies r JOIN ProductRatings pr ON r.rateID = pr.rateID WHERE pr.customerID = ? AND r.isRead = 0";
        String query = "SELECT rr.* FROM ProductFeedbacks rr WHERE rr.FeedbackID =?";
        try (
                 PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, replyID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                r = new ProductFeedback();
                r.setFeedbackID(rs.getInt("FeedbackID"));
                r.setStaffID(rs.getInt("StaffID"));
                r.setReply(rs.getString("Reply"));
                r.setIsRead(rs.getBoolean("IsRead"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
     public int UpdateReply(ProductFeedback feedback, String reply) {
        String query = "UPDATE ProductFeedbacks SET Reply= ? WHERE FeedbackID = ?";
        int result = 0;

        try {
            if (conn == null) {
                System.out.println("[ERROR] conn is null in UpdateReply!");
                return 0;
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, reply);
            stmt.setInt(2, feedback.getFeedbackID());

            result = stmt.executeUpdate();
            System.out.println("[DAO] Rows updated: " + result);

        } catch (Exception e) {
            System.out.println("[ERROR] UpdateReply failed: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
     
        public boolean DeleteReply(int feedbackID) {
        String query = "UPDATE ProductFeedbacks "
                 + "SET Reply = NULL, StaffID = NULL, ReplyDate = NULL "
                 + "WHERE FeedbackID = ?";

        try (
                 PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, feedbackID);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    // Đếm số feedback chưa đọc
    public int countUnreadFeedback() {
        String sql = "SELECT COUNT(*) FROM ProductFeedbacks WHERE IsRead = 0";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
