
<%@page import="model.ProductDetail"%>
<%@page import="model.Suppliers"%>
<%@page import="model.Brand"%>
<%@page import="model.Category"%>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    Product product = (Product) request.getAttribute("product");
    List<Category> categoryList = (List<Category>) request.getAttribute("categoryList");
    List<Brand> brandList = (List<Brand>) request.getAttribute("brandList");
    List<Suppliers> supList = (List<Suppliers>) request.getAttribute("supList");
    List<ProductDetail> productDetailList = (List<ProductDetail>) request.getAttribute("productDetailList");
%>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Product</title>
        <link rel="stylesheet" href="Css/staffUpdateProduct1.css">
    </head>
    <body>
        <% if (product != null && productDetailList != null) {%>
        <c:if test="${not empty sessionScope.existedProduct}">
            <span style="color:red">${sessionScope.existedProduct}</span>
        </c:if>
        <c:remove var="existedProduct" scope="session"/>

        <div class="form-wrapper">
            <div class="mb-3">
                <label class="form-label">ID</label>
                <input type="text" class="form-control" name="productId" required value="<%= product.getProductID()%>" readonly/>
            </div>

            <div class="mb-3">
                <label class="form-label">Product Name</label>
                <c:if test="${not empty sessionScope.errorProductName}">
                    <span style="color:red">${sessionScope.errorProductName}</span>
                </c:if>
                <c:remove var="errorProductName" scope="session"/>
                <input type="text" class="form-control" name="productName" required value="<%= product.getProductName()%>"/>
            </div>


            <div class="mb-3">
                <label class="form-label">Description</label>
                <c:if test="${not empty sessionScope.errorDescription}">
                    <span style="color:red">${sessionScope.errorDescription}</span>
                </c:if>
                <c:remove var="errorDescription" scope="session"/>
                <input type="text" class="form-control" name="description" required value="<%= product.getDescription()%>"/>
            </div><!-- comment -->
            <%
                String priceFormatted = "";
                if (product.getPrice() != null) {
                    BigDecimal price = product.getPrice();
                    Locale localeVN = new Locale("vi", "VN");
                    NumberFormat currencyVN = NumberFormat.getInstance(localeVN);
                    priceFormatted = currencyVN.format(price);
                }

            %>

            <div class="mb-3">
                <label class="form-label">Price</label>
                <c:if test="${not empty sessionScope.errorPrice}">
                    <span style="color:red">${sessionScope.errorPrice}</span>
                </c:if>
                <c:remove var="errorPrice" scope="session"/>
                <input type="text" min="1" class="form-control" name="price" required value="<%= product.getPrice()%>"/>
            </div>
            <%
                }
            %>


            <div class="mb-3">
                <label class="form-label">Warranty period</label>
                <c:if test="${not empty sessionScope.errorWarranty}">
                    <span style="color:red">${sessionScope.errorWarranty}</span>
                </c:if>
                <c:remove var="errorWarranty" scope="session"/>
                <input type="text" class="form-control" name="warranty" required value="<%= product.getWarrantyPeriod()%>"/>
            </div>

           

            <div class="mb-3">
                <label class="form-label">Supplier</label>
                <select class="form-control" id="supplier" name="supplier" required>
                    <option value="">-- Select Supplier --</option>
                    <% for (Suppliers sup : supList) {
                            boolean isSelected = (sup.getSupplierID() == product.getSupplierID());
                    %>
                    <option value="<%= sup.getSupplierID()%>" <%= isSelected ? "selected" : ""%>>
                        <%= sup.getName()%>
                    </option>
                    <% }%>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label">Category</label>
                <!-- Hiển thị tên category, readonly -->
                <input type="text" class="form-control" 
                       value="<%= categoryList.stream()
                               .filter(c -> c.getCategoryId() == product.getCategoryID())
                               .findFirst()
                               .map(Category::getCategoryName)
                               .orElse("")%>" 
                       readonly />

                <!-- Gửi id về server -->
                <input type="hidden" name="category" value="<%= product.getCategoryID()%>" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Brand</label>
                <select class="form-control" id="brand" name="brand" required>
                    <option value="">-- Select brand --</option>
                    <% for (Brand b : brandList) {
                            boolean isSelected = (b.getBrandId() == product.getBrandID());
                    %>
                    <option value="<%= b.getBrandId()%>" <%= isSelected ? "selected" : ""%>>
                        <%= b.getBrandName()%>
                    </option>
                    <% }%>
                </select>
            </div>



        </div>

    </body>

    <script>

        function updateBrands() {
            const categoryId = document.getElementById("category").value;
            const brandSelect = document.getElementById("brand");
            const selectedBrandId = "<%= product.getBrandID()%>";

            brandSelect.innerHTML = '<option value="">-- Chọn thương hiệu --</option>';
            jsBrandList.forEach(brand => {
                if (brand.categoryId.toString() === categoryId.toString()) {
                    const option = document.createElement("option");
                    option.value = brand.id;
                    option.textContent = brand.name;
                    if (brand.id.toString() === selectedBrandId.toString()) {
                        option.selected = true;
                    }
                    brandSelect.appendChild(option);
                }
            });
        }

        function previewSelectedImage(event) {
            const input = event.target;
            const preview = document.getElementById('previewImage');
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    preview.src = e.target.result;
                };
                reader.readAsDataURL(input.files[0]);
            }
        }
    </script>

    <style>
        .form-check-input.rounded-circle {
            border-radius: 50% !important;
            width: 1.2em;
            height: 1.2em;
        }
    </style>
</html>
