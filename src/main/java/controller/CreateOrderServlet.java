package controller;

import dao.AddressDAO;
import dao.CartItemDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.ProductDAO;
import dao.VoucherDAO;
import dao.CustomerVoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "CreateOrderServlet", urlPatterns = {"/CreateOrderServlet"})
public class CreateOrderServlet extends HttpServlet {

    private CartItemDAO cartItemDAO;
    private AddressDAO addressDAO;
    private VoucherDAO voucherDAO;
    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private ProductDAO productDAO;
    private CustomerVoucherDAO customerVoucherDAO;

    @Override
    public void init() throws ServletException {
        cartItemDAO = new CartItemDAO();
        addressDAO = new AddressDAO();
        voucherDAO = new VoucherDAO();
        orderDAO = new OrderDAO();
        orderDetailDAO = new OrderDetailDAO();
        productDAO = new ProductDAO();
        customerVoucherDAO = new CustomerVoucherDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        // Get selected cart item IDs from session
        String selectedItemIds = (String) session.getAttribute("selectedCartItemIds");
        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            session.setAttribute("message", "No items selected for checkout");
            response.sendRedirect("CartItem");
            return;
        }

        try {
            // Parse selected cart item IDs
            List<Integer> cartItemIds = Arrays.stream(selectedItemIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // Get selected cart items
            List<CartItem> selectedCartItems = cartItemDAO.getCartItemsByIds(cartItemIds);

            // Validate stock before showing order page
            StringBuilder stockErrors = new StringBuilder();
            boolean hasStockIssues = false;

            for (CartItem item : selectedCartItems) {
                Product currentProduct = productDAO.getProductByID(item.getProductID());

                if (currentProduct == null || !currentProduct.isIsActive()) {
                    stockErrors.append("Product '").append(item.getProduct().getProductName())
                            .append("' is no longer available.\n");
                    hasStockIssues = true;
                } else if (currentProduct.getQuantity() == 0) {
                    stockErrors.append("Product '").append(currentProduct.getProductName())
                            .append("' is out of stock.\n");
                    hasStockIssues = true;
                } else if (item.getQuantity() > currentProduct.getQuantity()) {
                    stockErrors.append("Product '").append(currentProduct.getProductName())
                            .append("' only has ").append(currentProduct.getQuantity())
                            .append(" items available, but you have ")
                            .append(item.getQuantity()).append(" in cart.\n");
                    hasStockIssues = true;
                }

                // Update product info in cart item
                item.setProduct(currentProduct);
            }

            if (hasStockIssues) {
                session.setAttribute("message", "Cannot proceed to checkout due to stock issues:\n" + stockErrors.toString());
                response.sendRedirect("CartItem");
                return;
            }

            // Get customer addresses
            List<Address> addresses = addressDAO.getAllAddressesByCustomerId(customer.getCustomerID());

            // Calculate total amount
            long totalAmount = 0;
            for (CartItem item : selectedCartItems) {
                totalAmount += item.getProduct().getPrice().longValue() * item.getQuantity();
            }

            // Get available vouchers for customer (vouchers they haven't used yet)
            List<Voucher> availableVouchers = voucherDAO.getAvailableVouchersForCustomer(customer.getCustomerID(), totalAmount);

            request.setAttribute("selectedCartItems", selectedCartItems);
            request.setAttribute("addresses", addresses);
            request.setAttribute("availableVouchers", availableVouchers);
            request.setAttribute("totalAmount", totalAmount);

            request.getRequestDispatcher("/WEB-INF/View/customer/orderManagement/CreateOrder.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Error loading order page");
            response.sendRedirect("CartItem");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("applyVoucher".equals(action)) {
            applyVoucher(request, response);
        } else if ("createOrder".equals(action)) {
            createOrder(request, response);
        }
    }

    private void applyVoucher(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String voucherIdStr = request.getParameter("voucherId");
        String totalAmountStr = request.getParameter("totalAmount");

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            response.getWriter().write("error:not_logged_in");
            return;
        }

        try {
            if (voucherIdStr == null || voucherIdStr.trim().isEmpty()) {
                response.getWriter().write("error:no_voucher_selected");
                return;
            }

            if (totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
                response.getWriter().write("error:invalid_amount");
                return;
            }

            int voucherId = Integer.parseInt(voucherIdStr);
            double totalAmount = Double.parseDouble(totalAmountStr);

            if (totalAmount <= 0) {
                response.getWriter().write("error:invalid_amount");
                return;
            }

            // Validate voucher for this customer and order
            Voucher voucher = voucherDAO.validateVoucherForCustomer(voucherId, customer.getCustomerID(), totalAmount);
            
            if (voucher == null) {
                response.getWriter().write("error:voucher_not_valid");
                return;
            }

            // Calculate discount
            long discount = Math.min(
                    (long) (totalAmount * voucher.getDiscountPercent()) / 100,
                    (long) voucher.getMaxDiscountAmount()
            );
            long finalAmount = (long) totalAmount - discount;

            // Format expiry date
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String expiryDateStr = sdf.format(voucher.getExpiryDate());

            // Return voucher information
            String voucherInfo = String.format("success:%d:%d:%d:%d:%s:%s:%.0f:%.0f:%d/%d",
                    discount,
                    finalAmount,
                    voucher.getVoucherID(),
                    voucher.getDiscountPercent(),
                    voucher.getDescription() != null ? voucher.getDescription().replace(":", "&#58;") : "Discount voucher",
                    expiryDateStr,
                    voucher.getMinOrderAmount(),
                    voucher.getMaxDiscountAmount(),
                    voucher.getUsedCount(),
                    voucher.getUsageLimit()
            );

            response.getWriter().write(voucherInfo);

        } catch (NumberFormatException e) {
            response.getWriter().write("error:invalid_data");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error:system_error");
        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            // Get form data
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            String voucherIdStr = request.getParameter("voucherId");
            String discountPercentStr = request.getParameter("discountPercent");
            long finalAmount = Long.parseLong(request.getParameter("finalAmount"));
            long discountAmount = Long.parseLong(request.getParameter("discountAmount"));

            // Check order limit
            final long ORDER_LIMIT = 500000000L; // 500 million VND
            if (finalAmount > ORDER_LIMIT) {
                session.setAttribute("message",
                        "The order exceeds " + String.format("%,d", ORDER_LIMIT)
                        + " VND due to the store policy. Please contact the admin for assistance.");
                response.sendRedirect("CreateOrderServlet");
                return;
            }

            // Get selected cart items
            String selectedItemIds = (String) session.getAttribute("selectedCartItemIds");
            List<Integer> cartItemIds = Arrays.stream(selectedItemIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<CartItem> selectedCartItems = cartItemDAO.getCartItemsByIds(cartItemIds);

            // CRITICAL: Final stock validation before creating order
            StringBuilder stockErrors = new StringBuilder();
            boolean hasStockIssues = false;

            for (CartItem item : selectedCartItems) {
                Product currentProduct = productDAO.getProductByID(item.getProductID());

                if (currentProduct == null || !currentProduct.isIsActive()) {
                    stockErrors.append("Product '").append(item.getProduct().getProductName())
                            .append("' is no longer available.\n");
                    hasStockIssues = true;
                } else if (currentProduct.getQuantity() == 0) {
                    stockErrors.append("Product '").append(currentProduct.getProductName())
                            .append("' is out of stock.\n");
                    hasStockIssues = true;
                } else if (item.getQuantity() > currentProduct.getQuantity()) {
                    stockErrors.append("Product '").append(currentProduct.getProductName())
                            .append("' only has ").append(currentProduct.getQuantity())
                            .append(" items available, but you're trying to order ")
                            .append(item.getQuantity()).append(".\n");
                    hasStockIssues = true;
                }
            }

            if (hasStockIssues) {
                session.setAttribute("message", "Cannot create order due to stock issues:\n" + stockErrors.toString()
                        + "\nPlease return to cart and update your selection.");
                response.sendRedirect("CartItem");
                return;
            }

            // Get address for snapshot
            Address address = addressDAO.getAddressById(addressId);
            String addressSnapshot = address.getProvinceName() + ", "
                    + address.getDistrictName() + ", "
                    + address.getWardName() + ", "
                    + address.getAddressDetails();

            // Create order
            Order order = new Order();
            order.setCustomerID(customer.getCustomerID());
            order.setTotalAmount(finalAmount);

            // Set discount percent from voucher
            int discountPercent = 0;
            if (discountPercentStr != null && !discountPercentStr.isEmpty()) {
                discountPercent = Integer.parseInt(discountPercentStr);
            }
            order.setDiscount(discountPercent);

            order.setAddressSnapshot(addressSnapshot);
            order.setAddressID(addressId);
            order.setStatus("Waiting");

            int orderId = orderDAO.createOrder(order);

            if (orderId > 0) {
                // Create order details
                for (CartItem item : selectedCartItems) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderID(orderId);
                    orderDetail.setProductID(item.getProductID());
                    orderDetail.setQuantity(item.getQuantity());
                    orderDetail.setPrice(item.getProduct().getPrice().longValue());

                    orderDetailDAO.createOrderDetail(orderDetail);
                }

                // Save voucher usage if voucher was used
                if (voucherIdStr != null && !voucherIdStr.isEmpty()) {
                    int voucherId = Integer.parseInt(voucherIdStr);
                    
                    // Double-check voucher is still valid before using
                    Voucher voucher = voucherDAO.validateVoucherForCustomer(voucherId, customer.getCustomerID(), finalAmount + discountAmount);
                    if (voucher != null) {
                        customerVoucherDAO.createCustomerVoucher(customer.getCustomerID(), voucherId);
                        voucherDAO.incrementVoucherUsage(voucherId);
                    }
                }

                // Clear selected cart items
                for (CartItem item : selectedCartItems) {
                    cartItemDAO.removeCartItem(item.getCartItemID());
                }

                // Clear selected items from session
                session.removeAttribute("selectedCartItemIds");

                session.setAttribute("message", "Order created successfully!");
                response.sendRedirect("ViewOrderOfCustomer?success=created");

            } else {
                session.setAttribute("message", "Failed to create order");
                response.sendRedirect("CreateOrderServlet");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Error creating order: " + e.getMessage());
            response.sendRedirect("CreateOrderServlet");
        }
    }
}