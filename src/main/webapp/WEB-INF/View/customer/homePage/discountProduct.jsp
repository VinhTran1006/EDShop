<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.Product"%>
<%@page import="java.util.List"%>
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
        <title>Sản phẩm mới</title>
        <style>
            /* Container chính */
            .main-container {
                width: 100%; 
                border-radius: 15px; 
                margin-top: 1%; 
                background-color: #fff; 
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
                box-sizing: border-box;
                overflow: hidden;
                padding: 20px;
            }

            /* Container grid cho sản phẩm */
            .product-grid {
                display: flex;
                flex-wrap: wrap;
                gap: 15px;
                width: 100%;
                justify-content: flex-start;
            }

            /* Card sản phẩm - giống như file đầu */
            .sanPhamMoi {
                box-sizing: border-box;
                border-radius: 12px;
                padding: 15px;
                background-color: #fff;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s ease, box-shadow 0.2s ease;
                flex-shrink: 0;
                width: calc(20% - 12px); /* 5 sản phẩm trên một hàng */
                min-width: 200px;
            }

            .sanPhamMoi:hover {
                transform: translateY(-3px);
                box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
            }

            /* Hình ảnh sản phẩm - giống như file đầu */
            .divHinh {
                margin-bottom: 10px;
                text-align: center;
            }

            .anhDienThoaiDocQuyen {
                width: 100% !important;
                height: 150px;
                object-fit: cover;
                border-radius: 8px;
            }

            /* Tên sản phẩm - giống như file đầu */
            .productName {
                font-size: 14px;
                font-weight: 600;
                color: #333;
                margin: 10px 0 5px 0;
                text-overflow: ellipsis;
                overflow: hidden;
                white-space: nowrap;
                line-height: 1.4;
            }

            /* Giá sản phẩm - giống như file đầu */
            .giaMoi {
                font-size: 16px;
                font-weight: bold;
                color: #e74c3c;
                margin: 5px 0;
            }

            /* Responsive */
            @media (max-width: 1200px) {
                .sanPhamMoi {
                    width: calc(25% - 11.25px); /* 4 sản phẩm trên một hàng */
                }
            }

            @media (max-width: 992px) {
                .sanPhamMoi {
                    width: calc(33.333% - 10px); /* 3 sản phẩm trên một hàng */
                }
            }

            @media (max-width: 768px) {
                .sanPhamMoi {
                    width: calc(50% - 7.5px); /* 2 sản phẩm trên một hàng */
                    min-width: 180px;
                }
                
                .main-container {
                    padding: 15px;
                }
            }

            @media (max-width: 480px) {
                .sanPhamMoi {
                    width: 100%; /* 1 sản phẩm trên một hàng */
                    min-width: 160px;
                    padding: 10px;
                }
                
                .main-container {
                    padding: 10px;
                }
                
                .product-grid {
                    gap: 10px;
                }
            }
        </style>
    </head>
    <body>
        <div class="main-container">
            <!-- Container grid cho sản phẩm -->
            <div class="product-grid">
                <% if (productList != null) {
                        for (Product pro : productList) {
                            oldPrice = pro.getPrice();
                            Locale localeVN = new Locale("vi", "VN");
                            NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                            String giaCuFormatted = currencyVN.format(oldPrice);
                %>
                <div class="sanPhamMoi">
                    <a href="<%= request.getContextPath()%>/ProductDetail?productId=<%= pro.getProductID()%>&categoryId=<%= pro.getCategoryID()%>" style="text-decoration: none; color: inherit; display: block;">
                        <div class="divHinh">
                            <img src="<%= pro.getImageUrl1()%>" alt="<%= pro.getProductName()%>" class="anhDienThoaiDocQuyen">
                        </div>
                        <div class="divTraGop">
                        </div>
                        <p class="productName"><%= pro.getProductName()%></p>
                        <% if(pro.getQuantity() >= 1 ) {%>
                        <p class="giaMoi"><%= giaCuFormatted%> đ</p>
                        <%} else { %>
                        <p class="giaMoi">Out Of Stock</p>
                        <%}%>
                    </a>
                </div>
                <%
                    } // end for
                } else { %>

                <p>No Product Available</p>

                <% }%>
            </div>
        </div>
    </body>
</html>