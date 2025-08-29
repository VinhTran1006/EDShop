<%@page import="model.Customer"%>
<%@page import="model.Staff"%>
<%
    Customer cus = (Customer) request.getAttribute("cus");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Profile - TShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/profile.css">
    </head>
    <body>
        <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />

        <div class="main-account container-fluid">
            <!-- SIDEBAR -->
            <jsp:include page="/WEB-INF/View/customer/sideBar.jsp" />

            <!-- UPDATE PROFILE -->
            <div class="profile-card">
                <div class="profile-header">
                    <h4>
                        <i class="bi bi-pencil-square me-2"></i>
                        Update Profile
                    </h4>
                </div>
                <div class="profile-body">
                    <% if (cus == null) { %>
                    <div class="alert alert-danger">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        There is no customer with that ID
                    </div>
                    <% } else {%>

                    <%
                        String errorMsg = (String) session.getAttribute("error");
                        if (errorMsg != null) {
                    %>
                    <div class="alert alert-danger mb-3" style="max-width: 500px;">
                        <i class="bi bi-exclamation-triangle-fill"></i>
                        <%= errorMsg%>
                    </div>
                    <%
                            session.removeAttribute("error");
                        }
                    %>
                    <form method="post" action="UpdateProfile">
                        <input type="hidden" name="id" value="<%= cus.getCustomerID()%>">

                        <div class="form-group">
                            <label>
                                <i class="bi bi-person profile-icon"></i>
                                Full Name:
                            </label>
                            <input type="text"
                                   name="fullname"
                                   value="<%= request.getSession().getAttribute("tempFullName") != null
                                           ? request.getSession().getAttribute("tempFullName")
                                           : (cus.getFullName() != null ? cus.getFullName() : "")%>"
                                   class="form-control"
                                   required
                                   maxlength="50"
                                   pattern="^[\p{L}\s]{2,50}$"
                                   title="Please enter letters only (Full name must be 2-50 letters, spaces allowed, no numbers or special characters.).">
                        </div>

                        <div class="form-group">
                            <label>
                                <i class="bi bi-telephone profile-icon"></i>
                                Phone Number:
                            </label>
                            <input type="tel"
                                   name="phone"
                                   value="<%= request.getSession().getAttribute("tempPhone") != null
                                           ? request.getSession().getAttribute("tempPhone")
                                           : (cus.getPhoneNumber() != null ? cus.getPhoneNumber() : "")%>"
                                   class="form-control"
                                   required
                                   maxlength="10"
                                   pattern="0[0-9]{9}"
                                   title="Please enter numbers only (no letters or special characters, no spaces and exactly 10 characters and must start with 0).">
                        </div>

                        <div class="form-group">
                            <label>
                                <i class="bi bi-calendar profile-icon"></i>
                                Date of Birth:
                            </label>
                            <input type="date"
                                   name="dob"
                                   value="<%= request.getSession().getAttribute("tempDob") != null
                                           ? request.getSession().getAttribute("tempDob")
                                           : (cus.getBirthDate() != null ? cus.getBirthDate() : "")%>"
                                   class="form-control"
                                   required
                                   max="<%= java.time.LocalDate.now().minusYears(18).toString() %>"
                                   min="<%= java.time.LocalDate.now().minusYears(100).withDayOfYear(1).toString()%>">

                        </div>

                        <%
                            String gender = cus.getGender();
                            if (request.getSession().getAttribute("tempGender") != null) {
                                gender = request.getSession().getAttribute("tempGender").toString();
                            }
                        %>
                        <div class="form-group">
                            <label>
                                <i class="bi bi-gender-ambiguous profile-icon"></i>
                                Gender:
                            </label>
                            <div class="gender-options">
                                <div class="gender-option">
                                    <input type="radio"
                                           class="form-check-input"
                                           name="gender"
                                           value="male"
                                           id="male"
                                           <%= "male".equalsIgnoreCase(cus.getGender()) ? "checked" : ""%>>
                                    <label for="male">Male</label>
                                </div>
                                <div class="gender-option">
                                    <input type="radio"
                                           class="form-check-input"
                                           name="gender"
                                           value="female"
                                           id="female"
                                           <%= "female".equalsIgnoreCase(cus.getGender()) ? "checked" : ""%>>
                                    <label for="female">Female</label>
                                </div>
                            </div>
                        </div>

                        <div class="profile-actions">
                            <a href="ViewProfile" class="btn-cancel">
                                <i class="bi bi-x-circle me-2"></i>
                                Cancel
                            </a>
                            <button type="submit" class="btn-update">
                                <i class="bi bi-check-circle me-2"></i>
                                Update Profile
                            </button>
                        </div>
                    </form>
                    <% }%>
                </div>
            </div>
        </div>

        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>