<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.*" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Create Order</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Inter', sans-serif;
                background: linear-gradient(45deg, #ADD8E6, #5F9EA0);
                min-height: 100vh;
                color: #333;
            }

            .main-container {
                max-width: 1400px;
                margin: 0 auto;
                padding: 40px 20px;
                min-height: calc(100vh - 160px); /* Adjust based on header/footer height */
            }

            .page-header {
                text-align: center;
                margin-bottom: 40px;
                color: #1A1A1A;
            }

            .page-header h1 {
                font-size: 2.5rem;
                font-weight: 700;
                margin-bottom: 10px;
            }

            .page-header p {
                font-size: 1.1rem;
                opacity: 0.9;
            }

            /* Notification Styles */
            .notification {
                background: linear-gradient(45deg, #56ab2f, #a8e6cf);
                color: white;
                padding: 16px 24px;
                border-radius: 12px;
                margin-bottom: 30px;
                box-shadow: 0 4px 15px rgba(86, 171, 47, 0.3);
                border: none;
                position: relative;
                overflow: hidden;
            }

            .notification::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
                transition: left 0.5s;
            }

            .notification:hover::before {
                left: 100%;
            }

            /* Main Content Layout */
            .content-wrapper {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 40px;
                margin-top: 20px;
            }

            .section-card {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                padding: 30px;
                box-shadow: 0 20px 40px rgba(0,0,0,0.1);
                border: 1px solid rgba(255,255,255,0.2);
                transition: all 0.3s ease;
            }

            .section-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 30px 60px rgba(0,0,0,0.15);
            }

            .section-title {
                font-size: 1.5rem;
                font-weight: 600;
                margin-bottom: 20px;
                color: #2d3748;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .section-title i {
                color: #667eea;
                font-size: 1.2rem;
            }

            /* Product List Styles */
            .products-container {
                max-height: 400px;
                overflow-y: auto;
                padding-right: 10px;
            }

            .products-container::-webkit-scrollbar {
                width: 6px;
            }

            .products-container::-webkit-scrollbar-track {
                background: #f1f1f1;
                border-radius: 10px;
            }

            .products-container::-webkit-scrollbar-thumb {
                background: linear-gradient(45deg, #667eea, #764ba2);
                border-radius: 10px;
            }

            .product-item {
                display: flex;
                align-items: center;
                padding: 20px;
                border-radius: 15px;
                background: linear-gradient(45deg, #f8f9ff, #fff);
                margin-bottom: 15px;
                transition: all 0.3s ease;
                border: 1px solid #e2e8f0;
            }

            .product-item:hover {
                transform: translateX(5px);
                box-shadow: 0 5px 20px rgba(102, 126, 234, 0.2);
                border-color: #667eea;
            }

            .product-image {
                width: 80px;
                height: 80px;
                border-radius: 12px;
                object-fit: cover;
                margin-right: 20px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            }

            .product-details {
                flex: 1;
            }

            .product-name {
                font-weight: 600;
                font-size: 1.1rem;
                margin-bottom: 8px;
                color: #2d3748;
            }

            .product-price {
                color: #667eea;
                font-weight: 500;
                margin-bottom: 5px;
            }

            .product-total {
                font-weight: 600;
                color: #48bb78;
                font-size: 1.1rem;
            }

            /* Address Section */
            .address-select {
                width: 100%;
                padding: 15px;
                border: 2px solid #e2e8f0;
                border-radius: 12px;
                font-size: 1rem;
                background: white;
                transition: all 0.3s ease;
                margin-bottom: 15px;
            }

            .address-select:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .no-address {
                color: #e53e3e;
                background: #fed7d7;
                padding: 15px;
                border-radius: 12px;
                text-align: center;
                font-weight: 500;
            }

            /* Voucher Section */
            .voucher-input-group {
                display: flex;
                gap: 10px;
                margin-bottom: 15px;
            }

            .voucher-input {
                flex: 1;
                padding: 15px;
                border: 2px solid #e2e8f0;
                border-radius: 12px;
                font-size: 1rem;
                transition: all 0.3s ease;
            }

            .voucher-input:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }

            .btn {
                padding: 15px 25px;
                border: none;
                border-radius: 12px;
                font-weight: 600;
                font-size: 0.95rem;
                cursor: pointer;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                gap: 8px;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .btn-primary {
                background: linear-gradient(45deg, #667eea, #764ba2);
                color: white;
            }

            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(102, 126, 234, 0.4);
            }

            .btn-secondary {
                background: linear-gradient(45deg, #6c757d, #5a6268);
                color: white;
            }

            .btn-secondary:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(108, 117, 125, 0.4);
            }

            .btn-success {
                background: linear-gradient(45deg, #28a745, #20c997);
                color: white;
            }

            .btn-success:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(40, 167, 69, 0.4);
            }

            /* Order Summary */
            .summary-row {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 15px;
                padding: 10px 0;
                border-bottom: 1px solid #e2e8f0;
            }

            .summary-row:last-of-type {
                border-bottom: none;
                margin-bottom: 0;
            }

            .summary-label {
                font-weight: 500;
                color: #4a5568;
            }

            .summary-value {
                font-weight: 600;
                color: #2d3748;
            }

            .discount-value {
                color: #48bb78 !important;
            }

            .total-row {
                background: linear-gradient(45deg, #f7fafc, #edf2f7);
                padding: 20px;
                border-radius: 12px;
                margin: 20px 0;
            }

            .total-row .summary-label,
            .total-row .summary-value {
                font-size: 1.3rem;
                font-weight: 700;
                color: #2d3748;
            }

            /* Messages */
            .message {
                padding: 15px 20px;
                border-radius: 12px;
                margin-top: 15px;
                font-weight: 500;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .message-success {
                background: linear-gradient(45deg, #c6f6d5, #9ae6b4);
                color: #22543d;
                border: 1px solid #9ae6b4;
            }

            .message-error {
                background: linear-gradient(45deg, #fed7d7, #fbb6ce);
                color: #742a2a;
                border: 1px solid #fbb6ce;
            }

            /* Create Order Button */
            .create-order-btn {
                width: 100%;
                padding: 20px;
                margin-top: 30px;
                background: linear-gradient(45deg, #48bb78, #38a169);
                color: white;
                border: none;
                border-radius: 15px;
                font-size: 1.2rem;
                font-weight: 700;
                cursor: pointer;
                transition: all 0.3s ease;
                text-transform: uppercase;
                letter-spacing: 1px;
                position: relative;
                overflow: hidden;
            }

            .create-order-btn::before {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
                transition: left 0.5s;
            }

            .create-order-btn:hover::before {
                left: 100%;
            }

            .create-order-btn:hover {
                transform: translateY(-3px);
                box-shadow: 0 15px 35px rgba(72, 187, 120, 0.4);
            }

            /* Responsive Design */
            @media (max-width: 1024px) {
                .content-wrapper {
                    grid-template-columns: 1fr;
                    gap: 30px;
                }

                .main-container {
                    padding: 20px 15px;
                }

                .page-header h1 {
                    font-size: 2rem;
                }
            }

            @media (max-width: 768px) {
                .voucher-input-group {
                    flex-direction: column;
                }

                .product-item {
                    flex-direction: column;
                    text-align: center;
                }

                .product-image {
                    margin-right: 0;
                    margin-bottom: 15px;
                }

                .section-card {
                    padding: 20px;
                }

                .page-header h1 {
                    font-size: 1.8rem;
                }
            }

            /* Loading States */
            .btn:disabled {
                opacity: 0.6;
                cursor: not-allowed;
                transform: none !important;
            }

            .loading {
                position: relative;
                overflow: hidden;
            }

            .loading::after {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent);
                animation: loading 1.5s infinite;
            }

            @keyframes loading {
                0% { left: -100%; }
                100% { left: 100%; }
            }

            /* Hidden Elements */
            .hidden {
                display: none !important;
            }

            /* Fade In Animation */
            .fade-in {
                animation: fadeIn 0.5s ease-in;
            }

            @keyframes fadeIn {
                from { opacity: 0; transform: translateY(20px); }
                to { opacity: 1; transform: translateY(0); }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />

        <div class="main-container">
            <div class="page-header fade-in">
                <h1><i class="fas fa-shopping-cart"></i> Create Order</h1>
                <p>Review your items and complete your purchase</p>
            </div>

            <!-- Display notification -->
            <%
                String message = (String) session.getAttribute("message");
                if (message != null) {
            %>
            <div class="notification fade-in">
                <i class="fas fa-check-circle"></i>
                <%= message%>
            </div>
            <%
                    session.removeAttribute("message");
                }
            %>

            <div class="content-wrapper">
                <!-- Left side - Selected Products -->
                <div class="section-card fade-in">
                    <h3 class="section-title">
                        <i class="fas fa-box"></i>
                        Selected Products
                    </h3>
                    <div class="products-container">
                        <%
                            List<CartItem> selectedCartItems = (List<CartItem>) request.getAttribute("selectedCartItems");
                            if (selectedCartItems != null && !selectedCartItems.isEmpty()) {
                                for (CartItem item : selectedCartItems) {
                                    Product product = item.getProduct();
                                    BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                        %>
                        <div class="product-item">
                            <img src="<%= product.getImageUrl1() != null ? product.getImageUrl1() : "https://via.placeholder.com/80"%>" 
                                 alt="<%= product.getProductName()%>" 
                                 class="product-image">
                            <div class="product-details">
                                <div class="product-name"><%= product.getProductName()%></div>
                                <div class="product-price">
                                    <i class="fas fa-tag"></i>
                                    <%= String.format("%,d", product.getPrice().longValue())%> VND
                                    × <%= item.getQuantity()%>
                                </div>
                                <div class="product-total">
                                    <i class="fas fa-calculator"></i>
                                    Total: <%= String.format("%,d", itemTotal.longValue())%> VND
                                </div>
                            </div>
                        </div>
                        <%
                                }
                            }
                        %>
                    </div>
                </div>

                <!-- Right side - Address & Voucher -->
                <div>
                    <!-- Address Selection -->
                    <div class="section-card fade-in">
                        <h3 class="section-title">
                            <i class="fas fa-map-marker-alt"></i>
                            Delivery Address
                        </h3>
                        <%
                            List<Address> addresses = (List<Address>) request.getAttribute("addresses");
                            if (addresses != null && !addresses.isEmpty()) {
                        %>
                        <select id="addressSelect" class="address-select">
                            <%
                                for (Address address : addresses) {
                            %>
                            <option value="<%= address.getAddressId()%>" <%= address.isDefault() ? "selected" : ""%>>
                                <%= address.getProvinceName()%>, <%= address.getDistrictName()%>, 
                                <%= address.getWardName()%>, <%= address.getAddressDetails()%>
                                <%= address.isDefault() ? " (Default)" : ""%>
                            </option>
                            <%
                                }
                            %>
                        </select>
                        <%
                        } else {
                        %>
                        <div class="no-address">
                            <i class="fas fa-exclamation-triangle"></i>
                            No delivery address found. Please add an address first.
                        </div>
                        <%
                            }
                        %>
                    </div>

                    <!-- Voucher Section -->
                    <div class="section-card fade-in">
                        <h3 class="section-title">
                            <i class="fas fa-ticket-alt"></i>
                            Apply Voucher
                        </h3>
                        <div class="voucher-input-group">
                            <input type="text" id="voucherCode" placeholder="Enter voucher code" 
                                   class="voucher-input" maxlength="20" pattern="[A-Z0-9_-]{3,20}">
                            <button type="button" id="applyVoucherBtn" class="btn btn-primary">
                                <i class="fas fa-check"></i>
                                Apply
                            </button>
                            <button type="button" id="clearVoucherBtn" class="btn btn-secondary hidden">
                                <i class="fas fa-times"></i>
                                Clear
                            </button>
                        </div>
                        <div id="voucherMessage"></div>
                    </div>

                    <!-- Order Summary -->
                    <div class="section-card fade-in">
                        <h3 class="section-title">
                            <i class="fas fa-receipt"></i>
                            Order Summary
                        </h3>
                        <%
                            Long totalAmount = (Long) request.getAttribute("totalAmount");
                        %>
                        <div class="summary-row">
                            <span class="summary-label">Subtotal:</span>
                            <span class="summary-value" id="subtotal"><%= String.format("%,d", totalAmount)%> VND</span>
                        </div>
                        <div class="summary-row hidden" id="discountRow">
                            <span class="summary-label">Discount:</span>
                            <span class="summary-value discount-value" id="discount">-0 VND</span>
                        </div>
                        
                        <div class="total-row">
                            <div class="summary-row">
                                <span class="summary-label">
                                    <i class="fas fa-money-bill-wave"></i>
                                    Total:
                                </span>
                                <span class="summary-value" id="finalTotal"><%= String.format("%,d", totalAmount)%> VND</span>
                            </div>
                        </div>

                        <button type="button" id="createOrderBtn" class="create-order-btn">
                            <i class="fas fa-credit-card"></i>
                            Place Order
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Hidden form for order creation -->
        <form id="orderForm" action="CreateOrderServlet" method="post" style="display: none;">
            <input type="hidden" name="action" value="createOrder">
            <input type="hidden" name="addressId" id="hiddenAddressId">
            <input type="hidden" name="voucherId" id="hiddenVoucherId">
            <input type="hidden" name="discountPercent" id="hiddenDiscountPercent">
            <input type="hidden" name="finalAmount" id="hiddenFinalAmount" value="<%= totalAmount%>">
            <input type="hidden" name="discountAmount" id="hiddenDiscountAmount" value="0">
        </form>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            let currentVoucherId = null;
            let originalAmount = <%= totalAmount%>;

            $(document).ready(function () {
                // Add fade-in animation to elements
                $('.fade-in').each(function(index) {
                    $(this).delay(index * 100).queue(function() {
                        $(this).addClass('fade-in').dequeue();
                    });
                });

                // Input validation for voucher code
                $('#voucherCode').on('input', function() {
                    let value = $(this).val().toUpperCase();
                    value = value.replace(/[^A-Z0-9_-]/g, '');
                    $(this).val(value);
                });

                // Apply voucher button click
                $('#applyVoucherBtn').click(function () {
                    const voucherCode = $('#voucherCode').val().trim();
                    if (!voucherCode) {
                        showVoucherMessage('Please enter a voucher code', 'error');
                        return;
                    }

                    if (!isValidVoucherFormat(voucherCode)) {
                        showVoucherMessage('Invalid voucher code format. Use only A-Z, 0-9, _, - (3-20 characters)', 'error');
                        return;
                    }

                    applyVoucher(voucherCode);
                });

                // Clear voucher button click
                $('#clearVoucherBtn').click(function() {
                    clearVoucher();
                });

                // Create order button click - Removed confirmation dialog
                $('#createOrderBtn').click(function () {
                    const addressId = $('#addressSelect').val();
                    if (!addressId) {
                        alert('Please select a delivery address');
                        return;
                    }

                    $('#hiddenAddressId').val(addressId);
                    $('#hiddenVoucherId').val(currentVoucherId || '');

                    // Directly submit without confirmation
                    $(this).addClass('loading').prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i> Processing...');
                    $('#orderForm').submit();
                });
            });

            function isValidVoucherFormat(code) {
                return /^[A-Z0-9_-]{3,20}$/.test(code);
            }

            function applyVoucherCode(code) {
                $('#voucherCode').val(code);
                applyVoucher(code);
            }

            function applyVoucher(voucherCode) {
                const $btn = $('#applyVoucherBtn');
                $btn.prop('disabled', true).addClass('loading').html('<i class="fas fa-spinner fa-spin"></i> Applying...');
                
                $.ajax({
                    url: 'CreateOrderServlet',
                    type: 'POST',
                    data: {
                        action: 'validateVoucher',
                        voucherCode: voucherCode,
                        totalAmount: originalAmount
                    },
                    success: function (response) {
                        if (response.startsWith('success:')) {
                            const parts = response.split(':');
                            const discountAmount = parseInt(parts[1]);
                            const finalAmount = parseInt(parts[2]);
                            const voucherId = parts[3];
                            const discountPercent = parts[4];

                            $('#discount').text('-' + formatPrice(discountAmount) + ' VND');
                            $('#finalTotal').text(formatPrice(finalAmount) + ' VND');
                            $('#discountRow').removeClass('hidden').addClass('fade-in');
                            $('#clearVoucherBtn').removeClass('hidden').addClass('fade-in');

                            $('#hiddenFinalAmount').val(finalAmount);
                            $('#hiddenDiscountAmount').val(discountAmount);
                            $('#hiddenDiscountPercent').val(discountPercent);
                            currentVoucherId = voucherId;

                            showVoucherMessage('Voucher applied successfully! You saved ' + formatPrice(discountAmount) + ' VND', 'success');

                        } else if (response.startsWith('error:')) {
                            const errorParts = response.split(':');
                            const errorType = errorParts[1];
                            let message = 'Error applying voucher';

                            switch (errorType) {
                                case 'empty_code':
                                    message = 'Please enter a voucher code';
                                    break;
                                case 'invalid_format':
                                    message = 'Invalid voucher code format. Use only A-Z, 0-9, _, - (3-20 characters)';
                                    break;
                                case 'voucher_not_found':
                                    message = 'Voucher code not found';
                                    break;
                                case 'voucher_already_used':
                                    message = 'You have already used this voucher';
                                    break;
                                case 'voucher_inactive':
                                    message = 'This voucher is no longer active';
                                    break;
                                case 'voucher_expired':
                                    message = 'This voucher has expired';
                                    break;
                                case 'voucher_limit_reached':
                                    message = 'This voucher has reached its usage limit';
                                    break;
                                case 'min_order_not_met':
                                    const minAmount = errorParts.length > 2 ? errorParts[2] : '0';
                                    message = 'Minimum order amount of ' + formatPrice(minAmount) + ' VND not met';
                                    break;
                                case 'voucher_invalid':
                                    message = 'This voucher is not valid for your order';
                                    break;
                                case 'invalid_amount':
                                    message = 'Invalid order amount';
                                    break;
                                case 'not_logged_in':
                                    message = 'Please log in to apply voucher';
                                    break;
                                case 'system_error':
                                    message = 'System error occurred. Please try again.';
                                    break;
                            }

                            showVoucherMessage(message, 'error');
                        }
                    },
                    error: function () {
                        showVoucherMessage('Connection error. Please try again.', 'error');
                    },
                    complete: function() {
                        $btn.prop('disabled', false).removeClass('loading').html('<i class="fas fa-check"></i> Apply');
                    }
                });
            }

            function clearVoucher() {
                $('#voucherCode').val('');
                $('#finalTotal').text(formatPrice(originalAmount) + ' VND');
                $('#discountRow').addClass('hidden');
                $('#clearVoucherBtn').addClass('hidden');
                $('#hiddenFinalAmount').val(originalAmount);
                $('#hiddenDiscountAmount').val(0);
                $('#hiddenDiscountPercent').val('');
                $('#voucherMessage').empty();
                currentVoucherId = null;
            }

            function showVoucherMessage(message, type) {
                const messageDiv = $('#voucherMessage');
                const iconClass = type === 'success' ? 'fas fa-check-circle' : 'fas fa-exclamation-triangle';
                const messageClass = type === 'success' ? 'message-success' : 'message-error';
                
                messageDiv.html('<i class="' + iconClass + '"></i>' + message);
                messageDiv.attr('class', 'message ' + messageClass + ' fade-in');
            }

            function formatPrice(price) {
                return parseInt(price).toLocaleString();
            }
            
        </script>

        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
    </body>
</html>