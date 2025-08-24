
<%@page import="model.ProductDetail"%>
<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Product product = (Product) request.getAttribute("product");
    List<ProductDetail> productDetail = (List<ProductDetail>) request.getAttribute("productDetailList");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="Css/imageProductDetail1.css">
        <style>
            /* productImage.jsp */
            body {
                font-family: "Segoe UI", Tahoma, sans-serif;
                background-color: #f9fafb;
                color: #333;
                margin: 0;
                padding: 20px;
            }

            .divAnhLon {
                text-align: center;
                margin-bottom: 15px;
            }
            .divAnhLon img {
                max-width: 100%;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }

            .div4AnhNho {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 24px;   /* khoảng cách giữa các khung ảnh nhỏ */
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

            .div4AnhNho .img-thumbnail{
                width:84px;
                height:84px;
                padding:0;                     /* quan trọng */
                border:1px solid #e5e7eb;
                border-radius:10px;
                background:#fff;
                overflow:hidden;               /* cắt phần thừa của ảnh */
                display:flex;
                align-items:center;
                justify-content:center;
                box-sizing:border-box;
            }

            /* Ảnh bên trong fill đầy khung, không méo */
            .div4AnhNho .img-thumbnail img{
                width:100%;
                height:100%;
                object-fit:cover;              /* cắt cho vuông */
                display:block;
            }

            /* Hiệu ứng hover/active (tuỳ chọn) */
            .div4AnhNho .img-thumbnail:hover{
                border-color:#0d6efd;
                box-shadow:0 0 0 3px rgba(13,110,253,.2);
            }
        </style>
    </head>
    <body>
        <%
            if (product != null) {
        %>

        <%
            if (productDetail != null && product != null) {
                for (ProductDetail proDetail : productDetail) {
        %>

        <div style = "width: 60%; margin: 0 auto;" class="divAnhLon text-center"> 
            <img id="mainImage" src="<%= product.getImageUrl1()%>" class="anhLon">
        </div>

        <div class="div4AnhNho">
            <div class="img-thumbnail">
                <img src="<%= product.getImageUrl2()%>" onclick="changeMainImage(this.src)">
            </div>
            <div class="img-thumbnail">
                <img src="<%= product.getImageUrl3()%>" onclick="changeMainImage(this.src)">
            </div>
            <div class="img-thumbnail">
                <img src="<%= product.getImageUrl4()%>" onclick="changeMainImage(this.src)">
            </div>
        </div>

        <%
                break;
            }
        } else {
        %>
        <div class="alert alert-warning">DO NOT HAVE PRODUCT DATA!</div>
        <% }
        %>
        <%
            } // { cua check khac null
        %>
    </body>

    <script>
        function changeMainImage(src) {
            document.getElementById("mainImage").src = src;
        }
    </script>
</html>
