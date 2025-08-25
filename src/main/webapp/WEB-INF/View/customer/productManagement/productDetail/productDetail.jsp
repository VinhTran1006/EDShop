<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="dao.CategoryDAO"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    Product product = (Product) request.getAttribute("product");
    String brandName = (String) request.getAttribute("brandName");
    CategoryDAO cateDAO = new CategoryDAO();
    String categoryname = cateDAO.getCategoryNameByCategoryId(product.getCategoryID());

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= product.getProductName()%> - Product Detail</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/customerProductDetail.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            body {
                background-color: #F2F4F7;
            }
            .fa-star.checked {
                color: #f5a623;
            }
        </style>
    </head>

    <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />

    <body>
        <div style="display: flex; gap: 2%; align-items: flex-start;">

            <div style="width: 53%;">
                <div style="display: flex; gap: 4px; font-size: 14px; color: #555; margin-top: 2%; margin-bottom: 2%;">
                    <a href="/" style="text-decoration: none; color: #007bff;">Home</a> >
                    <a href="#" style="text-decoration: none; color: #007bff;"><%= categoryname%></a> >
                    <a href="#" style="text-decoration: none; color: #000;"><%= product.getProductName()%></a>
                </div>

                <div style="text-align: left; margin-bottom: 10px;">
                    <h1 style="font-size: 20px; margin: 5px 0; font-weight: bold; color: #333;">
                        <%= product.getProductName()%>
                    </h1>
                    <p style="font-size: 16px; margin: 5px 0; color: #555;">
                        Brand: <%= brandName%>
                    </p>
                </div>


                <div style="flex: 2;">
                    <div class="customerDivImageDetail">
                        <jsp:include page="/WEB-INF/View/customer/productManagement/productDetail/imageProduct.jsp" />
                    </div>

                    <p style="font-size: 20px">In Stock: <%= product.getQuantity()%></p>
                    <% BigDecimal oldPrice = product.getPrice();
                        Locale localeVN = new Locale("vi", "VN");
                        NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                        String giaCuFormatted = currencyVN.format(oldPrice);%>
                    <p class="giaMoi" style="font-size: 20px">Price: <%= giaCuFormatted%></p>

                    <!-- ADD TO CART BUTTON -->
                    <div style="margin: 20px 0;">
                        <c:choose>
                            <c:when test="${sessionScope.user != null}">
                                <!-- Người dùng đã đăng nhập -->
                                <c:choose>
                                    <c:when test="${product.quantity > 0}">
                                        <!-- Còn hàng -->
                                        <form action="AddToCartServlet" method="post" style="display: inline-block;">
                                            <input type="hidden" name="productId" value="<%= product.getProductID() %>" />
                                            <input type="hidden" name="categoryId" value="<%= product.getCategoryID() %>" />
                                            <button type="submit" 
                                                    style="background: linear-gradient(45deg, #ff6b6b, #ee5a52); 
                                                           color: white; 
                                                           border: none; 
                                                           padding: 12px 30px; 
                                                           font-size: 16px; 
                                                           font-weight: bold; 
                                                           border-radius: 8px; 
                                                           cursor: pointer; 
                                                           transition: all 0.3s ease; 
                                                           box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);"
                                                    onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 6px 20px rgba(255, 107, 107, 0.4)';"
                                                    onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='0 4px 15px rgba(255, 107, 107, 0.3)';">
                                                <i class="fas fa-cart-plus" style="margin-right: 8px;"></i>
                                                Add to Cart
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Hết hàng -->
                                        <button disabled 
                                                style="background: #ccc; 
                                                       color: #666; 
                                                       border: none; 
                                                       padding: 12px 30px; 
                                                       font-size: 16px; 
                                                       font-weight: bold; 
                                                       border-radius: 8px; 
                                                       cursor: not-allowed;">
                                            <i class="fas fa-times-circle" style="margin-right: 8px;"></i>
                                            Out of Stock
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                        </c:choose>
                    </div>

                    <div class="customerDivCommitted">
                        <jsp:include page="/WEB-INF/View/customer/productManagement/productDetail/committed.jsp" />
                    </div>
                </div>
                <%-- ⭐ Feedback Section Start --%>
                <div class="customerFeedbackSection" style="margin-top: 30px; background: #fff; padding: 20px; border-radius: 12px; box-shadow: 0 0 8px rgba(0,0,0,0.05);">
                    <div style="text-align: center; margin-bottom: 20px; position: relative;">
                        <hr style="border: none; height: 1px; background: #ddd;">
                        <h2 style="font-size: 20px; font-weight: bold; margin: -15px auto 0; background: #fff; display: inline-block; padding: 0 15px; color: #333;">
                            Customer Feedback
                        </h2>
                    </div>
                    <c:if test="${not empty productRatings}">
                        <div style="text-align: center; margin-bottom: 20px;">
                            <strong style="font-size: 18px;">Average Rating:</strong>
                            <span style="color: #f5a623; font-size: 18px;">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="fa fa-star ${i <= averageRating ? 'checked' : ''}"></i>
                                </c:forEach>
                                (<fmt:formatNumber value="${averageRating}" maxFractionDigits="1" /> / 5)
                            </span>
                        </div>
                    </c:if>
                    <c:if test="${empty productRatings}">
                        <p style="text-align: center; color: gray;">No feedback available for this product.</p>
                    </c:if>

                    <c:forEach var="rating" items="${productRatings}">
                        <div style="border: 1px solid #e1e1e1; border-radius: 8px; padding: 15px; margin-bottom: 20px; background: #fafafa;">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <strong>${rating.fullName}</strong> 
                                <small style="color: gray;">${rating.createdDate}</small>
                            </div>

                            <c:set var="stars" value="${rating.star}" />
