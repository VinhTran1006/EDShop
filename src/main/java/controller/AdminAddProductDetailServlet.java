/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import dao.CategoryDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Attribute;
import model.CategoryDetail;
import model.CategoryDetailGroup;
import model.Product;
import model.ProductDetail;

/**
 *
 *
 */
@MultipartConfig

@WebServlet(name = "AdminAddProductDetailServlet", urlPatterns = {"/AdminAddProductDetail"})
public class AdminAddProductDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet AdminAddProductDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminAddProductDetailServlet at " + request.getContextPath() + "</h1>");
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

        ProductDAO proDAO = new ProductDAO();
        int productId = Integer.parseInt(request.getParameter("productId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Product product = proDAO.getProductByID(productId);
        request.setAttribute("product", product);

        CategoryDAO cateDAO = new CategoryDAO();
        List<Attribute> attributes = cateDAO.getAttributeByCategoryID(categoryId);
        request.setAttribute("attributes", attributes);
        request.setAttribute("categoryId", categoryId);
        request.getRequestDispatcher("/WEB-INF/View/admin/productManagement/addProduct/addProductDetail/adminAddProductDetail.jsp").forward(request, response);
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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        boolean checkInsertValue = false;
        String categoryIdStr = request.getParameter("categoryId");
        String productIdStr = request.getParameter("productId");
        if (categoryIdStr != null && productIdStr != null) {
            int categoryId = Integer.parseInt(categoryIdStr);
            int productId = Integer.parseInt(productIdStr);
            ProductDAO proDAO = new ProductDAO();
            CategoryDAO cateDAO = new CategoryDAO();
            List<Attribute> AttributeList = cateDAO.getAttributeByCategoryID(categoryId);

            for (Attribute a : AttributeList) {
                String paramName = "attribute_" + a.getAttributeID();

                String value = request.getParameter(paramName);

                if (value != null && !value.trim().isEmpty()) {
                    // Cập nhật lại DB
                    checkInsertValue = proDAO.insertProductDetail(productId, a.getAttributeID(), value);
                }

            }
            if (checkInsertValue) {
                response.sendRedirect("AdminCreateProduct?success=1");
            } else {
                response.sendRedirect("AdminCreateProduct?error=1");

            }
        }
    }
}
