<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Order List</title>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />

        <!-- Bootstrap & FontAwesome -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css" />

        <%
            NumberFormat currencyVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        %>
        <style>
            body {
                background-color: #f4f6fb;
                font-family: 'Segoe UI', sans-serif;
            }

            .status-1 {
                background-color: #f59e0b;
            }     /* Waiting */
            .status-2 {
                background-color: #0d6efd;
            }     /* Packing */
            .status-3 {
                background-color: #6366f1;
            }     /* Waiting for Delivery */
            .status-4 {
                background-color: #22c55e;
            }     /* Delivered */
            .status-5 {
                background-color: #ef4444;
            }     /* Cancelled */

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

            ul.list-group li span {
                min-width: 100px;
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

                    <h1>Orders</h1>

                    <!-- Search Form -->
                    <form class="search-form" method="get" action="ViewOrderList">
                        <input type="text" name="search" placeholder="Search by Name, Phone..." value="${searchQuery}" />
                        <button type="submit" class="search-btn">Search</button>
                    </form>
                    <!-- Order Table -->
                    <c:if test="${not empty orderList}">
                        <table>
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Customer Name</th>
                                    <th>Phone</th>
                                    <th>Total Amount</th>
                                    <th>Order Date</th>
                                    <th>Order Update</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${orderList}">
                                    <tr>
                                        <td>#${order.orderID}</td>
                                        <td>${order.customer.fullName}</td>
                                        <td>${order.customer.phoneNumber}</td>
                                        <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" /></td>
                                        <td>${fn:substring(order.orderDate, 0, 16)}</td>
                                        <td>${fn:substring(order.deliveredDate, 0, 16)}</td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${order.status eq 'Waiting'}">
                                                    <span class="badge status-1">Waiting</span>
                                                </c:when>
                                                <c:when test="${order.status eq 'Packing'}">
                                                    <span class="badge status-2">Packing</span>
                                                </c:when>
                                                <c:when test="${order.status eq 'Waiting for Delivery'}">
                                                    <span class="badge status-3">Waiting for Delivery</span>
                                                </c:when>
                                                <c:when test="${order.status eq 'Delivered'}">
                                                    <span class="badge status-4">Delivered</span>
                                                </c:when>
                                                <c:when test="${order.status eq 'Cancelled'}">
                                                    <span class="badge status-5">Cancelled</span>
                                                </c:when>
                                            </c:choose>
                                        </td>

                                        <td>
                                            <div class="d-flex gap-2">
                                                <form action="${pageContext.request.contextPath}/UpdateOrder" method="POST" class="d-flex gap-2">
                                                    <input type="hidden" name="orderID" value="${order.orderID}" />
                                                    <select name="update" class="form-select form-select-sm orderStatus" 
                                                            onchange="disableOptions(this)">
                                                        <option value="Waiting" <c:if test="${order.status eq 'Waiting'}">selected</c:if>>Waiting</option>
                                                        <option value="Packing" <c:if test="${order.status eq 'Packing'}">selected</c:if>>Packing</option>
                                                        <option value="Waiting for Delivery" <c:if test="${order.status eq 'Waiting for Delivery'}">selected</c:if>>Waiting for Delivery</option>
                                                        <option value="Delivered" <c:if test="${order.status eq 'Delivered'}">selected</c:if>>Delivered</option>
                                                        <option value="Cancelled" <c:if test="${order.status eq 'Cancelled'}">selected</c:if>>Cancelled</option>
                                                        </select>
                                                        <button type="submit" class="btn btn-success btn-sm">Save</button>
                                                    </form>
                                                    <a href="ViewOrderDetail?orderID=${order.orderID}" class="btn btn-primary btn-sm">Detail</a>
                                            </div>
                                        </td>


                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>

                    <!-- If order list is empty -->
                    <c:if test="${empty orderList}">
                        <div class="text-center">No orders found!</div>
                    </c:if>
                </main>
            </div>
        </div>
        <%
            String success = request.getParameter("success");
            String error = request.getParameter("error");
        %>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
                                                                window.onload = function () {
            <% if ("update".equals(success)) { %>
                                                                    Swal.fire({
                                                                        icon: 'success',
                                                                        title: 'Updated successfully!',
                                                                        text: 'Order status has been updated.',
                                                                        timer: 3000,
                                                                        confirmButtonText: 'OK'
                                                                    });
            <% } else if ("1".equals(error)) { %>
                                                                    Swal.fire({
                                                                        icon: 'error',
                                                                        title: 'Error!',
                                                                        text: 'Unable to update order status.',
                                                                        timer: 3000,
                                                                        confirmButtonText: 'Retry'
                                                                    });
            <% }%>
                                                                    // 🔁 Clean up the URL
                                                                    if (window.history.replaceState) {
                                                                        const url = new URL(window.location);
                                                                        url.searchParams.delete('success');
                                                                        url.searchParams.delete('error');
                                                                        window.history.replaceState({}, document.title, url.pathname);
                                                                    }
                                                                };
        </script>
        <script>
            function disableOptions(selectEl) {
                const status = selectEl.value;
                const options = selectEl.options;

                // reset all
                for (let i = 0; i < options.length; i++) {
                    options[i].disabled = false;
                }

                if (status === 'Waiting for Delivery') {
                    options[0].disabled = true; // Waiting
                    options[1].disabled = true; // Packing
                    options[4].disabled = true; // Cancelled
                } else if (status === 'Waiting') {
                    options[2].disabled = true; // Waiting for Delivery
                    options[3].disabled = true; // Delivered

                } else if (status === 'Packing') {
                    options[0].disabled = true; // Waiting
                    options[3].disabled = true; // Delivered

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
            window.onload = function () {
                // chạy disableOptions cho tất cả select có class orderStatus
                document.querySelectorAll('.orderStatus').forEach(function (selectEl) {
                    disableOptions(selectEl);
                });
            };

        </script>
    </body>
</html>