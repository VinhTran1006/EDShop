package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.ImportStockDetail;
import model.Product;
import utils.DBContext;

public class ImportStockDetailDAO extends DBContext {

    // Insert nhiều chi tiết (batch)
    public int createImportStockDetails(ArrayList<ImportStockDetail> detailList) {
        String query = "INSERT INTO ImportStockDetails (ImportID, ProductID, Stock, UnitPrice, StockLeft) VALUES (?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            for (ImportStockDetail d : detailList) {
                ps.setInt(1, d.getImportID());
                ps.setInt(2, d.getProductID());
                ps.setInt(3, d.getStock());
                ps.setLong(4, d.getUnitPrice());
                ps.setInt(5, d.getStockLeft());
                ps.addBatch();
            }
            int[] result = ps.executeBatch();
            return result.length;
        } catch (SQLException e) {
            System.out.println("createImportStockDetails: " + e.getMessage());
        }
        return 0;
    }

    // Insert 1 chi tiết
    public int createImportStockDetail(ImportStockDetail detail) {
        String query = "INSERT INTO ImportStockDetails (ImportID, ProductID, Stock, UnitPrice, StockLeft) VALUES (?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detail.getImportID());
            ps.setInt(2, detail.getProductID());
            ps.setInt(3, detail.getStock());
            ps.setLong(4, detail.getUnitPrice());
            ps.setInt(5, detail.getStockLeft());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("createImportStockDetail: " + e.getMessage());
        }
        return 0;
    }

    // Lấy list chi tiết theo ImportID
    public ArrayList<ImportStockDetail> getDetailsByImportId(int importId) {
        String query = "SELECT d.*, p.ProductName FROM ImportStockDetails d "
                + "JOIN Products p ON d.ProductID = p.ProductID "
                + "WHERE d.ImportID = ?";
        ArrayList<ImportStockDetail> list = new ArrayList<>();
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, importId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportStockDetail d = new ImportStockDetail();
                d.setImportStockDetailsID(rs.getInt("ImportStockDetailsID"));
                d.setImportID(rs.getInt("ImportID"));
                d.setProductID(rs.getInt("ProductID"));
                d.setStock(rs.getInt("Stock"));
                d.setStockLeft(rs.getInt("StockLeft"));
                d.setUnitPrice(rs.getLong("UnitPrice"));

                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                d.setProduct(p);

                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println("getDetailsByImportId: " + e.getMessage());
        }
        return list;
    }

    // Update số lượng, giá
    public int updateDetailById(ImportStockDetail d) {
        String query = "UPDATE ImportStockDetails SET Stock = ?, UnitPrice = ?, StockLeft = ? "
                + "WHERE ImportStockDetailsID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, d.getStock());
            ps.setLong(2, d.getUnitPrice());
            ps.setInt(3, d.getStockLeft());
            ps.setInt(4, d.getImportStockDetailsID());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateDetailById: " + e.getMessage());
        }
        return 0;
    }

    // Xóa chi tiết
    public int deleteDetailById(int detailId) {
        String query = "DELETE FROM ImportStockDetails WHERE ImportStockDetailsID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, detailId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteDetailById: " + e.getMessage());
        }
        return 0;
    }

    // Tính tổng tiền nhập
    public long calculateTotalPrice(int importId) {
        String query = "SELECT SUM(UnitPrice * Stock) AS TotalPrice "
                + "FROM ImportStockDetails WHERE ImportID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, importId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("TotalPrice");
            }
        } catch (SQLException e) {
            System.out.println("calculateTotalPrice: " + e.getMessage());
        }
        return 0;
    }

    // Lấy chi tiết nhập kho hôm nay
    public ArrayList<ImportStockDetail> getImportStocksToday() {
        String query = "SELECT d.*, p.ProductName FROM ImportStockDetails d "
                + "JOIN ImportStocks i ON d.ImportID = i.ImportID "
                + "JOIN Products p ON d.ProductID = p.ProductID "
                + "WHERE CAST(i.ImportDate AS DATE) = CAST(GETDATE() AS DATE)";
        ArrayList<ImportStockDetail> list = new ArrayList<>();
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportStockDetail d = new ImportStockDetail();
                d.setImportStockDetailsID(rs.getInt("ImportStockDetailsID"));
                d.setImportID(rs.getInt("ImportID"));
                d.setProductID(rs.getInt("ProductID"));
                d.setStock(rs.getInt("Stock"));
                d.setStockLeft(rs.getInt("StockLeft"));
                d.setUnitPrice(rs.getLong("UnitPrice"));

                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                d.setProduct(p);

                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println("getImportStocksToday: " + e.getMessage());
        }
        return list;
    }

    // Cập nhật StockLeft
    public int updateStockLeft(int importId, int productId, int stockLeft) {
        String query = "UPDATE ImportStockDetails SET StockLeft = ? WHERE ImportID = ? AND ProductID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, stockLeft);
            ps.setInt(2, importId);
            ps.setInt(3, productId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateStockLeft: " + e.getMessage());
        }
        return 0;
    }

    // Xuất kho (giảm StockLeft)
    public boolean updateStockForOrder(List<CartItem> cartItems) {
        String update = "UPDATE ImportStockDetails "
                + "SET StockLeft = StockLeft - ? "
                + "WHERE ProductID = ? AND StockLeft >= ?";
        try ( PreparedStatement ps = conn.prepareStatement(update)) {
            conn.setAutoCommit(false);
            try {
                for (CartItem item : cartItems) {
                    ps.setInt(1, item.getQuantity());
                    ps.setInt(2, item.getProductID());
                    ps.setInt(3, item.getQuantity());
                    ps.addBatch();
                }
                int[] counts = ps.executeBatch();
                for (int c : counts) {
                    if (c == 0) {
                        conn.rollback();
                        return false;
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("updateStockForOrder: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("updateStockForOrder: " + e.getMessage());
            return false;
        }
    }
}
