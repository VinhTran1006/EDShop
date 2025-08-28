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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierLists5.css">
    </head>
    <body>
        <% if (sta == null) {
                out.print("<p>There is no staff with that id</p>");
            } else {
        %>
        <div class="container mt-5">
            <div class="card mx-auto shadow" style="max-width: 700px;">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Staff Detail</h4>
                </div>
                <form method="post" action="StaffList?action=detail">
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
                        
                        <table class="table table-borderless">
                            <tr>
                                <th>Staff ID:</th>
                                <td><%= sta.getStaffID()%></td>
                            </tr>
                            <tr>
                                <th>Full Name:</th>
                                <td><%= sta.getFullName()%></td>
                            </tr>
                            <tr>
                                <th>Phone Number:</th>
                                <td><%= sta.getPhoneNumber()%></td>
                            </tr>
                            <tr>
                                <th>Email:</th>
                                <td><%= sta.getEmail()%></td>
                            </tr>
                            <tr>
                                <th>Position:</th>
                                <td><%= sta.getRole()%></td>
                            </tr>
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
                        <div class="d-flex justify-content-between mt-4">
                            <div>
                                <a href="${pageContext.request.contextPath}/ChangePasswordStaff?staffId=<%= sta.getStaffID()%>" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-tools"></i> Reset Password
                                </a>
                                <a href="StaffList" class="btn btn-outline-primary" id="back">
                                    <i class="bi bi-arrow-return-left"></i> Back to list
                                </a>
                            </div>
                        </div>
                    </div>
                </form>
                <% }%>
            </div>
        </div>
        
        <script>
            // Auto-hide success messages after 5 seconds
            setTimeout(function() {
                var alerts = document.querySelectorAll('.alert');
                alerts.forEach(function(alert) {
                    alert.style.opacity = '0';
                    setTimeout(function() {
                        alert.style.display = 'none';
                    }, 500);
                });
            }, 5000);
        </script>
    </body>
</html>