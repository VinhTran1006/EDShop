<%@page import="dao.BrandDAO"%>
<%@page import="dao.CategoryDAO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Product> productList = (List<Product>) request.getAttribute("productList");
%>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Product List</title>
        <style>
            .product-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                border-radius: 16px;
                overflow: hidden;
                box-shadow: 0 4px 20px rgba(0,0,0,0.05);
            }
            .product-table th,
            .product-table td {
                text-align: center;
                vertical-align: middle;
            }
            .product-table th {
                background-color: #2563eb;
                color: white;
            }
            .product-table img {
                max-width: 60px;
                display: block;
                margin: 0 auto;
            }
            .action-buttons {
                display: flex;
                justify-content: center;
                gap: 8px;
                flex-wrap: wrap;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/View/admin/productManagement/deleteProduct/adminDeleteProduct.jsp" />
        <div class="">
            <div style="overflow-x: auto; width: 100%;">
                <% if (productList != null) { %>
                <table class="product-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Category</th>
                            <th>Brand</th>
                            <th>Image</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            Locale localeVN = new Locale("vi", "VN");
                            NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                            CategoryDAO catedao = new CategoryDAO();
                            BrandDAO branddao = new BrandDAO();
                            for (Product product : productList) {
                               String brandname = branddao.getBrandNameByBrandId(product.getBrandID());
                               String CategoryName = catedao.getCategoryNameByCategoryId(product.getCategoryID());
                                if (product != null) {
                                    String giaFormatted = "______";
                                    if (product.getPrice() != null) {
                                        giaFormatted = currencyVN.format(product.getPrice());
                                    }
                        %>
                        <tr>
                            <td><%= product.getProductID()%></td>
                            <td><%= product.getProductName()%></td>
                            <td><%= giaFormatted%></td>
                            <td><%= CategoryName%></td>
                            <td><%= brandname %></td>
                            <td>
                                <img src="<%= (product.getImageUrl1() != null) ? product.getImageUrl2() : ""%>" alt="Product Image">
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="AdminViewProductDetail?productId=<%= product.getProductID()%>" class="btn btn-warning">Detail</a>
                                    <a href="AdminUpdateProduct?productId=<%= product.getProductID()%>" class="btn btn-primary">Edit</a>
                                    <button class="btn btn-danger" onclick="confirmDelete(<%= product.getProductID()%>)">Delete</button>
                                </div>
                            </td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
                <% } else { %>
                <div style="padding: 16px; text-align: center;">No Data!</div>

                <% } %>
            </div>
        </div>

      <%
            String success = request.getParameter("success");
            String successpro = request.getParameter("successpro");

            String error = request.getParameter("error");
        %>

        <script>
            window.onload = function () {
            <% if ("1".equals(success)) { %>
                Swal.fire({
                    icon: 'success',
                    title: 'Deleted!',
                    text: 'The product has been hidden.',
                    timer: 2000
                });
            <% } else if ("1".equals(error)) { %>
                Swal.fire({
                    icon: 'error',
                    title: 'Failed!',
                    text: 'Could not hide the product.',
                    timer: 2000
                });
            <% }%>
            };
            window.onload = function () {
            <% if ("1".equals(successpro)) { %>
                Swal.fire({
                    icon: 'success',
                    title: 'Successful!',
                    text: 'Set promotion successful.',
                    timer: 2000
                });
            <% }%>
        </script>
    </body>
</html>
