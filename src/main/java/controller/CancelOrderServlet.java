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
        OrderDAO dao = new OrderDAO();
        Order order = dao.getOrderByID(String.valueOf(orderID));

        String currentStatus = order.getStatus();

        if ("Waiting".equals(currentStatus)) {
            // Waiting → Cancelled (không hoàn stock)
            dao.updateStatus(orderID, "Cancelled");
            response.sendRedirect("ViewOrderOfCustomer?success=cancel");

        } else if ("Packing".equals(currentStatus)) {
            // Packing → Cancelled (có hoàn stock)
            dao.updateStatus(orderID, "Cancelled");
            System.out.println("[CancelOrder] OrderID=" + orderID + " | Status: Packing → Cancelled");

            OrderDetailDAO itemDAO = new OrderDetailDAO();
            ProductDAO productDAO = new ProductDAO();
            ImportStockDetailDAO stockDetailDAO = new ImportStockDetailDAO();

            List<OrderDetail> items = itemDAO.getOrderDetailsByOrderID(orderID);

            for (OrderDetail item : items) {
                // 1. Trả lại tổng số lượng tồn kho cho Product
                productDAO.increaseStock(item.getProductID(), item.getQuantity());
                System.out.println("[CancelOrder] ProductID=" + item.getProductID()
                        + " | Qty=" + item.getQuantity()
                        + " → Returned to Product stock");

                // 2. Trả lại theo batch ImportStockDetails
                List<int[]> batchList = item.getImportDetailBatch();
                if (batchList != null && !batchList.isEmpty()) {
                    for (int[] batch : batchList) {
                        int importDetailID = batch[0];
                        int qty = batch[1];
                        System.out.println("[CancelOrder]   Batch importDetailID=" + importDetailID
                                + ", Qty=" + qty
                                + " → Returned to ImportStockDetails");
                    }
                    stockDetailDAO.increaseStockBack(batchList);
                } else {
                    System.out.println("[CancelOrder]   No batch info for ProductID=" + item.getProductID());
                }
            }

            response.sendRedirect("ViewOrderOfCustomer?success=cancel");

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
