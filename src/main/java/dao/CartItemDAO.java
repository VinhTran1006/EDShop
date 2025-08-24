package dao;

import model.CartItem;
import model.Product;
import utils.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO extends DBContext {

    public CartItemDAO() {
        super();
    }

    // Lấy tất cả cart items theo customerID
    public List<CartItem> getCartItemsByCustomerId(int customerID) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.cartItemID, ci.productID, ci.quantity, ci.customerID, "
                + "p.productID, p.productName, p.description, p.AddedAt, p.price, "
                + "p.supplierID, p.categoryID, p.brandID, p.warrantyPeriod, "
                + "p.isActive, p.quantity as productQuantity, p.imageUrl1, p.imageUrl2, p.imageUrl3, p.imageUrl4 "
                + "FROM CartItems ci "
                + "INNER JOIN Products p ON ci.productID = p.productID "
                + "WHERE ci.customerID = ? AND p.isActive = 1";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemID(rs.getInt("cartItemID"));
                cartItem.setProductID(rs.getInt("productID"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setCustomerID(rs.getInt("customerID"));

                // Tạo Product object
                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setAddedAt(rs.getDate("AddedAt"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setSupplierID(rs.getInt("supplierID"));
                product.setCategoryID(rs.getInt("categoryID"));
                product.setBrandID(rs.getInt("brandID"));
                product.setWarrantyPeriod(rs.getInt("warrantyPeriod"));
                product.setIsActive(rs.getBoolean("isActive"));
                product.setQuantity(rs.getInt("productQuantity"));
                product.setImageUrl1(rs.getString("imageUrl1"));
                product.setImageUrl2(rs.getString("imageUrl2"));
                product.setImageUrl3(rs.getString("imageUrl3"));
                product.setImageUrl4(rs.getString("imageUrl4"));

                cartItem.setProduct(product);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    // Thêm sản phẩm vào giỏ hàng
    public boolean addToCart(int customerID, int productID, int quantity) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = getCartItem(customerID, productID);

        if (existingItem != null) {
            // Nếu đã có, cập nhật số lượng
            return updateCartItemQuantity(existingItem.getCartItemID(), existingItem.getQuantity() + quantity);
        } else {
            // Nếu chưa có, thêm mới
            String sql = "INSERT INTO CartItems (productID, quantity, customerID) VALUES (?, ?, ?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, productID);
                ps.setInt(2, quantity);
                ps.setInt(3, customerID);

                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    // Lấy cart item theo customerID và productID
    public CartItem getCartItem(int customerID, int productID) {
        String sql = "SELECT * FROM CartItems WHERE customerID = ? AND productID = ?"; // SỬA TÊN BẢNG
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemID(rs.getInt("cartItemID"));
                cartItem.setProductID(rs.getInt("productID"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setCustomerID(rs.getInt("customerID"));
                return cartItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật số lượng cart item
    public boolean updateCartItemQuantity(int cartItemID, int newQuantity) {
        String sql = "UPDATE CartItems SET quantity = ? WHERE cartItemID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartItemID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa cart item theo ID
    public boolean removeCartItem(int cartItemID) {
        String sql = "DELETE FROM CartItems WHERE cartItemID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartItemID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm các method sau vào CartItemDAO class của bạn:
// Lấy cart item theo ID
    public CartItem getCartItemById(int cartItemId) {
        String sql = "SELECT * FROM CartItems WHERE cartItemID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartItemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemID(rs.getInt("cartItemID"));
                cartItem.setProductID(rs.getInt("productID"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setCustomerID(rs.getInt("customerID"));
                return cartItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

// Xóa tất cả cart items của một customer
    public boolean clearCartByCustomerId(int customerId) {
        String sql = "DELETE FROM CartItems WHERE customerID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);

            return ps.executeUpdate() >= 0; // Return true nếu xóa thành công (có thể xóa 0 items)
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CartItem> getCartItemsByIds(List<Integer> cartItemIds) {
        List<CartItem> cartItems = new ArrayList<>();
        if (cartItemIds.isEmpty()) {
            return cartItems;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ci.*, p.* FROM CartItems ci ");
        sql.append("JOIN Products p ON ci.productID = p.productID ");
        sql.append("WHERE ci.cartItemID IN (");

        for (int i = 0; i < cartItemIds.size(); i++) {
            sql.append("?");
            if (i < cartItemIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        try {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < cartItemIds.size(); i++) {
                ps.setInt(i + 1, cartItemIds.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemID(rs.getInt("cartItemID"));
                cartItem.setProductID(rs.getInt("productID"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setCustomerID(rs.getInt("customerID"));

                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setProductName(rs.getString("productName"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setImageUrl1(rs.getString("imageUrl1"));

                cartItem.setProduct(product);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartItems;
    }
}
