<%@page import="java.util.List"%>
<%@page import="model.Customer"%>
<%

    Customer custo = (Customer) request.getAttribute("data");
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Customer Detail</title>
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

            /* Customer Information Table */
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

            .bg-danger {
                background: linear-gradient(135deg, #e74c3c, #c0392b);
            }

            /* Radio Button Styling */
            .radio-group {
                display: flex;
                gap: 20px;
                align-items: center;
            }

            .radio-item {
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .radio-item input[type="radio"] {
                width: 18px;
                height: 18px;
                accent-color: #3498db;
                cursor: not-allowed;
            }

            .radio-item label {
                color: #34495e;
                font-weight: 500;
                cursor: not-allowed;
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

            /* Section Headers */
            .section-header {
                color: #2c3e50;
                font-weight: 700;
                font-size: 1.3rem;
                margin: 30px 0 20px 0;
                display: flex;
                align-items: center;
                border-left: 4px solid #3498db;
                padding-left: 15px;
            }

            .section-header i {
                margin-right: 10px;
                color: #3498db;
            }

            /* Error/No Data Message */
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

                .radio-group {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 10px;
                }

                .btn {
                    width: 100%;
                    min-width: auto;
                }

                .section-header {
                    font-size: 1.1rem;
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

            /* Gender Display */
            .gender-display {
                display: flex;
                gap: 15px;
                align-items: center;
            }

            .gender-item {
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .gender-icon {
                width: 20px;
                height: 20px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 12px;
                color: white;
            }

            .gender-male {
                background: linear-gradient(135deg, #3498db, #2980b9);
            }

            .gender-female {
                background: linear-gradient(135deg, #e91e63, #c2185b);
            }
        </style>
    </head>
    <body>
        <div class="container">
            <% if (custo == null) { %>
                <jsp:include page="../sideBar.jsp" />
                <div class="wrapper">
                    <main class="main-content">
                        <div class="no-data">
                            <i class="fa-solid fa-user-slash"></i>
                            <p>There is no customer with that ID</p>
                            <a href="CustomerListAdmin" class="btn btn-outline-primary">
                                <i class="fa-solid fa-arrow-left"></i> Back to Customer List
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
                                    <h4><i class="fa-solid fa-user"></i> Customer Details</h4>
                                </div>

                                <div class="card-body">
                                    <table class="info-table">
                                        <tr>
                                            <th>Customer ID:</th>
                                            <td><%= custo.getCustomerID()%></td>
                                        </tr>
                                        <tr>
                                            <th>Full Name:</th>
                                            <td><%= custo.getFullName()%></td>
                                        </tr>
                                        <tr>
                                            <th>Phone Number:</th>
                                            <td><%= custo.getPhoneNumber()%></td>
                                        </tr>
                                        <tr>
                                            <th>Email:</th>
                                            <td><%= custo.getEmail()%></td>
                                        </tr>
                                        <tr>
                                            <th>Status:</th>
                                            <td>
                                                <% if (custo.isActive()) { %>
                                                    <span class="badge bg-success">Active</span>
                                                <% } else { %>
                                                    <span class="badge bg-danger">Blocked</span>
                                                <% } %>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Date of Birth:</th>
                                            <td><%= custo.getBirthDate()%></td>
                                        </tr>
                                        <tr>
                                            <th>Gender:</th>
                                            <td>
                                                <div class="gender-display">
                                                    <% if ("male".equalsIgnoreCase(custo.getGender())) { %>
                                                        <div class="gender-item">
                                                            <div class="gender-icon gender-male">
                                                                <i class="fa-solid fa-mars"></i>
                                                            </div>
                                                            <span>Male</span>
                                                        </div>
                                                    <% } else if ("female".equalsIgnoreCase(custo.getGender())) { %>
                                                        <div class="gender-item">
                                                            <div class="gender-icon gender-female">
                                                                <i class="fa-solid fa-venus"></i>
                                                            </div>
                                                            <span>Female</span>
                                                        </div>
                                                    <% } else { %>
                                                        <span class="text-muted">Not specified</span>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                    </table>

                                    <div class="text-center">
                                        <a href="CustomerListAdmin" class="btn btn-outline-primary">
                                            <i class="fa-solid fa-arrow-left"></i> Back to Customer List
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            <% } %>
        </div>
    </body>
</html>