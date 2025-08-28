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

        <!-- Bootstrap & FontAwesome -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />

        <!-- Custom Styles -->

        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css" />
        <style>
            body {
                background-color: #f4f6fb;
                font-family: 'Segoe UI', sans-serif;
            }

            .status-1 {
                background-color: #f59e0b;
            } /* Waiting */
            .status-2 {
                background-color: #0d6efd;
            } /* Packaging */
            .status-3 {
                background-color: #6366f1;
            } /* Waiting for Delivery */
            .status-4 {
                background-color: #22c55e;
            } /* Delivered */
            .status-5 {
                background-color: #ef4444;
            } /* Cancelled */

            .badge {
                padding: 6px 12px;
                border-radius: 999px;
                font-weight: 600;
                color: #fff;
                font-size: 14px;
            }

            .card h4 {
                font-weight: 700;
            }

            .form-select {
                border-radius: 8px;
            }

            .btn {
                border-radius: 8px;
                font-weight: 600;
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
                        <div class="card mx-auto shadow" style="max-width: 850px;">

                            <div class="card-header bg-primary text-white">
                                <h4 class="mb-0"><i class="fa-solid fa-receipt"></i> Order Detail</h4>
                            </div>

                            <div class="card-body">
                                <table class="table table-borderless">
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
                                    <tr><th>Total Amount:</th><td><fmt:formatNumber value="${data.totalAmount}" type="number" groupingUsed="true" />₫</td></tr>
                                    <tr><th>Customer Name:</th><td>${data.customer.fullName}</td></tr>
                                    <tr><th>Phone:</th><td>${data.customer.phoneNumber}</td></tr>
                                    <tr><th>Address:</th><td>${data.addressSnapshot}</td></tr>
                                </table>

                                <h5 class="mt-4"><i class="fa-solid fa-box"></i> Order Items</h5>
                                <table class="table table-bordered align-middle text-center">
                                    <thead class="table-light">
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


                                <h5><i class="fa-solid fa-cogs"></i> Manage Order</h5>

                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger mt-2">${errorMessage}</div>
                                </c:if>

                                <form action="${pageContext.request.contextPath}/UpdateOrder" method="POST" class="d-flex gap-3 flex-wrap align-items-center mt-3">
                                    <input type="hidden" name="orderID" value="${data.orderID}" />
                                    <select id="orderStatus" name="update" class="form-select w-auto">
                                        <option value="Waiting" <c:if test="${data.status eq 'Waiting'}">selected</c:if>>Waiting</option>
                                        <option value="Packing" <c:if test="${data.status eq 'Packing'}">selected</c:if>>Packing</option>
                                        <option value="Waiting for Delivery" <c:if test="${data.status eq 'Waiting for Delivery'}">selected</c:if>>Waiting for Delivery</option>
                                        <option value="Delivered" <c:if test="${data.status eq 'Delivered'}">selected</c:if>>Delivered</option>
                                        <option value="Cancelled" <c:if test="${data.status eq 'Cancelled'}">selected</c:if>>Cancelled</option>
                                        </select>
                                        <button type="submit" class="btn btn-success">Save</button>
                                        <a href="${pageContext.request.contextPath}/ViewOrderList" class="btn btn-outline-primary">Back to list</a>
                                </form>
                            </div>
                        </div>
                </main>
            </div>
        </div>
    </div>

    <!-- JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <script>

        function disableOptions() {
            const status = document.getElementById('orderStatus').value;
            const options = document.getElementById('orderStatus').options;
            // reset all
            for (let i = 0; i < options.length; i++) {
                options[i].disabled = false;
            }

            if (status === 'Waiting for Delivery') {
                // disable Waiting, Packaging, Cancelled
                options[0].disabled = true; // Waiting
                options[1].disabled = true; // Packaging
                options[4].disabled = true; // Cancelled
            } else if (status === 'Waiting') {
                options[2].disabled = true;
                options[3].disabled = true;

            } else if (status === 'Packing') {
                // disable Waiting
                options[0].disabled = true; // Waiting
                options[3].disabled = true;

            } else if (status === 'Delivered') {
                // disable Waiting, Packaging, Waiting for Delivery, Cancelled
                options[0].disabled = true;
                options[1].disabled = true;
                options[2].disabled = true;
                options[4].disabled = true;
            } else if (status === 'Cancelled') {
                // disable tất cả trừ Cancelled
                options[0].disabled = true;
                options[1].disabled = true;
                options[2].disabled = true;
                options[3].disabled = true;
            }
        }

        // gọi khi load trang để setup ban đầu
        disableOptions();
    </script>

</body>
</html>