<c:forEach begin="1" end="5" var="i">
    <i class="fa fa-star ${i <= stars.intValue() ? 'checked' : ''}"></i>
</c:forEach>


                            <p style="margin-top: 8px; font-size: 15px;">${rating.comment}</p>

                            <%-- ✅ Nếu feedback có reply từ staff --%>
                            <c:if test="${not empty rating.reply}">
                                <div style="margin-top: 15px; padding: 10px 15px; background: #eaf4ff; border-left: 4px solid #3399ff; border-radius: 6px;">
                                    <strong style="color: #004a99;">Staff Response:</strong>
                                    <p style="margin: 6px 0; font-size: 14px; color: #333;">${rating.reply}</p>

                                    <c:if test="${not empty rating.replyDate}">
                                        <small style="color: gray; display:block; margin-top:5px;">
                                            Replied on: ${rating.replyDate}
                                        </small>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>

                <%-- ⭐ Feedback Section End --%>
            </div>

            <div class="customerDivinfomationDetail" style="margin-top: 15px; width: 50%; float: right;">
                <jsp:include page="/WEB-INF/View/customer/productManagement/productDetail/infomationDetail.jsp" />
            </div>
        </div>
        <div class="customerDivaddToCartAndBuyNow" style="width: 40%; margin-top: 6.8%; max-height: 820px;"></div>
    </body>

    <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
    <% String successcreate = request.getParameter("successcreate"); %>
    <% String checkquantity = request.getParameter("checkquantity");%> 
    <% String error = request.getParameter("error");%>

    <script>
        window.onload = function () {
            var success = '<%= successcreate%>';
            var error = '<%= checkquantity%>';
            var generalError = '<%= error%>';

            if (success === '1') {
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Product has been added to cart.',
                    timer: 2000
                });
            } else if (error === '1') {
                Swal.fire({
                    icon: 'error',
                    title: 'Failed!',
                    text: 'The quantity of product in stock is not enough to order.',
                    timer: 2000
                });
            } else if (generalError === 'add_failed') {
                Swal.fire({
                    icon: 'error',
                    title: 'Failed!',
                    text: 'Failed to add product to cart. Please try again.',
                    timer: 2000
                });
            } else if (generalError === 'product_not_found') {
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'Product not found or no longer available.',
                    timer: 2000
                });
            }
        };
    </script>

</html>