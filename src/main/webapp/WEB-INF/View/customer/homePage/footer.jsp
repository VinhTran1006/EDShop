<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            /* Reset margin/padding cho body và html */
            html, body {
                margin: 0;
                padding: 0;
                min-height: 100vh; /* Đảm bảo chiều cao tối thiểu 100% viewport */
            }
            
            /* Container chính để sử dụng flexbox */
            .page-container {
                display: flex;
                flex-direction: column;
            }
            
            /* Nội dung chính sẽ chiếm hết không gian còn lại */
            .main-content {
                flex: 1;
                padding: 20px;
            }
            
            /* Footer sẽ tự động xuống cuối */
            .footer {
                background-color: #000;
                color: #ccc;
                padding: 20px;
                text-align: center;
                font-size: 14px;
                line-height: 1.6;
                margin-top: auto; /* Đẩy footer xuống cuối */
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
        </style>
    </head>
    <body>
        <div class="page-container">
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
        </div>
    </body>
</html>