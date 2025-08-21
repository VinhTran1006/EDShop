package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;
import model.Order;

import utils.DBContext;

public class OrderDAO extends DBContext {

    public List<Order> getOrderList() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, c.FullName, c.PhoneNumber "
                + "FROM Orders o "
                + "JOIN Customers c ON o.CustomerID = c.CustomerID "
                + "ORDER BY CASE o.Status "
                + "    WHEN 'Waiting' THEN 1 "
                + "    WHEN 'Packing' THEN 2 "
                + "    WHEN 'Waiting for Delivery' THEN 3 "
                + "    WHEN 'Delivered' THEN 4 "
                + "    WHEN 'Cancelled' THEN 5 "
                + "    ELSE 6 END";

        try ( PreparedStatement pre = conn.prepareStatement(sql);  ResultSet rs = pre.executeQuery()) {

            while (rs.next()) {

                Customer cus = new Customer();
                cus.setFullName(rs.getString("FullName"));
                cus.setPhoneNumber(rs.getString("PhoneNumber"));

                Order o = new Order(
                        rs.getInt("OrderID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("StaffID"),
                        rs.getLong("TotalAmount"),
                        rs.getString("OrderedDate"),
                        rs.getString("DeliveredDate"),
                        rs.getString("Status"),
                        rs.getInt("Discount"),
                        rs.getString("AddressSnapshot"),
                        rs.getInt("AddressID"),
                        rs.getString("UpdatedAt")
                );
                o.setCustomer(cus);
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Order getOrderByID(String orderID) {
        Order o = null;
        String query = "SELECT o.*, c.FullName, c.PhoneNumber "
                + "FROM Orders o "
                + "JOIN Customers c ON o.CustomerID = c.CustomerID "
                + "WHERE o.OrderID = ?";
        try ( PreparedStatement pre = conn.prepareStatement(query)) {
            pre.setString(1, orderID);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                Customer cus = new Customer();
                cus.setFullName(rs.getString("FullName"));
                cus.setPhoneNumber(rs.getString("PhoneNumber"));
                o = new Order(
                        rs.getInt("OrderID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("StaffID"),
                        rs.getLong("TotalAmount"),
                        rs.getString("OrderedDate"),
                        rs.getString("DeliveredDate"),
                        rs.getString("Status"),
                        rs.getInt("Discount"),
                        rs.getString("AddressSnapshot"),
                        rs.getInt("AddressID"),
                        rs.getString("UpdatedAt")
                );
                o.setCustomer(cus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    public boolean updateStatus(int orderId, String newStatus) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try ( PreparedStatement pre = conn.prepareStatement(query)) {
            pre.setString(1, newStatus); // set chuỗi
            pre.setInt(2, orderId);
            return pre.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> searchOrders(String keyword) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.FullName, c.PhoneNumber "
                + "FROM Orders o "
                + "JOIN Customers c ON o.CustomerID = c.CustomerID "
                + "WHERE c.FullName LIKE ? OR c.PhoneNumber LIKE ? "
                + "ORDER BY o.OrderedDate DESC";

        try ( PreparedStatement pre = conn.prepareStatement(sql)) {
            String searchKey = "%" + keyword + "%";
            pre.setString(1, searchKey);
            pre.setString(2, searchKey);

            try ( ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    Customer cus = new Customer();
                    cus.setFullName(rs.getString("FullName"));
                    cus.setPhoneNumber(rs.getString("PhoneNumber"));

                    Order order = new Order();
                    order.setOrderID(rs.getInt("OrderID"));
                    order.setCustomerID(rs.getInt("CustomerID"));
                    order.setStaffID(rs.getInt("StaffID"));
                    order.setTotalAmount(rs.getLong("TotalAmount"));
                    order.setOrderDate(rs.getString("OrderedDate"));
                    order.setDeliveredDate(rs.getString("DeliveredDate"));
                    order.setStatus(rs.getString("Status"));
                    order.setDiscount(rs.getInt("Discount"));
                    order.setAddressSnapshot(rs.getString("AddressSnapshot"));
                    order.setAddressID(rs.getInt("AddressID"));
                    order.setUpdatedAt(rs.getString("UpdatedAt"));

                    // Thêm thông tin khách hàng
                    order.setCustomer(cus);

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrdersByCustomerID(int customerID) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, c.FullName, c.PhoneNumber "
                + "FROM Orders o "
                + "JOIN Customers c ON o.CustomerID = c.CustomerID "
                + "WHERE o.CustomerID = ? "
                + "ORDER BY o.OrderedDate DESC";

        try ( PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, customerID);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                Customer cus = new Customer();
                cus.setFullName(rs.getString("FullName"));
                cus.setPhoneNumber(rs.getString("PhoneNumber"));
                
                Order o = new Order(
                        rs.getInt("OrderID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("StaffID"),
                        rs.getLong("TotalAmount"),
                        rs.getString("OrderedDate"),
                        rs.getString("DeliveredDate"),
                        rs.getString("Status"),
                        rs.getInt("Discount"),
                        rs.getString("AddressSnapshot"),
                        rs.getInt("AddressID"),
                        rs.getString("UpdatedAt")
                );
                // set thêm thông tin khách hàng
                o.setCustomer(cus);
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int updateOrder(int orderID, String status) {
        int count = 0;
        String query = "UPDATE Orders SET Status = ?, UpdatedAt = DATEADD(HOUR, 7, GETUTCDATE()) WHERE OrderID = ?";
        String queryWithDeliveredDate = "UPDATE Orders SET Status = ?, DeliveredDate = DATEADD(HOUR, 7, GETUTCDATE()), "
                + "UpdatedAt = DATEADD(HOUR, 7, GETUTCDATE()) WHERE OrderID = ?";

        try ( PreparedStatement pre = conn.prepareStatement(
                "Delivered".equalsIgnoreCase(status) ? queryWithDeliveredDate : query)) {

            pre.setString(1, status);
            pre.setInt(2, orderID);

            count = pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
// Ai làm thanh toán Sửa nha

    public int createOrder(Order order) {
        String sql = "INSERT INTO Orders (CustomerID, StaffID, AddressSnapshot, OrderedDate, DeliveredDate, Status, TotalAmount, Discount, AddressID, UpdatedAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, DATEADD(HOUR, 7, GETUTCDATE()))";

        try ( PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setInt(1, order.getCustomerID());
            pre.setInt(2, order.getStaffID());
            pre.setString(3, order.getAddressSnapshot());

            // OrderedDate
            if (order.getOrderDate() != null && !order.getOrderDate().trim().isEmpty()) {
                try {
                    pre.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
                } catch (IllegalArgumentException e) {
                    Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE,
                            "Invalid OrderedDate format: {0}", order.getOrderDate());
                    return -1;
                }
            } else {
                pre.setNull(4, java.sql.Types.TIMESTAMP);
            }

            // DeliveredDate
            if (order.getDeliveredDate() != null && !order.getDeliveredDate().trim().isEmpty()) {
                try {
                    pre.setTimestamp(5, Timestamp.valueOf(order.getDeliveredDate()));
                } catch (IllegalArgumentException e) {
                    Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE,
                            "Invalid DeliveredDate format: {0}", order.getDeliveredDate());
                    return -1;
                }
            } else {
                pre.setNull(5, java.sql.Types.TIMESTAMP);
            }

            pre.setString(6, order.getStatus());
            pre.setLong(7, order.getTotalAmount());
            pre.setInt(8, order.getDiscount());
            pre.setInt(9, order.getAddressID());

            int affectedRows = pre.executeUpdate();

            if (affectedRows > 0) {
                try ( ResultSet rs = pre.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về OrderID vừa insert
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE,
                    "SQL Error creating order: Message={0}, SQLState={1}, ErrorCode={2}",
                    new Object[]{e.getMessage(), e.getSQLState(), e.getErrorCode()});
            return -1;
        }
    }

    ////-------------Tai------///
    public int countTodayOrders() {
        String sql = "SELECT COUNT(*) FROM Orders WHERE CAST(OrderedDate AS DATE) = CAST(GETDATE() AS DATE)";
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
