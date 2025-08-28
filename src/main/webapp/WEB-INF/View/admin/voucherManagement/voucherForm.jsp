
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Voucher" %>
<%

    Voucher v = (Voucher) request.getAttribute("voucher");
    boolean isEdit = v != null;
%>
<!DOCTYPE html>
<html>
    <head>
        <title><%= isEdit ? "Update" : "Create"%> Voucher</title>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="card mx-auto shadow" style="max-width: 800px;">
                        <div class="card-header text-white">
                            <h4 class="mb-0"><%= isEdit ? "Update" : "Create"%> Voucher</h4>
                        </div>
                        <div class="card-body">
                            <% if (request.getAttribute("error") != null) {%>
                            <div class="alert alert-danger">
                                <%= request.getAttribute("error")%>
                            </div>
                            <% } %>

                            <form method="post" action="Voucher">
                                <% if (isEdit) {%>
                                <input type="hidden" name="id" value="<%= v.getVoucherID()%>"/>
                                <% }%>

                                <table class="supplier-form-table">
                                    <tr>
                                        <th>Voucher Code:</th>
                                        <td>
                                            <input type="text" class="form-control supplier-input" name="code" maxlength="50" required
                                                   value="<%= isEdit ? v.getCode() : ""%>"/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <th>Discount Percentage (%):</th>
                                        <td>
                                            <input type="number" class="form-control supplier-input" name="discountPercent" min="1" max="100" required
                                                   value="<%= isEdit ? v.getDiscountPercent() : 0%>"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Expiry Date:</th>
                                        <td>
                                            <input type="date" class="form-control supplier-input" name="expiryDate" required
                                                   value="<%= isEdit ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(v.getExpiryDate()) : ""%>"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Minimum Order Amount:</th>
                                        <td>
                                            <div class="input-group">
                                                <input type="number" class="form-control supplier-input" name="minOrderAmount" step="1000" min="0" required
                                                       value="<%= isEdit ? String.format("%.0f", v.getMinOrderAmount()).replace(",", "") : 0%>"/>
                                                <span class="input-group-text">VNĐ</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Maximum Discount Amount:</th>
                                        <td>
                                            <div class="input-group">
                                                <input type="number" class="form-control supplier-input" name="maxDiscountAmount" step="1000" min="0" required
                                                       value="<%= isEdit ? String.format("%.0f", v.getMaxDiscountAmount()).replace(",", "") : 0%>"/>
                                                <span class="input-group-text">VNĐ</span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Usage Limit:</th>
                                        <td>
                                            <input type="number" class="form-control supplier-input" name="usageLimit" min="1" required
                                                   value="<%= isEdit ? v.getUsageLimit() : 1%>"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Status:</th>
                                        <td>
                                            <div class="form-check form-check-inline">
                                                <input class="form-check-input" type="radio" name="isActive" value="true" id="active"
                                                       <%= !isEdit || v.isActive() ? "checked" : ""%>>
                                                <label class="form-check-label" for="active">Activate</label>
                                            </div>
                                            <div class="form-check form-check-inline ms-3">
                                                <input class="form-check-input" type="radio" name="isActive" value="false" id="inactive"
                                                       <%= isEdit && !v.isActive() ? "checked" : ""%>>
                                                <label class="form-check-label" for="inactive">Deactivate</label>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Description:</th>
                                        <td>
                                            <textarea class="form-control supplier-input" name="description" rows="3" maxlength="255"><%= isEdit ? v.getDescription() : ""%></textarea>
                                        </td>
                                    </tr>
                                </table>

                                <div class="button-container text-end">
                                    <button type="submit" class="btn btn-success me-2">
                                        <%= isEdit ? "Save" : "Create"%>
                                    </button>
                                    <a href="Voucher" class="btn btn-secondary">
                                        Cancel
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
            </div>
        </main>
    </div>
</body>
</html>
