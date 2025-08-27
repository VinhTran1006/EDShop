/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.util.Arrays;

import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 *
 */
@WebServlet(name = "CreateCategoryServlet", urlPatterns = {"/CreateCategory"})
public class CreateCategoryServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateCategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateCategoryServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("/WEB-INF/View/admin/categoryManagement/createCategory/createCategory.jsp").forward(request, response);

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
        int groupCount = Integer.parseInt(request.getParameter("groupCount"));
        CategoryDAO dao = new CategoryDAO();
        boolean exist = dao.checkExist(categoryName);
        if (exist) {
            session.setAttribute("existCategory", "This Category already exist.");
            error = true;
        }
        if (error) {
            request.setAttribute("createError", "1");
            request.getRequestDispatcher("/WEB-INF/View/admin/categoryManagement/createCategory/createCategory.jsp").forward(request, response);
        } else {
            int categoryId = dao.addCategory(categoryName, ImgURLLogo);
            for (int i = 1; i <= groupCount; i++) {
                String groupName = request.getParameter("groups[" + i + "][name]");
                if (groupName == null || groupName.trim().isEmpty()) {
                    continue;
                }
                int attributeID = dao.addAttribute(groupName, categoryId);
                if (attributeID == -1) {
                    request.setAttribute("createError", "1");
                    request.getRequestDispatcher("/WEB-INF/View/admin/categoryManagement/createCategory/createCategory.jsp").forward(request, response);
                    return;
                }
            }

            request.getParameterMap().forEach((k, v) -> {
                System.out.println(k + " = " + Arrays.toString(v));
            });

            // Thành công
            response.sendRedirect("CategoryView?createSuccess=1");

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
