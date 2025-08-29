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
                fb.setIsRead(rs.getBoolean("isVisible"));
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
                fb.setIsRead(rs.getBoolean("isVisible"));
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
                + "(CustomerID, ProductID, OrderID, Star, Comment, CreatedDate, IsActive, isVisible) "
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
        String sql = "UPDATE ProductFeedbacks SET Reply = ?, StaffID = ?, ReplyDate = GETDATE(), isVisible = 0 WHERE FeedbackID = ?";
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
                + "ORDER BY f.isVisible ASC";

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
                fb.setIsRead(rs.getBoolean("isVisible"));
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
                fb.setIsRead(rs.getBoolean("isVisible"));
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
        String sql = "INSERT INTO ProductFeedbacks (CustomerID, ProductID, OrderID, CreatedDate, Star, Comment, isActive, isVisible) "
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
                + "(CustomerID, ProductID, OrderID, CreatedDate, Star, Comment, IsActive, isVisible) "
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
        String sql = "UPDATE ProductFeedbacks SET Reply = ?, StaffID = ?, ReplyDate = GETDATE(), isVisible = 0 WHERE FeedbackID = ?";
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
        String query = "UPDATE ProductFeedbacks SET isVisible = 1  WHERE FeedbackID =?";
        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setInt(1, feedbackID);
            pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProductFeedback getReplyByFeedbackID(int feedbackID) {
    ProductFeedback reply = null;
    String sql = "SELECT StaffID, Reply, ReplyDate, isVisible FROM ProductFeedbacks WHERE FeedbackID = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, feedbackID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            reply = new ProductFeedback();
            reply.setStaffID(rs.getInt("StaffID"));
            reply.setReply(rs.getString("Reply"));
            reply.setReplyDate(rs.getTimestamp("ReplyDate"));
            reply.setIsRead(rs.getBoolean("isVisible"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return reply;
}


    public int UpdateReply(ProductFeedback feedback, String reply) {
        String query = "UPDATE ProductFeedbacks SET Reply= ?, ReplyDate = GETDATE() WHERE FeedbackID = ?";
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

    public List<ProductFeedback> getProductFeedbacksByProductId(int productId) {
        List<ProductFeedback> list = new ArrayList<>();

        // ✅ JOIN để lấy thêm FullName từ bảng Customers
        String sql = "SELECT P.*, C.FullName "
                + "FROM ProductFeedbacks P "
                + "JOIN Customers C ON P.CustomerID = C.CustomerID "
                + "WHERE P.ProductID = ? AND P.IsActive = 1 "
                + "ORDER BY P.CreatedDate DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductFeedback rating = new ProductFeedback();
                    rating.setFeedbackID(rs.getInt("FeedbackID"));
                    rating.setCustomerID(rs.getInt("CustomerID"));
                    rating.setProductID(rs.getInt("ProductID"));
                    rating.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    rating.setStar(rs.getInt("Star"));
                    rating.setComment(rs.getString("Comment"));
                    rating.setIsActive(rs.getBoolean("IsActive"));
                    rating.setIsRead(rs.getBoolean("isVisible"));
                    rating.setFullName(rs.getString("FullName"));
                    list.add(rating);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ProductFeedback> getAllRepliesByFeedbackID(int feedbackID) {
        List<ProductFeedback> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductFeedbacks WHERE FeedbackID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, feedbackID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductFeedback fb = new ProductFeedback();
                fb.setFeedbackID(rs.getInt("FeedbackID"));
                fb.setIsRead(rs.getBoolean("isVisible"));
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

    // Đếm số feedback chưa đọc
    public int countUnreadFeedback() {
            String sql = "SELECT COUNT(*) FROM ProductFeedbacks WHERE Reply is null AND IsActive != 0";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
        public static void main(String[] args) {

       int i;
        ProductFeedbackDAO dao = new ProductFeedbackDAO();
       i = dao.insertFeedback(1, 2, 2, 2, "ok");
            System.out.println(i);
    }
}
