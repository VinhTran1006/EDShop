<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="model.Staff"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
    Staff staff = (Staff) request.getAttribute("staff");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Update Staff</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container mt-5">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-edit"></i> Edit Staff</h4>
                            </div>
                            <div class="card-body">
                                <% if (request.getAttribute("errorMessage") != null) {%>
                                <div class="alert alert-danger">
                                    <i class="fa-solid fa-exclamation-circle"></i>
                                    <%= request.getAttribute("errorMessage")%>
                                </div>
                                <% } %>

                                <% if (staff == null) { %>
                                <div class="no-data">
                                    <i class="fa-solid fa-user-slash"></i>
                                    <p>Staff not found!</p>
                                    <a href="StaffList" class="btn btn-outline-primary">
                                        <i class="fa-solid fa-arrow-left"></i> Back to Staff List
                                    </a>
                                </div>
                                <% } else {%>
                                <form action="UpdateStaffServlet" method="post" class="row g-3" id="updateForm">
                                    <input type="hidden" name="staffID" value="<%= staff.getStaffID()%>">

                                    <table class="form-table">
                                        <!-- Account Information -->
                                        <tr>
                                            <th class="required">Email:</th>
                                            <td>
                                                <input type="email" id="email" name="email" class="form-control" 
                                                       value="<%= staff.getEmail() != null ? staff.getEmail() : ""%>" required>
                                                <div id="emailError" class="text-danger small mt-1"></div>
                                                <input type="hidden" id="currentEmail" 
                                                       value="<%= staff.getEmail() != null ? staff.getEmail() : ""%>">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Staff ID:</th>
                                            <td>
                                                <input type="text" id="staffId" class="form-control" 
                                                       value="<%= staff.getStaffID()%>" readonly>
                                            </td>
                                        </tr>

                                        <!-- Staff Information -->
                                        <tr>
                                            <th class="required">Full Name:</th>
                                            <td>
                                                <input type="text" id="fullName" name="fullName" class="form-control" 
                                                       value="<%= staff.getFullName() != null ? staff.getFullName() : ""%>" required>
                                                <div id="fullNameError" class="text-danger small mt-1"></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Phone Number:</th>
                                            <td>
                                                <input type="text" id="phoneNumber" name="phoneNumber" class="form-control" 
                                                       value="<%= staff.getPhoneNumber() != null ? staff.getPhoneNumber() : ""%>">
                                                <div id="phoneError" class="text-danger small mt-1"></div>
                                                <input type="hidden" id="currentPhone" 
                                                       value="<%= staff.getPhoneNumber() != null ? staff.getPhoneNumber() : ""%>">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Birth Date:</th>
                                            <td>
                                                <input type="date" id="birthDate" name="birthDate" class="form-control" 
                                                       value="<%= staff.getBirthDate() != null ? sdf.format(staff.getBirthDate()) : ""%>">
                                                <div id="birthDateError" class="text-danger small mt-1"></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Gender:</th>
                                            <td>
                                                <select id="gender" name="gender" class="form-select">
                                                    <option value="">Select Gender</option>
                                                    <option value="Male" <%= "Male".equals(staff.getGender()) ? "selected" : ""%>>Male</option>
                                                    <option value="Female" <%= "Female".equals(staff.getGender()) ? "selected" : ""%>>Female</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Hired Date:</th>
                                            <td>
                                                <input type="date" id="hiredDate" name="hiredDate" class="form-control" 
                                                       value="<%= staff.getHiredDate() != null ? sdf.format(staff.getHiredDate()) : ""%>">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Status:</th>
                                            <td>
                                                <div class="radio-group">
                                                    <select id="status" name="isActive" class="form-select">
                                                        <option value="true" <%= staff.isActive() ? "selected" : ""%>>Active</option>
                                                        <option value="false" <%= !staff.isActive() ? "selected" : ""%>>Inactive</option>
                                                    </select>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Role:</th>
                                            <td>
                                                <input type="text" id="role" class="form-control" 
                                                       value="<%= staff.getRole() != null ? staff.getRole() : ""%>" readonly>
                                            </td>
                                        </tr>
                                    </table>

                                    <div class="button-group">
                                        <a href="StaffList" class="btn btn-secondary">
                                            Cancel
                                        </a>
                                        <button type="submit" class="btn btn-success" id="submitBtn">
                                            Update
                                        </button>
                                    </div>
                                </form>
                                <% }%>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <!-- giữ nguyên phần JS validation cũ -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // (copy nguyên phần JS validation từ file gốc EditStaff)
        </script>
    </body>

</html>