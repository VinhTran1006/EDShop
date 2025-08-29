<%-- 
    Document   : customerList
    Created on : Jun 9, 2025, 1:21:22 PM
    Author     : pc
--%>

<%@page import="java.util.List"%>
<%@page import="model.Customer"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Customer List</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css">
        <style>
            .main-content{
                padding-top: 60px !important;
            }
            .main-content h1{
                margin-top: 0 !important;
                margin-bot: 0 !important;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">

                    <jsp:include page="../header.jsp" />

                    <h1>Customer List</h1>
                    <form class="search-form" action="CustomerList" method="get">        
                        <input type="text" name="keyword" class="form-control" placeholder="Search customer by name">
                        <input type="hidden" name="action" value="search">
                        <button type="submit" class="search-btn">Search</button>
                    </form>
                    <table aria-label="Staff table">
                        <th>ID</th>
                        <th>Email</th>
                        <th>Full Name</th>
                        <th>Phone</th>
                        <th>Created At</th>
                        <th>Status</th>
                        <th>Action</th>
                        </tr>
                        <%
                            List<Customer> cusList = (List<Customer>) request.getAttribute("userList");
                            for (Customer cus : cusList) {
                        %>
                        <tr>
                            <td><%= cus.getCustomerID()%></td>
                            <td><%= cus.getEmail()%></td>
                            <td><%= cus.getFullName()%></td>
                            <td><%= cus.getPhoneNumber()%></td>
                            <%
                                Date createdAt = cus.getCreatedAt();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = sdf.format(createdAt);
                            %>
                            <td><%= formattedDate%></td>
                            <td><%= cus.isActive() ? "Active" : "Block"%></td>
                            <td class="action-col">
                                <a href="CustomerList?action=changeStatus&id=<%= cus.getCustomerID()%>" 
                                   class="btn btn-warning">
                                    <%= cus.isActive() ? "Block" : "Unblock"%>
                                </a>
                                <a href="CustomerList?action=detail&id=<%= cus.getCustomerID()%>" class="btn btn-primary">Detail</a>                               
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                    <%
                        String error = (String) request.getAttribute("error");
                        if (error != null && !error.isEmpty()) {
                    %>
                    <div class="container">
                        <div class="alert alert-danger" role="alert">
                            <%= error%>
                        </div>
                    </div>
                    <%
                        }
                    %>
                </main>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <%
            String success = request.getParameter("success");
            if ("assigned".equals(success)) {
        %>
        <script>
            Swal.fire({
                icon: 'success',
                title: 'Assigned!',
                text: 'Voucher assigned successfully to customer.',
                confirmButtonText: 'OK'
            });
        </script>
        <%
            }
        %>

    </body>
</html>
