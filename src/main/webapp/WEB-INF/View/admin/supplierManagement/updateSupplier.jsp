<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Suppliers" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Supplier</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">
</head>
<body>
    <div class="container">
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            String successMessage = (String) request.getAttribute("successMessage");
            Suppliers supplier = (Suppliers) request.getAttribute("supplier");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        %>
        
        <% if (supplier == null) { %>
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="no-data">
                        <i class="fa-solid fa-truck-field-un"></i>
                        <p>Supplier not found!</p>
                        <a href="ViewSupplier" class="btn btn-outline-primary">
                            <i class="fa-solid fa-arrow-left"></i> Back to Supplier List
                        </a>
                    </div>
                </main>
            </div>
        <% } else { %>
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-edit"></i> Edit Supplier</h4>
                            </div>

                            <div class="card-body">
                                <% if (errorMessage != null) { %>
                                    <div class="alert alert-danger">
                                        <i class="fa-solid fa-exclamation-circle"></i>
                                        <%= errorMessage %>
                                    </div>
                                <% } %>
                                
                                <% if (successMessage != null) { %>
                                    <div class="alert alert-success">
                                        <i class="fa-solid fa-check-circle"></i>
                                        <%= successMessage %>
                                    </div>
                                <% } %>

                                <form method="post" action="UpdateSupplier" id="supplierForm">
                                    <input type="hidden" name="supplierID" value="<%= supplier.getSupplierID() %>">

                                    <table class="form-table">
                                        <tr>
                                            <th>Tax ID:</th>
                                            <td>
                                                <input type="text" name="taxId" class="form-control" 
                                                       value="<%= supplier.getTaxID() %>" readonly>
                                                <small class="text-muted">Tax ID cannot be modified</small>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th class="required">Supplier Name:</th>
                                            <td>
                                                <input type="text" name="name" class="form-control" 
                                                       value="<%= supplier.getName() %>" required
                                                       placeholder="Enter supplier name">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Email:</th>
                                            <td>
                                                <input type="email" name="email" class="form-control" 
                                                       value="<%= supplier.getEmail() != null ? supplier.getEmail() : "" %>"
                                                       placeholder="Enter email address">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Phone Number:</th>
                                            <td>
                                                <input type="tel" name="phoneNumber" class="form-control" 
                                                       value="<%= supplier.getPhoneNumber() != null ? supplier.getPhoneNumber() : "" %>"
                                                       placeholder="Enter phone number">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Address:</th>
                                            <td>
                                                <input type="text" name="address" class="form-control" 
                                                       value="<%= supplier.getAddress() != null ? supplier.getAddress() : "" %>"
                                                       placeholder="Enter address">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Contact Person:</th>
                                            <td>
                                                <input type="text" name="contactPerson" class="form-control" 
                                                       value="<%= supplier.getContactPerson() != null ? supplier.getContactPerson() : "" %>"
                                                       placeholder="Enter contact person name">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Description:</th>
                                            <td>
                                                <textarea name="description" class="form-control" 
                                                          placeholder="Enter description (optional)"><%= supplier.getDescription() != null ? supplier.getDescription() : "" %></textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Status:</th>
                                            <td>
                                                <div class="radio-group">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="radio" name="activate" 
                                                               value="1" id="activate" 
                                                               <%= supplier.getIsActive() == true ? "checked" : "" %> disabled>
                                                        <label class="form-check-label status-active" for="activate">
                                                             Active
                                                        </label>
                                                    </div>
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="radio" name="activate" 
                                                               value="0" id="deactivate" 
                                                               <%= supplier.getIsActive() == false ? "checked" : "" %> disabled>
                                                        <label class="form-check-label status-inactive" for="deactivate">
                                                             Inactive
                                                        </label>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>

                                    <div class="button-group">
                                        <a href="ViewSupplier" class="btn btn-secondary">
                                            Cancel
                                        </a>
                                        <button type="submit" class="btn btn-success">
                                            Save Changes
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        <% } %>
    </div>

    <script>
        // Form validation and enhancement
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('supplierForm');
            const nameField = document.querySelector('input[name="name"]');
            const emailField = document.querySelector('input[name="email"]');
            
            // Real-time validation
            nameField.addEventListener('input', function() {
                if (this.value.trim().length < 2) {
                    this.classList.add('is-invalid');
                    this.classList.remove('is-valid');
                } else {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                }
            });
            
            emailField.addEventListener('input', function() {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (this.value && !emailRegex.test(this.value)) {
                    this.classList.add('is-invalid');
                    this.classList.remove('is-valid');
                } else if (this.value) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                } else {
                    this.classList.remove('is-invalid', 'is-valid');
                }
            });
            
            // Form submission validation
            form.addEventListener('submit', function(e) {
                let isValid = true;
                
                // Check required fields
                if (nameField.value.trim().length < 2) {
                    nameField.classList.add('is-invalid');
                    isValid = false;
                }
                
                // Check email format if provided
                if (emailField.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailField.value)) {
                    emailField.classList.add('is-invalid');
                    isValid = false;
                }
                
                if (!isValid) {
                    e.preventDefault();
                    // Scroll to first invalid field
                    const firstInvalid = document.querySelector('.is-invalid');
                    if (firstInvalid) {
                        firstInvalid.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        firstInvalid.focus();
                    }
                }
            });
            
            // Auto-hide alerts after 5 seconds
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.opacity = '0';
                    alert.style.transform = 'translateY(-20px)';
                    setTimeout(() => {
                        alert.remove();
                    }, 300);
                }, 5000);
            });
        });
    </script>
</body>
</html>