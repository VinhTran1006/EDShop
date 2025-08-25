<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.Product"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Product> productList = (List<Product>) request.getAttribute("productListFeatured");
    BigDecimal oldPrice;
    BigDecimal newPrice;
%>
<!DOCTYPE html>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
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
            }

            /* Tiêu đề sản phẩm */
            .new-product-label {
                font-size: 24px;
                font-weight: bold;
                color: #333;
                text-align: center;
                margin: 20px 0;
                padding: 0 20px;
                text-transform: uppercase;
                letter-spacing: 1px;
            }

            /* Container flex cho ảnh và sản phẩm */
            .content-flex {
                display: flex;
                border-radius: 10px;
                gap: 20px;
                padding: 0 20px 20px 20px;
                box-sizing: border-box;
            }

            /* Phần ảnh bên trái */
            .image-section {
                flex: 0 0 300px; /* Cố định width */
                border-radius: 5px;
                margin-bottom: 1%;
            }

            .image-section img {
                width: 100%; 
                height: auto;
                border-radius: 10px;
                object-fit: cover;
            }

            /* Wrapper cho phần scroll sản phẩm */
            .product-scroll-wrapper {
                position: relative;
                flex: 1;
                overflow: hidden;
                min-width: 0; /* Quan trọng để flex hoạt động đúng */
            }

            /* Container scroll sản phẩm */
            #product-scroll-featured {
                display: flex;
                overflow-x: auto;
                scroll-behavior: smooth;
                padding-bottom: 10px;
                width: 100%;
                scroll-snap-type: x mandatory;
                gap: 15px;
                scrollbar-width: none; /* Firefox */
                -ms-overflow-style: none; /* IE/Edge */
                box-sizing: border-box;
            }

            #product-scroll-featured::-webkit-scrollbar {
                display: none; /* Chrome, Safari */
            }

            /* Nút scroll */
            .scroll-btn {
                background-color: white;
                border: 1px solid #ccc;
                padding: 8px;
                width: 40px;
                height: 40px;
                cursor: pointer;
                border-radius: 50%;
                font-size: 20px;
                font-weight: bold;
                color: #333;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                transition: all 0.3s ease;
                position: absolute;
                top: 40%;
                z-index: 10;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .scroll-btn:hover {
                background-color: #f0f0f0;
                box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
                transform: scale(1.1);
            }

            .scroll-left-featured {
                left: -5px;
            }

            .scroll-right-featured {
                right: -5px;
            }

            /* Card sản phẩm */
            .sanPhamMoi {
                box-sizing: border-box;
                border-radius: 12px;
                padding: 15px;
                background-color: #fff;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s ease, box-shadow 0.2s ease;
                flex-shrink: 0;
                scroll-snap-align: start;
                width: 200px; /* Cố định width để tránh tràn */
                min-width: 200px;
            }

            .sanPhamMoi:hover {
                transform: translateY(-3px);
                box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
            }

            /* Hình ảnh sản phẩm */
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

            /* Tên sản phẩm */
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

            /* Giá sản phẩm */
            .giaMoi {
                font-size: 16px;
                font-weight: bold;
                color: #e74c3c;
                margin: 5px 0;
            }

            /* Responsive */
            @media (max-width: 768px) {
                .content-flex {
                    flex-direction: column;
                    gap: 15px;
                }
                
                .image-section {
                    flex: none;
                    width: 100%;
                }
                
                .sanPhamMoi {
                    width: 180px;
                    min-width: 180px;
                }
                
                .new-product-label {
                    font-size: 20px;
                }
            }

            @media (max-width: 480px) {
                .sanPhamMoi {
                    width: 160px;
                    min-width: 160px;
                    padding: 10px;
                }
                
                .new-product-label {
                    font-size: 18px;
                    margin: 15px 0;
                }
                
                .content-flex {
                    padding: 0 10px 15px 10px;
                }
            }
        </style>
</head>
<body>
    <div class="main-container">
        <p class="new-product-label">Featured Product</p>

        <div class="content-flex">
            <div class="image-section">
                <img src="https://res.cloudinary.com/dgnyskpc3/image/upload/v1750919683/Discount_abpcr2.png" alt="Featured Banner">
            </div>

            <!-- PHẦN SẢN PHẨM CUỘN NGANG -->
            <div class="product-scroll-wrapper">
                <!-- Nút trái -->
                <button onclick="scrollToLeft()" class="scroll-btn scroll-left-featured">←</button>

                <!-- Danh sách sản phẩm -->
                <div id="product-scroll-featured">
                    <% if (productList != null) {
                            for (Product pro : productList) {
                                if (pro.isIsActive() == true) {
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
                            <p class="giaMoi"><%= giaCuFormatted%> đ</p>
                        </a>
                    </div>
                    <%
                                }
                            }
                        } else { %>
                    <p>Không có sản phẩm nào</p>
                    <% }%>
                </div>

                <!-- Nút phải -->
                <button onclick="scrollToRight()" class="scroll-btn scroll-right-featured">→</button>
            </div>
        </div>
    </div>

    <!-- JS scroll ngang -->
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const container = document.getElementById('product-scroll-featured');
            if (!container) return;

            const scrollAmount = container.clientWidth * 0.8; // Scroll 80% width mỗi lần

            // Đặt hàm scroll vào window object với tên khác để tránh conflict
            window.scrollToLeft = function() {
                container.scrollBy({left: -scrollAmount, behavior: 'smooth'});
            };

            window.scrollToRight = function() {
                container.scrollBy({left: scrollAmount, behavior: 'smooth'});
            };
        });
    </script>

</body>
</html>