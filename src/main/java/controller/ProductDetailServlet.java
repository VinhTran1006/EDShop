/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.BrandDAO;
import dao.CategoryDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Product;
import model.ProductDetail;
import dao.ProductRatingDAO;
import dao.CustomerDAO;
import dao.RatingRepliesDAO;
import java.util.ArrayList;
import model.Attribute;


/**
 *
 *
 */
@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/ProductDetail"})
public class ProductDetailServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet ProductDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailServlet at " + request.getContextPath() + "</h1>");
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

        String productIdStr = request.getParameter("productId");
        String categoryIdStr = request.getParameter("categoryId");

        if (productIdStr != null && categoryIdStr != null) {
            CategoryDAO cateDao = new CategoryDAO();
            ProductDAO proDao = new ProductDAO();
            BrandDAO brDAO = new BrandDAO();

            int productId = Integer.parseInt(productIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);

            // Lấy thông tin chi tiết sản phẩm
            List<Attribute> attributeList = cateDao.getAttributeByCategoryID(categoryId);
            Product product = proDao.getProductByID(productId);
            List<ProductDetail> productDetailList = proDao.getProductDetailByProductId(productId);

            // Lấy danh sách đánh giá và phản hồi
            ProductRatingDAO ratingDAO = new ProductRatingDAO();
            CustomerDAO customerDAO = new CustomerDAO();
            RatingRepliesDAO repliesDAO = new RatingRepliesDAO();
            String brandName = brDAO.getBrandNameByBrandId(product.getBrandID());
           // List<ProductRating> productRatings = ratingDAO.getProductRatingsByProductId(productId);
            double totalStars = 0;
            int visibleRatingCount = 0;

//            for (ProductRating rating : productRatings) {
//                if (!rating.isIsDeleted()) {  // <-- chỉ tính đánh giá chưa bị ẩn
//                    totalStars += rating.getStar();
//                    visibleRatingCount++;
//                }
//
//                Customer customer = customerDAO.getCustomerbyID(rating.getCustomerID());
//                rating.setFullName(customer.getFullName());
//
//                List<RatingReplies> replies = repliesDAO.getAllRatingRepliesByRateID(rating.getRateID());
//                if (replies == null) {
//                    replies = new ArrayList<>();
//                }
//                rating.setReplies(replies);
//            }

//            double averageRating = 0;
//            if (visibleRatingCount > 0) {
//                averageRating = totalStars / visibleRatingCount;
//            }
//            request.setAttribute("averageRating", averageRating);

            // Truyền dữ liệu sang JSP
            request.setAttribute("product", product);
            request.setAttribute("brandName", brandName);
            request.setAttribute("attributeList", attributeList);
            request.setAttribute("productDetailList", productDetailList);
 //           List<ProductRating> visibleRatings = new ArrayList<>();
//            for (ProductRating rating : productRatings) {
//                if (!rating.isIsDeleted()) {
//                    visibleRatings.add(rating);
//                }
//            }
//            request.setAttribute("productRatings", visibleRatings);

            request.getRequestDispatcher("/WEB-INF/View/customer/productManagement/productDetail/productDetail.jsp").forward(request, response);
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
        processRequest(request, response);
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
