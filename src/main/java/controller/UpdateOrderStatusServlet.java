/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.ProductDAO;
import dao.ImportStockDetailDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;
import model.OrderDetail;
import java.util.stream.Collectors;

/**
 *
 * @author VinhNTCE181630
 */
@WebServlet(name = "UpdateOrderStatusServlet", urlPatterns = {"/UpdateOrder"})
public class UpdateOrderStatusServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateOrderStatus</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateOrderStatus at " + request.getContextPath() + "</h1>");
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
        String orderID = request.getParameter("orderID");
        OrderDAO oDAO = new OrderDAO();
        Order o = oDAO.getOrderByID(orderID);

        OrderDetailDAO odDAO = new OrderDetailDAO();
        List<OrderDetail> list = odDAO.getOrderDetail(orderID);
        if (o != null) {
            request.setAttribute("dataDetail", list);
            request.setAttribute("data", o);
            request.getRequestDispatcher("/WEB-INF/View/staff/orderManagement/orderDetailsView.jsp").forward(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/ViewOrderListServlet");
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
        String status = request.getParameter("update");  // trạng thái muốn chuyển tới
        String orderID = request.getParameter("orderID");

        try {
            OrderDAO oDAO = new OrderDAO();
            OrderDetailDAO odDAO = new OrderDetailDAO();

            if (status != null && orderID != null) {
                int orderIdInt = Integer.parseInt(orderID);

                // LẤY ORDER TỪ DB TRƯỚC
                Order order = oDAO.getOrderByID(orderID);
                String currentStatus = order.getStatus(); // trạng thái trước update

                // ======================
                // 1. XỬ LÝ HỦY ĐƠN
                // ======================
                if ("Cancelled".equalsIgnoreCase(status)) {
                    if ("Waiting".equals(currentStatus)) {
                        // Waiting → Cancelled (không hoàn stock)
                        oDAO.updateStatus(orderIdInt, "Cancelled");

                    } else if ("Packing".equals(currentStatus)) {
                        // Packing → Cancelled (hoàn stock)
                        oDAO.updateStatus(orderIdInt, "Cancelled");

                        List<OrderDetail> list = odDAO.getOrderDetail(orderID);
                        ProductDAO pDAO = new ProductDAO();
                        ImportStockDetailDAO sDetailDAO = new ImportStockDetailDAO();

                        for (OrderDetail od : list) {
                            // 1. Trả lại tổng số lượng
                            pDAO.increaseStock(od.getProductID(), od.getQuantity());
                            // 2. Trả lại theo batch
                            List<int[]> batchList = od.getImportDetailBatch();
                            if (batchList != null && !batchList.isEmpty()) {
                                sDetailDAO.increaseStockBack(batchList);
                            }
                        }
                    } else {
                        // Các trạng thái khác không cho cancel
                        response.sendRedirect("ViewOrderList?error=not-cancelable");
                        return;
                    }

                    response.sendRedirect("ViewOrderList?success=update");
                    return;
                }

                // ======================
                // 2. XỬ LÝ NEXT (CHUYỂN TRẠNG THÁI TIẾN)
                // ======================
                if ("Packing".equalsIgnoreCase(status) && "Waiting".equals(currentStatus)) {
                    // Waiting → Packing (trừ stock FIFO)
                    oDAO.updateStatus(orderIdInt, "Packing");

                    List<OrderDetail> list = odDAO.getOrderDetail(orderID);
                    ImportStockDetailDAO sDetailDAO = new ImportStockDetailDAO();

                    for (OrderDetail od : list) {
                        List<int[]> deductedList = sDetailDAO.deductStockFIFO(od.getProductID(), od.getQuantity());
                        if (deductedList != null && !deductedList.isEmpty()) {
                            String batchStr = deductedList.stream()
                                    .map(arr -> arr[0] + ":" + arr[1])
                                    .collect(Collectors.joining(", ", "[", "]"));
                            odDAO.updateImportDetailBatch(od.getOrderDetailsID(), batchStr);
                        }
                    }

                } else if ("Waiting for Delivery".equalsIgnoreCase(status) && "Packing".equals(currentStatus)) {
                    // Packing → Waiting for Delivery
                    oDAO.updateStatus(orderIdInt, "Waiting for Delivery");

                } else if ("Delivered".equalsIgnoreCase(status) && "Waiting for Delivery".equals(currentStatus)) {
                    // Waiting for Delivery → Delivered
                    oDAO.updateStatus(orderIdInt, "Delivered");

                } else {
                    response.sendRedirect("ViewOrderList?error=invalid-transition");
                    return;
                }

                // ✅ Thành công
                response.sendRedirect("ViewOrderList?success=update");

            } else {
                response.sendRedirect("ViewOrderList?error=invalid-params");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewOrderList?error=exception");
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
