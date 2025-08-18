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
import model.CategoryDetail;
import model.CategoryDetailGroup;
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

    public ArrayList<CategoryDetailGroup> getCategoryDetailGroupById(int categoryId) {
        ArrayList<CategoryDetailGroup> categoryDetailGroupList = new ArrayList<>();
        String sql = "SELECT * from CategoryDetailsGroup where CategoryID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int categoryDetailsGroupID = rs.getInt("CategoryDetailsGroupID");
                String nameCategoryDetailsGroup = rs.getString("NameCategoryDetailsGroup");
                int categoryID = rs.getInt("CategoryID");
                boolean isActive = rs.getBoolean("IsActive");

                categoryDetailGroupList.add(new CategoryDetailGroup(categoryDetailsGroupID, nameCategoryDetailsGroup,categoryID,isActive));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryDetailGroupList;
    }

    public ArrayList<CategoryDetail> getCategoryDetailById(int categoryId) {
        ArrayList<CategoryDetail> categoryDetailList = new ArrayList<>();
        String sql = "SELECT CategoryDetailID, AttributeName, CategoryDetailsGroupID, IsActive from CategoryDetails where CategoryDetailsGroupID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int categoryDetailID = rs.getInt("CategoryDetailID");
           
                String attributeName = rs.getString("AttributeName");
                int categoryDetailsGroupID = rs.getInt("CategoryDetailsGroupID");
                boolean isActive = rs.getBoolean("IsActive");

                categoryDetailList.add(new CategoryDetail(categoryDetailID, attributeName, categoryDetailsGroupID, isActive));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryDetailList;
    }

//    public List<CategoryDetail> getAllCategoryDetails() {
//        List<CategoryDetail> categoryDetailList = new ArrayList<>();
//        String sql = "SELECT CategoryID, CategoryName, Description, CreatedAt, ImgURLLogo FROM Categories";
//
//        try {
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                int categoryId = rs.getInt("CategoryID");
//                String categoryName = rs.getString("CategoryName");
//                String descriptionCategory = rs.getString("Description");
//                Timestamp createdAt = rs.getTimestamp("CreatedAt");
//                String imgUrlLogo = rs.getString("ImgURLLogo");
//
//                categoryList.add(new Category(categoryId, categoryName, descriptionCategory, createdAt, imgUrlLogo));
//            }
//            return categoryList;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return categoryList;
//    }
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
    
   
    public boolean updateCategporyDetailGroup(int categoryDetailsGroupID, String nameCategoryDetailsGroup) {
        String sql = "UPDATE CategoryDetailsGroup SET NameCategoryDetailsGroup = ? WHERE CategoryDetailsGroupID = ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameCategoryDetailsGroup);
            pstmt.setInt(2, categoryDetailsGroupID);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCategporyDetail(int categoryDetailID, String attributeName) {
        String sql = "UPDATE CategoryDetails SET AttributeName = ? WHERE CategoryDetailID = ?";
        try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, attributeName);
            pstmt.setInt(2, categoryDetailID);

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

    public int addCategoryDetailsGroup(String nameCategoryDetailsGroup, int categoryID) {
        int categoryDetailsGroupID = -1;
        String sql = "INSERT INTO CategoryDetailsGroup (NameCategoryDetailsGroup, CategoryID) VALUES (?, ?)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nameCategoryDetailsGroup);
            stmt.setInt(2, categoryID);

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

    public int addCategoryDetails(int categoryID, String attributeName, int categoryDetailsGroupID) {
        int categoryDetailID = -1;
        String sql = "INSERT INTO CategoryDetails (CategoryID, AttributeName, CategoryDetailsGroupID) VALUES (?, ?, ?)";

        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, categoryID);
            stmt.setString(2, attributeName);
            stmt.setInt(3, categoryDetailsGroupID);  // ĐÚNG vị trí thứ 3

            int rowInserted = stmt.executeUpdate();

            if (rowInserted > 0) {
                try ( ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoryDetailID = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryDetailID; // Trả về -1 nếu lỗi
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
