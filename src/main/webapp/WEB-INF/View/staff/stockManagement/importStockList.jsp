<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>

<html>
    <head>
        <title>Import Stock History</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList6.css">
    </head>
    <body>
        <div class="container">
            <jsp:include page="/WEB-INF/View/staff/sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <jsp:include page="/WEB-INF/View/staff/header.jsp" />

                    <h1>Import Stock History</h1>
                    <button class="btn btn-success mb-2 float-end" onclick="location.href = 'ImportStock'">+ New Import</button>

                    <form class="search-form mb-3" method="get" action="">
                        <input type="date" name="from" value="${from != null ? from : ''}" />
                        <input type="date" name="to" value="${to != null ? to : ''}" />
                        <select name="supplierId">
                            <option value="">-- All Suppliers --</option>
                            <c:forEach items="${suppliers}" var="s">
                                <option value="${s.supplierID}" <c:if test="${supplierId != null && supplierId == s.supplierID}">selected</c:if>>
                                    ${s.name}
                                </option>
                            </c:forEach>
                        </select>
                        <button type="submit" class="btn btn-primary">Filter</button>
                    </form>

                    <table class="table table-bordered table-striped">
                        <thead class="table-dark">
                            <tr>
                                <th>#</th>
                                <th>Import Date</th>
                                <th>Supplier</th>
                                <th>Total Amount</th>
                                <th>Staff</th>
                                <th class="text-center">Detail</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${importHistory}" var="imp" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td><fmt:formatDate value="${imp.importDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td>${imp.supplier.name}</td>
                                    <td><fmt:formatNumber value="${imp.totalAmount}" type="currency" currencySymbol="₫"/></td>
                                    <td>${imp.fullName}</td>
                                    <td class="text-center">
                                        <a href="ImportHistoryDetail?id=${imp.importID}" class="btn btn-primary btn-sm">Detail</a>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty importHistory}">
                                <tr>
                                    <td colspan="6" class="text-center">No import history found!</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>

                    <div class="text-end mt-3">
                        <a href="ImportStatistic" class="btn btn-secondary">Back</a>
                    </div>

                </main>
            </div>
        </div>
    </body>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        .swal2-confirm-green {
            background-color: #28a745 !important;
            color: #fff !important;
            border: none !important;
            box-shadow: none !important;
        }
        .swal2-confirm-green:focus, .swal2-confirm-green:hover {
            background-color: #218838 !important;
        }
    </style>
    <script>
                document.getElementById('exportExcelBtn').onclick = function (e) {
                    e.preventDefault();
                    Swal.fire({
                        icon: 'question',
                        title: 'Export Excel',
                        text: 'Do you want to export the import stock history to Excel?',
                        showCancelButton: true,
                        confirmButtonText: 'Yes, export',
                        cancelButtonText: 'Cancel',
                        customClass: {
                            confirmButton: 'swal2-confirm-green'
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            const exportForm = document.createElement('form');
                            exportForm.method = 'post';
                            exportForm.action = 'ExportToFileExcelServlet';

                            const originalForm = this.form;
                            const inputs = originalForm.querySelectorAll('input, select');
                            inputs.forEach(input => {
                                if (input.name && input.value) {
                                    const hiddenInput = document.createElement('input');
                                    hiddenInput.type = 'hidden';
                                    hiddenInput.name = input.name;
                                    hiddenInput.value = input.value;
                                    exportForm.appendChild(hiddenInput);
                                }
                            });

                            const actionInput = document.createElement('input');
                            actionInput.type = 'hidden';
                            actionInput.name = 'action';
                            actionInput.value = 'export';
                            exportForm.appendChild(actionInput);

                            document.body.appendChild(exportForm);
                            exportForm.submit();
                            document.body.removeChild(exportForm);
                        }
                    });
                    return false;
                }
    </script>

</html>
