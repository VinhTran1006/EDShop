/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
import dao.ProfileDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;

import model.Customer;

/**
 *
 * @author pc
 */
@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/UpdateProfile"})
public class UpdateProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProfileServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();

        Customer cus = (Customer) session.getAttribute("user");

        // Lấy flash data (temp...) nếu có
        if (session.getAttribute("tempFullName") != null) {
            request.setAttribute("tempFullName", session.getAttribute("tempFullName"));
            session.removeAttribute("tempFullName");
        }
        if (session.getAttribute("tempPhone") != null) {
            request.setAttribute("tempPhone", session.getAttribute("tempPhone"));
            session.removeAttribute("tempPhone");
        }
        if (session.getAttribute("tempDob") != null) {
            request.setAttribute("tempDob", session.getAttribute("tempDob"));
            session.removeAttribute("tempDob");
        }
        if (session.getAttribute("tempGender") != null) {
            request.setAttribute("tempGender", session.getAttribute("tempGender"));
            session.removeAttribute("tempGender");
        }
        request.setAttribute("cus", cus);
        request.getRequestDispatcher("/WEB-INF/View/customer/profile/update-profile.jsp").forward(request, response);

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
        HttpSession session = request.getSession();
        CustomerDAO cusDao = new CustomerDAO();
        ProfileDAO dao = new ProfileDAO();

        int id = Integer.parseInt(request.getParameter("id"));
        String fullName = request.getParameter("fullname").trim();
        String phone = request.getParameter("phone").trim();
        String dob = request.getParameter("dob").trim();
        Date birthDate = Date.valueOf(dob);
        String gender = request.getParameter("gender");

        String namePattern = "^[\\p{L}\\s]{2,50}$";
        String phonePattern = "^0\\d{9}$";

        // Validate Full Name
        if (!fullName.matches(namePattern)) {
            setFlashError(session,
                    "Full name must be 2-50 letters, spaces allowed, no numbers or special characters.",
                    fullName, phone, dob, gender);
            response.sendRedirect("UpdateProfile");
            return;
        }      
        
        // Validate Phone
        if (!phone.matches(phonePattern)) {
            setFlashError(session,
                    "Phone number must start with 0 and be exactly 10 digits.",
                    fullName, phone, dob, gender);
            response.sendRedirect("UpdateProfile");
            return;
        }

        Customer currentCus = cusDao.getCustomerById(id);

        if (!phone.equals(currentCus.getPhoneNumber())) {
            if (dao.isPhoneExists(phone, id)) {
                setFlashError(session,
                        "Phone number already exists, please use another one.",
                        fullName, phone, dob, gender);
                response.sendRedirect("UpdateProfile");
                return;
            }
        }

        boolean success = dao.updateProfileCustomer(id, fullName, phone, birthDate, gender);

        if (success) {
            Customer updatedCus = cusDao.getCustomerById(id);
            session.setAttribute("user", updatedCus); // cập nhật lại session
            response.sendRedirect("ViewProfile");
        } else {
            Customer cus = cusDao.getCustomerById(id);
            request.setAttribute("cus", cus);
            session.setAttribute("error", "Update failed!");
            request.getRequestDispatcher("/WEB-INF/View/customer/profile/update-profile.jsp").forward(request, response);
        }
    }

    private void setFlashError(HttpSession session, String message,
            String fullName, String phone, String dob, String gender) {
        session.setAttribute("error", message);
        session.setAttribute("tempFullName", fullName);
        session.setAttribute("tempPhone", phone);
        session.setAttribute("tempDob", dob);
        session.setAttribute("tempGender", gender);
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
