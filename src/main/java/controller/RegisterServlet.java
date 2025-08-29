/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Category;
import utils.EmailService;
import utils.OTPManager;

/**
 *
 * @author pc
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/Register"})
public class RegisterServlet extends HttpServlet {

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
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
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
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categoryList = categoryDAO.getAllCategory(); // hoặc getAllCategory()
        request.setAttribute("categoryList", categoryList);

        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("tempPhone") != null) {
                request.setAttribute("tempPhone", session.getAttribute("tempPhone"));
            }
            if (session.getAttribute("tempEmail") != null) {
                request.setAttribute("tempEmail", session.getAttribute("tempEmail"));
            }
            if (session.getAttribute("tempFullName") != null) {
                request.setAttribute("tempFullName", session.getAttribute("tempFullName"));
            }
            if (session.getAttribute("error") != null) {
                request.setAttribute("error", session.getAttribute("error"));
                session.removeAttribute("error");
            }
        }
        request.getRequestDispatcher("WEB-INF/View/account/register.jsp").forward(request, response);
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
        AccountDAO dao = new AccountDAO();
        HttpSession session = request.getSession();
        String phone = request.getParameter("phone").trim();
        String email = request.getParameter("email").trim();
        String fullName = request.getParameter("fullName").trim();
        String password = request.getParameter("password").trim();
        String confirmPassword = request.getParameter("confirmPassword").trim();

        String passwordPattern = "^(?=.*@)[A-Z][A-Za-z0-9@]{7,254}$";
        String namePattern = "^[\\p{L}\\s]{2,50}$";
        String phonePattern = "^\\d{10}$";
        String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

        if (!fullName.matches(namePattern)) {
            setFlashError(session,
                    "Full name must be 2-50 letters, spaces allowed, no numbers or special characters.",
                    phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        // Validate phone
        if (!phone.matches(phonePattern)) {
            setFlashError(session,
                    "Phone number must be exactly 10 digits and contain only numbers.",
                    phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        // Validate phone unique
        if (dao.checkPhoneExisted(phone)) {
            setFlashError(session,
                    "This phone number is already registered.",
                    phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        // Validate email
        if (email.isEmpty()) {
            setFlashError(session, "Email cannot be empty.", phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }
        if (email.length() > 255) {
            setFlashError(session, "Email cannot be longer than 255 characters.", phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }
        if (!email.matches(emailPattern)) {
            setFlashError(session, "Invalid email format.", phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        if (!password.matches(passwordPattern)) {
            setFlashError(session, "Password must be 8–255 characters long, start with an uppercase letter, and contain at least one '@'.",
                    phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            setFlashError(session, "Password and Confirm Password do not match.", phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        if (dao.checkEmailExisted(email)) {
            setFlashError(session, "This email is already registered.", phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }
        // Gửi OTP có kiểm soát số lần và thời hạn
        int otpCode = EmailService.generateVerificationCode();
        boolean emailSent = EmailService.sendOTPEmail(email, otpCode, "REGISTER"); // Gửi đúng mã vừa tạo
        if (!emailSent) {
            setFlashError(session, "Failed to send OTP. You may have reached the resend limit (max 3 times).",
                    phone, fullName, email);
            request.getRequestDispatcher("/WEB-INF/View/account/register.jsp").forward(request, response);
            return;
        }

        OTPManager otpManager = new OTPManager(otpCode, 5);
        long expiryTime = System.currentTimeMillis() + (5 * 60 * 1000);
        session.setAttribute("registerOtpExpiryTime", expiryTime);
        session.setAttribute("otpManager", otpManager);
        session.setAttribute("otpPurpose", "register");

        // Lưu tạm thông tin người dùng chờ xác minh
        session.setAttribute("tempEmail", email);
        session.setAttribute("tempPassword", password);
        session.setAttribute("tempFullName", fullName);
        session.setAttribute("tempPhone", phone);

        response.sendRedirect("Verify");
    }

    private void setFlashError(HttpSession session, String message, String phone, String fullName, String email) {
        session.setAttribute("error", message);
        session.setAttribute("tempPhone", phone);
        session.setAttribute("tempFullName", fullName);
        session.setAttribute("tempEmail", email);
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
