/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ImportStockDAO;
import dao.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.ImportStock;
import model.Suppliers;

/**
 *
 * @author HP
 */
@WebServlet(name = "ViewImportStockServlet", urlPatterns = {"/ImportStockHistory"})
public class ViewImportStockServlet extends HttpServlet {

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
            out.println("<title>Servlet ViewImportStockServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewImportStockServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    ImportStockDAO importStockDAO = new ImportStockDAO();
    SupplierDAO supplierDAO = new SupplierDAO();

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

        String fromRaw = request.getParameter("from"); // yyyy-MM-dd
        String toRaw = request.getParameter("to");     // yyyy-MM-dd
        String supplierIdRaw = request.getParameter("supplierId");

        Integer supplierId = null;
        try {
            if (supplierIdRaw != null && !supplierIdRaw.isEmpty()) {
                supplierId = Integer.parseInt(supplierIdRaw);
            }
        } catch (NumberFormatException e) {
            supplierId = null;
        }

        Timestamp from = null;
        Timestamp to = null;
        try {
            if (fromRaw != null && !fromRaw.isEmpty()) {
                from = Timestamp.valueOf(fromRaw + " 00:00:00");
            }
            if (toRaw != null && !toRaw.isEmpty()) {
                to = Timestamp.valueOf(toRaw + " 23:59:59");
            }
        } catch (IllegalArgumentException e) {
        }

        List<ImportStock> history = new ArrayList<>();
        try {
            history = importStockDAO.getImportHistoryFiltered(
                    from != null ? from : new Timestamp(System.currentTimeMillis()),
                    to != null ? to : new Timestamp(System.currentTimeMillis()),
                    supplierId
            );
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi lấy dữ liệu nhập kho: " + e.getMessage());
        }

        List<Suppliers> suppliers = supplierDAO.getAllActiveSuppliers();

        request.setAttribute("importHistory", history);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("from", fromRaw);
        request.setAttribute("to", toRaw);
        request.setAttribute("supplierId", supplierId);

        request.getRequestDispatcher("/WEB-INF/View/staff/stockManagement/importStockList.jsp")
                .forward(request, response);
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
