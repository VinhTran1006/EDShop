/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Category;
import model.Attribute;
import utils.DBContext;

/**
 *
 * 
 */
public class CategoryDAO extends DBContext {

    public CategoryDAO() {
        super();
    }

    public ArrayList<Category> getAllCategory() {
        ArrayList<Category> categoryList = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName, ImgURLLogo, isActive FROM Categories WHERE IsActive != 0";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int categoryId = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");
                String imgUrlLogo = rs.getString("ImgURLLogo");
                boolean isActive = rs.getBoolean("IsActive");

                categoryList.add(new Category(categoryId, categoryName, imgUrlLogo, isActive));
            }
            return categoryList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return categoryList;
    }

    public ArrayList<Attribute> getAttributeByCategoryID(int categoryId) {
        ArrayList<Attribute> categoryDetailGroupList = new ArrayList<>();
        String sql = "SELECT * from Attributes where CategoryID = ? AND IsActive != 0";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int atrributeID = rs.getInt("AttributeID");
                String attributeName = rs.getString("AttributeName");
                int categoryID = rs.getInt("CategoryID");
                boolean isActive = rs.getBoolean("IsActive");

                categoryDetailGroupList.add(new Attribute(atrributeID,attributeName ,categoryID,isActive));

            }
        } catch (SQLException e) {
        }
        return categoryDetailGroupList;
    }

    
    public Category getCategoryById(int categoryID) {
        Category category = null;
        String sql = "SELECT CategoryID, CategoryName, ImgURLLogo, isActive FROM Categories where categoryID = ? AND IsActive != 0";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int categoryId = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");
             
               
                String imgUrlLogo = rs.getString("ImgURLLogo");
                boolean isActive = rs.getBoolean("isActive");

                category = new Category(categoryId, categoryName, imgUrlLogo, isActive);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    
    public boolean updateAtrribute(int attributeID, String attributeName) {
        String sql = "UPDATE Attributes SET AttributeName = ? WHERE AttributeID = ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attributeName);
            pstmt.setInt(2, attributeID);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategory(int categoryId, String categoryName, String imageUML) {
        String sql = "UPDATE Categories SET CategoryName = ?, ImgURLLogo =?, IsActive = 1 WHERE CategoryID = ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);
            pstmt.setString(2, imageUML);
            pstmt.setInt(3, categoryId);
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteCategory(int categoryId) {
        String sql = "UPDATE Categories SET isActive = 0 WHERE CategoryID = ?";

        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryId);

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
        
     public boolean deleteAttribute(int attributeID) {
        String sql = "UPDATE Attributes SET isActive = 0 WHERE AttributeID = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, attributeID);

            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    
    
    public int addCategory(String categoryName , String ImgURLLogo) {
        int categoryId = -1;
        String sql = "INSERT INTO Categories (CategoryName, ImgURLLogo, IsActive) VALUES (?, ?, 1)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoryName);
            stmt.setString(2, ImgURLLogo);

            int rowInserted = stmt.executeUpdate();

            if (rowInserted > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoryId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
        }
        return categoryId; // Trả về -1 nếu lỗi
    }

    public int addAttribute(String attributename, int categoryID) {
        int attributeID = -1;
        String sql = "INSERT INTO Attributes ( AttributeName, CategoryID, IsActive) VALUES ( ?, ?, 1)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
         
            stmt.setString(1, attributename);
            stmt.setInt(2, categoryID);
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        attributeID = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attributeID; // Trả về -1 nếu lỗi
    }
    
    
    
    
    public String getCategoryNameByCategoryId(int cateID) {
    String CategoryName = null;
    String sql = "SELECT CategoryName FROM Categories WHERE CategoryID = ? AND IsActive != 0";
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cateID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) { // phải gọi rs.next() trước
            CategoryName = rs.getString("CategoryName");
        }
        rs.close();
        ps.close();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    return CategoryName;
}

    
    
    
    
          
    public static void main(String[] args) {
//       Category result;
//       ArrayList<Attribute> r = new ArrayList<>();
    CategoryDAO dao = new CategoryDAO();
//     r = dao.getAttributeByCategoryID(1);
//        for (Attribute s : r) {
//            System.out.println(s.toString());
//        }
    dao.updateCategory(23,"bbbbbbb","bbbbbbbbb");
    }
}
