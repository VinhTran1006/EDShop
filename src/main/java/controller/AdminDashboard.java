/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductDAO;
import dao.RevenueStatisticDAO;
import dao.StaffDAO;
import dao.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 *
 * 
 * @author HP
 */
@WebServlet(name = "AdminDashboard", urlPatterns = {"/AdminDashboard"})
public class AdminDashboard extends HttpServlet {



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
        StaffDAO staffDAO = new StaffDAO();
        ProductDAO productDAO = new ProductDAO();
        SupplierDAO supplierDAO = new SupplierDAO();
        RevenueStatisticDAO revenueDAO = new RevenueStatisticDAO();
        //int totalStaff = staffDAO.getTotalStaff();
        int totalProduct = productDAO.getTotalProducts();
       // int totalSupplier = supplierDAO.getTotalSuppliers();
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
      //  long monthlyRevenue = revenueDAO.getMonthlyRevenue(month, year);

        //request.setAttribute("totalStaff", totalStaff);
        request.setAttribute("totalProduct", totalProduct);
       // request.setAttribute("totalSupplier", totalSupplier);
       // request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.getRequestDispatcher("/WEB-INF/View/admin/adminDashboard.jsp").forward(request, response);
    }


}
