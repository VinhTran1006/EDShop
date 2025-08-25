/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductFeedbackDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductFeedback;

/**
 *
 * @author DONG QUOC
 */
public class UpdateReplyAdmin extends HttpServlet {

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
            out.println("<title>Servlet UpdateReplyAdmin</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateReplyAdmin at " + request.getContextPath() + "</h1>");
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
        ProductFeedbackDAO rrDAO = new ProductFeedbackDAO();
        String answerUpdate = request.getParameter("answer");
        String replyID_raw = request.getParameter("feedbackID");
        System.out.println("UpdateReplyServlet received: replyID = " + replyID_raw + ", answer = " + answerUpdate);

        try {
            int replyID = Integer.parseInt(replyID_raw);
            if (replyID > 0 && answerUpdate != null && !answerUpdate.trim().isEmpty()) {
                ProductFeedback reply = rrDAO.getReplyByFeedbackID(replyID);
                if (reply == null) {
                    System.out.println("Reply not found for ID = " + replyID);
                    response.getWriter().write("Failed");
                    return;
                }
                int result = rrDAO.UpdateReply(reply, answerUpdate);
                System.out.println("Update result: " + result);
                if (result > 0) {
                    response.getWriter().write("Success");
                } else {
                    response.getWriter().write("Failed");
                }
            } else {
                response.getWriter().write("Invalid Input");
                System.out.println("Invalid input detected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
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
