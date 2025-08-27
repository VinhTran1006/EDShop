/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Attribute;
import model.CategoryDetail;

/**
 *
 *
 */
@WebServlet(name = "UpdateCategoryServlet", urlPatterns = {"/UpdateCategory"})
public class UpdateCategoryServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateCategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateCategoryServlet at " + request.getContextPath() + "</h1>");
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
        String cateId = request.getParameter("categoryId");
        int categoryId = (cateId != null) ? Integer.parseInt(cateId) : -1;
        if (categoryId != -1) {

            CategoryDAO categoryDAO = new CategoryDAO();

            ArrayList<Attribute> attributeList = categoryDAO.getAttributeByCategoryID(categoryId);
            request.setAttribute("attributeList", attributeList);
            Category category = categoryDAO.getCategoryById(categoryId);
            request.setAttribute("category", category);
            request.getRequestDispatcher("/WEB-INF/View/admin/categoryManagement/updateCategory/updateCategory.jsp").forward(request, response);

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
        CategoryDAO dao = new CategoryDAO();
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        HttpSession session = request.getSession();
        session.removeAttribute("errorCategoryName");
        session.removeAttribute("errorUrl");
        session.removeAttribute("existCategory");
        boolean error = false;
        String categoryName = request.getParameter("categoryName");
        if (categoryName == null || categoryName.trim().isEmpty()) {
            session.setAttribute("errorCategoryName", "Category name cannot be empty.");
            error = true;
        } else if (!categoryName.matches("^[a-zA-Z ]+$")) {
            session.setAttribute("errorCategoryName", "Category name contains invalid characters.");
            error = true;
        }
        String ImgURLLogo = request.getParameter("ImgURLLogo");
        if (ImgURLLogo == null || ImgURLLogo.trim().isEmpty()) {
            session.setAttribute("errorUrl", "Image URL cannot be empty.");
            error = true;
        }
        boolean exist = dao.checkExistUpdate(categoryName, categoryId);
        if (exist) {
            session.setAttribute("existCategory", "This Category already exist.");
            error = true;
        }

        if (error) {
            response.sendRedirect("UpdateCategory?categoryId=" + categoryId + "&error=1");
        } else {
            dao.updateCategory(categoryId, categoryName, ImgURLLogo);

            String[] attributeIDs = request.getParameterValues("attributeID");
            String[] attibuteNames = request.getParameterValues("attributeName");
            boolean checkUpdateCategoryDetail = true;
            if (attributeIDs != null && attibuteNames != null && attributeIDs.length == attibuteNames.length) {
                for (int i = 0; i < attributeIDs.length; i++) {
                    int groupId = Integer.parseInt(attributeIDs[i]);
                    String groupName = attibuteNames[i];
                    checkUpdateCategoryDetail &= dao.updateAtrribute(groupId, groupName);
                }
            }
            if (checkUpdateCategoryDetail == true) {
                response.sendRedirect("CategoryView");
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
