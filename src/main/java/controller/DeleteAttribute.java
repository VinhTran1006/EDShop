/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DONG QUOC
 */
@WebServlet(name = "DeleteAttribute", urlPatterns = {"/DeleteAttribute"})
public class DeleteAttribute extends HttpServlet {

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
        int attributeID = Integer.parseInt(request.getParameter("attributeID"));
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        boolean isSuccess = categoryDAO.deleteAttribute(attributeID);
        if (isSuccess) {
             response.sendRedirect("UpdateCategory?categoryId=" + categoryID + "&success=1");
            
        } else {
            response.sendRedirect("UpdateCategory?categoryId=" + categoryID + "&error=1");
           
        }
    }
}
