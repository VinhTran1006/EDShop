/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductFeedbackDAO;
import model.ProductFeedback;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 *
 */
@WebServlet(name = "UpdateReplyServlet", urlPatterns = {"/UpdateReply"})
public class UpdateReplyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductFeedbackDAO rrDAO = new ProductFeedbackDAO();
        String answerUpdate = request.getParameter("answer");
        String feedbackID_raw = request.getParameter("feedbackID");
        System.out.println("UpdateReplyServlet received: feedbackID = " + feedbackID_raw + ", answer = " + answerUpdate);

        try {
            int feedbackID = Integer.parseInt(feedbackID_raw);

            if (feedbackID > 0 && answerUpdate != null && !answerUpdate.trim().isEmpty()) {
                // Tạo đối tượng feedback chỉ để truyền ID
                ProductFeedback fb = new ProductFeedback();
                fb.setFeedbackID(feedbackID);

                int result = rrDAO.UpdateReply(fb, answerUpdate);
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
}
