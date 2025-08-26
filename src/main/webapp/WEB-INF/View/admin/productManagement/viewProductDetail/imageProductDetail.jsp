<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Product product = (Product) request.getAttribute("product");
%>

<style>
    /* --- Ảnh chính --- */
    .main-image {
        width: 100%;
        max-height: 400px;
        object-fit: contain;
        border-radius: 12px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        margin-bottom: 15px;
    }

    /* --- Ảnh nhỏ --- */
    .thumbnail-list {
        display: flex;
        justify-content: center;
        gap: 12px;
    }

    .thumbnail-list img {
        width: 90px;
        height: 90px;
        border-radius: 8px;
        object-fit: cover;
        cursor: pointer;
        border: 2px solid transparent;
        transition: all 0.2s ease;
    }

    .thumbnail-list img:hover {
        border-color: #007bff;
        transform: scale(1.05);
    }
</style>

<div class="text-center">
    <% if (product != null) { %>
        <!-- Ảnh chính -->
        <img id="mainImage" src="<%= product.getImageUrl1()%>" class="main-image">

        <!-- Ảnh nhỏ bên dưới -->
        <div class="thumbnail-list">
            <img src="<%= product.getImageUrl2()%>" onclick="changeMainImage(this.src)">
            <img src="<%= product.getImageUrl3()%>" onclick="changeMainImage(this.src)">
            <img src="<%= product.getImageUrl4()%>" onclick="changeMainImage(this.src)">
        </div>
    <% } else { %>
        <div class="alert alert-warning">Do NOT HAVE PRODUCT DATA!</div>
    <% } %>
</div>

<script>
    function changeMainImage(src) {
        document.getElementById("mainImage").src = src;
    }
</script>
