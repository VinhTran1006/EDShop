<%--<jsp:include page="/sideBar.jsp" />--%>


<body>
    <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />
    <div class="canle">
        <!-- Display notification -->
        <%
            String message = (String) session.getAttribute("message");
            if (message != null) {
        %>
        <div class="alert alert-info text-center">
            <%= message%>
        </div>
        <%
                session.removeAttribute("message");
            }
        %>

        <jsp:include page="/WEB-INF/View/customer/homePage/section.jsp" />
        <jsp:include page="/WEB-INF/View/customer/homePage/banner.jsp" />
        <jsp:include page="/WEB-INF/View/customer/homePage/smallBanner.jsp" />
        <jsp:include page="/WEB-INF/View/customer/homePage/bestSellerProduct.jsp" />
        <jsp:include page="/WEB-INF/View/customer/homePage/banner2.jsp" />
        <jsp:include page="/WEB-INF/View/customer/homePage/featuredProduct.jsp" />
        <jsp:include page="/WEB-INF/View/customer/homePage/discountProduct.jsp" />
    </div>
    <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
</body>

<style>
    body {
        background-color: #F2F4F7 !important;
    }
    .canle {
        padding-top: 0;
        padding-left: 30px;
        padding-right: 30px;
    }
</style>

