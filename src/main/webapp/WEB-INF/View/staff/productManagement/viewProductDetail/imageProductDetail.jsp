<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Product product = (Product) request.getAttribute("product");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết hình ảnh sản phẩm</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

        <link rel="stylesheet" href="Css/imageProductDetail1.css">
        <style>
            /* --- Ảnh sản phẩm --- */
            .divAnhLon {
                text-align: center;
                margin-bottom: 15px;
            }
            .divAnhLon img {
                max-width: 100%;
                border-radius: 12px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            }

            /* --- Ảnh nhỏ (thumbnail) --- */
            .div4AnhNho {
                display: flex;
                justify-content: center;
                gap: 10px;
                margin-top: 12px;
            }
            .div4AnhNho img {
                width: 80px;
                height: 80px;
                border-radius: 8px;
                object-fit: cover;
                cursor: pointer;
                border: 2px solid transparent;
                transition: all 0.2s ease;
            }
            .div4AnhNho img:hover {
                border-color: #007bff;
                transform: scale(1.05);
            }

        </style>

    </head>

    <body>


        <div class = "divAllImg" style = "width: 100%">
            <%
                if (product != null) {

            %>

            <div class="divAnhLon text-center col-md-12"> 
                <img style = "width: 100%" id="mainImage" src="<%= product.getImageUrl1()%>" class="anhLon img-fluid main-img">
            </div>

            <div style = "" class="d-flex flex-wrap gap-2 row div4AnhNho">
                <div class="img-thumbnail col-auto" style = "margin-left: 8%;">
                    <img class ="" src="<%= product.getImageUrl2()%>" onclick="changeMainImage(this.src)">
                </div>

                <div class="img-thumbnail col-auto">
                    <img class = "" src="<%= product.getImageUrl3()%>" onclick="changeMainImage(this.src)">
                </div>

                <div class="img-thumbnail col-auto">
                    <img class = "" src="<%= product.getImageUrl4()%>" onclick="changeMainImage(this.src)">
                </div>
            </div>
            <%
            } else {
            %>
            <div class="alert alert-warning">Do NOT HAVE PRODUCT DATA!</div>
            <% }
            %>
        </div>
    </body>
</html>

<script>
    function changeMainImage(src) {
        document.getElementById("mainImage").src = src;
    }
</script>

