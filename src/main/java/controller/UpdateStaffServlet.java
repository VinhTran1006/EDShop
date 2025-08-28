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
            try {
                int staffId = Integer.parseInt(staffIdStr);
                StaffDAO dao = new StaffDAO();
                Staff staff = dao.getStaffById(staffId);

                if (staff != null) {
                    request.setAttribute("staff", staff);
                    request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/updateStaff.jsp")
                            .forward(request, response);
                } else {
                    response.sendRedirect("StaffList?error=notfound");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("StaffList?error=invalid_id");
            }
        } else {
            response.sendRedirect("StaffList");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            // Get current staff data first
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            StaffDAO dao = new StaffDAO();
            Staff currentStaff = dao.getStaffById(staffID);

            if (currentStaff == null) {
                response.sendRedirect("StaffList?error=notfound");
                return;
            }

            // Get form data
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String birthDateStr = request.getParameter("birthDate");
            String gender = request.getParameter("gender");
            String hiredDateStr = request.getParameter("hiredDate");
            String isActiveStr = request.getParameter("isActive");

            // Validate required fields
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name is required");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }

            // Process dates
            java.sql.Date birthDate = null;
            if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                try {
                    birthDate = java.sql.Date.valueOf(birthDateStr);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid birth date format");
                }
            }

            java.sql.Date hiredDate = null;
            if (hiredDateStr != null && !hiredDateStr.trim().isEmpty()) {
                try {
                    hiredDate = java.sql.Date.valueOf(hiredDateStr);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid hired date format");
                }
            }

            boolean isActive = "true".equals(isActiveStr);

            // Create updated staff object - preserve existing data that shouldn't change
            Staff staff = new Staff();
            staff.setStaffID(staffID);
            staff.setEmail(email.trim());
            staff.setPasswordHash(currentStaff.getPasswordHash()); // Keep existing password
            staff.setFullName(fullName.trim());
            staff.setPhoneNumber(phone != null ? phone.trim() : null);
            staff.setBirthDate(birthDate);
            staff.setGender(gender);
            staff.setRole(currentStaff.getRole()); // Keep existing role - don't allow changing via this form
            staff.setHiredDate(hiredDate != null ? hiredDate : currentStaff.getHiredDate()); // Keep existing if not provided
            staff.setActive(isActive);
            staff.setCreatedAt(currentStaff.getCreatedAt()); // Keep original creation date

            // Update staff
            boolean success = dao.updateStaff(staff);

            if (success) {
                session.setAttribute("successedit", "1");
                response.sendRedirect("StaffList");
            } else {
                request.setAttribute("errorMessage", "Failed to update staff. Please try again.");
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/updateStaff.jsp")
                        .forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid staff ID format.");
            response.sendRedirect("StaffList?error=invalid_id");
        } catch (IllegalArgumentException e) {
            // Keep form data and show error
            Staff staff = new Staff();
            try {
                staff.setStaffID(Integer.parseInt(request.getParameter("staffID")));
                staff.setFullName(request.getParameter("fullName"));
                staff.setPhoneNumber(request.getParameter("phoneNumber"));
                staff.setEmail(request.getParameter("email"));
                staff.setGender(request.getParameter("gender"));
                staff.setActive("true".equals(request.getParameter("isActive")));

                String birthDateStr = request.getParameter("birthDate");
                if (birthDateStr != null && !birthDateStr.isEmpty()) {
                    try {
                        staff.setBirthDate(java.sql.Date.valueOf(birthDateStr));
                    } catch (Exception ignored) {
                    }
                }

                String hiredDateStr = request.getParameter("hiredDate");
                if (hiredDateStr != null && !hiredDateStr.isEmpty()) {
                    try {
                        staff.setHiredDate(java.sql.Date.valueOf(hiredDateStr));
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            }

            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("staff", staff);
            request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/updateStaff.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());

            // Try to preserve form data
            Staff staff = new Staff();
            try {
                staff.setStaffID(Integer.parseInt(request.getParameter("staffID")));
            } catch (Exception ignored) {
            }
            staff.setFullName(request.getParameter("fullName"));
            staff.setPhoneNumber(request.getParameter("phoneNumber"));
            staff.setEmail(request.getParameter("email"));
            staff.setGender(request.getParameter("gender"));
            staff.setActive("true".equals(request.getParameter("isActive")));

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
