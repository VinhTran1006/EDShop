<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create New Staff</title>
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
                    <div class="container">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fas fa-user-plus"></i> Create New Staff</h4>
                            </div>

                            <div class="card-body">
                                <%
                                    String errorMessage = (String) request.getAttribute("errorMessage");
                                    if (errorMessage != null) {
                                %>
                                <div class="alert alert-danger" role="alert">
                                    <i class="fas fa-exclamation-circle me-2"></i><%= errorMessage%>
                                </div>
                                <%
                                    }
                                %>

                                <form action="CreateStaffServlet" method="post">
                                    <!-- Account Information Section -->
                                    <div class="form-section">
                                        <table class="supplier-form-table">
                                            <tr>
                                                <th class="required">Email:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="email" id="email" name="email" class="form-control supplier-input" required>
                                                        <div id="emailError" class="text-danger small mt-1"></div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Password:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="password" id="password" name="password" class="form-control supplier-input" required>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Full Name:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" id="fullName" name="fullName" class="form-control supplier-input" required>
                                                        <div id="fullNameError" class="text-danger small mt-1"></div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Phone Number:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" id="phoneNumber" name="phoneNumber" class="form-control supplier-input">
                                                        <div id="phoneError" class="text-danger small mt-1"></div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Birth Date:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="date" id="birthDate" name="birthDate" class="form-control supplier-input">
                                                        <div id="birthDateError" class="text-danger small mt-1"></div>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th>Gender:</th>
                                                <td>
                                                    <select id="gender" name="gender" class="form-control supplier-input">
                                                        <option value="">Select Gender</option>
                                                        <option value="Male">Male</option>
                                                        <option value="Female">Female</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Role:</th>
                                                <td>
                                                    <select id="role" name="role" class="form-control supplier-input" required>
                                                        <option value="">Select Role</option>
                                                        <option value="Admin">Admin</option>
                                                        <option value="Staff">Staff</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Hired Date:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="date" id="hiredDate" name="hiredDate" class="form-control supplier-input" required>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Status:</th>
                                                <td>
                                                    <div class="form-check form-check-inline">
                                                        <input type="radio" name="isActive" id="active" value="true" class="form-check-input" required checked>
                                                        <label for="active" class="form-check-label">
                                                            Active
                                                        </label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input type="radio" name="isActive" id="inactive" value="false" class="form-check-input" required>
                                                        <label for="inactive" class="form-check-label">
                                                            Inactive
                                                        </label>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>

                                    <div class="button-container">
                                        <button type="submit" class="btn btn-success">Create Staff</button>
                                        <a href="StaffList" class="btn btn-secondary">Cancel</a>
                                    </div>
                                </form>

                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const emailInput = document.getElementById("email");
            const emailError = document.getElementById("emailError");
            const submitBtn = document.querySelector("button[type='submit']");

            emailInput.addEventListener("blur", function () {
                const email = emailInput.value.trim();
                const emailPattern = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
                emailError.textContent = "";
                submitBtn.disabled = false;

                if (!emailPattern.test(email)) {
                    emailError.textContent = "Email must be a valid Gmail address.";
                    submitBtn.disabled = true;
                    return;
                }

                fetch("CheckEmailServlet?email=" + encodeURIComponent(email))
                        .then(response => response.text())
                        .then(data => {
                            if (data === "EXISTS") {
                                emailError.textContent = "This email already exists.";
                                submitBtn.disabled = true;
                            }
                        })
                        .catch(error => {
                            emailError.textContent = "Error checking email.";
                            submitBtn.disabled = true;
                            console.error("Error:", error);
                        });
            });

            emailInput.addEventListener("input", function () {
                emailError.textContent = "";
                submitBtn.disabled = false;
            });

            const phoneInput = document.getElementById("phoneNumber");
            const phoneError = document.getElementById("phoneError");

            phoneInput.addEventListener("blur", function () {
                const phone = phoneInput.value.trim();
                phoneError.textContent = "";
                submitBtn.disabled = false;

                const phonePattern = /^0\d{9}$/;
                if (!phonePattern.test(phone)) {
                    phoneError.textContent = "Phone must start with 0 and have exactly 10 digits.";
                    submitBtn.disabled = true;
                    return;
                }

                fetch("CheckPhoneServlet?phone=" + encodeURIComponent(phone))
                        .then(response => response.text())
                        .then(data => {
                            if (data === "EXISTS") {
                                phoneError.textContent = "Phone number already exists.";
                                submitBtn.disabled = true;
                            }
                        })
                        .catch(error => {
                            phoneError.textContent = "Error checking phone.";
                            submitBtn.disabled = true;
                            console.error("Error:", error);
                        });
            });

            phoneInput.addEventListener("input", function () {
                phoneError.textContent = "";
                submitBtn.disabled = false;
            });

            const birthInput = document.getElementById("birthDate");
            const birthError = document.getElementById("birthDateError");

            birthInput.addEventListener("blur", function () {
                const birthDate = new Date(this.value);
                const today = new Date();

                birthError.textContent = "";
                submitBtn.disabled = false;

                if (isNaN(birthDate.getTime()))
                    return;

                let age = today.getFullYear() - birthDate.getFullYear();
                const m = today.getMonth() - birthDate.getMonth();

                if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
                    age--;
                }

                if (age < 18) {
                    birthError.textContent = "Staff must be at least 18 years old.";
                    submitBtn.disabled = true;
                }
            });

            const fullNameInput = document.getElementById("fullName");
            const fullNameError = document.getElementById("fullNameError");

            fullNameInput.addEventListener("blur", function () {
                let name = fullNameInput.value.trim();
                fullNameError.textContent = "";
                submitBtn.disabled = false;
                name = name.replace(/\s+/g, " ");
                fullNameInput.value = name;

                const namePattern = /^([A-ZÀ-ỸĐ][a-zà-ỹđ]+)(\s[A-ZÀ-ỸĐ][a-zà-ỹđ]+)*$/u;

                if (!namePattern.test(name)) {
                    fullNameError.textContent = "Names must be initialed, contain no numbers or special characters, and have no spaces.";
                    submitBtn.disabled = true;
                }
            });

            fullNameInput.addEventListener("input", function () {
                fullNameError.textContent = "";
                submitBtn.disabled = false;
            });

            function checkDuplicateEmail(email) {
                fetch("CheckEmailServlet?email=" + encodeURIComponent(email))
                        .then(response => response.text())
                        .then(data => {
                            if (data === "EXISTS") {
                                alert("Email already exists.");
                                document.getElementById("email").focus();
                            }
                        });
            }
        </script>
    </body>
</html>