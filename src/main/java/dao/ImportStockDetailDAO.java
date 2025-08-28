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
                ps.setBigDecimal(4, d.getUnitPrice());
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
            ps.setBigDecimal(4, detail.getUnitPrice());
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
                d.setUnitPrice(rs.getBigDecimal("UnitPrice"));

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
            ps.setBigDecimal(2, d.getUnitPrice());
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
                d.setUnitPrice(rs.getBigDecimal("UnitPrice"));

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

    public List<int[]> deductStockFIFO(int productID, int quantityToDeduct) throws SQLException {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<int[]> deductedList = new ArrayList<>(); // lưu [detailID, deductedQuantity]

        try {
            conn.setAutoCommit(false); // bắt đầu transaction

            // 1. Kiểm tra tổng quantity trong bảng Products
            String checkQtySql = "SELECT Quantity FROM Products WHERE ProductID = ?";
            ps = conn.prepareStatement(checkQtySql);
            ps.setInt(1, productID);
            rs = ps.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                return null; // product không tồn tại
            }
            int currentQty = rs.getInt("Quantity");
            if (currentQty < quantityToDeduct) {
                conn.rollback();
                return null; // không đủ hàng
            }
            rs.close();
            ps.close();

            // 2. Lấy danh sách ImportStockDetails theo FIFO
            String fifoSql = "SELECT d.ImportStockDetailsID, d.StockLeft "
                    + "FROM ImportStockDetails d "
                    + "JOIN ImportStocks i ON d.ImportID = i.ImportID "
                    + "WHERE d.ProductID = ? AND d.StockLeft > 0 "
                    + "ORDER BY i.ImportDate ASC, d.ImportStockDetailsID ASC";
            ps = conn.prepareStatement(fifoSql);
            ps.setInt(1, productID);
            rs = ps.executeQuery();

            List<int[]> fifoList = new ArrayList<>();
            while (rs.next()) {
                fifoList.add(new int[]{
                    rs.getInt("ImportStockDetailsID"),
                    rs.getInt("StockLeft")
                });
            }
            rs.close();
            ps.close();

            int remaining = quantityToDeduct;

            // 3. Duyệt từng lô và update StockLeft
            for (int[] detail : fifoList) {
                if (remaining <= 0) {
                    break;
                }

                int detailID = detail[0];
                int stockLeft = detail[1];
                int deduct = Math.min(stockLeft, remaining);

                String updateDetailSql = "UPDATE ImportStockDetails "
                        + "SET StockLeft = StockLeft - ? "
                        + "WHERE ImportStockDetailsID = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateDetailSql);
                psUpdate.setInt(1, deduct);
                psUpdate.setInt(2, detailID);
                psUpdate.executeUpdate();
                psUpdate.close();

                // lưu cặp [ID lô, số lượng trừ]
                deductedList.add(new int[]{detailID, deduct});
                remaining -= deduct;
            }

            if (remaining > 0) {
                conn.rollback();
                return null; // không đủ stock
            }

            // 4. Update bảng Products
            String updateProductSql = "UPDATE Products SET Quantity = Quantity - ? WHERE ProductID = ?";
            PreparedStatement psUpdate = conn.prepareStatement(updateProductSql);
            psUpdate.setInt(1, quantityToDeduct);
            psUpdate.setInt(2, productID);
            psUpdate.executeUpdate();
            psUpdate.close();

            conn.commit();
            return deductedList; // trả về danh sách chi tiết lô đã trừ

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    // Trả hàng lại theo batch List<int[]> {ImportStockDetailsID, quantity}
    public boolean increaseStockBack(List<int[]> batchList) {
        if (batchList == null || batchList.isEmpty()) {
            return true;
        }

        String update = "UPDATE ImportStockDetails SET StockLeft = StockLeft + ? WHERE ImportStockDetailsID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(update)) {
            conn.setAutoCommit(false);  // bắt đầu transaction
            try {
                // Duyệt từng lô và add vào batch
                for (int[] pair : batchList) {
                    int importDetailID = pair[0];
                    int quantity = pair[1];

                    // In log dễ kiểm tra
                    System.out.println("Returning " + quantity + " units to ImportStockDetailsID " + importDetailID);

                    ps.setInt(1, quantity);
                    ps.setInt(2, importDetailID);
                    ps.addBatch();
                }

                int[] counts = ps.executeBatch();

                // Kiểm tra nếu có lô nào update 0 dòng => rollback
                for (int c : counts) {
                    if (c == 0) {
                        conn.rollback();
                        System.err.println("increaseStockBack: rollback, some rows not updated");
                        return false;
                    }
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Chỉ dùng trong DAO, private hoặc public static
private List<int[]> parseBatchString(String batchStr) {
    List<int[]> list = new ArrayList<>();
    if (batchStr == null || batchStr.trim().isEmpty()) return list;

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


}
