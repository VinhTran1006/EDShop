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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <!-- Fontawesome CDN -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
        <!-- Sidebar CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">

        <!-- Dashboard CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
            /* Tất cả nút hành động */
            .btn-status {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                gap: 6px;
                border-radius: 8px;     /* bo góc nhẹ */
                font-size: 14px;        /* font dễ đọc */
                font-weight: 600;
                padding: 6px 14px;      /* padding cân đối */
                min-width: 160px;       /* đảm bảo các nút cùng kích thước */
                height: 38px;           /* chiều cao đồng bộ */
                border: none;
                color: #fff;
                transition: all 0.2s ease;
                cursor: pointer;
            }

            .btn-status:hover {
                opacity: 0.9;
                transform: translateY(-1px);
            }

            /* Màu theo chức năng */
            .btn-next-packing {
                background-color: #0d6efd;   /* xanh dương */
            }
            .btn-next-delivery {
                background-color: #6366f1;   /* tím */
            }
            .btn-next-delivered {
                background-color: #22c55e;   /* xanh lá */
            }
            .btn-cancel {
                background-color: #ef4444;   /* đỏ */
            }
            .btn-detail {
                background-color: #3b82f6;   /* xanh dương nhạt */
            }

            /* Khoảng cách giữa các nút */
            .d-flex.gap-2 > form,
            .d-flex.gap-2 > a {
                margin-bottom: 4px; /* khoảng cách dọc nếu wrap xuống dòng */
            }

        </style>

    </head>
    <body>

        <div class="container">
            <jsp:include page="/WEB-INF/View/admin/sideBar.jsp"/>
            <div class="wrapper">
                <main class="main-content">
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
                                        <td>${fn:substring(order.updatedAt, 0, 16)}</td>

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
                                            <div class="d-flex flex-wrap gap-2">
                                                <c:choose>
                                                    <c:when test="${order.status eq 'Waiting'}">
                                                        <c:choose>
                                                            <c:when test="${order.hasOutOfStock}">
                                                                <small class="text-danger w-100 mt-1">
                                                                    Out of stock:
                                                                    <c:forEach var="p" items="${order.outOfStockProducts}" varStatus="loop">
                                                                        ${p}<c:if test="${!loop.last}">, </c:if>
                                                                    </c:forEach>
                                                                </small>
                                                                <form action="${pageContext.request.contextPath}/UpdateOrderStatusAdmin" method="POST" class="d-inline cancel-form">
                                                                    <input type="hidden" name="orderID" value="${order.orderID}" />
                                                                    <input type="hidden" name="update" value="Cancelled"/>
                                                                    <button type="button" class="btn-status btn-cancel cancel-btn">
                                                                        <i class="fa-solid fa-xmark"></i> Cancel
                                                                    </button>
                                                                </form>


                                                            </c:when>

                                                            <c:otherwise>
                                                                <form action="${pageContext.request.contextPath}/UpdateOrderStatusAdmin" method="POST" class="d-inline">
                                                                    <input type="hidden" name="orderID" value="${order.orderID}" />
                                                                    <input type="hidden" name="update" value="Packing"/>
                                                                    <button type="submit" class="btn-status btn-next-packing">
                                                                        <i class="fa-solid fa-arrow-right"></i> Next (Packing)
                                                                    </button>
                                                                </form>

                                                                <form action="${pageContext.request.contextPath}/UpdateOrderStatusAdmin" method="POST" class="d-inline cancel-form">
                                                                    <input type="hidden" name="orderID" value="${order.orderID}" />
                                                                    <input type="hidden" name="update" value="Cancelled"/>
                                                                    <button type="button" class="btn-status btn-cancel cancel-btn">
                                                                        <i class="fa-solid fa-xmark"></i> Cancel
                                                                    </button>
                                                                </form>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>

                                                    <c:when test="${order.status eq 'Packing'}">
                                                        <form action="${pageContext.request.contextPath}/UpdateOrderStatusAdmin" method="POST" class="d-inline">
                                                            <input type="hidden" name="orderID" value="${order.orderID}" />
                                                            <input type="hidden" name="update" value="Waiting for Delivery"/>
                                                            <button type="submit" class="btn-status btn-next-delivery">
                                                                <i class="fa-solid fa-arrow-right"></i> Next (Waiting for Delivery)
                                                            </button>
                                                        </form>

                                                        <form action="${pageContext.request.contextPath}/UpdateOrderStatusAdmin" method="POST" class="d-inline cancel-form">
                                                            <input type="hidden" name="orderID" value="${order.orderID}" />
                                                            <input type="hidden" name="update" value="Cancelled"/>
                                                            <button type="button" class="btn-status btn-cancel cancel-btn">
                                                                <i class="fa-solid fa-xmark"></i> Cancel
                                                            </button>
                                                        </form>
                                                    </c:when>

                                                    <c:when test="${order.status eq 'Waiting for Delivery'}">
                                                        <form action="${pageContext.request.contextPath}/UpdateOrderStatusAdmin" method="POST" class="d-inline">
                                                            <input type="hidden" name="orderID" value="${order.orderID}" />
                                                            <input type="hidden" name="update" value="Delivered"/>
                                                            <button type="submit" class="btn-status btn-next-delivered">
                                                                <i class="fa-solid fa-arrow-right"></i> Next (Delivered)
                                                            </button>
                                                        </form>
                                                    </c:when>
                                                </c:choose>

                                                <a href="ViewOrderDetailAdmin?orderID=${order.orderID}" class="btn-status btn-detail">
                                                    <i class="fa-solid fa-eye"></i> Detail
                                                </a>
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

            // ✅ xử lý Swal + dọn URL
            window.addEventListener("load", function () {
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

                if (window.history.replaceState) {
                    const url = new URL(window.location);
                    url.searchParams.delete('success');
                    url.searchParams.delete('error');
                    window.history.replaceState({}, document.title, url.pathname);
                }
            });

            // ✅ xử lý disableOptions cho tất cả select
            window.addEventListener("load", function () {
                document.querySelectorAll('.orderStatus').forEach(function (selectEl) {
                    disableOptions(selectEl);
                });
            });

        </script>

        <script>
            document.querySelectorAll('.btn-cancel').forEach(btn => {
                btn.addEventListener('click', function (e) {
                    e.preventDefault();
                    Swal.fire({
                        title: 'Are you sure?',
                        text: "You want to cancel this order!",
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#ef4444',
                        cancelButtonColor: '#6c757d',
                        confirmButtonText: 'Yes, cancel it!'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            btn.closest('form').submit();
                        }
                    })
                });
            });
        </script>

    </body>
</html>