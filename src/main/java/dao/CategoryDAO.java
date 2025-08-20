/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
        String sql = "SELECT CategoryID, CategoryName, ImgURLLogo, isActive FROM Categories";
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
        String sql = "SELECT * from Attibutes where AttributeID = ?";
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
            e.printStackTrace();
        }
        return categoryDetailGroupList;
    }

    
    public Category getCategoryById(int categoryID) {
        Category category = null;
        String sql = "SELECT CategoryID, CategoryName, ImgURLLogo, isActive FROM Categories where categoryID = ?";
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
        String sql = "UPDATE Categories SET CategoryName = ?, imgURLogo =? WHERE CategoryID = ?";
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
    
    public int addCategory(String categoryName, String descriptionCategory) {
        int categoryId = -1;
        String sql = "INSERT INTO Categories (CategoryName, Description) VALUES (?, ?)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoryName);
            stmt.setString(2, descriptionCategory);

            int rowInserted = stmt.executeUpdate();

            if (rowInserted > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoryId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryId; // Trả về -1 nếu lỗi
    }

    public int addAttribute(String attributename, int categoryID, int atrributeID) {
        int categoryDetailsGroupID = -1;
        String sql = "INSERT INTO Attributes (AttributeID, AttributeName, CategoryID, IsActive) VALUES (?, ?, ?, ?)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, atrributeID);
            stmt.setString(2, attributename);
            stmt.setInt(3, categoryID);
            stmt.setBoolean(4, true);
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoryDetailsGroupID = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryDetailsGroupID; // Trả về -1 nếu lỗi
    }
          
    public static void main(String[] args) {
       // ArrayList<Category> result = new ArrayList<>();
       Category result;
    CategoryDAO dao = new CategoryDAO();
    result = dao.getCategoryById(1);
        System.out.println(result);
//       for (Category a : result) {
//           System.out.println(a.toString());
//       }
    }
}
