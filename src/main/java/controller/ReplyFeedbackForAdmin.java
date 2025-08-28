/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductFeedbackDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Staff;

/**
 *
 * @author DONG QUOC
 */
public class ReplyFeedbackForAdmin extends HttpServlet {

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
            out.println("<title>Servlet ReplyFeedbackForAdmin</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReplyFeedbackForAdmin at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession(false); // không tạo session mới nếu mất
        Staff staff = (Staff) session.getAttribute("staff");
          System.out.println("qua servlet");
        if (staff != null) {
            System.out.println("Staff k empty");
            try {
                int feedbackID = Integer.parseInt(request.getParameter("feedbackID"));
                String answer = request.getParameter("answer");
                
                System.out.println("fb ID " + feedbackID);

                int stID = staff.getStaffID(); // Lấy trực tiếp từ object Staff

                ProductFeedbackDAO rrDAO = new ProductFeedbackDAO();
                boolean success = rrDAO.addReply(stID, feedbackID, answer);
                rrDAO.updateisReadComment(feedbackID);
                if (success) {
                    response.sendRedirect("ViewFeedbackForAdmin?feedbackID=" + feedbackID + "&success=success");
                } else {
                    response.sendRedirect("ViewFeedbackForAdmin?feedbackID=" + feedbackID + "&success=failed");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("ViewFeedbackForAdmin?feedbackID=" + request.getParameter("feedbackID") + "&success=error");
            }
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
