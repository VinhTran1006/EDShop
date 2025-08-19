<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Suppliers" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Supplier</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierLists5.css">

    </head>
    <body>
        <div class="container mt-5">
            <div class="card mx-auto shadow" style="max-width: 700px;">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Edit Supplier</h4>
                </div>
                <div class="card-body">
                    <%
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        String successMessage = (String) request.getAttribute("successMessage");
                        Suppliers supplier = (Suppliers) request.getAttribute("supplier");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        if (errorMessage != null) {
                    %>
                    <div class="alert alert-danger"><%= errorMessage%></div>
                    <% }
                        if (successMessage != null) {
                    %>
                    <div class="alert alert-success"><%= successMessage%></div>
                    <% }
                        if (supplier != null) {
                    %>

                    <form method="post" action="UpdateSupplier">
                        <input type="hidden" name="supplierID" value="<%= supplier.getSupplierID()%>">

                        <table class="table table-borderless">
                            <tr>
                                <th>Tax ID:</th>
                                <td><input type="text" name="taxId" class="form-control" value="<%= supplier.getTaxID()%>" readonly></td>
                            </tr>
                            <tr>
                                <th>Name:</th>
                                <td><input type="text" name="name" class="form-control" value="<%= supplier.getName()%>" required></td>
                            </tr>
                            <tr>
                                <th>Email:</th>
                                <td><input type="email" name="email" class="form-control" value="<%= supplier.getEmail() != null ? supplier.getEmail() : ""%>"></td>
                            </tr>
                            <tr>
                                <th>Phone Number:</th>
                                <td><input type="text" name="phoneNumber" class="form-control" value="<%= supplier.getPhoneNumber() != null ? supplier.getPhoneNumber() : ""%>"></td>
                            </tr>
                            <tr>
                                <th>Address:</th>
                                <td><input type="text" name="address" class="form-control" value="<%= supplier.getAddress() != null ? supplier.getAddress() : ""%>"></td>
                            </tr>
                            <tr>
                                <th>Contact Person:</th>
                                <td><input type="text" name="contactPerson" class="form-control" value="<%= supplier.getContactPerson() != null ? supplier.getContactPerson() : ""%>"></td>
                            </tr>
                            <tr>
                                
                            </tr>

                            <tr>
                                <th>Description:</th>
                                <td><textarea name="description" class="form-control"><%= supplier.getDescription() != null ? supplier.getDescription() : ""%></textarea></td>
                            </tr>
                            <tr>
                                <th>Status:</th>
                                <td>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="radio" name="activate" value="1"
                                               <%= supplier.getIsActive()== true ? "checked" : ""%>> <label class="form-check-label">Activate</label>
                                    </div>
                                    <div class="form-check form-check-inline ms-3">
                                        <input class="form-check-input" type="radio" name="activate" value="0"
                                               <%= supplier.getIsActive() == true ? "checked" : ""%>> <label class="form-check-label">Deactivate</label>
                                    </div>
                                </td>
                            </tr>
                
                        </table>

                        <div class="text-end">
                            <button type="submit" class="btn btn-success me-2">Save</button>
                            <a href="ViewSupplier" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>

                    <% } else { %>
                    <div class="alert alert-danger">Supplier not found!</div>
                    <a href="ViewSupplier" class="btn btn-outline-primary">Back to List</a>
                    <% }%>
                </div>
            </div>
        </div>

    </body>
</html>



