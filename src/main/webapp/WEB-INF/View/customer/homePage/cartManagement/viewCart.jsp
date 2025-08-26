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
                        <input type="checkbox" id="selectAll">
                        <label for="selectAll" class="ms-2 fw-bold">Select All</label>
                    </div>
                    <div>
                        <a href="javascript:void(0);" class="btn btn-danger btn-sm">
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
                                BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

                                if (itemTotal == null || unitPrice == null) {
                                    System.out.println("Tính giá không hợp lệ cho cartItemId: " + item.getCartItemID());
                                    itemTotal = BigDecimal.ZERO;
                                    unitPrice = BigDecimal.ZERO;
                                }
                        %>
                        <tr data-unit-price="<%= unitPrice.setScale(0, BigDecimal.ROUND_HALF_UP).toString()%>" 
                            data-cart-item-id="<%= item.getCartItemID()%>" 
                            data-item-total="<%= itemTotal.setScale(0, BigDecimal.ROUND_HALF_UP).toString()%>"
                            data-max-quantity="<%= product.getQuantity()%>">    
                            <td><input type="checkbox" class="selectItem" data-item-total="<%= itemTotal.setScale(0, BigDecimal.ROUND_HALF_UP).toString()%>"></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ProductDetail?productId=<%= product.getProductID()%>&categoryId=<%= product.getCategoryID()%>" class="product-link">
                                    <div class="product-details">
                                        <img src="<%= product.getImageUrl1() != null ? product.getImageUrl1() : "https://via.placeholder.com/80"%>" alt="<%= product.getProductName()%>" style="width: 80px; height: 80px; object-fit: cover;">
                                        <div class="product-name"><%= product.getProductName()%></div>
                                    </div>
                                </a>
                            </td>
                            <td class="price">
                                <%= String.format("%,d", unitPrice.setScale(0, BigDecimal.ROUND_HALF_UP).longValue())%> VND
                            </td>
                            <td>
                                <div class="quantity-container" style="display: flex; align-items: center; gap: 5px;">
                                    <button type="button" class="quantity-btn btn btn-outline-secondary btn-sm" style="width: 30px; height: 30px;">-</button>
                                    <input type="number" 
                                           value="<%= item.getQuantity()%>" 
                                           class="form-control quantity-value text-center" 
                                           id="quantity_<%= item.getCartItemID()%>" 
                                           min="1" 
                                           max="<%= product.getQuantity()%>"
                                           data-original-value="<%= item.getQuantity()%>"
                                           data-cart-item-id="<%= item.getCartItemID()%>"
                                           style="width: 70px;">
                                    <button type="button" class="quantity-btn btn btn-outline-secondary btn-sm" style="width: 30px; height: 30px;">+</button>
                                </div>
                                <small class="text-muted">Max: <%= product.getQuantity()%></small>
                            </td>
                            <td class="price" id="total_<%= item.getCartItemID()%>"><%= String.format("%,d", itemTotal.setScale(0, BigDecimal.ROUND_HALF_UP).longValue())%> VND</td>
                            <td class="action-buttons">
                                <a href="javascript:void(0);" class="delete-icon btn btn-outline-danger btn-sm"><i class="fas fa-trash"></i></a>
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
                        <span><strong>Selected Total:</strong></span>
                        <span id="cartTotal" style="font-weight: bold; font-size: 1.2em;">0 VND</span>
                    </div>
                    <div class="text-end mt-4">
                        <!-- Đơn giản hóa form checkout -->
                        <button type="button" id="checkoutBtn" class="btn btn-success me-3">Create Order</button>
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
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
            <script>
                $(document).ready(function () {
                    // Utility function to format number
                    function formatPrice(price) {
                        return parseInt(price).toLocaleString() + ' VND';
                    }

                    // Update total for a specific item
                    function updateItemTotal(row) {
                        const unitPrice = parseInt(row.data('unit-price'));
                        const quantity = parseInt(row.find('.quantity-value').val());
                        const total = unitPrice * quantity;

                        row.find('td:nth-child(5)').text(formatPrice(total));
                        row.data('item-total', total);
                        row.find('.selectItem').data('item-total', total);

                        updateSelectedTotal();
                    }

                    // Update selected total
                    function updateSelectedTotal() {
                        let total = 0;
                        $('.selectItem:checked').each(function () {
                            const itemTotal = parseInt($(this).data('item-total'));
                            total += itemTotal;
                        });
                        $('#cartTotal').text(formatPrice(total));
                    }

                    // Handle quantity change via AJAX
                    function updateQuantityServer(cartItemId, newQuantity) {
                        $.ajax({
                            url: 'CartItem',
                            type: 'POST',
                            data: {
                                action: 'updateQuantity',
                                cartItemId: cartItemId,
                                quantity: newQuantity
                            },
                            success: function (response) {
                                if (response.startsWith('error:')) {
                                    const errorType = response.split(':')[1];
                                    let message = 'Có lỗi xảy ra!';

                                    switch (errorType) {
                                        case 'invalid_quantity':
                                            message = 'Số lượng không hợp lệ!';
                                            break;
                                        case 'item_not_found':
                                            message = 'Không tìm thấy sản phẩm!';
                                            break;
                                        case 'product_not_available':
                                            message = 'Sản phẩm không còn khả dụng!';
                                            break;
                                        case 'insufficient_stock':
                                            const availableStock = response.split(':')[2];
                                            message = 'Chỉ còn ' + availableStock + ' sản phẩm trong kho!';
                                            break;
                                        case 'update_failed':
                                            message = 'Cập nhật thất bại!';
                                            break;
                                        default:
                                            message = 'Có lỗi xảy ra!';
                                    }

                                    alert(message);
                                    // Restore original value
                                    const input = $('#quantity_' + cartItemId);
                                    input.val(input.data('original-value'));
                                    updateItemTotal(input.closest('tr'));
                                } else if (response === 'success') {
                                    // Update original value
                                    const input = $('#quantity_' + cartItemId);
                                    input.data('original-value', newQuantity);
                                }
                            },
                            error: function () {
                                alert('Có lỗi kết nối xảy ra!');
                                // Restore original value
                                const input = $('#quantity_' + cartItemId);
                                input.val(input.data('original-value'));
                                updateItemTotal(input.closest('tr'));
                            }
                        });
                    }

                    // Handle + button click
                    $(document).on('click', '.quantity-btn:contains("+")', function () {
                        const row = $(this).closest('tr');
                        const input = row.find('.quantity-value');
                        const currentValue = parseInt(input.val());
                        const maxQuantity = parseInt(row.data('max-quantity'));
                        const cartItemId = parseInt(input.data('cart-item-id'));

                        if (currentValue < maxQuantity) {
                            const newValue = currentValue + 1;
                            input.val(newValue);
                            updateItemTotal(row);
                            updateQuantityServer(cartItemId, newValue);
                        } else {
                            alert('Quantity cannot exceed ' + maxQuantity);
                        }
                    });

                    // Handle - button click
                    $(document).on('click', '.quantity-btn:contains("-")', function () {
                        const row = $(this).closest('tr');
                        const input = row.find('.quantity-value');
                        const currentValue = parseInt(input.val());
                        const cartItemId = parseInt(input.data('cart-item-id'));

                        if (currentValue > 1) {
                            const newValue = currentValue - 1;
                            input.val(newValue);
                            updateItemTotal(row);
                            updateQuantityServer(cartItemId, newValue);
                        } else {
                            alert('Quantity cannot be less than 1');
                        }
                    });

                    // Handle direct input change
                    $(document).on('change', '.quantity-value', function () {
                        const row = $(this).closest('tr');
                        const input = $(this);
                        const newValue = parseInt(input.val());
                        const maxQuantity = parseInt(row.data('max-quantity'));
                        const cartItemId = parseInt(input.data('cart-item-id'));

                        if (isNaN(newValue) || newValue < 1) {
                            alert('Quantity must be a positive integer!');
                            input.val(input.data('original-value'));
                            return;
                        }

                        if (newValue > maxQuantity) {
                            alert('Quantity cannot exceed ' + maxQuantity);
                            input.val(input.data('original-value'));
                            return;
                        }

                        updateItemTotal(row);
                        updateQuantityServer(cartItemId, newValue);
                    });

                    // Handle individual item delete
                    $(document).on('click', '.delete-icon', function (e) {
                        e.preventDefault();

                        if (confirm('Are you sure you want to remove this product from your cart?')) {
                            const row = $(this).closest('tr');
                            const cartItemId = row.data('cart-item-id');

                            // Create a form and submit
                            const form = $('<form>', {
                                method: 'POST',
                                action: 'CartItem'
                            });

                            form.append($('<input>', {
                                type: 'hidden',
                                name: 'action',
                                value: 'removeItem'
                            }));

                            form.append($('<input>', {
                                type: 'hidden',
                                name: 'cartItemId',
                                value: cartItemId
                            }));

                            $('body').append(form);
                            form.submit();
                        }
                    });

                    // Handle Clear All
                    $(document).on('click', 'a:contains("Clear All")', function (e) {
                        e.preventDefault();

                        if (confirm('Are you sure you want to remove all items from your cart?')) {
                            // Create a form and submit
                            const form = $('<form>', {
                                method: 'POST',
                                action: 'CartItem'
                            });

                            form.append($('<input>', {
                                type: 'hidden',
                                name: 'action',
                                value: 'clearAll'
                            }));

                            $('body').append(form);
                            form.submit();
                        }
                    });

                    // Handle Select All checkbox
                    $('#selectAll').on('change', function () {
                        const isChecked = $(this).is(':checked');
                        $('.selectItem').prop('checked', isChecked);
                        updateSelectedTotal();
                    });

                    // Handle individual item selection
                    $(document).on('change', '.selectItem', function () {
                        updateSelectedTotal();

                        // Update Select All checkbox state
                        const totalItems = $('.selectItem').length;
                        const checkedItems = $('.selectItem:checked').length;

                        if (checkedItems === 0) {
                            $('#selectAll').prop('indeterminate', false).prop('checked', false);
                        } else if (checkedItems === totalItems) {
                            $('#selectAll').prop('indeterminate', false).prop('checked', true);
                        } else {
                            $('#selectAll').prop('indeterminate', true);
                        }
                    });

                    // Handle checkout form submission
                    // Handle checkout form submission
                    $('#checkoutBtn').on('click', function () {
                        const selectedItems = [];
                        $('.selectItem:checked').each(function () {
                            const cartItemId = $(this).closest('tr').data('cart-item-id');
                            selectedItems.push(cartItemId);
                        });

                        if (selectedItems.length === 0) {
                            alert('Please select at least one product to checkout!');
                            return false;
                        }

                        // Save selected items to session via AJAX
                        $.ajax({
                            url: 'CartItem',
                            type: 'POST',
                            data: {
                                action: 'saveSelectedItems',
                                selectedCartItemIds: selectedItems.join(',')
                            },
                            success: function (response) {
                                if (response === 'success') {
                                    // Chuyển hướng trực tiếp đến CreateOrderServlet
                                    window.location.href = 'CreateOrderServlet';
                                } else {
                                    alert('Có lỗi xảy ra khi lưu thông tin!');
                                }
                            },
                            error: function () {
                                alert('Có lỗi kết nối xảy ra!');
                            }
                        });
                    });

                    // Initialize selected total on page load
                    updateSelectedTotal();
                });
            </script>
        </div>
        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
    </body>

</html>