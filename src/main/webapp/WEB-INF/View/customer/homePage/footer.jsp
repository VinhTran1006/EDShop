<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            .footer {
                position: fixed;         /* Cố định footer */
                bottom: 0;               /* Dính vào đáy */
                left: 0;                 /* Căn từ trái */
                width: 100%;             /* Trải ngang toàn màn hình */
                background-color: #000;  /* Nền đen */
                color: #ccc;             /* Màu chữ xám nhẹ */
                padding: 20px;
                text-align: center;
                font-size: 14px;
                line-height: 1.6;
                z-index: 1000;           /* Đảm bảo nằm trên nội dung khác */
            }

            .footer a {
                color: #ccc;
                text-decoration: none;
                margin: 0 10px;
            }

            .footer a:hover {
                color: #fff;
            }

            .footer .footer-links {
                margin-bottom: 12px;
            }

            .footer .copyright {
                font-size: 13px;
                color: #888;
            }

            /* Chừa khoảng trống phía dưới nội dung để không bị footer che */
            body {
                margin: 0;
                padding-bottom: 80px; /* cao hơn chiều cao footer một chút */
            }

        </style>
    </head>
    <body>
        <div class="footer">
            <div class="footer-links">
                <a href="#">Giới thiệu</a> |
                <a href="#">Liên hệ</a> |
                <a href="#">Chính sách bảo mật</a> |
                <a href="#">Điều khoản sử dụng</a>
            </div>
            <div class="copyright">
                &copy; 2025 Bản quyền thuộc về <strong>Group 2</strong>. All rights reserved.
            </div>
        </div>
    </body>
</html>
