<%-- 
    Document   : view-staff-detail
    Created on : Jun 12, 2025, 10:01:59 PM
    Author     : pc
--%>

<%@page import="model.Staff"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Staff sta = (Staff) request.getAttribute("data");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Staff detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">
    </head>
    <body>
        <% if (sta == null) { %>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container mt-5">
                        <div class="card">
                            <div class="card-body">
                                <div class="no-data">
                                    <p>There is no staff with that id</p>
                                    <a href="StaffList" class="btn btn-outline-primary">
                                        <i class="fa-solid fa-arrow-left"></i> Back to list
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
        <% } else {%>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container mt-5">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-id-card"></i> Staff Details</h4>
                            </div>

                            <div class="card-body">
                                <!-- Hiển thị thông báo thành công khi change password -->
                                <c:if test="${not empty sessionScope.passwordChangeSuccess}">
                                    <div class="alert alert-success text-center">
                                        ${sessionScope.passwordChangeSuccess}
                                    </div>
                                    <c:remove var="passwordChangeSuccess" scope="session"/>
                                </c:if>

                                <!-- Hiển thị thông báo thành công từ các action khác -->
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success text-center">${success}</div>
                                </c:if>

                                <!-- Hiển thị thông báo lỗi -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger text-center">${error}</div>
                                </c:if>

                                <!-- Thông tin nhân viên -->
                                <table class="info-table">
                                    <tr><th>Staff ID:</th><td><%= sta.getStaffID()%></td></tr>
                                    <tr><th>Full Name:</th><td><%= sta.getFullName()%></td></tr>
                                    <tr><th>Phone Number:</th><td><%= sta.getPhoneNumber()%></td></tr>
                                    <tr><th>Email:</th><td><%= sta.getEmail()%></td></tr>
                                    <tr><th>Position:</th><td><%= sta.getRole()%></td></tr>
                                    <tr>
                                        <th>Date of Birth:</th>
                                        <td>
                                            <%= (sta.getBirthDate() != null
                                                    ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(sta.getBirthDate())
                                                    : "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Hired Date:</th>
                                        <td>
                                            <%= (sta.getHiredDate() != null
                                                    ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(sta.getHiredDate())
                                                    : "")%>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Sex:</th>
                                        <td>
                                            <input type="radio" class="form-check-input" name="sex" value="male"
                                                   <%= ("male".equalsIgnoreCase(sta.getGender()) ? "checked" : "")%> disabled /> Male
                                            <input type="radio" class="form-check-input ms-3" name="sex" value="female"
                                                   <%= ("female".equalsIgnoreCase(sta.getGender()) ? "checked" : "")%> disabled /> Female
                                        </td>
                                    </tr>
                                </table>

                                <!-- Khu vực thao tác quản lý -->
                                <div class="management-section">
                                    <div class="form-controls">
                                        <a href="${pageContext.request.contextPath}/ChangePasswordStaff?staffId=<%= sta.getStaffID()%>"
                                           class="btn btn-secondary">
                                            <i class="fa-solid fa-key"></i> Reset Password
                                        </a>
                                        <a href="StaffList" class="btn btn-outline-primary">
                                            <i class="fa-solid fa-arrow-left"></i> Back to List
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
        <% }%>

        <script>
            // Auto-hide success messages after 5 seconds
            setTimeout(function () {
                var alerts = document.querySelectorAll('.alert');
                alerts.forEach(function (alert) {
                    alert.style.opacity = '0';
                    setTimeout(function () {
                        alert.style.display = 'none';
                    }, 500);
                });
            }, 5000);
        </script>
    </body>

</html>