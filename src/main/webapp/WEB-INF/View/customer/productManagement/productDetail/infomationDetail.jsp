

<%@page import="model.Attribute"%>
<%@page import="model.Product"%>
<%@page import="model.ProductDetail"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Attribute> attributeList = (List<Attribute>) request.getAttribute("attributeList");
    List<ProductDetail> productDetailList = (List<ProductDetail>) request.getAttribute("productDetailList");
    Product product = (Product) request.getAttribute("product");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="Css/productDetail1.css">
    </head>
    <body>
        <h1 style = "font-size: 16px; text-align: center;">
            Specifications
        </h1>

        <div class = " col-md-12">
            <div class = "row">
                <div class="col-md-12">
                    <table class="category-table">
                        <!-- Tên nhóm -->
                        <tbody id="detailGroup">
                            <%
                                if (attributeList != null && !attributeList.isEmpty()) {
                                    for (Attribute a : attributeList) {
                                        boolean hasValue = false;
                            %>
                            <tr>
                                <td class="category-name" style = "font-size: 14px;">
                                    <%= a.getAtrributeName()%>
                                </td>
                                <td class="attribute-values">
                                    <%
                                        if (productDetailList != null) {
                                            for (ProductDetail proDetail : productDetailList) {
                                                if (proDetail.getAttibuteID() == a.getAttributeID()) {
                                                    hasValue = true;
                                    %>
                                    <div style = "font-size: 14px;" class="attribute-item"><%= proDetail.getAttributeValue()%></div>

                                    <%
                                                }
                                            }
                                        }
                                        if (!hasValue) {
                                    %>
                                    <div class="attribute-item">No data</div>
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script>
            function toggleDetails(index) {
                const detailGroup = document.getElementById("detailGroup" + index);
                const arrowIcon = document.getElementById("arrow" + index);

                if (detailGroup.classList.contains("hidden")) {
                    detailGroup.classList.remove("hidden");
                    arrowIcon.innerText = "▲"; // hoặc dùng ▾ nếu thích
                } else {
                    detailGroup.classList.add("hidden");
                    arrowIcon.innerText = "▼";
                }
            }
        </script>

        <style>
            /* specification.jsp */
            /* productDetail.jsp */
            body {
                font-family: "Segoe UI", Tahoma, sans-serif;
                background-color: #f9fafb;
                color: #333;
                margin: 0;
                padding: 20px;
            }

            .product-detail-container {
                display: flex;
                gap: 24px;
                align-items: flex-start;
            }

            /* Cột trái - Hình ảnh */
            .product-images {
                flex: 1;
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
                gap: 12px;
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

            /* Cột phải - Thông tin chi tiết */
            .product-specs {
                flex: 1;
            }
            h1 {
                font-size: 20px;
                font-weight: 600;
                color: #222;
                margin-bottom: 16px;
            }
            .category-table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 2px 8px rgba(0,0,0,0.05);
            }
            .category-table tr {
                border-bottom: 1px solid #eee;
            }
            .category-table tr:last-child {
                border-bottom: none;
            }
            .category-table td {
                padding: 10px 14px;
                font-size: 14px;
            }
            .category-name {
                width: 35%;
                font-weight: 600;
                color: #333;
                background: #f3f4f6;
            }
            .attribute-item {
                padding: 4px 8px;
                border-radius: 6px;
                display: inline-block;
                margin: 2px;
                background: #f9fafb;
                border: 1px solid #e5e7eb;
                font-size: 13px;
            }
            .category-table .attribute-item {
                color: #333 !important;  /* ép màu chữ đen */
            }

        </style>
    </body>
</html>
