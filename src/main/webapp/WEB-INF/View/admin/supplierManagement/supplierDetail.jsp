<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Suppliers" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Supplier Detail</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    
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

        /* Supplier Information Table */
        .info-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin-bottom: 30px;
            background: #ffffff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .info-table th,
        .info-table td {
            padding: 5px 20px;
            border-bottom: 1px solid #f1f3f4;
            text-align: left;
        }

        .info-table th {
            background: linear-gradient(135deg, #ecf0f1, #d5dbdb);
            color: #2c3e50;
            font-weight: 600;
            font-size: 14px;
            width: 200px;
        }

        .info-table td {
            font-size: 15px;
            color: #34495e;
        }

        .info-table tr:last-child th,
        .info-table tr:last-child td {
            border-bottom: none;
        }

        /* Status Badges */
        .badge {
            padding: 8px 16px;
            border-radius: 25px;
            font-weight: 600;
            font-size: 13px;
            color: #fff;
            display: inline-block;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
        }

        .bg-success {
            background: linear-gradient(135deg, #27ae60, #229954);
        }

        .bg-secondary {
            background: linear-gradient(135deg, #95a5a6, #7f8c8d);
        }

        /* Buttons */
        .btn {
            padding: 12px 20px;
            font-weight: 600;
            font-size: 14px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-decoration: none;
            display: inline-block;
            text-align: center;
            min-width: 120px;
            margin: 0 5px;
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

        .btn-warning {
            background: linear-gradient(135deg, #f39c12, #e67e22);
            color: white;
            border: none;
        }

        .btn-warning:hover {
            background: linear-gradient(135deg, #e67e22, #d35400);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(243, 156, 18, 0.3);
        }

        .btn-danger {
            background: linear-gradient(135deg, #e74c3c, #c0392b);
            color: white;
            border: none;
        }

        .btn-danger:hover {
            background: linear-gradient(135deg, #c0392b, #a93226);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(231, 76, 60, 0.3);
        }

        /* Button Groups */
        .button-group {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 30px;
            flex-wrap: wrap;
            gap: 15px;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        /* Alert styling */
        .alert {
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            border: none;
            font-weight: 500;
        }

        .alert-danger {
            background: linear-gradient(135deg, #fee2e2, #fecaca);
            color: #dc2626;
            border-left: 4px solid #dc2626;
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

            .info-table th {
                width: auto;
                font-size: 13px;
            }

            .info-table th,
            .info-table td {
                padding: 12px 15px;
                font-size: 14px;
            }

            .button-group {
                flex-direction: column;
                align-items: stretch;
            }

            .action-buttons {
                justify-content: center;
                width: 100%;
            }

            .btn {
                width: 100%;
                min-width: auto;
                margin: 5px 0;
            }

            .action-buttons .btn {
                flex: 1;
                max-width: 120px;
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

        /* Form styling */
        .delete-form {
            display: inline-block;
        }

        /* Icon styling */
        .fa {
            margin-right: 8px;
        }

        /* Special styling for supplier specific fields */
        .tax-id {
            font-weight: 600;
            color: #2c3e50;
        }

        .contact-info {
            color: #3498db;
        }

        .description-text {
            font-style: italic;
            color: #7f8c8d;
            line-height: 1.5;
        }
    </style>
</head>
<body>
    <div class="container">
        <%
            Suppliers supplier = (Suppliers) request.getAttribute("supplier");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        %>
        
        <% if (supplier == null) { %>
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="no-data">
                        <i class="fa-solid fa-truck-field"></i>
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
                                <h4><i class="fa-solid fa-truck-field"></i> Supplier Details</h4>
                            </div>

                            <div class="card-body">
                                <table class="info-table">
                                    <tr>
                                        <th>Tax ID:</th>
                                        <td><span class="tax-id"><%= supplier.getTaxID()%></span></td>
                                    </tr>
                                    <tr>
                                        <th>Supplier Name:</th>
                                        <td><%= supplier.getName() %></td>
                                    </tr>
                                    <tr>
                                        <th>Email:</th>
                                        <td><span class="contact-info"><%= supplier.getEmail() %></span></td>
                                    </tr>
                                    <tr>
                                        <th>Phone Number:</th>
                                        <td><span class="contact-info"><%= supplier.getPhoneNumber() %></span></td>
                                    </tr>
                                    <tr>
                                        <th>Address:</th>
                                        <td><%= supplier.getAddress() %></td>
                                    </tr>
                                    <tr>
                                        <th>Contact Person:</th>
                                        <td><%= supplier.getContactPerson() != null ? supplier.getContactPerson() : "N/A" %></td>
                                    </tr>
                                    <tr>
                                        <th>Description:</th>
                                        <td>
                                            <% if (supplier.getDescription() != null && !supplier.getDescription().trim().isEmpty()) { %>
                                                <span class="description-text"><%= supplier.getDescription() %></span>
                                            <% } else { %>
                                                <span class="text-muted">N/A</span>
                                            <% } %>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Status:</th>
                                        <td>
                                            <% if (supplier.getIsActive() == true) { %>
                                                <span class="badge bg-success">Active</span>
                                            <% } else { %>
                                                <span class="badge bg-secondary">Inactive</span>
                                            <% } %>
                                        </td>
                                    </tr>
                                </table>

                                <div class="button-group">
                                    <a href="ViewSupplier" class="btn btn-outline-primary">
                                         Back to List
                                    </a>
                                    
                                    <div class="action-buttons">
                                        <a href="UpdateSupplier?id=<%= supplier.getSupplierID() %>" class="btn btn-warning">
                                             Edit
                                        </a>
                                        
                                        <form class="delete-form" action="DeleteSupplier" method="post">
                                            <input type="hidden" name="supplierID" value="<%= supplier.getSupplierID() %>"/>
                                            <input type="hidden" name="taxId" value="<%= supplier.getTaxID()%>"/>
                                            <button type="button" class="btn btn-danger delete-btn"
                                                data-supplier-id="<%= supplier.getSupplierID() %>"
                                                data-tax-id="<%= supplier.getTaxID()%>"
                                                data-supplier-name="<%= supplier.getName() %>">
                                                 Delete
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        <% } %>
    </div>

    <script>
        window.onload = function () {
            // SweetAlert delete confirmation
            const deleteBtns = document.querySelectorAll('.delete-btn');
            deleteBtns.forEach(function(btn) {
                btn.onclick = function(e) {
                    e.preventDefault();
                    const form = btn.closest('form');
                    const supplierId = btn.getAttribute('data-supplier-id');
                    const taxId = btn.getAttribute('data-tax-id');
                    const supplierName = btn.getAttribute('data-supplier-name');
                    
                    Swal.fire({
                        title: 'Are you sure?',
                        html: `Do you want to delete supplier <strong>"${supplierName}"</strong>?<br><small>Tax ID: ${taxId}</small>`,
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#d33',
                        cancelButtonColor: '#3085d6',
                        confirmButtonText: '<i class="fa-solid fa-trash"></i> Yes, delete it!',
                        cancelButtonText: '<i class="fa-solid fa-times"></i> Cancel',
                        customClass: {
                            confirmButton: 'btn btn-danger',
                            cancelButton: 'btn btn-secondary'
                        },
                        buttonsStyling: false
                    }).then((result) => {
                        if (result.isConfirmed) {
                            // Show loading
                            Swal.fire({
                                title: 'Deleting...',
                                text: 'Please wait while we delete the supplier.',
                                allowOutsideClick: false,
                                didOpen: () => {
                                    Swal.showLoading();
                                }
                            });
                            form.submit();
                        }
                    });
                }
            });
        };
    </script>
</body>
</html>