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
import model.Staff;

/**
 *
 * @author pc
 */
@WebServlet(name = "LoginStaffServlet", urlPatterns = {"/LoginStaff"})
public class LoginStaffServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("pass");

        AccountDAO dao = new AccountDAO();
        HttpSession session = request.getSession();
        Staff staff = dao.verifyStaff(email, pass);

        String clientIp = request.getRemoteAddr();
        String loginTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date());

        if (staff != null) {
            if (!staff.isActive()) {
                request.setAttribute("err", "<p style='color:red'>Your account is inactive.</p>");
                request.getRequestDispatcher("WEB-INF/View/account/login-staff.jsp").forward(request, response);
                return;
            }

            // Lưu staff vào session
            session.setAttribute("user", staff);
            session.setAttribute("role", staff.getRole());

            // Log thông tin login
            System.out.println("========== [LOGIN SUCCESS] ==========");
            System.out.println("Time: " + loginTime);
            System.out.println("IP: " + clientIp);
            System.out.println("Email: " + staff.getEmail());
            System.out.println("FullName: " + staff.getFullName());
            System.out.println("Role: " + staff.getRole());
            System.out.println("IsActive: " + staff.isActive());

            // Log thông tin session
            System.out.println("---------- [SESSION INFO] ----------");
            System.out.println("SessionID: " + session.getId());
            System.out.println("CreatedAt: " + new java.util.Date(session.getCreationTime()));
            System.out.println("LastAccessed: " + new java.util.Date(session.getLastAccessedTime()));
            System.out.println("User in session: " + session.getAttribute("user"));
            System.out.println("Role in session: " + session.getAttribute("role"));
            System.out.println("====================================");

            if ("admin".equalsIgnoreCase(staff.getRole())) {
                response.sendRedirect("StaffDashboard");
            } else if ("staff".equalsIgnoreCase(staff.getRole())) {
                response.sendRedirect("StaffDashboard");
            } else {
                request.setAttribute("err", "<p style='color:red'>You do not have permission to access this page.</p>");
                request.getRequestDispatcher("WEB-INF/View/account/login-staff.jsp").forward(request, response);
            }
        } else {
            System.out.println("========== [LOGIN FAILED] ==========");
            System.out.println("Time: " + loginTime);
            System.out.println("IP: " + clientIp);
            System.out.println("Email: " + email + " nhập sai mật khẩu");
            System.out.println("====================================");

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
