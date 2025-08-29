<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Order Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/Css/profile.css" rel="stylesheet">
        <style>
            .badge {
                padding: 10px 12px;
                border-radius: 999px;
                font-size: 14px;
                font-weight: 600;
            }

            .status-Waiting {
                background-color: #f59e0b;
                color: #fff;
            }
            .status-Packing {
                background-color: #0d6efd;
                color: #fff;
            }
            .status-Waiting.for.Delivery {
                background-color: #6366f1 !important;
                color: #fff;
            }
            .status-Delivered {
                background-color: #22c55e;
                color: #fff;
            }
            .status-Cancelled {
                background-color: #ef4444;
                color: #fff;
            }
            .total-amount {
                font-size: 1.2rem;       /* chữ to hơn */
                font-weight: bold;       /* chữ đậm */
                color: #d9534f;          /* màu đỏ nhấn mạnh */
                background: #f9f9f9;     /* nền nhạt */
                padding: 10px 15px;      /* khoảng cách bên trong */
                border-radius: 8px;      /* bo tròn góc */
                border: 1px solid #eee;  /* viền mảnh */
                display: inline-block;   /* ô gọn lại */
                margin-top: 10px;
            }

            .total-amount span {
                color: #000;             /* số tiền màu đen rõ ràng */
                margin-left: 8px;
            }

        </style>
    </head>
    <body class="d-flex flex-column min-vh-100">
        <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />

        <div class="main-account container-fluid">
            <jsp:include page="/WEB-INF/View/customer/sideBar.jsp" />
            <div class="profile-card flex-grow-1">
                <c:choose>
                    <c:when test="${not empty orderList}">

                        <div class="profile-header">
                            <div class="fw-semibold fs-5 text-center">
                                <i class="bi bi-receipt-cutoff me-2"></i>
                                
                                Order Date: <span class="fw-bold">${fn:substring(data.orderDate, 0, 16)}</span>
                            </div>
                        </div>

                        <div class="mb-4 p-4" style="background: #fff; border-radius: 16px; box-shadow: 0 4px 14px rgba(0,0,0,0.08);">
                            <div class="profile-body">
                                <p><strong><i class="bi bi-pencil-square me-1"></i>Last Updated:</strong> ${fn:substring(data.updatedAt, 0, 16)}</p>
                                <p><strong><i class="bi bi-bar-chart-line me-1"></i>Status:</strong>
                                    <span class="badge status-${data.status}">
                                        <c:choose>
                                            <c:when test="${data.status eq 'Waiting'}">Waiting</c:when>
                                            <c:when test="${data.status eq 'Packing'}">Packing</c:when>
                                            <c:when test="${data.status eq 'Waiting for Delivery'}">Waiting for Delivery</c:when>
                                            <c:when test="${data.status eq 'Delivered'}">Delivered</c:when>
                                            <c:otherwise>Cancelled</c:otherwise>
                                        </c:choose>
                                    </span>
                                </p>
                                <p><strong><i class="bi bi-person-lines-fill me-1"></i>Recipient:</strong> ${data.customer.fullName} - ${data.customer.phoneNumber}</p>
                                <p><strong><i class="bi bi-geo-alt-fill me-1"></i>Address:</strong> ${data.addressSnapshot}</p>
                                <c:set var="subtotal" value="0" />
                                <c:forEach var="item" items="${dataDetail}">
                                    <c:set var="subtotal" value="${subtotal + (item.price * item.quantity)}" />
                                </c:forEach>
                                <p>
                                    <strong><i class="bi bi-cart4 me-1"></i>Subtotal:</strong>
                                    <fmt:formatNumber value="${subtotal}" type="number" groupingUsed="true"/>₫
                                </p>
                                <p>
                                    <strong><i class="bi bi-cash-coin me-1"></i>Discount:</strong>
                                    - <fmt:formatNumber value="${subtotal - data.totalAmount}" type="number" groupingUsed="true"/>₫
                                    (giảm ${data.discount}%)
                                </p>

                                <p class="total-amount">
                                    <strong><i class="bi bi-currency-exchange me-1"></i>Total Amount:</strong>
                                    <span><fmt:formatNumber value="${data.totalAmount}" type="number" groupingUsed="true"/>₫</span>
                                </p>


                                <h5 class="mb-3"><i class="bi bi-box-seam me-1"></i> Products</h5>
                                <table class="table table-bordered detail-table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Product Name</th>
                                            <th>Quantity</th>
                                            <th>Price (₫)</th>
                                            <th>Total product price (₫)</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${dataDetail}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>${item.productName}</td>
                                                <td>${item.quantity}</td>
                                                <td><fmt:formatNumber value="${item.price}" type="number" groupingUsed="true"/>đ</td>
                                                <td><fmt:formatNumber value="${item.price * item.quantity}" type="number" groupingUsed="true"/>đ</td>
                                                 <td>
                                                    <c:if test="${data.status eq 'Delivered'}">
                                                        <button type="button"
                                                                class="btn btn-outline-primary btn-sm"
                                                                onclick="openFeedbackModal('${item.productID}', '${fn:escapeXml(item.productName)}', '${data.orderID}')">
                                                            <i class="bi bi-pencil-square"></i> Write Feedback
                                                        </button>
                                                    </c:if>
                                                </td>

                                            </tr>

                                        </c:forEach>
                                    </tbody>
                                </table>
                                <!-- Feedback Modal -->

                                <div class="modal fade" id="feedbackModal" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <form class="modal-content" method="post" action="WriteFeedback">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Write Feedback</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                            </div>
                                            <div class="modal-body">
                                                <!-- ✅ Sử dụng sessionScope.cus thay vì sessionScope.customer -->
                                                <c:choose>
                                                    <c:when test="${not empty sessionScope.cus}">
                                                        <!-- ✅ Sử dụng sessionScope.cus.id thay vì customerID -->
                                                        <input type="hidden" name="customerID" value="${sessionScope.cus.customerID}" />

                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="alert alert-danger">
                                                            Error: No customer in session. Please login first.
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>

                                                <input type="hidden" name="productID" id="feedbackProductID">
                                                <input type="hidden" name="orderID" id="feedbackOrderID">

                                                <div class="mb-3">
                                                    <label class="form-label">Product</label>
                                                    <input type="text" id="feedbackProductName" class="form-control" readonly>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Rating</label>
                                                    <select name="star" class="form-select" required>
                                                        <option value="">Select rating</option>
                                                        <c:forEach var="i" begin="1" end="5">
                                                            <option value="${i}">${i} ★</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Feedback</label>
                                                    <textarea name="comment" class="form-control" rows="4" required></textarea>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="submit" class="btn btn-success">Submit</button>
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                            </div>
                                        </form>
                                    </div>
                                </div>


                                <div class="action-buttons mt-3">
                                    <c:if test="${data.status eq 'Waiting' || data.status eq 'Packing'}">
                                        <form class="cancel-form" action="CancelOrder" method="POST">
                                            <input type="hidden" name="orderID" value="${data.orderID}" />
                                            <button type="button" class="btn btn-outline-danger cancel-btn">
                                                <i class="bi bi-x-circle"></i> Cancel Order
                                            </button>
                                        </form>
                                    </c:if>
                                </div>

                            </c:when>
                        </c:choose>
                    </div>
                </div>

                <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>


                <!-- SweetAlert on success/error -->
                <c:if test="${not empty param.success || not empty param.error}">
                    <script>
                        window.onload = function () {
                        <c:if test="${param.success == 'feedback'}">
                            Swal.fire({
                                icon: 'success',
                                title: 'Feedback Submitted',
                                text: 'Your feedback has been submitted successfully!',
                                timer: 3000,
                                confirmButtonText: 'OK'
                            });
                        </c:if>
                        <c:if test="${param.error == 'feedback'}">
                            Swal.fire({
                                icon: 'error',
                                title: 'Feedback Failed',
                                text: 'There was a problem submitting your feedback. Please try again.',
                                timer: 3000,
                                confirmButtonText: 'Close'
                            });
                        </c:if>

                            if (window.history.replaceState) {
                                const url = new URL(window.location);
                                url.searchParams.delete('success');
                                url.searchParams.delete('error');
                                window.history.replaceState({}, document.title, url.pathname + url.search);
                            }

                        };
                    </script>

                </c:if>

                <c:if test="${param.error == 'alreadyRated'}">
                    <script>
                        Swal.fire({
                            icon: 'info',
                            title: 'Feedback Exists',
                            text: 'You have already rated this product for this order.'
                        });
                    </script>
                </c:if>


                <!-- SweetAlert cancel confirmation -->
                <script>
                    document.addEventListener('DOMContentLoaded', function () {
                        document.querySelectorAll('.cancel-btn').forEach(function (button) {
                            button.addEventListener('click', function () {
                                const form = button.closest('form');
                                Swal.fire({
                                    title: 'Are you sure?',
                                    text: "Do you want to cancel this order?",
                                    icon: 'warning',
                                    showCancelButton: true,
                                    confirmButtonColor: '#d33',
                                    cancelButtonColor: '#3085d6',
                                    confirmButtonText: 'Yes, cancel it!',
                                    cancelButtonText: 'No'
                                }).then((result) => {
                                    if (result.isConfirmed) {
                                        form.submit();
                                    }
                                });
                            });
                        });
                    });
                </script>

                <script>
                    function openFeedbackModal(productID, productName, orderID) {
                        document.getElementById('feedbackProductID').value = productID;
                        document.getElementById('feedbackProductName').value = productName;
                        document.getElementById('feedbackOrderID').value = orderID;

                        const modalElement = document.getElementById('feedbackModal');
                        const modal = bootstrap.Modal.getOrCreateInstance(modalElement);
                        modal.show();
                    }
                </script>


                </body>
                </html>