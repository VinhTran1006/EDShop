<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Suppliers" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Add Supplier</title>
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
                max-width: 800px;
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
                display: flex;
                align-items: center;
            }

            .card-header i {
                margin-right: 12px;
                color: #3498db;
                font-size: 1.3rem;
            }

            .card-body {
                padding: 30px;
            }

            /* Alert Styling */
            .alert {
                border-radius: 10px;
                border: none;
                padding: 15px 20px;
                margin-bottom: 25px;
                box-shadow: 0 2px 10px rgba(231, 76, 60, 0.2);
                font-weight: 500;
            }

            .alert-danger {
                background: linear-gradient(135deg, #e74c3c, #c0392b);
                color: white;
            }

            /* Form Table */
            .supplier-form-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                margin-bottom: 30px;
            }

            .supplier-form-table th {
                background: linear-gradient(135deg, #ecf0f1, #d5dbdb);
                color: #2c3e50;
                font-weight: 600;
                font-size: 14px;
                padding: 5px 20px;
                border-bottom: 1px solid #f1f3f4;
                text-align: left;
                width: 200px;
                vertical-align: middle;
            }

            .supplier-form-table td {
                padding: 5px 20px;
                border-bottom: 1px solid #f1f3f4;
                vertical-align: middle;
            }

            .supplier-form-table tr:last-child th,
            .supplier-form-table tr:last-child td {
                border-bottom: none;
            }

            /* Form Inputs */
            .supplier-input {
                border: 2px solid #e0e6ed;
                border-radius: 8px;
                padding: 5px 15px;
                font-size: 14px;
                transition: all 0.3s ease;
                background: #ffffff;
                color: #34495e;
                box-shadow: 0 2px 4px rgba(0,0,0,0.02);
            }

            .supplier-input:focus {
                outline: none;
                border-color: #3498db;
                box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
                background: #fdfdfd;
            }

            .supplier-input:valid {
                border-color: #27ae60;
            }

            .supplier-input:invalid:not(:focus):not(:placeholder-shown) {
                border-color: #e74c3c;
            }

            /* Textarea Styling */
            textarea.supplier-input {
                resize: vertical;
                min-height: 100px;
            }

            /* Radio Button Styling */
            .form-check {
                margin: 0;
            }

            .form-check-input {
                width: 18px;
                height: 18px;
                margin-top: 0.125em;
                margin-right: 8px;
                border: 2px solid #3498db;
                background: white;
                cursor: pointer;
                transition: all 0.2s ease;
            }

            .form-check-input:checked {
                background: linear-gradient(135deg, #3498db, #2980b9);
                border-color: #3498db;
            }

            .form-check-input:focus {
                box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
                outline: none;
            }

            .form-check-label {
                color: #34495e;
                font-weight: 500;
                cursor: pointer;
                margin-left: 5px;
            }

            .form-check-inline {
                display: inline-flex;
                align-items: center;
                margin-right: 20px;
            }

            /* Button Styling */
            .btn {
                padding: 12px 24px;
                font-weight: 600;
                font-size: 14px;
                border: none;
                border-radius: 10px;
                cursor: pointer;
                transition: all 0.3s ease;
                text-decoration: none;
                display: inline-block;
                text-align: center;
                min-width: 120px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }

            .btn-success {
                background: linear-gradient(135deg, #27ae60, #229954);
                color: white;
                border: 2px solid transparent;
            }

            .btn-success:hover {
                background: linear-gradient(135deg, #229954, #1e8449);
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(39, 174, 96, 0.3);
            }

            .btn-secondary {
                background: transparent;
                color: #7f8c8d;
                border: 2px solid #7f8c8d;
            }

            .btn-secondary:hover {
                background: linear-gradient(135deg, #7f8c8d, #6c7b7d);
                color: white;
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(127, 140, 141, 0.3);
            }

            /* Button Container */
            .button-container {
                display: flex;
                gap: 15px;
                justify-content: flex-end;
                margin-top: 30px;
                padding-top: 20px;
                border-top: 2px solid #ecf0f1;
            }

            /* Section Headers for form groups */
            .form-section {
                margin-bottom: 25px;
            }

            .form-section-header {
                color: #2c3e50;
                font-weight: 600;
                font-size: 1.1rem;
                margin-bottom: 15px;
                display: flex;
                align-items: center;
                border-left: 4px solid #3498db;
                padding-left: 15px;
            }

            .form-section-header i {
                margin-right: 10px;
                color: #3498db;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                main.main-content {
                    margin-left: 0;
                    padding: 15px;
                }

                .card {
                    max-width: 100%;
                    margin: 10px;
                }

                .card-body {
                    padding: 20px;
                }

                .supplier-form-table th {
                    width: auto;
                    font-size: 13px;
                    padding: 15px;
                }

                .supplier-form-table th,
                .supplier-form-table td {
                    padding: 15px;
                }

                .button-container {
                    flex-direction: column;
                }

                .btn {
                    width: 100%;
                    min-width: auto;
                }

                .form-check-inline {
                    display: block;
                    margin-bottom: 10px;
                    margin-right: 0;
                }
            }

            /* Form Validation Styling */
            .invalid-feedback {
                display: block;
                color: #e74c3c;
                font-size: 13px;
                margin-top: 5px;
                font-weight: 500;
            }


            /* Input Group for better organization */
            .input-group {
                position: relative;
            }

            .input-icon {
                position: absolute;
                right: 15px;
                top: 50%;
                transform: translateY(-50%);
                color: #bdc3c7;
                z-index: 2;
            }

            .supplier-input:focus + .input-icon {
                color: #3498db;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container mt-5">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-truck"></i> Add Supplier</h4>
                            </div>

                            <div class="card-body">
                                <% if (request.getAttribute("errorMsg") != null) {%>
                                <div class="alert alert-danger">
                                    <i class="fa-solid fa-triangle-exclamation me-2"></i>
                                    <%= request.getAttribute("errorMsg")%>
                                </div>
                                <% }%>

                                <form method="post" action="CreateSupplier">
                                    <div class="form-section">
                                        <table class="supplier-form-table">
                                            <tr>
                                                <th class="required">Tax ID:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="taxId" class="form-control supplier-input" 
                                                               required pattern="^[0-9]{8,15}$" 
                                                               title="Tax ID chỉ được chứa số, độ dài 8–15 ký tự"
                                                               placeholder="Enter tax identification number">
                                                        
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Company Name:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="name" class="form-control supplier-input" 
                                                               required minlength="3" maxlength="100"
                                                               placeholder="Enter company name">
                                                        
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Email:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="email" name="email" class="form-control supplier-input" 
                                                               required placeholder="Enter email address">
                                                        
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Phone Number:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="phoneNumber" class="form-control supplier-input" 
                                                               required pattern="^[0-9+\-\s]{8,15}$" 
                                                               title="Số điện thoại chỉ được chứa số, dấu +, dấu - và khoảng trắng"
                                                               placeholder="Enter phone number">
                                                       
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Address:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="address" class="form-control supplier-input" 
                                                               required minlength="5" maxlength="200"
                                                               pattern="^[a-zA-Z0-9\s,.\-\/]+$"
                                                               title="Chỉ cho phép chữ cái, số, khoảng trắng, dấu , . - /"
                                                               placeholder="Enter full address">
                                                        
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Contact Person:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="contactPerson" class="form-control supplier-input" 
                                                               required minlength="3" maxlength="50"
                                                               pattern="^[a-zA-Z\s]+$"
                                                               title="Chỉ cho phép chữ cái và khoảng trắng"
                                                               placeholder="Enter contact person name">
                                                        
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Description:</th>
                                                <td>
                                                    <textarea name="description" class="form-control supplier-input" rows="4" 
                                                              required minlength="5" maxlength="500"
                                                              placeholder="Enter supplier description and services offered..."></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Status:</th>
                                                <td>
                                                    <div class="form-check form-check-inline">
                                                        <input type="radio" name="activate" id="active" value="1" class="form-check-input" required checked>
                                                        <label for="active" class="form-check-label">
                                                            Activate
                                                        </label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input type="radio" name="activate" id="inactive" value="0" class="form-check-input" required>
                                                        <label for="inactive" class="form-check-label">
                                                            Deactivate
                                                        </label>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>

                                    <div class="button-container">
                                        <button type="submit" class="btn btn-success">
                                            Create Supplier
                                        </button>
                                        <a href="ViewSupplier" class="btn btn-secondary">
                                            Cancel
                                        </a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>