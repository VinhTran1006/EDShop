<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Suppliers" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Edit Supplier</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
        }

        .container {
            background: transparent;
        }

        main.main-content {
            flex: 1;
            margin-left: 220px;
            min-height: 100vh;
            box-sizing: border-box;
            padding: 20px;
        }

        .wrapper {
            width: 100%;
            max-width: 100%;
            margin: 0 auto;
            margin-left: -30px;
            background: transparent;
        }

        .card {
            background: #ffffff;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            overflow: hidden;
            margin: 0 auto;
            max-width: 900px;
        }

        .card-header {
            background: linear-gradient(135deg, #34495e, #2c3e50);
            color: #ffffff;
            padding: 5px 20px;
        }

        .card-header h4 {
            font-weight: 700;
            font-size: 1.5rem;
            letter-spacing: 0.5px;
            margin: 0;
        }

        .card-header i {
            margin-right: 10px;
            color: #3498db;
        }

        .card-body {
            padding: 30px;
        }

        /* Form Table Styling */
        .form-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin-bottom: 30px;
            background: #ffffff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .form-table th,
        .form-table td {
            padding: 5px 20px;
            border-bottom: 1px solid #f1f3f4;
            text-align: left;
            vertical-align: top;
        }

        .form-table th {
            background: linear-gradient(135deg, #ecf0f1, #d5dbdb);
            color: #2c3e50;
            font-weight: 600;
            font-size: 14px;
            width: 200px;
        }

        .form-table td {
            color: #34495e;
        }

        .form-table tr:last-child th,
        .form-table tr:last-child td {
            border-bottom: none;
        }

        /* Form Controls */
        .form-control {
            width: 100%;
            padding: 5px 15px;
            border: 2px solid #e9ecef;
            border-radius: 8px;
            font-size: 14px;
            color: #2c3e50;
            transition: all 0.3s ease;
            background: #ffffff;
        }

        .form-control:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
            transform: translateY(-1px);
        }

        .form-control[readonly] {
            background: #f8f9fa;
            cursor: not-allowed;
            color: #6c757d;
        }

        /* Textarea */
        textarea.form-control {
            min-height: 100px;
            resize: vertical;
        }

        /* Radio Button Styling */
        .radio-group {
            display: flex;
            gap: 20px;
            align-items: center;
            flex-wrap: wrap;
        }

        .form-check {
            display: flex;
            align-items: center;
            gap: 8px;
            margin: 0;
        }

        .form-check-input {
            width: 18px;
            height: 18px;
            border: 2px solid #3498db;
            border-radius: 50%;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .form-check-input:checked {
            background-color: #3498db;
            border-color: #3498db;
        }

        .form-check-label {
            color: #34495e;
            font-weight: 500;
            cursor: pointer;
            user-select: none;
        }

        /* Alert Messages */
        .alert {
            padding: 15px 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            border: none;
            font-weight: 500;
            display: flex;
            align-items: center;
        }

        .alert-danger {
            background: linear-gradient(135deg, #fee2e2, #fecaca);
            color: #dc2626;
            border-left: 4px solid #dc2626;
        }

        .alert-success {
            background: linear-gradient(135deg, #d1fae5, #a7f3d0);
            color: #059669;
            border-left: 4px solid #059669;
        }

        .alert i {
            margin-right: 10px;
            font-size: 1.1em;
        }

        /* Buttons */
        .btn {
            padding: 12px 24px;
            font-weight: 600;
            font-size: 14px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-decoration: none;
            display: inline-block;
            text-align: center;
            min-width: 100px;
            margin: 0 5px;
        }

        .btn-success {
            background: linear-gradient(135deg, #27ae60, #229954);
            color: white;
        }

        .btn-success:hover {
            background: linear-gradient(135deg, #229954, #1e8449);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(39, 174, 96, 0.3);
        }

        .btn-secondary {
            background: linear-gradient(135deg, #95a5a6, #7f8c8d);
            color: white;
        }

        .btn-secondary:hover {
            background: linear-gradient(135deg, #7f8c8d, #6c7b7d);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(149, 165, 166, 0.3);
        }

        .btn-outline-primary {
            background: transparent;
            color: #3498db;
            border: 2px solid #3498db;
        }

        .btn-outline-primary:hover {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
        }

        /* Button Groups */
        .button-group {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 10px;
            margin-top: 30px;
            flex-wrap: wrap;
        }

        /* Form Validation */
        .form-control.is-invalid {
            border-color: #e74c3c;
            box-shadow: 0 0 0 3px rgba(231, 76, 60, 0.1);
        }

        .form-control.is-valid {
            border-color: #27ae60;
            box-shadow: 0 0 0 3px rgba(39, 174, 96, 0.1);
        }

        /* Required Field Indicator */
        .required::after {
            content: " *";
            color: #e74c3c;
            font-weight: bold;
        }

        /* No Data Message */
        .no-data {
            text-align: center;
            padding: 40px 20px;
            color: #7f8c8d;
            font-size: 1.1rem;
            background: #ffffff;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            max-width: 600px;
            margin: 0 auto;
        }

        .no-data i {
            font-size: 3rem;
            color: #bdc3c7;
            margin-bottom: 15px;
            display: block;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            main.main-content {
                margin-left: 0;
                padding: 15px;
            }

            .wrapper {
                margin-left: 0;
            }

            .card {
                max-width: 100%;
                margin: 10px;
            }

            .card-body {
                padding: 20px;
            }

            .form-table th {
                width: auto;
                font-size: 13px;
            }

            .form-table th,
            .form-table td {
                padding: 12px 15px;
                font-size: 14px;
                display: block;
                width: 100%;
                border-bottom: none;
            }

            .form-table th {
                background: #f8f9fa;
                margin-top: 15px;
                margin-bottom: 5px;
                border-radius: 5px;
                padding: 8px 15px;
            }

            .form-table tr:first-child th {
                margin-top: 0;
            }

            .radio-group {
                flex-direction: column;
                align-items: flex-start;
                gap: 10px;
            }

            .button-group {
                flex-direction: column;
                align-items: stretch;
            }

            .btn {
                width: 100%;
                margin: 5px 0;
            }
        }

        /* Animation for page load */
        .card {
            animation: slideInUp 0.5s ease-out;
        }

        @keyframes slideInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Form Focus Animation */
        .form-control {
            position: relative;
        }

        .form-control:focus {
            animation: focusPulse 0.3s ease-out;
        }

        @keyframes focusPulse {
            0% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.02);
            }
            100% {
                transform: scale(1);
            }
        }

        /* Status indicators */
        .status-active {
            color: #27ae60;
        }

        .status-inactive {
            color: #e74c3c;
        }
    </style>
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
                    <div class="container mt-5">
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
                                            <i class="fa-solid fa-times"></i> Cancel
                                        </a>
                                        <button type="submit" class="btn btn-success">
                                            <i class="fa-solid fa-save"></i> Save Changes
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