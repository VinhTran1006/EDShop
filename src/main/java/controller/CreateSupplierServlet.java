/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.SupplierDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import model.Suppliers;

/**
 *
 * @author HP
 */
@WebServlet(name = "CreateSupplierServlet", urlPatterns = {"/CreateSupplier"})
public class CreateSupplierServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateSupplierServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateSupplierServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("/WEB-INF/View/admin/supplierManagement/createSupplier.jsp").forward(request, response);
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
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    String taxId = request.getParameter("taxId");
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String phoneNumber = request.getParameter("phoneNumber");
    String address = request.getParameter("address");
    String contactPerson = request.getParameter("contactPerson");
    String description = request.getParameter("description");
    boolean active = "1".equals(request.getParameter("activate"));

    String errorMsg = null;
    SupplierDAO dao = new SupplierDAO();

    // Kiểm tra trùng dữ liệu
    if (dao.isSupplierExist(taxId, email)) {
        errorMsg = "Tax ID or Email already exists!";
    } else if (dao.isSupplierNameExist(name)) {
        errorMsg = "Company name already exists!";
    }

    if (errorMsg != null) {
        request.setAttribute("errorMsg", errorMsg);
        request.setAttribute("oldTaxId", taxId);
        request.setAttribute("oldName", name);
        request.setAttribute("oldEmail", email);
        request.setAttribute("oldPhone", phoneNumber);
        request.setAttribute("oldAddress", address);
        request.setAttribute("oldContact", contactPerson);
        request.setAttribute("oldDescription", description);
        request.setAttribute("oldActive", active);
        request.getRequestDispatcher("/WEB-INF/View/admin/supplierManagement/createSupplier.jsp").forward(request, response);
        return;
    }

    // Tạo supplier mới theo model
    Suppliers supplier = new Suppliers(
            0, taxId, name, email, phoneNumber, address,
            contactPerson, description, active
    );

    int result = dao.insertSupplier(supplier);

    if (result > 0) {
        response.sendRedirect("ViewSupplier?success=create");
    } else {
        request.setAttribute("errorMsg", "Failed to add supplier!");
        request.getRequestDispatcher("/WEB-INF/View/admin/supplierManagement/createSupplier.jsp").forward(request, response);
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
