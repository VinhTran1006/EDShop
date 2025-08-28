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
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Arial', sans-serif;
            }

            .card {
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                border: none;
                max-width: 800px;
                margin: 0 auto;
            }

            .card-header {
                border-radius: 10px 10px 0 0 !important;
                padding: 0.75rem 1.5rem;
                background-color: #0d6efd;
                color: white;
                font-size: 1.25rem;
                font-weight: 600;
            }

            .form-section {
                margin-bottom: 2rem;
            }

            .section-title {
                color: #000000;
                border-bottom: 1px solid #dee2e6;
                padding-bottom: 0.5rem;
                margin-bottom: 1.5rem;
                font-weight: 600;
            }

            .form-label {
                font-weight: 600;
                color: #000000;
                margin-bottom: 0.25rem;
            }

            .form-control, .form-select {
                border-radius: 5px;
                padding: 0.5rem 0.75rem;
                border: 1px solid #ced4da;
            }

            .form-control:focus, .form-select:focus {
                border-color: #80bdff;
                box-shadow: 0 0 0 0.25rem rgba(0, 123, 255, 0.25);
            }

            .btn {
                border-radius: 5px;
                padding: 0.5rem 1.5rem;
                font-weight: 500;
            }

            .btn-primary {
                background-color: #28a745;
                border: none;
                color: white;
            }

            .btn-primary:focus,
            .btn-primary:active,
            .btn-primary:hover {
                background-color: #28a745 !important;
                border: none !important;
                color: white !important;
                box-shadow: none !important;
            }

            .btn-secondary {
                background-color: #ced4da;
                border-color: #ced4da;
                color: white;
            }

            .btn-secondary:hover {
                background-color: #adb5bd;
                color: white;
            }

            .text-danger {
                font-size: 0.85rem;
            }

            .alert {
                border-radius: 5px;
            }

            .readonly {
                background-color: #f8f9fa;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <div class="card mx-auto shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Edit Staff</h4>
                </div>
                <div class="card-body">
                    <% if (request.getAttribute("errorMessage") != null) {%>
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i><%= request.getAttribute("errorMessage")%>
                    </div>
                    <% }%>

                    <% if (staff == null) {%>
                    <div class="alert alert-warning" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>Staff not found.
                    </div>
                    <div class="text-center">
                        <a href="StaffList" class="btn btn-secondary">Back to Staff List</a>
                    </div>
                    <% } else { %>

                    <form action="UpdateStaffServlet" method="post" class="row g-3" id="updateForm">
                        <!-- Hidden Staff ID -->
                        <input type="hidden" name="staffID" value="<%= staff.getStaffID()%>">

                        <!-- Account Information -->
                        <div class="col-12 form-section">
                            <h5 class="section-title"><i class="fas fa-user-circle me-2"></i>Account Information</h5>
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="email" class="form-label">Email:</label>
                                    <input type="email" id="email" name="email" class="form-control" 
                                           value="<%= staff.getEmail() != null ? staff.getEmail() : ""%>" required>
                                    <div id="emailError" class="text-danger small mt-1"></div>
                                    <input type="hidden" id="currentEmail" value="<%= staff.getEmail() != null ? staff.getEmail() : ""%>">
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="staffId" class="form-label">Staff ID:</label>
                                    <input type="text" id="staffId" class="form-control readonly" 
                                           value="<%= staff.getStaffID()%>" readonly>
                                </div>
                            </div>
                        </div>

                        <!-- Staff Information -->
                        <div class="col-12 form-section">
                            <h5 class="section-title"><i class="fas fa-id-card me-2"></i>Staff Information</h5>
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="fullName" class="form-label">Full Name:</label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" 
                                           value="<%= staff.getFullName() != null ? staff.getFullName() : ""%>" required>
                                    <div id="fullNameError" class="text-danger small mt-1"></div>
                                </div>

                                <div class="col-md-6">
                                    <label for="phoneNumber" class="form-label">Phone Number:</label>
                                    <input type="text" id="phoneNumber" name="phoneNumber" class="form-control" 
                                           value="<%= staff.getPhoneNumber() != null ? staff.getPhoneNumber() : ""%>">
                                    <div id="phoneError" class="text-danger small mt-1"></div>
                                    <input type="hidden" id="currentPhone" value="<%= staff.getPhoneNumber() != null ? staff.getPhoneNumber() : ""%>">
                                </div>

                                <div class="col-md-6">
                                    <label for="birthDate" class="form-label">Birth Date:</label>
                                    <input type="date" id="birthDate" name="birthDate" class="form-control" 
                                           value="<%= staff.getBirthDate() != null ? sdf.format(staff.getBirthDate()) : ""%>">
                                    <div id="birthDateError" class="text-danger small mt-1"></div>
                                </div>

                                <div class="col-md-6">
                                    <label for="gender" class="form-label">Gender:</label>
                                    <select id="gender" name="gender" class="form-select">
                                        <option value="">Select Gender</option>
                                        <option value="Male" <%= "Male".equals(staff.getGender()) ? "selected" : ""%>>Male</option>
                                        <option value="Female" <%= "Female".equals(staff.getGender()) ? "selected" : ""%>>Female</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label for="hiredDate" class="form-label">Hired Date:</label>
                                    <input type="date" id="hiredDate" name="hiredDate" class="form-control" 
                                           value="<%= staff.getHiredDate() != null ? sdf.format(staff.getHiredDate()) : ""%>">
                                </div>

                                <div class="col-md-6">
                                    <label for="status" class="form-label">Status:</label>
                                    <select id="status" name="isActive" class="form-select">
                                        <option value="true" <%= staff.isActive() ? "selected" : ""%>>Active</option>
                                        <option value="false" <%= !staff.isActive() ? "selected" : ""%>>Inactive</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label for="role" class="form-label">Role:</label>
                                    <input type="text" id="role" class="form-control readonly" 
                                           value="<%= staff.getRole() != null ? staff.getRole() : ""%>" readonly>
                                </div>
                            </div>
                        </div>

                        <div class="col-12 mt-4 text-end">
                            <button type="submit" class="btn btn-primary me-2" id="submitBtn">Update</button>
                            <a href="StaffList" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                    <% } %>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const submitBtn = document.getElementById("submitBtn");
            const currentEmail = document.getElementById("currentEmail").value;
            const currentPhone = document.getElementById("currentPhone").value;

            // ===== EMAIL VALIDATION =====
            const emailInput = document.getElementById("email");
            const emailError = document.getElementById("emailError");

            emailInput.addEventListener("blur", function () {
                const email = emailInput.value.trim();
                const emailPattern = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
                emailError.textContent = "";
                submitBtn.disabled = false;

                if (!email) {
                    emailError.textContent = "Email is required.";
                    submitBtn.disabled = true;
                    return;
                }

                if (!emailPattern.test(email)) {
                    emailError.textContent = "Email must be a valid Gmail address.";
                    submitBtn.disabled = true;
                    return;
                }

                // Only check for duplicates if email changed
                if (email !== currentEmail) {
                    fetch("CheckEmailServlet?email=" + encodeURIComponent(email))
                            .then(response => response.text())
                            .then(data => {
                                if (data === "EXISTS") {
                                    emailError.textContent = "This email already exists.";
                                    submitBtn.disabled = true;
                                }
                            })
                            .catch(error => {
                                console.error("Error checking email:", error);
                                emailError.textContent = "Error checking email availability.";
                                submitBtn.disabled = true;
                            });
                }
            });

            emailInput.addEventListener("input", function () {
                emailError.textContent = "";
                submitBtn.disabled = false;
            });

            // ===== PHONE VALIDATION =====
            const phoneInput = document.getElementById("phoneNumber");
            const phoneError = document.getElementById("phoneError");

            phoneInput.addEventListener("blur", function () {
                const phone = phoneInput.value.trim();
                phoneError.textContent = "";
                submitBtn.disabled = false;

                if (phone) {
                    const phonePattern = /^0\d{9}$/;
                    
                    if (!phonePattern.test(phone)) {
                        phoneError.textContent = "Phone must start with 0 and have exactly 10 digits.";
                        submitBtn.disabled = true;
                        return;
                    }

                    // Only check for duplicates if phone changed
                    if (phone !== currentPhone) {
                        fetch("CheckPhoneServlet?phone=" + encodeURIComponent(phone))
                                .then(response => response.text())
                                .then(data => {
                                    if (data === "EXISTS") {
                                        phoneError.textContent = "Phone number already exists.";
                                        submitBtn.disabled = true;
                                    }
                                })
                                .catch(error => {
                                    console.error("Error checking phone:", error);
                                    phoneError.textContent = "Error checking phone availability.";
                                    submitBtn.disabled = true;
                                });
                    }
                }
            });

            phoneInput.addEventListener("input", function () {
                phoneError.textContent = "";
                submitBtn.disabled = false;
            });

            // ===== BIRTH DATE VALIDATION =====
            const birthInput = document.getElementById("birthDate");
            const birthError = document.getElementById("birthDateError");

            birthInput.addEventListener("blur", function () {
                const birthDateValue = this.value;
                birthError.textContent = "";
                submitBtn.disabled = false;

                if (birthDateValue) {
                    const birthDate = new Date(birthDateValue);
                    const today = new Date();
                    
                    if (isNaN(birthDate.getTime())) {
                        birthError.textContent = "Invalid date format.";
                        submitBtn.disabled = true;
                        return;
                    }

                    let age = today.getFullYear() - birthDate.getFullYear();
                    const monthDiff = today.getMonth() - birthDate.getMonth();

                    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                        age--;
                    }

                    if (age < 18) {
                        birthError.textContent = "Staff must be at least 18 years old.";
                        submitBtn.disabled = true;
                    }
                }
            });

            // ===== FULL NAME VALIDATION =====
            const fullNameInput = document.getElementById("fullName");
            const fullNameError = document.getElementById("fullNameError");

            fullNameInput.addEventListener("blur", function () {
                let name = fullNameInput.value.trim();
                fullNameError.textContent = "";
                submitBtn.disabled = false;

                if (!name) {
                    fullNameError.textContent = "Full name is required.";
                    submitBtn.disabled = true;
                    return;
                }

                // Clean up multiple spaces
                name = name.replace(/\s+/g, " ");
                fullNameInput.value = name;

                // Validate name format (Vietnamese names with accents)
                const namePattern = /^([A-ZÀ-ỸĂÂÊÔÔƯa-zà-ỹăâêôơư]+)(\s[A-ZÀ-ỸĂÂÊÔÔƯa-zà-ỹăâêôơư]+)*$/u;

                if (!namePattern.test(name)) {
                    fullNameError.textContent = "Names must start with capital letters and contain no numbers or special characters.";
                    submitBtn.disabled = true;
                }
            });

            fullNameInput.addEventListener("input", function () {
                fullNameError.textContent = "";
                submitBtn.disabled = false;
            });

            // ===== FORM SUBMISSION VALIDATION =====
            document.getElementById("updateForm").addEventListener("submit", function (e) {
                // Check if submit button is disabled
                if (submitBtn.disabled) {
                    e.preventDefault();
                    alert("Please fix all validation errors before submitting.");
                    return false;
                }

                // Final validation check
                const email = emailInput.value.trim();
                const fullName = fullNameInput.value.trim();

                if (!email || !fullName) {
                    e.preventDefault();
                    alert("Please fill in all required fields.");
                    return false;
                }
            });
        </script>
    </body>
</html>