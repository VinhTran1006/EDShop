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
                int count = oDAO.updateOrder(Integer.parseInt(orderID), status);

                // ✅ Nếu status = cancel thì hoàn kho
                    if ("Cancelled".equalsIgnoreCase(status)) {
                        List<OrderDetail> list = odDAO.getOrderDetail(orderID);
                        ProductDAO pDAO = new ProductDAO();
                        ImportStockDetailDAO sDetailDAO = new ImportStockDetailDAO();

                        System.out.println("Cancelling order: " + orderID + ", total items: " + list.size());

                        for (OrderDetail od : list) {
                            System.out.println("OrderDetailID: " + od.getOrderDetailsID());
                            System.out.println("ProductID: " + od.getProductID());
                            System.out.println("Quantity to return: " + od.getQuantity());

                            // 1. Trả lại tổng số lượng sản phẩm
                            try {
                                pDAO.increaseStock(od.getProductID(), od.getQuantity());
                                System.out.println("Product stock increased successfully for ProductID: " + od.getProductID());
                            } catch (Exception e) {
                                System.err.println("Failed to increase product stock for ProductID: " + od.getProductID());
                                e.printStackTrace();
                            }

                            // 2. Trả lại số lượng từng lô
                            List<int[]> batchList = od.getImportDetailBatch();
                            if (batchList != null && !batchList.isEmpty()) {
                                System.out.println("Returning stock to batches for OrderDetailID: " + od.getOrderDetailsID());
                                for (int[] pair : batchList) {
                                    System.out.println("Batch ImportStockDetailsID: " + pair[0] + ", quantity: " + pair[1]);
                                }

                                boolean ok = sDetailDAO.increaseStockBack(batchList);
                                if (!ok) {
                                    System.err.println("Failed to rollback stock for OrderDetailID: " + od.getOrderDetailsID());
                                } else {
                                    System.out.println("Batch stock rollback successful for OrderDetailID: " + od.getOrderDetailsID());
                                }
                            } else {
                                System.out.println("No batch stock to rollback for OrderDetailID: " + od.getOrderDetailsID());
                            }
                        }

                        System.out.println("Order cancellation process completed for orderID: " + orderID);
                    }

                    if ("packing".equalsIgnoreCase(status)) {
                        List<OrderDetail> list = odDAO.getOrderDetail(orderID);

                        // Tạo DAO để update stock
                        dao.ImportStockDetailDAO sDetailDAO = new dao.ImportStockDetailDAO();
                        dao.ProductDAO pDAO = new dao.ProductDAO();
                        for (OrderDetail od : list) {
                            System.out.println("ProductID: " + od.getProductID());
                            System.out.println("Quantity: " + od.getQuantity());

                            // 1. Trừ kho theo FIFO và nhận về danh sách [ImportStockDetailsID, quantityDeducted]
                            List<int[]> deductedList = sDetailDAO.deductStockFIFO(od.getProductID(), od.getQuantity());

                            if (deductedList != null && !deductedList.isEmpty()) {
                                // 2. Lưu toàn bộ chi tiết lô đã trừ vào object
                                od.setImportDetailBatch(deductedList);

                                // 3. Chuyển sang chuỗi dễ đọc để lưu DB
                                String batchStr = deductedList.stream()
                                        .map(arr -> arr[0] + ":" + arr[1])
                                        .collect(Collectors.joining(", ", "[", "]"));

                                // 4. Ghi vào DB dựa trên orderDetailsID
                                boolean updated = odDAO.updateImportDetailBatch(od.getOrderDetailsID(), batchStr);
                                if (!updated) {
                                    System.err.println("Failed to update importDetailBatch for OrderDetailID: " + od.getOrderDetailsID());
                                }

                                // 5. In ra log
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
