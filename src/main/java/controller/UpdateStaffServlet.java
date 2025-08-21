package controller;

import dao.StaffDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Staff;

import java.io.IOException;

@WebServlet(name = "UpdateStaffServlet", urlPatterns = {"/UpdateStaffServlet"})
public class UpdateStaffServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffIdStr = request.getParameter("id");
        if (staffIdStr != null) {
            int staffId = Integer.parseInt(staffIdStr);
            StaffDAO dao = new StaffDAO();
            Staff staff = dao.getStaffById(staffId);

            if (staff != null) {
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/updateStaff.jsp")
                       .forward(request, response);
            } else {
                // không tìm thấy staff
                response.sendRedirect("StaffList?error=notfound");
            }
        } else {
            response.sendRedirect("StaffList");
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        // Lấy dữ liệu từ form
        int staffID = Integer.parseInt(request.getParameter("staffID"));
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String birthDateStr = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String role = request.getParameter("role"); // vì Staff có field role
        String hiredDateStr = request.getParameter("hiredDate");
        String isActiveStr = request.getParameter("isActive"); // checkbox hoặc select

        // Chuyển kiểu Date
        java.sql.Date birthDate = (birthDateStr != null && !birthDateStr.isEmpty())
                ? java.sql.Date.valueOf(birthDateStr) : null;
        java.sql.Date hiredDate = (hiredDateStr != null && !hiredDateStr.isEmpty())
                ? java.sql.Date.valueOf(hiredDateStr) : null;

        boolean isActive = (isActiveStr != null && isActiveStr.equals("true"));

        // Tạo đối tượng Staff
        Staff staff = new Staff();
        staff.setStaffID(staffID);
        staff.setFullName(fullName);
        staff.setPhoneNumber(phone);
        staff.setEmail(email);
        staff.setBirthDate(birthDate);
        staff.setGender(gender);
        staff.setRole(role);
        staff.setHiredDate(hiredDate);
        staff.setActive(isActive);

        // Gọi DAO để update
        StaffDAO dao = new StaffDAO();
        boolean success = dao.updateStaff(staff);

        if (success) {
            response.sendRedirect("StaffList?successedit=1");
        } else {
            response.sendRedirect("StaffList?erroredit=1");
        }

    } catch (Exception e) {
        e.printStackTrace();

        // Giữ lại dữ liệu nhập để load lại form khi có lỗi
        Staff staff = new Staff();
        try {
            staff.setStaffID(Integer.parseInt(request.getParameter("staffID")));
        } catch (Exception ignored) {}
        staff.setFullName(request.getParameter("fullName"));
        staff.setPhoneNumber(request.getParameter("phoneNumber"));
        staff.setEmail(request.getParameter("email"));

        String birthDateStr = request.getParameter("birthDate");
        String hiredDateStr = request.getParameter("hiredDate");
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            staff.setBirthDate(java.sql.Date.valueOf(birthDateStr));
        }
        if (hiredDateStr != null && !hiredDateStr.isEmpty()) {
            staff.setHiredDate(java.sql.Date.valueOf(hiredDateStr));
        }

        staff.setGender(request.getParameter("gender"));
        staff.setRole(request.getParameter("role"));
        staff.setActive("true".equals(request.getParameter("isActive")));

        request.setAttribute("errorMessage", "Error: " + e.getMessage());
        request.setAttribute("staff", staff);

        request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/updateStaff.jsp")
                .forward(request, response);
    }
}


    @Override
    public String getServletInfo() {
        return "Servlet for updating staff information";
    }
}
