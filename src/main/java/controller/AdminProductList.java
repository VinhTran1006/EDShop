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
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Product;
import model.Brand;

/**
 *
 *
 */
@WebServlet(name = "StaffProductList", urlPatterns = {"/AdminProduct"})
public class AdminProductList extends HttpServlet {

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
            out.println("<title>Servlet StaffProductList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StaffProductList at " + request.getContextPath() + "</h1>");
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
        CategoryDAO cateDAO = new CategoryDAO();
        BrandDAO brandDAO = new BrandDAO();
        String filter = request.getParameter("filter");
        List<Product> productList = new ArrayList<>();
        List<Category> cateList = new ArrayList<>();
        List<Brand> brandList = new ArrayList<>();

        if (filter == null || filter.equals("All")) {
            productList = proDAO.getProductListAdmin();
        } else if (filter.equals("Featured")) {
            productList = proDAO.getProductIsFeaturedAdmin();
        } else if (filter.equals("Bestseller")) {
            productList = proDAO.getProductIsBestSellerAdmin();
        } else if (filter.equals("New")) {
            productList = proDAO.getProductIsNewAdmin();
        } 
        cateList = cateDAO.getAllCategory();
        brandList = brandDAO.getAllBrand();
        String keyword = request.getParameter("keyword");
        if (keyword != null) {
            productList = proDAO.getProductByKeyword(keyword);
        }

        request.setAttribute("productList", productList);
        request.setAttribute("cateList", cateList);
        request.setAttribute("brandList", brandList);
        request.setAttribute("selectedFilter", filter);
        request.getRequestDispatcher("/WEB-INF/View/admin/productManagement/viewProductList/adminProductList.jsp").forward(request, response);

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
