package controller;

import dao.AccountDAO;
import dao.StaffDAO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Staff;

@WebServlet(name = "CreateStaffServlet", urlPatterns = {"/CreateStaffServlet"})
public class CreateStaffServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            // Lấy dữ liệu từ form
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phoneNumber");
            String birthDateStr = request.getParameter("birthDate");
            String gender = request.getParameter("gender");
            String role = request.getParameter("role");
            String hiredDateStr = request.getParameter("hiredDate");

            // Validate dữ liệu cơ bản
            if (email == null || email.trim().isEmpty()
                    || password == null || password.trim().isEmpty()
                    || fullName == null || fullName.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()) {

                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

            // Kiểm tra email đã tồn tại
            StaffDAO dao = new StaffDAO();
            if (dao.isEmailExists(email)) {
                request.setAttribute("errorMessage", "Email đã tồn tại trong hệ thống!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

            // Kiểm tra số điện thoại đã tồn tại
            if (dao.isPhoneExists(phone)) {
                request.setAttribute("errorMessage", "Số điện thoại đã tồn tại trong hệ thống!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

            // Chuyển đổi ngày tháng sang java.util.Date
            java.util.Date birthDate = null;
            java.util.Date hiredDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                    birthDate = dateFormat.parse(birthDateStr);
                }
                if (hiredDateStr != null && !hiredDateStr.trim().isEmpty()) {
                    hiredDate = dateFormat.parse(hiredDateStr);
                }
            } catch (ParseException e) {
                request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

            // Hash password
            AccountDAO accDao = new AccountDAO();
            String hashPass = accDao.hashMD5(password);

            // Tạo đối tượng Staff - chỉ set role là "Staff", không có Manager
            Staff staff = new Staff();
            staff.setEmail(email.trim());
            staff.setPasswordHash(hashPass);
            staff.setFullName(fullName.trim());
            staff.setPhoneNumber(phone.trim());
            staff.setBirthDate(birthDate);
            staff.setGender(gender);
            staff.setRole(role);
            staff.setHiredDate(hiredDate);
            staff.setActive(true); // mặc định active khi tạo mới
            staff.setCreatedAt(new java.util.Date());

            // Gọi DAO để thêm vào DB
            boolean success = dao.createStaff(staff);

            if (success) {
                session.setAttribute("successcreate", "1");
                response.sendRedirect("StaffList");
            } else {
                request.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo nhân viên!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for creating new staff";
    }
}
