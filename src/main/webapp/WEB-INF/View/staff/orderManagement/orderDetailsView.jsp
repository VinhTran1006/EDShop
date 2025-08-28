<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Order Details</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css" />

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
                padding: 0px 30px;
            }

            /* Order Information Table */
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
                padding: 5px 10px;
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

            /* Items Table */
            .items-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                background: #ffffff;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 2px 10px rgba(0,0,0,0.05);
                margin-bottom: 30px;
            }

            .items-table th {
                background: linear-gradient(135deg, #ecf0f1, #d5dbdb);
                color: #2c3e50;
                font-weight: 600;
                padding: 5px 20px;
                text-align: center;
                border-bottom: 1px solid #f1f3f4;
            }

            .items-table td {
                padding: 15px 20px;
                border-bottom: 1px solid #f1f3f4;
                color: #34495e;
                text-align: center;
            }

            .items-table td.text-start {
                text-align: left;
            }

            .items-table tr:hover {
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
            }

            .items-table tr:last-child td {
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

            .bg-warning {
                background: linear-gradient(135deg, #f39c12, #e67e22);
            }

            .bg-primary {
                background: linear-gradient(135deg, #3498db, #2980b9);
            }

            .status-3 {
                background: linear-gradient(135deg, #6366f1, #4f46e5);
            }

            .bg-success {
                background: linear-gradient(135deg, #27ae60, #229954);
            }

            .bg-danger {
                background: linear-gradient(135deg, #e74c3c, #c0392b);
            }

            /* Section Headers */
            .section-header {
                color: #2c3e50;
                font-weight: 700;
                font-size: 1.3rem;
                margin: 5px 0 5px 0;
                display: flex;
                align-items: center;
                border-left: 4px solid #3498db;
                padding-left: 15px;
            }

            .section-header i {
                margin-right: 10px;
                color: #3498db;
            }

            /* Management Form */
            .management-section {
                background: linear-gradient(135deg, #f8f9fa, #e9ecef);
                padding: 5px 25px;
                border-radius: 10px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            }

            .form-controls {
                display: flex;
                gap: 15px;
                align-items: center;
                flex-wrap: wrap;
                margin-top: 20px;
            }

            .form-select {
                padding: 12px 16px;
                font-size: 15px;
                border-radius: 10px;
                border: 2px solid #e8ecef;
                background: #ffffff;
                transition: all 0.3s ease;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);
                min-width: 200px;
            }

            .form-select:focus {
                border-color: #3498db;
                outline: none;
                box-shadow: 0 4px 12px rgba(52, 152, 219, 0.15);
                transform: translateY(-1px);
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

            .btn-success {
                background: linear-gradient(135deg, #27ae60, #229954);
                color: white;
            }

            .btn-success:hover {
                background: linear-gradient(135deg, #229954, #1e8449);
                transform: translateY(-2px);
                box-shadow: 0 4px 15px rgba(39, 174, 96, 0.3);
                color: white;
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
                text-decoration: none;
            }

            /* Alert Messages */
            .alert {
                border-radius: 10px;
                border: none;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                padding: 15px 20px;
                margin: 15px 0;
            }

            .alert-danger {
                background: linear-gradient(135deg, #fadbd8, #f1948a);
                color: #922b21;
                border-left: 4px solid #e74c3c;
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
                .info-table td,
                .items-table th,
                .items-table td {
                    padding: 12px 15px;
                    font-size: 14px;
                }

                .form-controls {
                    flex-direction: column;
                    width: 100%;
                }

                .form-select,
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
        </style>
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <jsp:include page="../header.jsp" />
                    <div class="container mt-5">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-receipt"></i> Order Details</h4>
                            </div>

                            <div class="card-body">
                                <table class="info-table">
                                    <tr><th>Order ID:</th><td>${data.orderID}</td></tr>
                                    <tr><th>Order Date:</th><td>${fn:substring(data.orderDate, 0, 16)}</td></tr>
                                    <tr><th>Update Date:</th><td>${fn:substring(data.deliveredDate, 0, 16)}</td></tr>
                                    <tr>
                                        <th>Status:</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${data.status eq 'Waiting'}">
                                                    <span class="badge bg-warning">Waiting</span>
                                                </c:when>
                                                <c:when test="${data.status eq 'Packing'}">
                                                    <span class="badge bg-primary">Packing</span>
                                                </c:when>
                                                <c:when test="${data.status eq 'Waiting for Delivery'}">
                                                    <span class="badge status-3">Waiting for Delivery</span>
                                                </c:when>
                                                <c:when test="${data.status eq 'Delivered'}">
                                                    <span class="badge bg-success">Delivered</span>
                                                </c:when>
                                                <c:when test="${data.status eq 'Cancelled'}">
                                                    <span class="badge bg-danger">Cancelled</span>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <c:set var="subtotal" value="0" scope="page" />
                                    <c:forEach items="${dataDetail}" var="detail">
                                        <c:set var="subtotal" value="${subtotal + (detail.quantity * detail.price)}" />
                                    </c:forEach>

                                    <tr><th>Subtotal:</th>
                                        <td><fmt:formatNumber value="${subtotal}" type="number" groupingUsed="true"/>₫</td>
                                    </tr>
                                    <tr>
                                        <th>Discount:</th>
                                        <td>
                                            <fmt:formatNumber 
                                                value="${(data.totalAmount - subtotal)}" 
                                                type="number" 
                                                groupingUsed="true"/>₫ 
                                            (<c:out value="${data.discount}"/>%)
                                        </td>
                                    </tr>
                                    <tr><th>Total Amount:</th><td><strong><fmt:formatNumber value="${data.totalAmount}" type="number" groupingUsed="true" />₫</strong></td></tr>
                                    <tr><th>Customer Name:</th><td>${data.customer.fullName}</td></tr>
                                    <tr><th>Phone:</th><td>${data.customer.phoneNumber}</td></tr>
                                    <tr><th>Address:</th><td>${data.addressSnapshot}</td></tr>
                                </table>

                                <h5 class="section-header"><i class="fa-solid fa-box"></i> Order Items</h5>
                                <table class="items-table">
                                    <thead>
                                        <tr>
                                            <th>Product</th>
                                            <th style="width: 120px;">Quantity</th>
                                            <th style="width: 150px;">Price</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${dataDetail}" var="detail">
                                            <tr>
                                                <td class="text-start"><strong>${detail.productName}</strong></td>
                                                <td>${detail.quantity}</td>
                                                <td><fmt:formatNumber value="${detail.price}" type="number" groupingUsed="true"/>₫</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <div class="management-section">
                                    <h5 class="section-header"><i class="fa-solid fa-cogs"></i> Manage Order</h5>

                                    <c:if test="${not empty errorMessage}">
                                        <div class="alert alert-danger">${errorMessage}</div>
                                    </c:if>

                                    <form action="${pageContext.request.contextPath}/UpdateOrder" method="POST">
                                        <input type="hidden" name="orderID" value="${data.orderID}" />
                                        <div class="form-controls">
                                            <select id="orderStatus" name="update" class="form-select">
                                                <option value="Waiting" <c:if test="${data.status eq 'Waiting'}">selected</c:if>>Waiting</option>
                                                <option value="Packing" <c:if test="${data.status eq 'Packing'}">selected</c:if>>Packing</option>
                                                <option value="Waiting for Delivery" <c:if test="${data.status eq 'Waiting for Delivery'}">selected</c:if>>Waiting for Delivery</option>
                                                <option value="Delivered" <c:if test="${data.status eq 'Delivered'}">selected</c:if>>Delivered</option>
                                                <option value="Cancelled" <c:if test="${data.status eq 'Cancelled'}">selected</c:if>>Cancelled</option>
                                            </select>
                                            <button type="submit" class="btn btn-success">
                                                <i class="fa-solid fa-save"></i> Save Changes
                                            </button>
                                            <a href="${pageContext.request.contextPath}/ViewOrderList" class="btn btn-outline-primary">
                                                <i class="fa-solid fa-arrow-left"></i> Back to List
                                            </a>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script>
//            function disableOptions() {
//                const status = document.getElementById('orderStatus').value;
//                const options = document.getElementById('orderStatus').options;
//
//                // Reset all options
//                for (let i = 0; i < options.length; i++) {
//                    options[i].disabled = false;
//                }
//
//                if (status === 'Waiting for Delivery') {
//                    options[0].disabled = true; // Waiting
//                    options[1].disabled = true; // Packing
//                    options[4].disabled = true; // Cancelled
//                } else if (status === 'Packing') {
//                    options[0].disabled = true; // Waiting
//                } else if (status === 'Delivered') {
//                    options[0].disabled = true;
//                    options[1].disabled = true;
//                    options[2].disabled = true;
//                    options[4].disabled = true;
//                } else if (status === 'Cancelled') {
//                    options[0].disabled = true;
//                    options[1].disabled = true;
//                    options[2].disabled = true;
//                    options[3].disabled = true;
//                }
//            }
//
//            // Initialize on page load
//            document.addEventListener('DOMContentLoaded', function () {
//                disableOptions();
//
//                // Add change event listener
//                document.getElementById('orderStatus').addEventListener('change', disableOptions);
//            });
        </script>
    </body>
</html>