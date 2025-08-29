/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.AccountDAO;
import dao.AccountDAO;
import dao.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import model.Customer;
import model.Staff;
import utils.GoogleOAuthService;

/**
 *
 * @author pc
 */
@WebServlet(name = "LoginGoogleServlet", urlPatterns = {"/LoginGoogle"})
public class LoginGoogleServlet extends HttpServlet {

    String CLIENT_ID = System.getenv("GOOGLE_CLIENT_ID");
    String CLIENT_SECRET = System.getenv("GOOGLE_CLIENT_SECRET");
    private static final String REDIRECT_URI = "http://localhost:8080/EDShop/LoginGoogle";

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
            out.println("<title>Servlet LoginGoogleServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginGoogleServlet at " + request.getContextPath() + "</h1>");
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
        AccountDAO dao = new AccountDAO();
        CustomerDAO CusDao = new CustomerDAO();

        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }
        GoogleOAuthService googleService = new GoogleOAuthService(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        JsonObject jsonObject = googleService.getUserInfoFromCode(code);
        String email = jsonObject.get("email").getAsString();
        String name = jsonObject.get("name").getAsString();

        HttpSession session = request.getSession();
        session.setAttribute("email", email);
        session.setAttribute("name", name);

        boolean existed = dao.checkEmailExisted(email);

        if (!existed) {
            request.getRequestDispatcher("WEB-INF/View/account/save-phone.jsp").forward(request, response);
            return;
        }

        Object acc = dao.getAccountByEmail(email);
        boolean isActive = true;
        if (acc instanceof Customer) {
            isActive = ((Customer) acc).isActive();
        } else if (acc instanceof Staff) {
            isActive = ((Staff) acc).isActive();
        }

        if (!isActive) {
            session.setAttribute("errorMessage", "Your account is now locked and cannot be logged in!");
            response.sendRedirect("Login");
            return;
        }
        String role = dao.getRoleByEmail(email);
        session.setAttribute("user", acc);
        session.setAttribute("role", role);
//        session.setAttribute("accountId", acc.getAccountID());
//        
//        Customer cus = CusDao.getCustomerByAccountId(acc.getAccountID());
//        session.setAttribute("cus", cus);
//
//        if (role == 1) {
//            response.sendRedirect("AdminDashboard");
//        } else if (role == 2) {
//            response.sendRedirect("StaffDashboard");
//        } else {
//            response.sendRedirect("Home");
//        }
        if (acc instanceof Customer) {
            Customer c = (Customer) acc;
            // xử lý cho customer
            session.setAttribute("user", c);
            session.setAttribute("cus", c);
            response.sendRedirect("Home");
        } else if (acc instanceof Staff) {
            session.removeAttribute("user");
            session.setAttribute("errorMessage", "This account cannot log in because the role does not match the current page!");
            response.sendRedirect("Login");
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AccountDAO dao = new AccountDAO();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String name = (String) session.getAttribute("name");
        String phone = request.getParameter("phone");

        String phonePattern = "^\\d{10}$";

        if (email != null && phone != null && !phone.isEmpty()) {
            boolean existed = dao.checkEmailExisted(email);
            if (existed) {
                request.setAttribute("error", "Email already exists in the system.");
                request.getRequestDispatcher("WEB-INF/View/account/save-phone.jsp").forward(request, response);
                return;
            }

            // Validate phone
            if (!phone.matches(phonePattern)) {
                setFlashError(session,
                        "Phone number must be exactly 10 digits and contain only numbers.",
                        phone);
                request.getRequestDispatcher("WEB-INF/View/account/save-phone.jsp").forward(request, response);
                return;
            }

            // Validate phone unique
            if (dao.checkPhoneExisted(phone)) {
                setFlashError(session,
                        "This phone number is already registered.",
                        phone);
                request.getRequestDispatcher("WEB-INF/View/account/save-phone.jsp").forward(request, response);
                return;
            }

            // Tạo mới account Google
            dao.addNewAccountGoogle(email, name, phone); // sửa để có phone
            Object acc = dao.getAccountByEmail(email);
            String role = dao.getRoleByEmail(email);

            // Lưu session
            session.setAttribute("user", acc);
            session.setAttribute("role", role);
            session.setAttribute("cus", acc);
//            session.setAttribute("accountId", acc.getAccountID());

            response.sendRedirect("Home");
        } else {
            session.setAttribute("error", "Please enter phone number.");
            response.sendRedirect("SavePhone");
        }
    }

    private void setFlashError(HttpSession session, String message, String phone) {
        session.setAttribute("error", message);
        session.setAttribute("tempPhone", phone);
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
