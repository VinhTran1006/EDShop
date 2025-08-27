/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.EmailService;
import utils.OTPManager;

/**
 *
 * @author pc
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/ForgotPassword"})
public class ForgotPasswordServlet extends HttpServlet {

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
            out.println("<title>Servlet ForgotPasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ForgotPasswordServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("WEB-INF/View/account/forgot-password.jsp").forward(request, response);
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
        String email = request.getParameter("email").trim();
        HttpSession session = request.getSession();
        AccountDAO dao = new AccountDAO();

        // Kiểm tra email có tồn tại không
        if (!dao.checkEmailExisted(email)) {
            request.setAttribute("error", "Email does not exist in the system.");
            request.getRequestDispatcher("WEB-INF/View/account/forgot-password.jsp").forward(request, response);
            return;
        }

        // Tạo OTP mới
        int otpCode = EmailService.generateVerificationCode();
        OTPManager otpManager = new OTPManager(otpCode, 5); // hết hạn sau 5 phút
        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000);
        session.setAttribute("resetOtpExpiryTime", expiryTime);
        session.setAttribute("resetOtpManager", otpManager);

        // Gửi OTP qua email
        boolean emailSent = EmailService.sendOTPEmail(email, otpCode, "RESET_PASSWORD");
        if (!emailSent) {
            request.setAttribute("error", "Failed to send OTP email. Please try again later.");
            request.getRequestDispatcher("WEB-INF/View/account/forgot-password.jsp").forward(request, response);
            return;
        }

        // Lưu OTP và mục đích vào session
        session.setAttribute("otpManager", otpManager);
        session.setAttribute("otpPurpose", "forgot");
        session.setAttribute("resetEmail", email);

        // Điều hướng đến trang xác minh OTP
        response.sendRedirect("Verify");
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
