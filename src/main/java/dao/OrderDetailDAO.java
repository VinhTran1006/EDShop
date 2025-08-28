/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import utils.DBContext;
import model.OrderDetail;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;

/**
 *
 * @author Vinh ne
 */
public class OrderDetailDAO extends DBContext {

    public List<OrderDetail> getAllByOrderId(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String query = "SELECT od.OrderID, od.ProductID, od.Quantity, od.Price, "
                + "FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID "
                + "WHERE od.OrderID = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setInt(1, orderId);
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderID(rs.getInt("OrderID"));
                detail.setProductID(rs.getInt("ProductID"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getLong("Price"));
                list.add(detail);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public OrderDetail getOrderDetailOfEachOrder(int orderID) {
        OrderDetail od = null;
        try {
            PreparedStatement pre = conn.prepareStatement("SELECT TOP 1 od.OrderID, od.ProductID, od.Quantity, od.Price, c.[CategoryName], p.ProductName FROM OrderDetails as od\n"
                    + "join Products as p on p.ProductID = od.ProductID\n"
                    + "join Categories c ON p.CategoryID = c.CategoryID\n"
                    + "WHERE OrderID = ?");
            pre.setInt(1, orderID);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                od = new OrderDetail(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));

            }
        } catch (SQLException e) {
        }
        return od;
    }

    public List<OrderDetail> getOrderDetail(String orderid) {
        List<OrderDetail> list = new ArrayList<>();
        String query = "SELECT od.OrderDetailsID, od.OrderID, od.ProductID, od.Quantity, od.Price, "
                + "p.CategoryID, p.ProductName, od.importDetailBatch " // <-- thêm cột batch
                + "FROM OrderDetails od "
                + "JOIN Products p ON p.ProductID = od.ProductID "
                + "WHERE od.OrderID = ?";

        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setString(1, orderid);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                OrderDetail od = new OrderDetail(
                        rs.getInt("OrderDetailsID"),
                        rs.getInt("OrderID"),
                        rs.getInt("ProductID"),
                        rs.getInt("Quantity"),
                        rs.getLong("Price")
                );
                od.setProductName(rs.getString("ProductName"));

                // ✅ Parse importDetailBatch thành List<int[]>
                String batchStr = rs.getString("importDetailBatch");
                od.setImportDetailBatch(parseBatchString(batchStr));

                list.add(od);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

// Hàm parse batch
    private List<int[]> parseBatchString(String batchStr) {
        List<int[]> list = new ArrayList<>();
        if (batchStr == null || batchStr.trim().isEmpty()) {
            return list;
        }

        batchStr = batchStr.trim();
        if (batchStr.startsWith("[") && batchStr.endsWith("]")) {
            batchStr = batchStr.substring(1, batchStr.length() - 1);
        }

        String[] pairs = batchStr.split(",");
        for (String p : pairs) {
            String[] parts = p.trim().split(":");
            if (parts.length == 2) {
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int qty = Integer.parseInt(parts[1].trim());
                    list.add(new int[]{id, qty});
                } catch (NumberFormatException e) {
                    System.err.println("Invalid batch format: " + p);
                }
            }
        }
        return list;
    }

    public boolean getCustomerByProductID(int customerId, int productId) {
        boolean isOk = false;
        String query = "SELECT CASE "
                + "WHEN NOT EXISTS (SELECT 1 FROM ProductRatings WHERE CustomerID = ? AND ProductID = ?) "
                + "AND EXISTS (SELECT 1 FROM OrderDetails od "
                + "JOIN Orders o ON od.OrderID = o.OrderID "
                + "WHERE o.CustomerID = ? AND od.ProductID = ? AND o.Status = 4) "
                + "THEN 1 ELSE 0 END AS CanReview;";

        try {
            PreparedStatement pre = conn.prepareStatement(query);
            pre.setInt(1, customerId);
            pre.setInt(2, productId);
            pre.setInt(3, customerId);
            pre.setInt(4, productId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                isOk = rs.getBoolean("CanReview");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isOk;
    }

    public static void main(String[] args) {
        OrderDetailDAO od = new OrderDetailDAO();
        //System.out.println(od.getOrderDetailOfEachOrder(2));
        for (OrderDetail order : od.getOrderDetail("3")) {
            System.out.println(order.getPrice());
        }
    }

    public List<OrderDetail> getOrderDetailsByOrderID(int orderID) {
        List<OrderDetail> list = new ArrayList<>();
        String sql
                = "SELECT od.OrderID, od.ProductID, od.Quantity, od.Price, "
                + "p.ProductName, c.CategoryName "
                + "FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID "
                + "JOIN Categories c ON p.CategoryID = c.CategoryID "
                + "WHERE od.OrderID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderID(rs.getInt("OrderID"));
                detail.setProductID(rs.getInt("ProductID"));
                detail.setProductName(rs.getString("ProductName"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getLong("Price"));

                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean createOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO OrderDetails (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, orderDetail.getOrderID());
            ps.setInt(2, orderDetail.getProductID());
            ps.setInt(3, orderDetail.getQuantity());
            ps.setLong(4, orderDetail.getPrice());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật importDetailBatch cho 1 OrderDetail
    public boolean updateImportDetailBatch(int orderDetailsID, String batchStr) {
        String sql = "UPDATE OrderDetails SET importDetailBatch = ? WHERE OrderDetailsID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, batchStr);
            ps.setInt(2, orderDetailsID);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
