<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Suppliers" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Add Supplier</title>
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
                                <h4><i class="fa-solid fa-truck"></i> Add Supplier</h4>
                            </div>

                            <div class="card-body">
                                <% if (request.getAttribute("errorMsg") != null) {%>
                                <div class="alert alert-danger">
                                    <i class="fa-solid fa-triangle-exclamation me-2"></i>
                                    <%= request.getAttribute("errorMsg")%>
                                </div>
                                <% }%>

                                <form method="post" action="CreateSupplier">
                                    <div class="form-section">
                                        <table class="supplier-form-table">
                                            <tr>
                                                <th class="required">Tax ID:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="taxId" class="form-control supplier-input" 
                                                               required pattern="^[0-9]{8,15}$" 
                                                               title="Tax ID chỉ được chứa số, độ dài 8–15 ký tự"
                                                               placeholder="Enter tax identification number">

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Company Name:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="name" class="form-control supplier-input" 
                                                               required minlength="3" maxlength="100"
                                                               placeholder="Enter company name">

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Email:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="email" name="email" class="form-control supplier-input" 
                                                               required placeholder="Enter email address">

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Phone Number:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="phoneNumber" class="form-control supplier-input" 
                                                               required pattern="^[0-9+\-\s]{8,15}$" 
                                                               title="Số điện thoại chỉ được chứa số, dấu +, dấu - và khoảng trắng"
                                                               placeholder="Enter phone number">

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Address:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="address" class="form-control supplier-input" 
                                                               required minlength="5" maxlength="200"
                                                               pattern="^[a-zA-Z0-9\s,.\-\/]+$"
                                                               title="Chỉ cho phép chữ cái, số, khoảng trắng, dấu , . - /"
                                                               placeholder="Enter full address">

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Contact Person:</th>
                                                <td>
                                                    <div class="input-group">
                                                        <input type="text" name="contactPerson" class="form-control supplier-input" 
                                                               required minlength="3" maxlength="50"
                                                               pattern="^[a-zA-Z\s]+$"
                                                               title="Chỉ cho phép chữ cái và khoảng trắng"
                                                               placeholder="Enter contact person name">

                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Description:</th>
                                                <td>
                                                    <textarea name="description" class="form-control supplier-input" rows="4" 
                                                              required minlength="5" maxlength="500"
                                                              placeholder="Enter supplier description and services offered..."></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="required">Status:</th>
                                                <td>
                                                    <div class="form-check form-check-inline">
                                                        <input type="radio" name="activate" id="active" value="1" class="form-check-input" required checked>
                                                        <label for="active" class="form-check-label">
                                                            Activate
                                                        </label>
                                                    </div>
                                                    <div class="form-check form-check-inline">
                                                        <input type="radio" name="activate" id="inactive" value="0" class="form-check-input" required>
                                                        <label for="inactive" class="form-check-label">
                                                            Deactivate
                                                        </label>
                                                    </div>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>

                                    <div class="button-container">
                                        <button type="submit" class="btn btn-success">
                                            Create Supplier
                                        </button>
                                        <a href="ViewSupplier" class="btn btn-secondary">
                                            Cancel
                                        </a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>