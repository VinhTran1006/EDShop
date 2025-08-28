/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ImportStockDetailDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import model.Order;
import model.OrderDetail;

/**
 *
 * @author DONG QUOC
 */
public class UpdateOrderStatusAdmin extends HttpServlet {

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
            out.println("<title>Servlet UpdateOrderStatusAdmin</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateOrderStatusAdmin at " + request.getContextPath() + "</h1>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("update");
        String orderID = request.getParameter("orderID");

        try {
            OrderDAO oDAO = new OrderDAO();
            OrderDetailDAO odDAO = new OrderDetailDAO();

            if (status != null && orderID != null) {
                int orderIdInt = Integer.parseInt(orderID);

                // LẤY ORDER TỪ DB TRƯỚC
                Order order = oDAO.getOrderByID(orderID);
                String currentStatus = order.getStatus(); // trạng thái trước update

                if ("Waiting".equals(currentStatus)) {
                    // Chỉ update, không hoàn stock
                    oDAO.updateStatus(orderIdInt, "Cancelled");

                } else if ("Packing".equals(currentStatus)) {
                    // Update
                    oDAO.updateStatus(orderIdInt, "Cancelled");

                    // Hoàn stock cả tổng và batch
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
                    // Các trạng thái khác: không cho hủy
                    response.sendRedirect("ViewOrderOfCustomer?error=not-cancelable");
                    return;
                }

                // Xử lý khi chuyển sang Packing (trừ kho FIFO, lưu batch)
                if ("packing".equalsIgnoreCase(status)) {
                    List<OrderDetail> list = odDAO.getOrderDetail(orderID);

                    ImportStockDetailDAO sDetailDAO = new ImportStockDetailDAO();
                    ProductDAO pDAO = new ProductDAO();

                    for (OrderDetail od : list) {
                        List<int[]> deductedList = sDetailDAO.deductStockFIFO(od.getProductID(), od.getQuantity());

                        if (deductedList != null && !deductedList.isEmpty()) {
                            od.setImportDetailBatch(deductedList);

                            String batchStr = deductedList.stream()
                                    .map(arr -> arr[0] + ":" + arr[1])
                                    .collect(Collectors.joining(", ", "[", "]"));

                            boolean updated = odDAO.updateImportDetailBatch(od.getOrderDetailsID(), batchStr);
                            if (!updated) {
                                System.err.println("Failed to update importDetailBatch for OrderDetailID: " + od.getOrderDetailsID());
                            }
                            System.out.println("Batch details saved: " + batchStr);
                        }
                    }

                    // ✅ Thành công
                    response.sendRedirect(request.getContextPath() + "/ViewOrderList?success=update");
                } else {
                    // ❌ Không update được
                    response.sendRedirect(request.getContextPath() + "/ViewOrderList?error=update-failed");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/ViewOrderList?error=invalid-params");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(); // In log server
            response.sendRedirect(request.getContextPath() + "/ViewOrderList?error=invalid-orderid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ViewOrderList?error=exception");
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
