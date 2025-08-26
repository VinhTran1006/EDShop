
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
        <title>JSP Page</title>
    </head>

    <body>
        <div class = "container">
            <div  style="display: flex; align-items: center; gap: 10px; margin-bottom: 3%;">

                <h1 class = "display-5 fw-bold" style="font-size: 320%; margin: 0;">Product Management</h1>
                <span style="font-size: 120%; color: gray; margin-top: 4%;">View Product Detail</span>
            </div>
           

            <div style="text-align: left; margin-bottom: 10px;">
                <h1 style="font-size: 20px; margin: 5px 0; font-weight: bold; color: #333;">
                    <%= product.getProductName()%>
                </h1>
                <p style="font-size: 20px">In Stock: <%= product.getQuantity()%></p>
                <% BigDecimal oldPrice = product.getPrice();
                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                    String giaCuFormatted = currencyVN.format(oldPrice);%>
                <p class="giaMoi" style="font-size: 20px">Price: <%= giaCuFormatted%></p>
            </div>
        </div>
    </body>
</html>
