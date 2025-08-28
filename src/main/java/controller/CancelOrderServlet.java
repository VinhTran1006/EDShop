/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ImportStockDetailDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Customer;

import model.Order;
import model.OrderDetail;

/**
 *
 * @author VinhNTCE181630
 */
@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/CancelOrder"})
public class CancelOrderServlet extends HttpServlet {

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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

//        if (customer == null) {
//
//            response.sendRedirect("Login");
//            return;
//        }
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
        processRequest(request, response);
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            response.sendRedirect("Login");
            return;
        }

        int orderID = Integer.parseInt(request.getParameter("orderID"));
        String oID = request.getParameter("orderID");
        OrderDAO dao = new OrderDAO();
        Order order = dao.getOrderByID(orderID + "");

        if ("Waiting".equals(order.getStatus())) {
            // Waiting → chưa trừ stock → chỉ đổi trạng thái
            dao.updateStatus(orderID, "Cancelled");
            response.sendRedirect("ViewOrderOfCustomer?success=cancel");

        } else if ("Packing".equals(order.getStatus())) {
            // Packing → đã trừ stock → đổi trạng thái và trả lại stock
            dao.updateStatus(orderID, "Cancelled");

            // --- gọi lại hàm trả stock ---
            OrderDetailDAO odDAO = new OrderDetailDAO();
            ProductDAO pDAO = new ProductDAO();
            ImportStockDetailDAO sDetailDAO = new ImportStockDetailDAO();

            List<OrderDetail> list = odDAO.getOrderDetail(oID);
            for (OrderDetail od : list) {
                // 1. Trả lại tổng số lượng
                pDAO.increaseStock(od.getProductID(), od.getQuantity());

                // 2. Trả lại số lượng từng lô
                List<int[]> batchList = od.getImportDetailBatch();
                if (batchList != null && !batchList.isEmpty()) {
                    sDetailDAO.increaseStockBack(batchList);
                }
            }

            response.sendRedirect("ViewOrderOfCustomer?success=cancel");

        } else {
            // Các trạng thái khác thì không hủy được
            response.sendRedirect("ViewOrderOfCustomer?error=not-cancelable");
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
