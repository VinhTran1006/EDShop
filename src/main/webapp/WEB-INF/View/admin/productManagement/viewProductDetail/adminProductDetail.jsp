<html>
    <head>
        <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            html, body {
                height: 100%;
                background-color: #F2F4F7;
                font-family: "Segoe UI", Tahoma, sans-serif;
                color: #333;
            }

            .divAll {
                background-color: #F2F4F7;
                min-height: 100vh; /* đảm bảo full height */
                padding: 20px;
            }

            /* Container trắng */
            .container {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.05);
                padding: 24px;
            }

            /* Bố cục hàng chính */
            .row {
                display: flex;
                flex-wrap: wrap; /* xuống hàng khi màn nhỏ */
                gap: 20px;
            }

            /* Khối title (nằm full width trên cùng) */
            .row > div:first-child {
                flex: 1 1 100%;
            }

            /* Cột ảnh sản phẩm */
            .row > div:nth-child(2) {
                flex: 1 1 40%;
                min-width: 280px;
            }

            /* Cột thông số kỹ thuật */
            .row > div:nth-child(3) {
                flex: 1 1 60%;
                min-width: 320px;
            }

            /* Tiêu đề thông số */
            h4 {
                font-size: 1.25rem;
                font-weight: 600;
                color: #444;
                margin-bottom: 12px;
            }

            /* Nút Back */
            .btn-back {
                color: #fff !important;
                background-color: #6c757d;
                border: none;
                padding: 8px 18px;
                border-radius: 6px;
                font-size: 14px;
                text-decoration: none;
                transition: all 0.3s ease;
            }
            .btn-back:hover {
                background-color: #5c636a;
                transform: translateY(-2px);
            }

        </style>
    </head>
    <body>
        <div class="divAll">
            <div class="container">
                <div class="row">
                    <div>
                        <jsp:include page="/WEB-INF/View/admin/productManagement/viewProductDetail/productTitle.jsp" />
                    </div>

                    <div class="" style = "width: 40%">

                        <jsp:include page="/WEB-INF/View/admin/productManagement/viewProductDetail/imageProductDetail.jsp" />
                    </div>
                    <div class="" style = "width: 60%">
                        <div style = "display: flex">
                            <h4>
                                Technical specifications
                            </h4>

                            <div style = "display: flex; margin-left: auto;">
                                <a href="AdminProduct" class="btn btn-back" style = "text-decoration: none">Back</a>
                            </div>
                        </div>
                        <jsp:include page="/WEB-INF/View/admin/productManagement/viewProductDetail/productDetail.jsp" />
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>
