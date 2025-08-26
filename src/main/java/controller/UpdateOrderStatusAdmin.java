/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws ServletException, IOException {
        String orderID = request.getParameter("orderID");
        OrderDAO oDAO = new OrderDAO();
        Order o = oDAO.getOrderByID(orderID);

        OrderDetailDAO odDAO = new OrderDetailDAO();
        List<OrderDetail> list = odDAO.getOrderDetail(orderID);
        if (o != null) {
            request.setAttribute("dataDetail", list);
            request.setAttribute("data", o);
            request.getRequestDispatcher("/WEB-INF/View/admin/orderManagement/orderDetailsView.jsp").forward(request, response);

        } else {
            response.sendRedirect(request.getContextPath() + "/ViewOrderListServletAdmin");
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
protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
        throws ServletException, IOException {
    String status = request.getParameter("update");
    String orderID = request.getParameter("orderID");

    try {
        OrderDAO oDAO = new OrderDAO();
OrderDetailDAO odDAO = new OrderDetailDAO();

        if (status != null && orderID != null) {
            int count = oDAO.updateOrder(Integer.parseInt(orderID), status);

            if (count > 0) {
                // ✅ Nếu status = cancel thì hoàn kho
                if ("Cancelled".equalsIgnoreCase(status)) {
                    List<OrderDetail> list = odDAO.getOrderDetail(orderID);

                    // Tạo ProductDAO để update stock
                    dao.ProductDAO pDAO = new dao.ProductDAO();

                    for (OrderDetail od : list) {
                        pDAO.increaseStock(od.getProductID(), od.getQuantity());
                    }
                }
                // ✅ Thành công
                response.sendRedirect(request.getContextPath() + "/ViewOrderListServletAdmin?success=update");
            } else {
                // ❌ Không update được
                response.sendRedirect(request.getContextPath() + "/ViewOrderListServletAdmin?error=update-failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/ViewOrderListServletAdmin?error=invalid-params");
        }

    } catch (NumberFormatException e) {
        e.printStackTrace(); // In log server
        response.sendRedirect(request.getContextPath() + "/ViewOrderListServletAdmin?error=invalid-orderid");
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect(request.getContextPath() + "/ViewOrderListServletAdmin?error=exception");
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
