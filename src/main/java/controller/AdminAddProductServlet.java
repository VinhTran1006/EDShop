/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dao.BrandDAO;
import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Brand;
import model.Category;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import model.Product;
import model.Suppliers;

/**
 *
 *
 */
@MultipartConfig
@WebServlet(name = "AdminAddProductServlet", urlPatterns = {"/AdminCreateProduct"})
public class AdminAddProductServlet extends HttpServlet {

    private Cloudinary cloudinary;

    @Override
    public void init() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dgnyskpc3",
                "api_key", "398517693378845",
                "api_secret", "ho0bvkCgpHDBFoUW3M9bG8apAKk",
                "secure", true
        ));
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminAddProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminAddProductServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO cateDAO = new CategoryDAO();
        List<Category> categoryList = cateDAO.getAllCategory();

        BrandDAO brandDAO = new BrandDAO();
        List<Brand> brandList = brandDAO.getAllBrand();

        request.setAttribute("categoryList", categoryList);
        request.setAttribute("brandList", brandList);

        SupplierDAO supDAO = new SupplierDAO();
        List<Suppliers> supList = supDAO.getAllSuppliers();

        request.setAttribute("supList", supList);
        request.getRequestDispatcher("/WEB-INF/View/admin/productManagement/addProduct/addProductInfo/adminAddProduct.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO proDAO = new ProductDAO();
        HttpSession session = request.getSession();
        session.removeAttribute("errorProductName");
        boolean error = false;
        session.removeAttribute("errorProductName");
        session.removeAttribute("errorDescription");
        session.removeAttribute("errorPrice");
        session.removeAttribute("errorWarranty");
        session.removeAttribute("errorQuantity");
        session.removeAttribute("existedProduct");
        String productName = request.getParameter("productName");
        if (productName == null || productName.trim().isEmpty()) {
            session.setAttribute("errorProductName", "Product name cannot be empty.");
            error = true;
        } else if (!productName.matches("[a-zA-Z0-9/\\.\\- ]+")) {
            session.setAttribute("errorProductName", "Product name contains invalid characters.");
            error = true;
        }
        String discription = request.getParameter("description");
        if (discription == null || discription.trim().isEmpty()) {
            session.setAttribute("errorDescription", "Description cannot be empty.");
            error = true;
        } else if (!discription.matches("[a-zA-Z0-9/\\.\\- ]+")) {
            session.setAttribute("errorDescription", "Description contains invalid characters.");
            error = true;
        }

        String priceRaw = request.getParameter("price");
        BigDecimal price = null;
        if (priceRaw == null || priceRaw.trim().isEmpty()) {
            session.setAttribute("errorPrice", "Price cannot be empty.");
            error = true;
        } else {
            try {
                price = new BigDecimal(priceRaw);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    session.setAttribute("errorPrice", "Price must be non-negative.");
                    error = true;
                } else if (price.compareTo(new BigDecimal("1000000000")) > 0) {
                    session.setAttribute("errorPrice", "Price must be less than or equal to 1,000,000,000.");
                    error = true;
                }

            } catch (NumberFormatException e) {
                session.setAttribute("errorPrice", "Price must be a valid number.");
                error = true;
            }
        }

        int Category = Integer.parseInt(request.getParameter("category"));
        int Brand = Integer.parseInt(request.getParameter("brand"));
        int Suppliers = Integer.parseInt(request.getParameter("supplier"));

        int warranty = 0;
        int quantity = 0;
        String warrantyRaw = request.getParameter("warranty");

        if (warrantyRaw == null || warrantyRaw.trim().isEmpty()) {
            session.setAttribute("errorWarranty", "Warranty cannot be empty.");
            error = true;
        } else {
            try {
                warranty = Integer.parseInt(warrantyRaw);
                if (warranty <= 0) {
                    session.setAttribute("errorWarranty", "Warranty must be a positive integer.");
                    error = true;
                } else if (warranty > 100) {
                    session.setAttribute("errorWarranty", "Warranty must be smalller than 100");
                    error = true;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorWarranty", "Warranty must be a valid integer.");
                error = true;
            }
        }
        String quantityRaw = request.getParameter("quantity");

        if (quantityRaw == null || quantityRaw.trim().isEmpty()) {
            session.setAttribute("errorQuantity", "Quantity cannot be empty.");
            error = true;
        } else {
            try {
                quantity = Integer.parseInt(quantityRaw);
                if (quantity <= 0) {
                    session.setAttribute("errorQuantity", "Quantity must be a positive integer.");
                    error = true;
                } else if (quantity > 1000) {
                    session.setAttribute("errorQuantity", "Quantity must be smaller than 1000");
                    error = true;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorQuantity", "Quantity must be a valid integer.");
                error = true;
            }
        }

        boolean exist = proDAO.checkExist(productName, Brand);
        if (exist) {
            session.setAttribute("existedProduct", "This product has already added in the shop.");
            error = true;
        }
//        <====================================== Xử lý ảnh ===========================================>
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

// ⚡ Khai báo map chứa 4 ảnh (1 chính, 3 phụ)
        Map<String, String> imageUrlMap = new LinkedHashMap<>();
        imageUrlMap.put("fileMain", null);
        imageUrlMap.put("fileSub1", null);
        imageUrlMap.put("fileSub2", null);
        imageUrlMap.put("fileSub3", null);

// tạo danh sách future
        Map<String, CompletableFuture<String>> futures = new LinkedHashMap<>();

        for (String key : imageUrlMap.keySet()) {
            Part part = request.getPart(key);

            if (part != null && part.getSize() > 0) {
                futures.put(key, CompletableFuture.supplyAsync(() -> {
                    try ( InputStream is = part.getInputStream();  ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                        byte[] data = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = is.read(data)) != -1) {
                            buffer.write(data, 0, bytesRead);
                        }
                        byte[] fileBytes = buffer.toByteArray();

                        Map uploadResult = cloudinary.uploader().upload(fileBytes,
                                ObjectUtils.asMap("resource_type", "auto"));

                        return (String) uploadResult.get("secure_url");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "https://redthread.uoregon.edu/files/original/affd16fd5264cab9197da4cd1a996f820e601ee4.png";
                    }
                }));
            } else {
                // nếu ko có file thì set mặc định ngay
                imageUrlMap.put(key, "https://redthread.uoregon.edu/files/original/affd16fd5264cab9197da4cd1a996f820e601ee4.png");
            }
        }

// đợi tất cả upload xong
        for (String key : futures.keySet()) {
            try {
                String url = futures.get(key).get(); // lấy kết quả upload
                imageUrlMap.put(key, url != null ? url
                        : "https://redthread.uoregon.edu/files/original/affd16fd5264cab9197da4cd1a996f820e601ee4.png");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                imageUrlMap.put(key, "https://redthread.uoregon.edu/files/original/affd16fd5264cab9197da4cd1a996f820e601ee4.png");
            }
        }
//        <====================================== Xử lý anh ===========================================>
        if (!error) {
            int productId = proDAO.insertProduct(productName, discription, price, Suppliers, Category, Brand, warranty, quantity, imageUrlMap.get("fileMain"), imageUrlMap.get("fileSub1"), imageUrlMap.get("fileSub2"), imageUrlMap.get("fileSub3"));
            response.sendRedirect("AdminAddProductDetail?productId=" + productId + "&categoryId=" + Category);
        } else {
            response.sendRedirect("AdminCreateProduct?error=1");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
