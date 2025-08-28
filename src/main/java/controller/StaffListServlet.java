/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.StaffDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Staff;

/**
 *
 * @author pc
 */
@WebServlet(name = "StaffListServlet", urlPatterns = {"/StaffList"})
public class StaffListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        StaffDAO dao = new StaffDAO();
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        if (action.equalsIgnoreCase("list")) {
            List<Staff> staff = dao.getAllStaff();
            request.setAttribute("staff", staff);
            request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/staffList.jsp").forward(request, response);
        }
        if (action.equalsIgnoreCase("search")) {
            String keyword = request.getParameter("keyword");
            List<Staff> list = dao.searchStaff(keyword);
            if (keyword != null && !keyword.trim().isEmpty()) {
                list = dao.searchStaff(keyword);
            } else {
                list = dao.getAllStaff();
            }
            request.setAttribute("staff", list);
            if (list.isEmpty()) {
                request.setAttribute("message", "No staff found.");
            }
            request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/staffList.jsp").forward(request, response);
            return;
        }
        if (action.equalsIgnoreCase("detail")) {
            String idRaw = request.getParameter("id");

            int id = 0;
            try {
                id = Integer.parseInt(idRaw);
                Staff sta = dao.getStaffById(id);

                request.setAttribute("data", sta);
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/view-staff-detail.jsp").forward(request, response);

                System.out.println("Session ID: " + session.getId());
                // System.out.println("accountId: " + session.getAttribute("accountId"));
            } catch (Exception e) {
                PrintWriter out = response.getWriter();
                out.print(e.getMessage());
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

}
