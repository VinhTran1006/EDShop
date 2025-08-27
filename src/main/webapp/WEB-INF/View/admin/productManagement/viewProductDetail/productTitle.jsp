<%@page import="model.Brand"%>
<%@page import="model.Category"%>
<%@page import="dao.BrandDAO"%>
<%@page import="dao.CategoryDAO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Product product = (Product) request.getAttribute("product");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Detail</title>
        <!-- Bootstrap CDN -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Fontawesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
        <style>
            body {
                background-color: #f9fafb;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .page-header {
                display: flex;
                align-items: center;
                gap: 12px;
                margin-bottom: 25px;
            }
            .page-header h1 {
                font-size: 2.5rem;
                margin: 0;
                color: #1e3a8a;
                font-weight: 700;
            }
            .page-header span {
                font-size: 1.2rem;
                color: gray;
                margin-top: 8px;
            }
            .card {
                border-radius: 16px;
                box-shadow: 0 4px 20px rgba(0,0,0,0.08);
                padding: 25px;
                background: white;
            }
            .card h2 {
                font-size: 1.6rem;
                font-weight: 600;
                color: #111827;
                margin-bottom: 10px;
            }
            .card p {
                font-size: 1rem;
                margin: 6px 0;
                color: #374151;
            }
            .price {
                font-size: 1.2rem;
                font-weight: bold;
                color: #dc2626;
            }
            .badge-category {
                background: #2563eb;
                color: white;
                padding: 4px 10px;
                border-radius: 10px;
                font-size: 0.9rem;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/View/admin/productManagement/deleteProduct/adminDeleteProduct.jsp" />
        <div class="container py-4">
            <!-- Header -->
            <div class="page-header">
                <h1><i class="fa fa-box"></i> Product Management</h1>
                <span>View Product Detail</span>
            </div>

            <!-- Product Card -->
            <div class="card">
                <h2><%= product.getProductName()%></h2>
                <p><strong>In Stock:</strong> <%= product.getQuantity()%></p>
                <p><strong>Description:</strong> <%= product.getDescription()%></p>

                <%
                    CategoryDAO cateDAO = new CategoryDAO();
                    BrandDAO braDAO = new BrandDAO();

                    Category category = cateDAO.getCategoryById(product.getCategoryID());
                    String brand = braDAO.getBrandNameByBrandId(product.getBrandID());

                    BigDecimal oldPrice = product.getPrice();
                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                    String giaCuFormatted = currencyVN.format(oldPrice);
                %>

                <p><strong>Category:</strong> 
                    <span class="badge-category"><%= category.getCategoryName()%></span>
                </p>
                <p><strong>Brand:</strong> <%= brand%></p>
                <p class="price"><i class="fa fa-tags"></i> Price: <%= giaCuFormatted%> ₫</p>
                <div class="action-buttons">
                    <a href="AdminUpdateProduct?productId=<%= product.getProductID()%>" class="btn btn-primary">Edit</a>
                    <button class="btn btn-danger" onclick="confirmDelete(<%= product.getProductID()%>)">Delete</button>
                </div>
            </div>
        </div>
    </body>
</html>
