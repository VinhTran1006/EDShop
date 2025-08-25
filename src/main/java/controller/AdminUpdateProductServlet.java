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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import model.Attribute;
import model.Brand;
import model.Category;

import model.Product;
import model.ProductDetail;
import model.Suppliers;

/**
 *
 *
 */
@MultipartConfig
@WebServlet(name = "AdminUpdateProductServlet", urlPatterns = {"/AdminUpdateProduct"})
public class AdminUpdateProductServlet extends HttpServlet {

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
            out.println("<title>Servlet StaffUpdateInfoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffUpdateInfoServlet at " + request.getContextPath() + "</h1>");
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

        String productIdString = request.getParameter("productId");

        int productId = ((productIdString != null) ? Integer.parseInt(productIdString) : -1);

        if (productId != -1) {

            ProductDAO proDao = new ProductDAO();
            CategoryDAO cateDao = new CategoryDAO();
            BrandDAO brandDao = new BrandDAO();

            List<Category> categoryList = cateDao.getAllCategory();
            List<Brand> brandList = brandDao.getAllBrand();
            Product product = proDao.getProductByID(productId);

            List<ProductDetail> productDetailList = proDao.getProductDetailByProductId(productId);
            List<Attribute> attributeList = cateDao.getAttributeByCategoryID(product.getCategoryID());

            request.setAttribute("categoryList", categoryList);
            request.setAttribute("brandList", brandList);
            request.setAttribute("product", product);
            request.setAttribute("productDetailList", productDetailList);
            request.setAttribute("attributeList", attributeList);
            request.setAttribute("productId", productId);

            SupplierDAO supDAO = new SupplierDAO();
            List<Suppliers> supList = supDAO.getAllSuppliers();

            request.setAttribute("supList", supList);
            request.getRequestDispatcher("/WEB-INF/View/admin/productManagement/updateProduct/updateProduct.jsp").forward(request, response);
        }

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

