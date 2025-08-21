<%@page import="model.Customer"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="model.CartItem"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="0">
        <title>View Cart - TShop</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <style>
            body {
                background: linear-gradient(135deg, #b2dfdb 0%, #80cbc4 100%);
                min-height: 100vh;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
            }
            .container {
                margin: 30px auto;
                max-width: 1200px;
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(12px);
                border-radius: 20px;
                padding: 30px;
                box-shadow: 0 20px 40px rgba(0, 105, 92, 0.25);
                border: 2px solid #00695c;
            }
            h2 {
                color: #004d40;
                font-weight: 700;
                margin-bottom: 30px;
                text-align: center;
                position: relative;
            }
            h2::after {
                content: '';
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
                width: 100px;
                height: 4px;
                background: linear-gradient(90deg, #009688, #26a69a);
                border-radius: 2px;
            }
            .cart-table {
                background: white;
                border-radius: 15px;
                overflow: hidden;
                box-shadow: 0 10px 30px rgba(0, 150, 136, 0.2);
                border: 1px solid #009688;
            }
            .cart-table thead {
                background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
                color: white;
            }
            .cart-table thead th {
                border: none;
                padding: 20px 15px;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                font-size: 0.9rem;
            }
            .cart-table tbody tr {
                transition: all 0.3s ease;
                border-bottom: 1px solid #b2dfdb;
            }
            .cart-table tbody tr:hover {
                background: linear-gradient(135deg, rgba(0, 150, 136, 0.05) 0%, rgba(77, 182, 172, 0.05) 100%);
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0, 150, 136, 0.15);
            }
            .cart-table tbody td {
                padding: 20px 15px;
                vertical-align: middle;
                border: none;
            }
            .cart-table img {
                width: 80px;
                height: 80px;
                object-fit: contain;
                border-radius: 10px;
                transition: transform 0.3s ease;
                background: #f5f5f5;
                padding: 5px;
            }
            .cart-table img:hover {
                transform: scale(1.1);
            }
            .product-details {
                display: flex;
                align-items: center;
                gap: 15px;
            }
            .quantity-container {
                display: flex;
                align-items: center;
                gap: 5px;
                justify-content: center;
            }
            .quantity-btn {
                width: 35px;
                height: 35px;
                border-radius: 50%;
                border: 2px solid #009688;
                background: white;
                color: #009688;
                font-size: 1rem;
                font-weight: bold;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 0;
            }
            .quantity-btn:hover {
                background: #009688;
                color: white;
                transform: scale(1.1);
                box-shadow: 0 5px 15px rgba(0, 150, 136, 0.3);
            }
            .quantity-btn:active {
                transform: scale(0.95);
            }
            .quantity-value {
                width: 60px;
                text-align: center;
                border: 2px solid #b2dfdb;
                border-radius: 10px;
                padding: 8px;
                font-weight: 600;
                color: #004d40;
                background: white;
                transition: all 0.3s ease;
            }
            .quantity-value:focus {
                outline: none;
                border-color: #009688;
                box-shadow: 0 0 0 3px rgba(0, 150, 136, 0.2);
            }
            .quantity-value::-webkit-outer-spin-button,
            .quantity-value::-webkit-inner-spin-button {
                -webkit-appearance: none;
                margin: 0;
            }
            .quantity-value {
                -moz-appearance: textfield;
            }
            .cart-total {
                font-size: 1.5rem;
                font-weight: 700;
                color: #004d40;
            }
            .action-buttons a {
                display: inline-block;
                margin-right: 5px;
            }
            .price {
                white-space: nowrap;
                font-weight: 600;
                color: #00897b;
                font-size: 1.1rem;
            }
            .table-header-actions {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
                padding: 15px 20px;
                background: white;
                border-radius: 15px;
                box-shadow: 0 5px 15px rgba(0, 150, 136, 0.2);
                border: 1px solid #009688;
            }
            .delete-selected-icon, .delete-icon {
                color: #e74c3c;
                font-size: 1.3rem;
                padding: 8px;
                border-radius: 50%;
                transition: all 0.3s ease;
                cursor: pointer;
            }
            .delete-selected-icon:hover, .delete-icon:hover {
                color: #c0392b;
                background: rgba(231, 76, 60, 0.1);
                transform: scale(1.1);
            }
            .card {
                background: white;
                border: none;
                border-radius: 20px;
                box-shadow: 0 15px 35px rgba(0, 150, 136, 0.2);
                backdrop-filter: blur(10px);
                border: 1px solid #009688;
            }
            .btn {
                border-radius: 25px;
                padding: 12px 30px;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
                transition: all 0.3s ease;
                border: none;
            }
            .btn-success {
                background: linear-gradient(135deg, #00897b 0%, #26a69a 100%);
                box-shadow: 0 10px 30px rgba(0, 137, 123, 0.3);
            }
            .btn-success:hover {
                transform: translateY(-3px);
                box-shadow: 0 15px 40px rgba(0, 137, 123, 0.4);
            }
            .btn-secondary {
                background: linear-gradient(135deg, #b0bec5 0%, #eceff1 100%);
                box-shadow: 0 10px 30px rgba(176, 190, 197, 0.3);
            }
            .btn-secondary:hover {
                transform: translateY(-3px);
                box-shadow: 0 15px 40px rgba(176, 190, 197, 0.4);
            }
            .alert {
                border: none;
                border-radius: 15px;
                padding: 20px;
                margin-bottom: 25px;
                box-shadow: 0 10px 30px rgba(0, 150, 136, 0.15);
                background: linear-gradient(135deg, rgba(0, 150, 136, 0.1) 0%, rgba(38, 166, 154, 0.1) 100%);
                border-left: 5px solid #0097a7;
                color: #004d40;
            }
            input[type="checkbox"] {
                width: 20px;
                height: 20px;
                accent-color: #009688;
                cursor: pointer;
            }
            .product-link {
                text-decoration: none;
                color: inherit;
                display: block;
                transition: all 0.3s ease;
            }
            .product-name {
                font-weight: 600;
                color: #004d40;
                transition: color 0.3s ease;
            }
            .product-link:hover .product-name {
                color: #009688;
            }
            @media (max-width: 768px) {
                .container {
                    margin: 15px;
                    padding: 20px;
                }
                .cart-table {
                    font-size: 0.9rem;
                }
                .cart-table img {
                    width: 60px;
                    height: 60px;
                }
                .quantity-btn {
                    width: 30px;
                    height: 30px;
                }
                .quantity-value {
                    width: 50px;
                }
                .product-details {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 10px;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />
        <div class="container">
            <h2 class="mb-4">Your Shopping Cart</h2>

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

            <!-- Cart Items Table -->
            <%
                List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
                if (cartItems != null && !cartItems.isEmpty()) {
            %>
            <form id="deleteForm" action="${pageContext.request.contextPath}/CartItem" method="post">
                <input type="hidden" name="action" value="removeMultiple">
                <input type="hidden" name="selectedItems" id="selectedItems">
                <input type="hidden" name="customerId" value="<%= session.getAttribute("cus") != null ? ((Customer) session.getAttribute("cus")).getCustomerID() : 0%>">
                <div class="table-header-actions">
                    <div>
                        <input type="checkbox" id="selectAll" onclick="toggleSelectAll()">
                        <label for="selectAll" class="ms-2 fw-bold">Select All</label>
                    </div>
                    <div>
                        <a href="javascript:void(0);" class="btn btn-danger btn-sm" onclick="confirmClearAll()">
                            <i class="fas fa-trash-alt"></i> Clear All
                        </a>
                    </div>
                </div>
                <table class="table cart-table">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (CartItem item : cartItems) {
                                Product product = item.getProduct();
                                if (product == null) {
                                    System.out.println("Sản phẩm null cho cartItemId: " + item.getCartItemID());
                                    continue;
                                }
                                BigDecimal unitPrice = product.getPrice();
                                BigDecimal discount = BigDecimal.valueOf(product.getDiscount());
                                BigDecimal discountFactor = BigDecimal.ONE.subtract(discount.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                                BigDecimal discountedPrice = unitPrice != null ? unitPrice.multiply(discountFactor) : BigDecimal.ZERO;
                                BigDecimal itemTotal = discountedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                                if (itemTotal == null || discountedPrice == null) {
                                    System.out.println("Tính giá không hợp lệ cho cartItemId: " + item.getCartItemID() + ", discountedPrice: " + discountedPrice + ", quantity: " + item.getQuantity());
                                    itemTotal = BigDecimal.ZERO;
                                }
                        %>
                        <tr data-unit-price="<%= discountedPrice.setScale(0, BigDecimal.ROUND_HALF_UP).toString()%>" 
                            data-cart-item-id="<%= item.getCartItemID()%>" 
                            data-item-total="<%= itemTotal.setScale(0, BigDecimal.ROUND_HALF_UP).toString()%>"
                            data-max-quantity="<%= product.getQuantity()%>">    
                            <td><input type="checkbox" class="selectItem" data-item-total="<%= itemTotal.setScale(0, BigDecimal.ROUND_HALF_UP).toString()%>" onclick="updateCartTotal(); saveSelectedItems();"></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ProductDetail?productId=<%= product.getProductID()%>&categoryId=<%= product.getCategoryID()%>" class="product-link">
                                    <div class="product-details">
                                        <img src="<%= product.getImageUrl1() != null ? product.getImageUrl1() : "https://via.placeholder.com/80"%>" alt="<%= product.getProductName()%>">
                                        <div class="product-name"><%= product.getProductName()%></div>
                                    </div>
                                </a>
                            </td>
                            <td class="price">
                                <%= String.format("%,d", discountedPrice.setScale(0, BigDecimal.ROUND_HALF_UP).longValue())%> VND
                                <% if (discount.compareTo(BigDecimal.ZERO) > 0) {%>
                                <small class="text-muted"><del><%= String.format("%,d", unitPrice.setScale(0, BigDecimal.ROUND_HALF_UP).longValue())%> VND</del></small>
                                <% }%>
                            </td>
                            <td>
                                <div class="quantity-container">
                                    <button type="button" class="quantity-btn" onclick="decreaseQuantity(<%= item.getCartItemID()%>)">-</button>
                                    <input type="number" 
                                           value="<%= item.getQuantity()%>" 
                                           class="form-control quantity-value" 
                                           id="quantity-<%= item.getCartItemID()%>" 
                                           min="1" 
                                           max="<%= product.getQuantity()%>"
                                           data-original-value="<%= item.getQuantity()%>"
                                           onchange="onQuantityChange(<%= item.getCartItemID()%>)"
                                           onblur="onQuantityChange(<%= item.getCartItemID()%>)">
                                    <button type="button" class="quantity-btn" onclick="increaseQuantity(<%= item.getCartItemID()%>)">+</button>
                                </div>
                            </td>
                            <td class="price" id="total-<%= item.getCartItemID()%>"><%= String.format("%,d", itemTotal.setScale(0, BigDecimal.ROUND_HALF_UP).longValue())%> VND</td>
                            <td class="action-buttons">
                                <a href="javascript:void(0);" class="delete-icon" onclick="confirmDeleteCart(<%= item.getCartItemID()%>)"><i class="fas fa-trash"></i></a>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                <!-- Cart Summary -->
                <div class="card p-4 mb-4">
                    <div class="d-flex justify-content-between cart-total">
                        <span>Total:</span>
                        <span id="cartTotal">0 VND</span>
                    </div>
                    <div class="text-end mt-4">
                        <form id="checkoutForm" action="${pageContext.request.contextPath}/CheckoutServlet" method="get">
                            <input type="hidden" name="selectedCartItemIds" id="selectedCartItemIds">
                            <button type="submit" class="btn btn-success me-3" onclick="prepareCheckout()">Proceed to Checkout</button>
                            <a href="${pageContext.request.contextPath}/Home" class="btn btn-secondary">Continue Shopping</a>
                        </form>
                    </div>
                </div>
            </form>
            <%
            } else {
            %>
            <div class="alert alert-info text-center">
                <h4>Your cart is empty</h4>
                <p class="mb-3">Looks like you haven't added any items to your cart yet.</p>
                <a href="${pageContext.request.contextPath}/Home" class="btn btn-primary">Start Shopping!</a>
            </div>
            <%
                }
            %>
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script>
                                const CUSTOMER_ID = '<%= session.getAttribute("cus") != null ? ((Customer) session.getAttribute("cus")).getCustomerID() : 0%>';

// Confirm delete single cart item
                                function confirmDeleteCart(cartItemId) {
                                    Swal.fire({
                                        title: 'Xác nhận xóa',
                                        text: "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?",
                                        icon: 'warning',
                                        showCancelButton: true,
                                        confirmButtonColor: '#d33',
                                        cancelButtonColor: '#3085d6',
                                        confirmButtonText: 'Xóa',
                                        cancelButtonText: 'Hủy'
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            const form = document.createElement('form');
                                            form.method = 'POST';
                                            form.action = '${pageContext.request.contextPath}/CartItem';

                                            const actionInput = document.createElement('input');
                                            actionInput.type = 'hidden';
                                            actionInput.name = 'action';
                                            actionInput.value = 'removeItem';

                                            const itemIdInput = document.createElement('input');
                                            itemIdInput.type = 'hidden';
                                            itemIdInput.name = 'cartItemId';
                                            itemIdInput.value = cartItemId;

                                            form.appendChild(actionInput);
                                            form.appendChild(itemIdInput);
                                            document.body.appendChild(form);
                                            form.submit();
                                        }
                                    });
                                }

// Confirm delete multiple items
                                function confirmDeleteMultiple() {
                                    const selected = Array.from(document.querySelectorAll('.selectItem:checked'))
                                            .map(item => item.closest('tr').getAttribute('data-cart-item-id'));

                                    if (selected.length === 0) {
                                        Swal.fire({
                                            icon: 'warning',
                                            title: 'Không có sản phẩm nào được chọn',
                                            text: 'Vui lòng chọn ít nhất một sản phẩm để xóa.',
                                            showConfirmButton: true
                                        });
                                        return;
                                    }

                                    Swal.fire({
                                        title: 'Xác nhận xóa',
                                        text: `Bạn có chắc chắn muốn xóa ${selected.length} sản phẩm?`,
                                        icon: 'warning',
                                        showCancelButton: true,
                                        confirmButtonColor: '#d33',
                                        cancelButtonColor: '#3085d6',
                                        confirmButtonText: 'Xóa',
                                        cancelButtonText: 'Hủy'
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            document.getElementById('selectedItems').value = selected.join(',');
                                            document.getElementById('deleteForm').submit();
                                        }
                                    });
                                }

// Clear all cart items
                                function confirmClearAll() {
                                    Swal.fire({
                                        title: 'Xác nhận xóa tất cả',
                                        text: "Bạn có chắc chắn muốn xóa tất cả sản phẩm khỏi giỏ hàng?",
                                        icon: 'warning',
                                        showCancelButton: true,
                                        confirmButtonColor: '#d33',
                                        cancelButtonColor: '#3085d6',
                                        confirmButtonText: 'Xóa tất cả',
                                        cancelButtonText: 'Hủy'
                                    }).then((result) => {
                                        if (result.isConfirmed) {
                                            const form = document.createElement('form');
                                            form.method = 'POST';
                                            form.action = '${pageContext.request.contextPath}/CartItem';

                                            const actionInput = document.createElement('input');
                                            actionInput.type = 'hidden';
                                            actionInput.name = 'action';
                                            actionInput.value = 'clearAll';

                                            form.appendChild(actionInput);
                                            document.body.appendChild(form);
                                            form.submit();
                                        }
                                    });
                                }

// Format number with commas
                                function formatNumber(number) {
                                    if (isNaN(number) || number === null || number === undefined) {
                                        return "0";
                                    }
                                    return Math.round(number).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                                }

// Update cart total
                                function updateCartTotal() {
                                    let total = 0;
                                    document.querySelectorAll('.selectItem:checked').forEach(item => {
                                        const itemTotal = parseInt(item.getAttribute('data-item-total') || 0);
                                        total += itemTotal;
                                    });
                                    document.getElementById('cartTotal').textContent = formatNumber(total) + ' VND';
                                }

// Save selected items to session
                                function saveSelectedItems() {
                                    const selected = Array.from(document.querySelectorAll('.selectItem:checked'))
                                            .map(item => item.closest('tr').getAttribute('data-cart-item-id'));

                                    $.ajax({
                                        url: '${pageContext.request.contextPath}/CartItem',
                                        type: 'POST',
                                        data: {
                                            action: 'saveSelectedItems',
                                            selectedCartItemIds: selected.join(',')
                                        },
                                        success: function (response) {
                                            console.log('Selected items saved to session');
                                        },
                                        error: function (xhr, status, error) {
                                            console.error('Error saving selected items:', error);
                                        }
                                    });
                                }

// Toggle select all checkbox
                                function toggleSelectAll() {
                                    const selectAll = document.getElementById('selectAll');
                                    document.querySelectorAll('.selectItem').forEach(item => {
                                        item.checked = selectAll.checked;
                                    });
                                    updateCartTotal();
                                    saveSelectedItems();
                                }

// Increase quantity
                                function increaseQuantity(cartItemId) {
                                    const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                    if (!quantityInput) {
                                        console.error('Quantity input not found for cartItemId:', cartItemId);
                                        return;
                                    }

                                    const originalQuantity = parseInt(quantityInput.value) || 1;
                                    const row = document.querySelector(`tr[data-cart-item-id="${cartItemId}"]`);
                                    const maxQuantity = parseInt(row.getAttribute('data-max-quantity')) || 1000;
                                    const newQuantity = originalQuantity + 1;

                                    if (newQuantity <= maxQuantity) {
                                        // Update UI immediately
                                        quantityInput.value = newQuantity;
                                        updateItemTotal(cartItemId);

                                        // Send AJAX request to update database
                                        updateQuantityAjax(cartItemId, newQuantity, originalQuantity);
                                    } else {
                                        Swal.fire({
                                            icon: 'warning',
                                            title: 'Vượt quá số lượng',
                                            text: `Số lượng tối đa cho sản phẩm này là ${maxQuantity}`,
                                            showConfirmButton: true
                                        });
                                    }
                                }

// Decrease quantity
                                function decreaseQuantity(cartItemId) {
                                    const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                    if (!quantityInput) {
                                        console.error('Quantity input not found for cartItemId:', cartItemId);
                                        return;
                                    }

                                    const originalQuantity = parseInt(quantityInput.value) || 1;
                                    const newQuantity = originalQuantity - 1;

                                    if (newQuantity >= 1) {
                                        // Update UI immediately
                                        quantityInput.value = newQuantity;
                                        updateItemTotal(cartItemId);

                                        // Send AJAX request to update database
                                        updateQuantityAjax(cartItemId, newQuantity, originalQuantity);
                                    }
                                }

                                function updateQuantityAjax(cartItemId, newQuantity, originalQuantity) {
                                    $.ajax({
                                        url: '${pageContext.request.contextPath}/CartItem',
                                        type: 'POST',
                                        data: {
                                            action: 'updateQuantity',
                                            cartItemId: cartItemId,
                                            quantity: newQuantity
                                        },
                                        success: function (response) {
                                            console.log("AJAX response:", response);

                                            if (response.trim() === "success") {
                                                console.log('Quantity updated successfully in database');
                                                // Không cần cập nhật UI ở đây vì đã cập nhật trước đó
                                            } else if (response.startsWith("error:insufficient_stock:")) {
                                                const maxStock = response.split(":")[2];
                                                // Rollback quantity input
                                                const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                                quantityInput.value = originalQuantity;
                                                updateItemTotal(cartItemId);

                                                Swal.fire({
                                                    icon: 'error',
                                                    title: 'Không đủ hàng',
                                                    text: `Sản phẩm này chỉ còn ${maxStock} trong kho`,
                                                    showConfirmButton: true
                                                });
                                            } else if (response.includes("error:")) {
                                                // Rollback quantity input
                                                const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                                quantityInput.value = originalQuantity;
                                                updateItemTotal(cartItemId);

                                                Swal.fire({
                                                    icon: 'error',
                                                    title: 'Lỗi',
                                                    text: 'Có lỗi xảy ra khi cập nhật số lượng',
                                                    showConfirmButton: true
                                                });
                                            }
                                        },
                                        error: function (xhr, status, error) {
                                            console.error('AJAX error:', error);
                                            // Rollback quantity input
                                            const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                            quantityInput.value = originalQuantity;
                                            updateItemTotal(cartItemId);

                                            Swal.fire({
                                                icon: 'error',
                                                title: 'Lỗi kết nối',
                                                text: 'Không thể cập nhật số lượng, vui lòng thử lại',
                                                showConfirmButton: true
                                            });
                                        }
                                    });
                                }

// Update quantity via AJAX
                                function updateQuantity(cartItemId, newQuantity) {
                                    updateQuantityAjax(cartItemId, newQuantity);
                                }

// Handle manual quantity input change
                                function onQuantityChange(cartItemId) {
                                    const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                    const originalQuantity = parseInt(quantityInput.getAttribute('data-original-value')) || parseInt(quantityInput.defaultValue) || 1;
                                    let newQuantity = parseInt(quantityInput.value) || 1;

                                    const row = document.querySelector(`tr[data-cart-item-id="${cartItemId}"]`);
                                    const maxQuantity = parseInt(row.getAttribute('data-max-quantity')) || 1000;

                                    // Validate quantity
                                    if (newQuantity < 1) {
                                        newQuantity = 1;
                                        quantityInput.value = newQuantity;
                                    }

                                    if (newQuantity > maxQuantity) {
                                        Swal.fire({
                                            icon: 'warning',
                                            title: 'Vượt quá số lượng',
                                            text: `Số lượng tối đa cho sản phẩm này là ${maxQuantity}`,
                                            showConfirmButton: true
                                        });
                                        quantityInput.value = Math.min(originalQuantity, maxQuantity);
                                        newQuantity = parseInt(quantityInput.value);
                                    }

                                    // Only update if quantity actually changed
                                    if (newQuantity !== originalQuantity) {
                                        // Update UI immediately
                                        updateItemTotal(cartItemId);

                                        // Send AJAX request
                                        updateQuantityAjax(cartItemId, newQuantity, originalQuantity);

                                        // Update the original value for next comparison
                                        quantityInput.setAttribute('data-original-value', newQuantity.toString());
                                    }
                                }

// Update item total when quantity changes
                                function updateItemTotal(cartItemId) {
                                    const row = document.querySelector(`tr[data-cart-item-id="${cartItemId}"]`);
                                    if (!row) {
                                        console.error("Row not found for cartItemId:", cartItemId);
                                        return;
                                    }

                                    const unitPrice = parseFloat(row.getAttribute('data-unit-price')) || 0;
                                    const quantityInput = document.getElementById(`quantity-${cartItemId}`);
                                    const quantity = parseInt(quantityInput.value) || 1;
                                    const newTotal = unitPrice * quantity;

                                    const totalCell = document.getElementById(`total-${cartItemId}`);
                                    if (totalCell) {
                                        totalCell.textContent = formatNumber(newTotal) + ' VND';
                                    }

                                    // Update row data attributes
                                    row.setAttribute('data-item-total', Math.round(newTotal).toString());

                                    const checkbox = row.querySelector('.selectItem');
                                    if (checkbox) {
                                        checkbox.setAttribute('data-item-total', Math.round(newTotal).toString());
                                    }

                                    // Update cart total if item is selected
                                    if (checkbox && checkbox.checked) {
                                        updateCartTotal();
                                    }
                                }

// Prepare checkout
                                function prepareCheckout() {
                                    const selected = Array.from(document.querySelectorAll('.selectItem:checked'))
                                            .map(item => item.closest('tr').getAttribute('data-cart-item-id'));

                                    if (selected.length === 0) {
                                        Swal.fire({
                                            icon: 'warning',
                                            title: 'Không có sản phẩm nào được chọn',
                                            text: 'Vui lòng chọn ít nhất một sản phẩm để thanh toán.',
                                            showConfirmButton: true
                                        });
                                        return false;
                                    }

                                    document.getElementById('selectedCartItemIds').value = selected.join(',');
                                    return true;
                                }

// Initialize on page load
                                document.addEventListener('DOMContentLoaded', function () {
                                    // Store original quantities for rollback
                                    document.querySelectorAll('.quantity-value').forEach(input => {
                                        input.setAttribute('data-original-value', input.value);
                                    });

                                    // Update cart total on page load
                                    updateCartTotal();

                                    // Add event listeners for quantity inputs
                                    document.querySelectorAll('.quantity-value').forEach(input => {
                                        input.addEventListener('change', function () {
                                            const cartItemId = this.id.replace('quantity-', '');
                                            onQuantityChange(cartItemId);
                                        });

                                        // Prevent negative values and non-numeric input
                                        input.addEventListener('keypress', function (e) {
                                            // Allow: backspace, delete, tab, escape, enter
                                            if ([46, 8, 9, 27, 13].indexOf(e.keyCode) !== -1 ||
                                                    // Allow: Ctrl+A, Ctrl+C, Ctrl+V, Ctrl+X
                                                            (e.keyCode === 65 && e.ctrlKey === true) ||
                                                            (e.keyCode === 67 && e.ctrlKey === true) ||
                                                            (e.keyCode === 86 && e.ctrlKey === true) ||
                                                            (e.keyCode === 88 && e.ctrlKey === true)) {
                                                return;
                                            }
                                            // Ensure that it is a number and stop the keypress
                                            if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                                                e.preventDefault();
                                            }
                                        });
                                    });

                                    // Add event listeners for checkboxes
                                    document.querySelectorAll('.selectItem').forEach(checkbox => {
                                        checkbox.addEventListener('change', function () {
                                            updateCartTotal();
                                            saveSelectedItems();
                                        });
                                    });
                                });
            </script>
        </div>
        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
    </body>
</html>