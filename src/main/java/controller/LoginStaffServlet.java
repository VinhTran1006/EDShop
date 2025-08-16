/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.StaffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Staff;

/**
 *
 * @author pc
 */
@WebServlet(name = "LoginStaffServlet", urlPatterns = {"/LoginStaff"})
public class LoginStaffServlet extends HttpServlet {

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
            out.println("<title>Servlet LoginStaffServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginStaffServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("WEB-INF/View/account/login-staff.jsp").forward(request, response);
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
        String email = request.getParameter("email");
String pass = request.getParameter("pass");

AccountDAO dao = new AccountDAO();
StaffDAO staffDAO = new StaffDAO();
HttpSession session = request.getSession();

Account acc = dao.verifyMD5(email, pass);
if (acc != null) {
    if ("Staff".equalsIgnoreCase(acc.getRole())) {
        // Lấy Staff theo AccountID
        Staff staff = staffDAO.getStaffByAccountId(acc.getAccountId());

        // Lưu vào session
        session.setAttribute("user", acc);
        session.setAttribute("staff", staff);
        session.setAttribute("role", acc.getRole());

        // --- LOG detail ---
        System.out.println("=== LOGIN STAFF SUCCESS ===");
        System.out.println("Account ID: " + acc.getAccountId());
        System.out.println("Email: " + acc.getEmail());
        System.out.println("Role: " + acc.getRole());
        System.out.println("Staff ID: " + staff.getStaffId());
        System.out.println("Full Name: " + staff.getFullName());
        System.out.println("Phone: " + staff.getPhoneNumber());
        System.out.println("Position: " + staff.getPosition());
        System.out.println("==========================");
        // Log detail
        
        
        response.sendRedirect("StaffDashboard");
    } else {
        request.setAttribute("err", "<p style='color:red'>You do not have permission to access this page.</p>");
        request.getRequestDispatcher("WEB-INF/View/account/login-staff.jsp").forward(request, response);
    }
} else {
    request.setAttribute("err", "<p style='color:red'>Email or password invalid</p>");
    request.getRequestDispatcher("WEB-INF/View/account/login-staff.jsp").forward(request, response);
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
