
<%@page import="java.util.Locale"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Voucher" %>
<%@ page import="java.text.NumberFormat" %>
<%
    Voucher v = (Voucher) request.getAttribute("voucher");
    NumberFormat currencyVN = NumberFormat.getInstance(new Locale("vi", "VN"));
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <title>Voucher Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-ticket"></i> Voucher Details</h4>
                            </div>

                            <div class="card-body">
                                <% if (v != null) {%>
                                <table class="info-table">
                                    <tr><th>ID:</th><td><%= v.getVoucherID()%></td></tr>
                                    <tr><th>Code:</th><td><%= v.getCode()%></td></tr>                       
                                    <tr><th>Discount (%):</th><td><%= v.getDiscountPercent()%>%</td></tr>
                                    <tr><th>Expiry Date:</th><td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(v.getExpiryDate())%></td></tr>
                                    <tr><th>Min Order:</th><td><%= currencyVN.format(v.getMinOrderAmount()) + " ₫"%></td></tr>
                                    <tr><th>Max Discount:</th><td><%= currencyVN.format(v.getMaxDiscountAmount()) + " ₫"%></td></tr>
                                    <tr><th>Usage Limit:</th><td><%= v.getUsageLimit()%></td></tr>
                                    <tr><th>Used Count:</th><td><%= v.getUsedCount()%></td></tr>
                                            <%
                                                boolean isOverused = v.getUsedCount() >= v.getUsageLimit();
                                                boolean isCurrentlyActive = v.isActive() && !isOverused;
                                            %>
                                    <tr>
                                        <th>Status:</th>
                                        <td>
                                            <% if (isCurrentlyActive) { %>
                                            <span class="badge bg-success">Active</span>
                                            <% } else { %>
                                            <span class="badge bg-danger">Inactive</span>
                                            <% } %>
                                            <% if (isOverused) { %>
                                            <i class="fa-solid fa-triangle-exclamation text-warning" title="Usage limit reached"></i>
                                            <% }%>
                                        </td>
                                    </tr>
                                    <tr><th>Description:</th><td><%= v.getDescription() != null ? v.getDescription() : "N/A"%></td></tr>
                                </table>

                                <div class="management-section">
                                    <div class="form-controls">
                                        <a href="Voucher" class="btn btn-outline-primary">
                                            <i class="fa-solid fa-arrow-left"></i> Back to List
                                        </a>
                                        <a href="Voucher?action=edit&id=<%= v.getVoucherID()%>" class="btn btn-warning">
                                            Edit
                                        </a>
                                        <button class="btn btn-danger delete-btn"
                                                data-delete-url="Voucher?action=delete&id=<%= v.getVoucherID()%>"
                                                data-voucher-id="<%= v.getVoucherID()%>"
                                                data-voucher-code="<%= v.getCode()%>">
                                            Delete
                                        </button>
                                    </div>
                                </div>

                                <% } else { %>
                                <div class="alert alert-danger">Voucher not found!</div>
                                <div class="form-controls">
                                    <a href="Voucher" class="btn btn-outline-primary">
                                        Back to List
                                    </a>
                                </div>
                                <% }%>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const btn = document.querySelector('.delete-btn');
                if (btn) {
                    btn.addEventListener('click', function (e) {
                        e.preventDefault();
                        const deleteUrl = btn.getAttribute('data-delete-url');
                        const voucherId = btn.getAttribute('data-voucher-id');
                        const voucherCode = btn.getAttribute('data-voucher-code');

                        Swal.fire({
                            title: 'Are you sure?',
                            html: `Do you want to delete voucher <b>${voucherCode}</b> (ID: <b>${voucherId}</b>)?`,
                            icon: 'warning',
                            showCancelButton: true,
                            confirmButtonColor: '#d33',
                            cancelButtonColor: '#3085d6',
                            confirmButtonText: 'Yes, delete it!',
                            cancelButtonText: 'Cancel'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                window.location.href = deleteUrl;
                            }
                        });
                    });
                }
            });
        </script>
    </body>
</html>
