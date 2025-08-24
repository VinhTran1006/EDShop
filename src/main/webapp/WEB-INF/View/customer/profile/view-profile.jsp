
<%@page import="model.Customer"%>
<%@page import= "java.sql.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Customer cus = (Customer) request.getAttribute("cus");
    boolean hasPassword = false;

    if (cus != null && cus.getPasswordHash() != null && !cus.getPasswordHash().isEmpty()) {
        hasPassword = true;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Profile - TShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/profile.css">
    </head>
    <body>
        <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />

        <div class="main-account container-fluid">
            <!-- SIDEBAR -->
            <jsp:include page="/WEB-INF/View/customer/sideBar.jsp" />

            <!-- PROFILE -->
            <div class="profile-card">
                <div class="profile-header">
                    <h4>
                        <i class="bi bi-person-circle me-2"></i>
                        Profile Information
                    </h4>
                </div>
                <form method="get" action="ViewProfile?action=list">
                    <div class="profile-body">
                        <table class="table profile-table">
                            <tr>
                                <th><i class="bi bi-hash profile-icon"></i> ID:</th>
                                <td class="profile-value"><%= cus.getCustomerID()%></td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-person profile-icon"></i> Full Name:</th>
                                <td class="profile-value"><%= cus.getFullName()%></td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-telephone profile-icon"></i> Phone Number:</th>
                                <td>
                                    <%= (cus.getPhoneNumber() == null || cus.getPhoneNumber().isEmpty())
                                            ? "<span class='error-message'><i class='bi bi-exclamation-triangle me-1'></i>Please enter your phone number</span>"
                                            : "<span class='profile-value'>" + cus.getPhoneNumber() + "</span>"%>
                                </td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-envelope profile-icon"></i> Email:</th>
                                <td class="profile-value"><%= cus.getEmail()%></td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-calendar profile-icon"></i> Date of Birth:</th>
                                <td>
                                    <%
                                        java.util.Date birth = cus.getBirthDate();
                                        String birthFormatted = "";

                                        if (birth != null) {
                                            try {
                                                java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
                                                birthFormatted = outputFormat.format(birth);
                                            } catch (Exception e) {
                                                birthFormatted = "Your birthday is invalid";
                                            }
                                        }
                                    %>

                                    <%= (birth == null)
                                            ? "<span class='error-message'><i class='bi bi-exclamation-triangle me-1'></i>Please enter your date of birth</span>"
                                            : "<span class='profile-value'>" + birthFormatted + "</span>"%>

                                </td>
                            </tr>
                            <tr>
                                <th><i class="bi bi-gender-ambiguous profile-icon"></i> Sex:</th>
                                <td>
                                    <div class="gender-options">
                                        <div class="gender-option">
                                            <input type="radio" class="form-check-input" name="sex" value="male"
                                                   <%= ("male".equalsIgnoreCase(cus.getGender()) ? "checked" : "")%> disabled />
                                            <span>Male</span>
                                        </div>
                                        <div class="gender-option">
                                            <input type="radio" class="form-check-input" name="sex" value="female"
                                                   <%= ("female".equalsIgnoreCase(cus.getGender()) ? "checked" : "")%> disabled />
                                            <span>Female</span>
                                        </div>
                                    </div>
                                    <% if (cus.getGender() == null || cus.getGender().trim().isEmpty()) { %>
                                    <div class="mt-2">
                                        <span class="error-message">
                                            <i class="bi bi-exclamation-triangle me-1"></i>
                                            Please enter your gender
                                        </span>
                                    </div>
                                    <% }%> 
                                </td>
                            </tr>
                        </table>
                        <div class="profile-actions">
                            <% if (hasPassword) { %>
                            <a href="ChangePassword" class="btn-cancel">
                                <i class="bi bi-shield-lock me-2"></i>
                                Change Password
                            </a>
                            <% }%>
                            <a href="UpdateProfile" class="btn-update">
                                <i class="bi bi-pencil-square me-2"></i>
                                Update Profile
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>