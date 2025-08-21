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
                if (rs.getTimestamp("ReplyDate") != null) {
                    fb.setReplyDate(new DateTime(rs.getTimestamp("ReplyDate")));
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

                // chú ý: ReplyDate trong DB có thể là DateTime SQL
                java.sql.Timestamp replyDate = rs.getTimestamp("ReplyDate");
                if (replyDate != null) {
                    fb.setReplyDate(new com.google.api.client.util.DateTime(replyDate));
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
                    fb.setReplyDate(new com.google.api.client.util.DateTime(replyDate));
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
