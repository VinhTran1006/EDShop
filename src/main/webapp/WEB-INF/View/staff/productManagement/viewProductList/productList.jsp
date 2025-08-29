<%@page import="model.Suppliers"%>
<%@page import="dao.SupplierDAO"%>
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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
                padding: 12px;
            }
            .product-table th {
                background-color: #2563eb;
                color: white;
            }
            .product-table img {
                max-width: 60px;
                display: block;
                margin: 0 auto;
                border-radius: 8px;
            }
            .action-buttons {
                display: flex;
                justify-content: center;
                gap: 8px;
                flex-wrap: wrap;
            }
            .product-table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
                border-radius: 16px;
                overflow: hidden;
                box-shadow: 0 4px 20px rgba(0,0,0,0.05);
                table-layout: fixed; /* quan trọng */
            }

            .product-table th,
            .product-table td {
                text-align: center;
                vertical-align: middle;
                padding: 12px;
                word-wrap: break-word; /* để text tự xuống dòng */
            }

            /* Đặt chiều rộng cho từng cột */
            .product-table th:nth-child(1),
            .product-table td:nth-child(1) {
                width: 60px;
            }   /* ID */
            .product-table th:nth-child(2),
            .product-table td:nth-child(2) {
                width: 200px;
                text-align: left;
            } /* Product Name */
            .product-table th:nth-child(3),
            .product-table td:nth-child(3) {
                width: 120px;
            }  /* Price */
            .product-table th:nth-child(4),
            .product-table td:nth-child(4) {
                width: 120px;
            }  /* Category */
            .product-table th:nth-child(5),
            .product-table td:nth-child(5) {
                width: 120px;
            }  /* Brand */
            .product-table th:nth-child(6),
            .product-table td:nth-child(6) {
                width: 150px;
            }  /* Supplier */
            .product-table th:nth-child(7),
            .product-table td:nth-child(7) {
                width: 100px;
            }  /* Quantity */
            .product-table th:nth-child(8),
            .product-table td:nth-child(8) {
                width: 80px;
            }   /* Image */
            .product-table th:nth-child(9),
            .product-table td:nth-child(9) {
                width: 120px;
            }  /* Action */

        </style>
    </head>
    <body>
        <div style="width: 80%; margin-left: 18.9%">
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
                            <th>Supplier</th>
                            <th>Quantity</th>
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
                            SupplierDAO supDAO = new SupplierDAO();
                            for (Product product : productList) {
                                String brandname = branddao.getBrandNameByBrandId(product.getBrandID());
                                String CategoryName = catedao.getCategoryNameByCategoryId(product.getCategoryID());
                                Suppliers Supplier = supDAO.getSupplierById(product.getSupplierID());
                                if (product != null) {
                                    String giaFormatted = "N/A";
                                    if (product.getPrice() != null) {
                                        giaFormatted = currencyVN.format(product.getPrice());
                                    }
                        %>
                        <tr>
                            <td><%= product.getProductID()%></td>
                            <td class="fw-semibold text-start"><%= product.getProductName()%></td>
                            <td class="text-danger fw-bold"><%= giaFormatted%></td>
                            <td><%= CategoryName%></td>
                            <td><%= brandname%></td>
                            <td><%= Supplier.getName()%></td>
                            <td><%= product.getQuantity()%></td>
                            <td>
                                <img src="<%= (product.getImageUrl1() != null) ? product.getImageUrl1() : ""%>" alt="Product Image">
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <a href="ProductListForStaffDetail?productId=<%= product.getProductID()%>" 
                                       class="btn btn-sm btn-warning text-white px-2 py-1" 
                                       style="font-size: 12px;">
                                        <i class="fa fa-eye"></i> Detail
                                    </a>
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
        </script>
    </body>
</html>
