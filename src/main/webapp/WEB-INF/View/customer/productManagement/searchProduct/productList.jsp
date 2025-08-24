

<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.Product"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Product> productList = (List<Product>) request.getAttribute("productList");
    BigDecimal oldPrice;
    BigDecimal newPrice;
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/filterProduct.css">
        <style>
            #product-scroll-new {
                display: flex;
                overflow-x: auto;
                scroll-behavior: smooth;
                padding-bottom: 10px;
                width: 100%;
                max-width: 100%;
                scroll-snap-type: x mandatory;
                gap: 10px;
                scrollbar-width: none;       /* Firefox */
                -ms-overflow-style: none;    /* IE/Edge */
            }
            #product-scroll-new::-webkit-scrollbar {
                display: none; /* Chrome, Safari */
            }

            .divProduct {
                box-sizing: border-box;
                border-radius: 12px;
                background-color: #fff;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
                transition: transform 0.2s ease, box-shadow 0.2s ease;
                flex-shrink: 0;
                scroll-snap-align: start;
                padding: 10px;
                margin: 6px;
            }

            .divProduct:hover {
                transform: translateY(-3px);
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
            }

            .divHinh {
                width: 100%;
                height: 200px;
                display: flex;
                align-items: center;
                justify-content: center;
                overflow: hidden;
                border-radius: 8px;
                background-color: #f9f9f9;
            }

            .divHinh img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                border-radius: 8px;
            }

            /* Thêm style text */
            .productName {
                font-size: 14px;
                font-weight: 500;
                color: #222;
                margin: 8px 0 4px;
                line-height: 1.4;
                height: 40px;
                overflow: hidden;
            }

            .giaMoi {
                font-size: 15px;
                font-weight: 600;
                color: #e63946;
                margin-bottom: 6px;
            }

            .divTraGop {
                margin-top: 4px;
            }
            .traGop {
                font-size: 12px;
                font-weight: 500;
                color: #0077cc;
                margin: 0;
            }
        </style>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/filterProduct.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/newProduct2.css">
    </head>
    <body>
        <!-- Danh sách sản phẩm -->
        <div style="gap: 0.25%; display: flex; width: 100%; flex-wrap: wrap;">
            <%
                if (productList == null || productList.isEmpty()) {
            %>
            <p>No matching products found</p>
            <%
            } else {
                for (Product pro : productList) {
                    oldPrice = pro.getPrice();
                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                    String giaCuFormatted = currencyVN.format(oldPrice);
            %>
            <div class="divProduct" style="width: 18.9%">
                <a href="<%= request.getContextPath()%>/ProductDetail?productId=<%= pro.getProductID()%>&categoryId=<%= pro.getCategoryID()%>" 
                   style="text-decoration: none; color: inherit; display: block;">
                    <div class="divHinh">
                        <img style="width: 100%" src="<%= pro.getImageUrl1()%>" alt="anhDienThoai" class="anhDienThoaiDocQuyen">
                    </div>
                    <div class="divTraGop">
                        <p class="traGop">Trả góp 0%</p>
                    </div>
                    <p class="productName"><%= pro.getProductName()%></p>
                    <p class="giaMoi"><%= giaCuFormatted%> đ</p>
                </a>
            </div>
            <%
                    } // end for
                }
            %>
        </div>


    </body>
</html>
