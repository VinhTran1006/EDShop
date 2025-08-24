package dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Product;
import model.ProductDetail;

import utils.DBContext;

/**
 *
 *
 */
public class ProductDAO extends DBContext {

    public List<Product> getProductIsNew() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP 30 * \n"
                + "FROM Products WHERE IsActive != 0 \n"
                + "ORDER BY AddedAt DESC;";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Product> getProductIsFeatured() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP 30 *"
                + "FROM Products p WHERE IsActive != 0 "
                + "ORDER BY NEWID()";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Product> getProductIsBestSeller() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP 30\n"
                + "    p.ProductID,p.ProductName,p.Description,p.Price,p.SupplierID,p.ImageURL1,p.ImageURL2,p.ImageURL3,p.ImageURL4,p.AddedAt,p.Quantity,p.CategoryID,p.BrandID,p.WarrantyPeriod, p.isActive,SUM(od.Quantity) AS TotalSold\n"
                + "FROM Products p\n"
                + "JOIN OrderDetails od ON od.ProductID = p.ProductID\n"
                + "JOIN Orders o ON o.OrderID = od.OrderID\n"
                + "WHERE o.Status = 'Delivered' AND o.OrderedDate >= DATEADD(MONTH, -2, GETDATE()) AND IsActive != 0 \n"
                + "GROUP BY \n"
                + "    p.ProductID,p.ProductName,p.Quantity,p.Description,p.ImageURL1,p.ImageURL2,p.ImageURL3,p.ImageURL4,p.Price,p.SupplierID,p.AddedAt,p.CategoryID,p.BrandID,p.WarrantyPeriod, p.isActive\n"
                + "ORDER BY TotalSold DESC;";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Product> getProductListCustomer() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE IsActive = 1 AND Quantity != 0";

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
        } catch (Exception e) {
        }

        return list;
    }

    public List<Product> getProductListAdmin() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE IsActive = 1";

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
        } catch (Exception e) {
        }

        return list;
    }

    public List<Product> searchProductByNameForCustomer(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT *"
                + "FROM Products p "
                + "WHERE LOWER(p.ProductName) LIKE ? AND IsActive != 0 AND Quantity != 0";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("ProductID");
                    String productName = rs.getString("ProductName");
                    String description = rs.getString("Description");
                    BigDecimal price = rs.getBigDecimal("Price");
                    int supplierId = rs.getInt("SupplierID");
                    int categoryId = rs.getInt("CategoryID");
                    int brandId = rs.getInt("BrandID");
                    int warrantyPeriod = rs.getInt("WarrantyPeriod");
                    Date addedAt = rs.getDate("AddedAt");
                    boolean isActive = rs.getBoolean("isActive");
                    String imageUrl1 = rs.getString("ImageURL1");
                    String imageUrl2 = rs.getString("ImageURL2");
                    String imageUrl3 = rs.getString("ImageURL3");
                    String imageUrl4 = rs.getString("ImageURL4");
                    int quantity = rs.getInt("Quantity");

                    Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                    list.add(product);
                }
            }
        } catch (Exception e) {
        }

        return list;
    }

    public List<Product> searchProductByNameForAdmin(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT *"
                + "FROM Products p "
                + "WHERE LOWER(p.ProductName) LIKE ? AND IsActive != 0";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("ProductID");
                    String productName = rs.getString("ProductName");
                    String description = rs.getString("Description");
                    BigDecimal price = rs.getBigDecimal("Price");
                    int supplierId = rs.getInt("SupplierID");
                    int categoryId = rs.getInt("CategoryID");
                    int brandId = rs.getInt("BrandID");
                    int warrantyPeriod = rs.getInt("WarrantyPeriod");
                    Date addedAt = rs.getDate("AddedAt");
                    boolean isActive = rs.getBoolean("isActive");
                    String imageUrl1 = rs.getString("ImageURL1");
                    String imageUrl2 = rs.getString("ImageURL2");
                    String imageUrl3 = rs.getString("ImageURL3");
                    String imageUrl4 = rs.getString("ImageURL4");
                    int quantity = rs.getInt("Quantity");

                    Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                    list.add(product);
                }
            }
        } catch (Exception e) {
        }

        return list;
    }

    public Product getProductByID(int productID) {
        Product product = null;
        String sql = "SELECT *"
                + "FROM Products p "
                + "WHERE p.ProductID = ? AND IsActive != 0";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    int productId = rs.getInt("ProductID");
                    String productName = rs.getString("ProductName");
                    String description = rs.getString("Description");
                    BigDecimal price = rs.getBigDecimal("Price");
                    int supplierId = rs.getInt("SupplierID");
                    int categoryId = rs.getInt("CategoryID");
                    int brandId = rs.getInt("BrandID");
                    int warrantyPeriod = rs.getInt("WarrantyPeriod");
                    Date addedAt = rs.getDate("AddedAt");
                    boolean isActive = rs.getBoolean("isActive");
                    String imageUrl1 = rs.getString("ImageURL1");
                    String imageUrl2 = rs.getString("ImageURL2");
                    String imageUrl3 = rs.getString("ImageURL3");
                    String imageUrl4 = rs.getString("ImageURL4");
                    int quantity = rs.getInt("Quantity");

                    product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);
                }
            }
        } catch (Exception e) {
        }

        return product;
    }

    public boolean deleteProduct(int productId) {
        String sql = "UPDATE Products SET IsActive = 0 WHERE ProductID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Rows affected by rejectProduct: " + rowsAffected + " for ProductID: " + productId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in rejectProduct: " + e.getMessage());
            return false;
        }
    }

    public List<ProductDetail> getProductDetailByProductId(int productId) {
        List<ProductDetail> productDetailList = new ArrayList<>();
        String sql = "SELECT * FROM ProductDetails p WHERE p.ProductID = ? AND IsActive != 0";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productDetailId = rs.getInt("ProductDetailID");
                int productID = rs.getInt("ProductID");
                int attributeID = rs.getInt("AttributeID");
                String attributeValue = rs.getString("AttributeValue");
                boolean isActive = rs.getBoolean("IsActive");
                ProductDetail productDetail = new ProductDetail(
                        productDetailId, productID, attributeID, attributeValue, isActive
                );
                productDetailList.add(productDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetailList;
    }

    public boolean updateProduct(int id, String productName, String description, BigDecimal price, int supplierid, int categoryid, int brandid, int warranty, int quantity, String url1, String url2, String url3, String url4) {
        String sql1 = "UPDATE Products SET ProductName = ?,Description=?, Price = ?, SupplierID = ?, CategoryID = ?, "
                + "BrandID = ?,WarrantyPeriod =? ,Quantity = ?, ImageURL1 = ? ,ImageURL2 =? , ImageURL3 =? ,ImageURL4 =? WHERE ProductID = ?";

        try (
                 PreparedStatement pstmt1 = conn.prepareStatement(sql1);) {
            pstmt1.setString(1, productName);
            pstmt1.setString(2, description);
            pstmt1.setBigDecimal(3, price);
            pstmt1.setInt(4, supplierid);
            pstmt1.setInt(5, categoryid);
            pstmt1.setInt(6, brandid);
            pstmt1.setInt(7, warranty);
            pstmt1.setInt(8, quantity);
            pstmt1.setString(9, url1);
            pstmt1.setString(10, url2);
            pstmt1.setString(11, url3);
            pstmt1.setString(12, url4);
            pstmt1.setInt(13, id);

            int rows1 = pstmt1.executeUpdate();

            return rows1 > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean updateProductDetail(int productdetailID, String atrributeValue) {
        String sql = "UPDATE ProductDetails SET AttributeValue = ? WHERE ProductDetailID  = ?";
        try (
                 PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, atrributeValue);
            pstmt.setInt(2, productdetailID);
            int rows2 = pstmt.executeUpdate();
            return rows2 > 0;
        } catch (SQLException e) {
        }
        return false;
    }

    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM Products Where IsActive != 0";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
        }
        return 0;
    }

    public int insertProduct(String productName, String description, BigDecimal price, int supplierid, int categoryid, int brandid, int warranty, int quantity, String url1, String url2, String url3, String url4) {
        int productId = 0;
        String sql = "INSERT INTO Products (ProductName, Description,Price,SupplierID,CategoryID, BrandID, WarrantyPeriod, Quantity, ImageURL1, ImageURL2,ImageURL3,ImageURL4, IsActive ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, 1)";

        try ( PreparedStatement pstmt1 = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            pstmt1.setString(1, productName);
            pstmt1.setString(2, description);
            pstmt1.setBigDecimal(3, price);
            pstmt1.setInt(4, supplierid);
            pstmt1.setInt(5, categoryid);
            pstmt1.setInt(6, brandid);
            pstmt1.setInt(7, warranty);
            pstmt1.setInt(8, quantity);
            pstmt1.setString(9, url1);
            pstmt1.setString(10, url2);
            pstmt1.setString(11, url3);
            pstmt1.setString(12, url4);
            int affectedRows = pstmt1.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insert product failed, no rows affected.");
            }

            try ( ResultSet generatedKeys = pstmt1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    productId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
        }

        return productId;
    }

    public boolean insertProductDetail(int productId, int addtributeid, String attributeValue) {
        boolean rowInserted;
        String sql = "INSERT INTO ProductDetails (ProductID, AttributeID, AttributeValue, IsActive) VALUES (?, ?, ?,?)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            stmt.setInt(2, addtributeid);
            stmt.setString(3, attributeValue);
            stmt.setBoolean(4, true);

            rowInserted = stmt.executeUpdate() > 0;

            if (rowInserted) {
                return true;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    public List<Product> getProductByBrandAndPrice(int brandId, BigDecimal min, BigDecimal max) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT *"
                + "FROM Products p "
                + "WHERE p.BrandID = ? AND p.Price >= ? AND p.Price <= ? AND p.IsActive != 0";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, brandId);
            ps.setBigDecimal(2, min);
            ps.setBigDecimal(3, max);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error in getProductByKeyword: " + e.getMessage());
        }
        return list;
    }

    public List<Product> getProductByCategoryAndPrice(int categoryId, BigDecimal min, BigDecimal max) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Products p "
                + "WHERE p.CategoryID = ? AND p.Price >= ? AND p.Price <= ? AND p.IsActive != 0";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ps.setBigDecimal(2, min);
            ps.setBigDecimal(3, max);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error in getProductByKeyword: " + e.getMessage());
        }
        return list;
    }

    public List<Product> getProductByKeyword(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Products p "
                + "JOIN Categories cate ON cate.CategoryID = p.CategoryID "
                + "JOIN Brands br ON br.BrandID = p.BrandID "
                + "WHERE (LOWER(p.ProductName) LIKE ? "
                + "OR LOWER(br.BrandName) LIKE ? "
                + "OR LOWER(cate.CategoryName) LIKE ?) "
                + "AND p.IsActive != 0";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            String keywordPattern = "%" + keyword.toLowerCase() + "%";
            ps.setString(1, keywordPattern);
            ps.setString(2, keywordPattern);
            ps.setString(3, keywordPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error in getProductByKeyword: " + e.getMessage());
        }
        return list;
    }

    public List<Product> getProductByBrandAndCategory(int brandId, int categoryID) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Products p "
                + "WHERE p.BrandID = ? AND p.CategoryID = ? AND IsActive != 0";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, brandId);
            ps.setInt(2, categoryID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int categoryId = rs.getInt("CategoryID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Product> getProductByCategoryID(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT *"
                + "FROM Products p "
                + "where p.CategoryID = ? AND IsActive != 0";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                String description = rs.getString("Description");
                BigDecimal price = rs.getBigDecimal("Price");
                int supplierId = rs.getInt("SupplierID");
                int brandId = rs.getInt("BrandID");
                int warrantyPeriod = rs.getInt("WarrantyPeriod");
                Date addedAt = rs.getDate("AddedAt");
                boolean isActive = rs.getBoolean("isActive");
                String imageUrl1 = rs.getString("ImageURL1");
                String imageUrl2 = rs.getString("ImageURL2");
                String imageUrl3 = rs.getString("ImageURL3");
                String imageUrl4 = rs.getString("ImageURL4");
                int quantity = rs.getInt("Quantity");

                Product product = new Product(productId, productName, description, addedAt, price, supplierId, categoryId, brandId, warrantyPeriod, isActive, quantity, imageUrl1, imageUrl2, imageUrl3, imageUrl4);

                list.add(product);
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public int countLowStockDynamic(int threshold) {
        String sql = "SELECT COUNT(*) AS LowStockCount FROM ( "
                + "SELECT p.ProductID, "
                + "ISNULL(SUM(isd.Quantity), 0) - ISNULL((SELECT SUM(od.Quantity) FROM OrderDetails od WHERE od.ProductID = p.ProductID), 0) AS StockLeft "
                + "FROM Products p "
                + "LEFT JOIN ImportStockDetails isd ON p.ProductID = isd.ProductID "
                + "GROUP BY p.ProductID "
                + ") AS StockView WHERE StockLeft <= ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold); // Ví dụ, threshold = 5
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, Integer> getAllProductStocks() {
        Map<Integer, Integer> stockMap = new HashMap<>();
        String sql = "SELECT p.ProductID, "
                + "ISNULL(SUM(isd.Quantity), 0) - ISNULL((SELECT SUM(od.Quantity) FROM OrderDetails od WHERE od.ProductID = p.ProductID), 0) AS Stock AND IsActive != 0"
                + "FROM Products p "
                + "LEFT JOIN ImportStockDetails isd ON p.ProductID = isd.ProductID "
                + "GROUP BY p.ProductID";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                int stock = rs.getInt("Stock");
                stockMap.put(productId, stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockMap;
    }

    public ProductDetail getOneProductDetailById(int productId) {
        ProductDetail productDetail = null;
        String sql = "SELECT *"
                + "FROM ProductDetails p "
                + "where p.ProductID = ? AND IsActive != 0";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int productDetailId = rs.getInt("ProductDetailID");
                int productID = rs.getInt("ProductID");
                int attributeID = rs.getInt("AttribiteID");
                String attributeValue = rs.getString("AttributeValue");
                boolean isActive = rs.getBoolean("isAvtive");
                productDetail = new ProductDetail(productDetailId, productID, attributeID, attributeValue, isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetail;
    }

    public static void main(String[] args) {

        List<Product> r = new ArrayList<>();
        ProductDAO dao = new ProductDAO();
        dao.updateProductDetail(1, "something");
    }

}
