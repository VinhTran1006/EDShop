<%-- 
    Document   : change-password-staff
    Created on : Jul 30, 2025, 5:04:04 PM
    Author     : pc
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Reset Staff Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierLists5.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card mx-auto shadow" style="max-width: 600px;">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0">Reset Staff Password</h4>
                <c:if test="${not empty staff}">
                    <small class="text-light">Staff: ${staff.fullName} (${staff.email})</small>
                </c:if>
            </div>
            <div class="card-body">
                <!-- Hiển thị thông báo -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success text-center">${success}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger text-center">
                        ${error}
                    </div>
                </c:if>
                
                <!-- Form đổi mật khẩu -->
                <form action="${pageContext.request.contextPath}/ChangePasswordStaff" method="post" onsubmit="return validateForm()">
                    <!-- Hidden field để truyền staffId -->
                    <input type="hidden" name="staffId" value="${param.staffId != null ? param.staffId : staffId}">
                    
                    <table class="table table-borderless">
                        <tr>
                            <th>New Password:</th>
                            <td>
                                <input type="password" id="newPassword" name="newPassword"
                                       class="form-control" placeholder="Enter new password" required>
                                <small class="text-muted">Password must be at least 9 characters long</small>
                            </td>
                        </tr>
                        <tr>
                            <th>Confirm New Password:</th>
                            <td>
                                <input type="password" id="confirmPassword" name="confirmPassword"
                                       class="form-control" placeholder="Re-enter new password" required>
                            </td>
                        </tr>
                    </table>
                    
                    <div class="d-flex justify-content-between mt-4">
                        <div>
                            <c:choose>
                                <c:when test="${param.staffId != null || staffId != null}">
                                    <a href="${pageContext.request.contextPath}/StaffList?action=detail&id=${param.staffId != null ? param.staffId : staffId}" 
                                       class="btn btn-outline-primary">
                                        <i class="bi bi-arrow-return-left"></i> Back to Staff Detail
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/StaffList" class="btn btn-outline-primary">
                                        <i class="bi bi-arrow-return-left"></i> Back to Staff List
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div>
                            <button type="submit" class="btn btn-warning">Reset Password</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function validateForm() {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword.length < 9) {
                alert('Password must be at least 9 characters long.');
                return false;
            }
            
            if (newPassword !== confirmPassword) {
                alert('New password and confirm password do not match.');
                return false;
            }
            
            return true;
        }
    </script>
</body>
</html>