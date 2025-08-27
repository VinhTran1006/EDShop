

<%@page import="model.Suppliers"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.Category"%>
<%@page import="model.Brand"%>
<%@page import="java.util.List"%>
<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Product product = (Product) request.getAttribute("product");
    List<Category> categoryList = (List<Category>) request.getAttribute("categoryList");
    List<Brand> brandList = (List<Brand>) request.getAttribute("brandList");
    List<Suppliers> supList = (List<Suppliers>) request.getAttribute("supList");
%>
<!DOCTYPE html>
<html>
    <head>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="form-wrapper" style = "width: 100%">

            <c:if test="${not empty sessionScope.existedProduct}">
                <span style="color:red">${sessionScope.existedProduct}</span>
            </c:if>
            <c:remove var="existedProduct" scope="session"/>

            <div class="mb-3">
                <label class="form-label">Product Name</label>
                <c:if test="${not empty sessionScope.errorProductName}">
                    <span style="color:red">${sessionScope.errorProductName}</span>
                </c:if>
                <c:remove var="errorProductName" scope="session"/>
                <input type="text" class="form-control" name="productName"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <c:if test="${not empty sessionScope.errorDescription}">
                    <span style="color:red">${sessionScope.errorDescription}</span>
                </c:if>
                <c:remove var="errorDescription" scope="session"/>
                <input type="text" class="form-control" name="description"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Price</label>
                <c:if test="${not empty sessionScope.errorPrice}">
                    <span style="color:red">${sessionScope.errorPrice}</span>
                </c:if>
                <c:remove var="errorPrice" scope="session"/>
                <input type="text" class="form-control" name="price"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Warranty period</label>
                <c:if test="${not empty sessionScope.errorWarranty}">
                    <span style="color:red">${sessionScope.errorWarranty}</span>
                </c:if>
                <c:remove var="errorWarranty" scope="session"/>
                <input type="text" class="form-control" name="warranty"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Quantity</label>
                <c:if test="${not empty sessionScope.errorQuantity}">
                    <span style="color:red">${sessionScope.errorQuantity}</span>
                </c:if>
                <c:remove var="errorQuantity" scope="session"/>
                <input type="text" class="form-control" name="quantity"/>
            </div>

            <div class="mb-3">
                <label class="form-label">Suppliers</label>
                <select class="form-control" id="suppliers" name="supplier" required>
                    <option value="">-- Select suppliers --</option>
                    <% for (Suppliers sup : supList) {%>
                    <option value="<%= sup.getSupplierID()%>"><%= sup.getName()%></option>
                    <% }%>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Category</label>
                <select class="form-control" id="category" name="category" onchange="updateBrands()" required>
                    <option value="">-- Select category --</option>
                    <% for (Category c : categoryList) {%>
                    <option value="<%= c.getCategoryId()%>"><%= c.getCategoryName()%></option>
                    <% }%>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Brand</label>
                <select class="form-control" id="brand" name="brand" required>
                    <option value="">-- Select brand --</option>
                    <% for (Brand b : brandList) {%>
                    <option value="<%= b.getBrandId()%>"><%= b.getBrandName()%></option>
                    <% }%>
                </select>
            </div>
        </div>
    </body>


</html>
