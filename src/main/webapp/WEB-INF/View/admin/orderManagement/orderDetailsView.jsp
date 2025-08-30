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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
                                <h4><i class="fa-solid fa-receipt"></i> Order Details</h4>
                            </div>

                            <div class="card-body">
                                <table class="info-table">
                                    <tr><th>Order ID:</th><td>${data.orderID}</td></tr>
                                    <tr><th>Order Date:</th><td>${fn:substring(data.orderDate, 0, 16)}</td></tr>
                                    <tr><th>Update Date:</th><td>${fn:substring(data.updatedAt, 0, 16)}</td></tr>
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
                                    <tr><th>Discount:</th> <td>
                                            <fmt:formatNumber 
                                                value="${(data.totalAmount - subtotal)}" 
                                                type="number" 
                                                    groupingUsed="true"/>₫ 
                                            (<c:out value="${data.discount}"/>%)
                                        </td></tr>

                                    <tr><th>Total Amount:</th><td><strong><fmt:formatNumber value="${data.totalAmount}" type="number" groupingUsed="true" />₫</strong></td></tr>
                                    <tr><th>Customer Name:</th><td>${data.customer.fullName}</td></tr>
                                    <tr><th>Phone:</th><td>${data.customer.phoneNumber}</td></tr>
                                    <tr><th>Address:</th><td>${data.addressSnapshot}</td></tr>
                                </table>


                                <h5 class="section-header"><i class="fa-solid fa-box"></i> Order Items</h5>
                                <div class="order-items">
                                    <c:forEach items="${dataDetail}" var="detail">
                                        <div class="order-item">
                                            <div class="item-name">${detail.productName}</div>
                                            <div class="item-quantity">Quantity: ${detail.quantity}</div>
                                            <div class="item-price">Price: <fmt:formatNumber value="${detail.price}" type="number" groupingUsed="true" />₫</div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <div class="management-section">
                                   

                                    <c:if test="${not empty errorMessage}">
                                        <div class="alert alert-danger">${errorMessage}</div>
                                    </c:if>


                                    <a href="${pageContext.request.contextPath}/ViewOrderListServletAdmin" class="btn btn-outline-primary">
                                        <i class="fa-solid fa-arrow-left"></i> Back to List
                                    </a>


                                </div>
                            </div>
                        </div>
                </main>
            </div>
        </div>

        <script>
            function disableOptions() {
                const status = document.getElementById('orderStatus').value;
                const options = document.getElementById('orderStatus').options;

                // Reset all options
                for (let i = 0; i < options.length; i++) {
                    options[i].disabled = false;
                }

                if (status === 'Waiting for Delivery') {
                    options[0].disabled = true; // Waiting
                    options[1].disabled = true; // Packing
                    options[4].disabled = true; // Cancelled
                } else if (status === 'Packing') {
                    options[0].disabled = true; // Waiting
                } else if (status === 'Delivered') {
                    options[0].disabled = true;
                    options[1].disabled = true;
                    options[2].disabled = true;
                    options[4].disabled = true;
                } else if (status === 'Cancelled') {
                    options[0].disabled = true;
                    options[1].disabled = true;
                    options[2].disabled = true;
                    options[3].disabled = true;
                }
            }

            // Initialize on page load
            document.addEventListener('DOMContentLoaded', function () {
                disableOptions();

                // Add change event listener
                document.getElementById('orderStatus').addEventListener('change', disableOptions);
            });
        </script>
    </body>
</html>