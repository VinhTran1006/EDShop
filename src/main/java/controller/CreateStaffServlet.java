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
                    || role == null || role.trim().isEmpty()
                    || hiredDateStr == null || hiredDateStr.trim().isEmpty()) {

                request.setAttribute("errorMessage", "Please fill in all required information!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

// Validate email format
            String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (!email.matches(emailPattern)) {
                request.setAttribute("errorMessage", "Please enter a valid email address!");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

// Validate phone number format if provided
            if (phone != null && !phone.trim().isEmpty()) {
                String phonePattern = "^0\\d{9}$";
                if (!phone.matches(phonePattern)) {
                    request.setAttribute("errorMessage", "Phone number must start with 0 and have exactly 10 digits!");
                    request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                    return;
                }
            }

// Kiểm tra email đã tồn tại
            StaffDAO dao = new StaffDAO();
            if (dao.isEmailExists(email)) {
                request.setAttribute("errorMessage", "This email has been registered");
                request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                return;
            }

// Kiểm tra số điện thoại đã tồn tại (nếu có nhập)
            if (phone != null && !phone.trim().isEmpty() && dao.isPhoneExists(phone)) {
                request.setAttribute("errorMessage", "Phone number is already registered!");
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

                    // Validate birth date
                    java.util.Date today = new java.util.Date();
                    if (birthDate.after(today)) {
                        request.setAttribute("errorMessage", "Birth date cannot be in the future!");
                        request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                        return;
                    }

                    // Check age >= 18
                    java.util.Calendar birthCal = java.util.Calendar.getInstance();
                    birthCal.setTime(birthDate);
                    java.util.Calendar todayCal = java.util.Calendar.getInstance();
                    todayCal.setTime(today);

                    int age = todayCal.get(java.util.Calendar.YEAR) - birthCal.get(java.util.Calendar.YEAR);
                    if (todayCal.get(java.util.Calendar.DAY_OF_YEAR) < birthCal.get(java.util.Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    if (age < 18) {
                        request.setAttribute("errorMessage", "Staff must be at least 18 years old!");
                        request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                        return;
                    }
                }

                if (hiredDateStr != null && !hiredDateStr.trim().isEmpty()) {
                    hiredDate = dateFormat.parse(hiredDateStr);

                    // Validate hired date
                    java.util.Date today = new java.util.Date();
                    java.util.Calendar oneYearFromNow = java.util.Calendar.getInstance();
                    oneYearFromNow.add(java.util.Calendar.YEAR, 1);

                    if (hiredDate.after(oneYearFromNow.getTime())) {
                        request.setAttribute("errorMessage", "Hired date cannot be more than 1 year in the future!");
                        request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                        return;
                    }

                    // Validate hired date relative to birth date
                    if (birthDate != null) {
                        java.util.Calendar minHiredCal = java.util.Calendar.getInstance();
                        minHiredCal.setTime(birthDate);
                        minHiredCal.add(java.util.Calendar.YEAR, 16); // Minimum 16 years old to be hired

                        if (hiredDate.before(minHiredCal.getTime())) {
                            request.setAttribute("errorMessage", "Staff must be at least 16 years old on the hired date!");
                            request.getRequestDispatcher("/WEB-INF/View/admin/staffManagement/createStaff.jsp").forward(request, response);
                            return;
                        }
                    }
                }
            } catch (ParseException e) {
                request.setAttribute("errorMessage", "Invalid date format!");
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
                request.setAttribute("errorMessage", "An error occurred while creating the staff!");
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