        String productIdString = request.getParameter("productId");
        int productId = ((productIdString != null) ? Integer.parseInt(productIdString) : -1);
        System.out.println("id khong loi");
        HttpSession session = request.getSession();
        session.removeAttribute("errorProductName");
        session.removeAttribute("errorDescription");
        session.removeAttribute("errorPrice");
        session.removeAttribute("errorWarranty");
        session.removeAttribute("errorQuantity");
        String productName = request.getParameter("productName");
        if (productName == null || productName.trim().isEmpty()) {
            session.setAttribute("errorProductName", "Product name cannot be empty.");
        } else if (!productName.matches("[a-zA-Z0-9/\\.\\- ]+")) {
            session.setAttribute("errorProductName", "Product name contains invalid characters.");
        }
        System.out.println("name khong loi");
        String discription = request.getParameter("description");
        if (discription == null || discription.trim().isEmpty()) {
            session.setAttribute("errorDescription", "Description cannot be empty.");
        } else if (!discription.matches("[a-zA-Z0-9/\\.\\- ]+")) {
            session.setAttribute("errorDescription", "Description contains invalid characters.");
        }
        System.out.println("description khong loi");
        String priceFormatted = request.getParameter("price");
        priceFormatted = priceFormatted.replace(".", "") // bỏ dấu chấm ngăn cách hàng nghìn
                .replace("₫", "") // bỏ ký tự tiền
                .trim();
        BigDecimal price = new BigDecimal(priceFormatted);
        if (priceFormatted == null || priceFormatted.trim().isEmpty()) {
            session.setAttribute("errorPrice", "Price cannot be empty.");
        } else {
            try {
                price = new BigDecimal(priceFormatted);
                if (price.compareTo(BigDecimal.ZERO) < 0) {
                    session.setAttribute("errorPrice", "Price must be non-negative.");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorPrice", "Price must be a valid number.");
            }
        }
        System.out.println("Price khong loi");
        int Category = Integer.parseInt(request.getParameter("category"));
        System.out.println("category khong loi");
        int Brand = Integer.parseInt(request.getParameter("brand"));
        System.out.println("brand khong loi");
        int Suppliers = Integer.parseInt(request.getParameter("supplier"));
        System.out.println("suplier khong loi");
        int warranty = 0;
        int quantity = 0;
        String warrantyRaw = request.getParameter("warranty");

        if (warrantyRaw == null || warrantyRaw.trim().isEmpty()) {
            session.setAttribute("errorWarranty", "Warranty cannot be empty.");
        } else {
            try {
                warranty = Integer.parseInt(warrantyRaw);
                if (warranty <= 0) {
                    session.setAttribute("errorWarranty", "Warranty must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorWarranty", "Warranty must be a valid integer.");
            }
        }
        System.out.println("warranty khong loi");
        String quantityRaw = request.getParameter("quantity");

        if (quantityRaw == null || quantityRaw.trim().isEmpty()) {
            session.setAttribute("errorQuantity", "Quantity cannot be empty.");
        } else {
            try {
                quantity = Integer.parseInt(quantityRaw);
                if (quantity <= 0) {
                    session.setAttribute("errorQuantity", "Quantity must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorQuantity", "Quantity must be a valid integer.");
            }
        }
        System.out.println("quantity khong loi");
        System.out.println("id " + productId);
        System.out.println("name " + productName);
        System.out.println("description " + discription);
        System.out.println("price " + price);
        System.out.println("category " + Category);
        System.out.println("Brand " + Brand);
        System.out.println("suplier " + Suppliers);
        System.out.println("warranty " + warranty);
        System.out.println("quantity " + quantity);
        ProductDAO proDAO = new ProductDAO();
        Product product = proDAO.getProductByID(productId);
//        <====================================== Xử lý ảnh ===========================================>
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

// ⚡ Khai báo map chứa 4 ảnh (1 chính, 3 phụ)
        Map<String, String> imageUrlMap = new LinkedHashMap<>();
        imageUrlMap.put("fileMain", product.getImageUrl1());
        imageUrlMap.put("file1", product.getImageUrl2());
        imageUrlMap.put("file2", product.getImageUrl3());
        imageUrlMap.put("file3", product.getImageUrl4());

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
                        return null; // để sau xử lý
                    }
                }));
            }
        }

        for (String key : futures.keySet()) {
            try {
                String url = futures.get(key).get();
                if (url != null) {
                    imageUrlMap.put(key, url); // update link mới
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imageUrlMap.replaceAll((key, oldUrl)
                -> (oldUrl == null || oldUrl.isEmpty())
                ? "https://redthread.uoregon.edu/files/original/affd16fd5264cab9197da4cd1a996f820e601ee4.png"
                : oldUrl);

        //        <====================================== Xử lý ảnh ===========================================>
        boolean res = proDAO.updateProduct(productId, productName, discription, price, Suppliers, Category, Brand, warranty, quantity, imageUrlMap.get("fileMain"), imageUrlMap.get("file1"), imageUrlMap.get("file2"), imageUrlMap.get("file3"));

        //        <====================================== Xử lý thông tin ===========================================>
        List<Attribute> attributeList = new CategoryDAO().getAttributeByCategoryID(Category);

        for (Attribute attr : attributeList) {
            String paramName = "attribute_" + attr.getAttributeID();
            String value = request.getParameter(paramName);

            if (value != null && !value.trim().isEmpty()) {
                // Kiểm tra xem ProductDetail đã tồn tại chưa
                ProductDetail existing = proDAO.getProductDetailByProductAndAttribute(productId, attr.getAttributeID());

                if (existing != null) {
                    // Update nếu có
                    res = proDAO.updateProductDetail(existing.getProductDetailID(), value);
                } else {
                    // Insert nếu chưa có
                    res = proDAO.insertProductDetail(productId, attr.getAttributeID(), value);
                }
            }
        }

        if (res) {
            response.sendRedirect("AdminProduct?updatesuccess=1");
        } else {
            response.sendRedirect("AdminUpdateProduct?productId=" + productId + "&error=1");
        }

        //        <====================================== Xử lý thông tin ===========================================>
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
