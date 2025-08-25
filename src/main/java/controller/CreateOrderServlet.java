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
import java.util.regex.Pattern;

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
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get selected cart item IDs from session
        String selectedItemIds = (String) session.getAttribute("selectedCartItemIds");
        if (selectedItemIds == null || selectedItemIds.isEmpty()) {
            session.setAttribute("message", "No items selected for checkout");
            response.sendRedirect("viewCart.jsp");
            return;
        }
        
        try {
            // Parse selected cart item IDs
            List<Integer> cartItemIds = Arrays.stream(selectedItemIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            
            // Get selected cart items
            List<CartItem> selectedCartItems = cartItemDAO.getCartItemsByIds(cartItemIds);
            
            // Get customer addresses
            List<Address> addresses = addressDAO.getAllAddressesByCustomerId(customer.getCustomerID());
            
            // Get customer vouchers (chỉ lấy voucher chưa dùng)
            List<Voucher> availableVouchers = voucherDAO.getAvailableVouchersForCustomer(customer.getCustomerID());
            
            // Calculate total amount
            long totalAmount = 0;
            for (CartItem item : selectedCartItems) {
                totalAmount += item.getProduct().getPrice().longValue() * item.getQuantity();
            }
            
            request.setAttribute("selectedCartItems", selectedCartItems);
            request.setAttribute("addresses", addresses);
            request.setAttribute("availableVouchers", availableVouchers);
            request.setAttribute("totalAmount", totalAmount);
            
            request.getRequestDispatcher("/WEB-INF/View/customer/orderManagement/CreateOrder.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Error loading order page");
            response.sendRedirect("viewCart.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("validateVoucher".equals(action)) {
            validateVoucher(request, response);
        } else if ("createOrder".equals(action)) {
            createOrder(request, response);
        }
    }
    
    private void validateVoucher(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String voucherCode = request.getParameter("voucherCode");
        String totalAmountStr = request.getParameter("totalAmount");
        
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");
        
        if (customer == null) {
            response.getWriter().write("error:not_logged_in");
            return;
        }
        
        try {
            // Validate input
            if (voucherCode == null || voucherCode.trim().isEmpty()) {
                response.getWriter().write("error:empty_code");
                return;
            }
            
            // Clean and validate voucher code (chỉ cho phép chữ cái, số và một số ký tự đặc biệt)
            voucherCode = voucherCode.trim().toUpperCase();
            if (!isValidVoucherCode(voucherCode)) {
                response.getWriter().write("error:invalid_format");
                return;
            }
            
            if (totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
                response.getWriter().write("error:invalid_amount");
                return;
            }
            
            long totalAmount = Long.parseLong(totalAmountStr);
            if (totalAmount <= 0) {
                response.getWriter().write("error:invalid_amount");
                return;
            }
            
            // 1. Kiểm tra voucher có tồn tại không
            Voucher voucher = voucherDAO.getVoucherByCode(voucherCode);
            if (voucher == null) {
                response.getWriter().write("error:voucher_not_found");
                return;
            }
            
            // 2. Kiểm tra khách hàng đã sử dụng voucher này chưa
            if (customerVoucherDAO.hasCustomerUsedVoucher(customer.getCustomerID(), voucher.getVoucherID())) {
                response.getWriter().write("error:voucher_already_used");
                return;
            }
            
            // 3. Validate voucher cho đơn hàng (kiểm tra các điều kiện khác)
            Voucher validVoucher = voucherDAO.validateVoucherForOrder(voucherCode, customer.getCustomerID(), totalAmount);
            if (validVoucher == null) {
                // Kiểm tra cụ thể lý do không hợp lệ
                if (!voucher.isActive()) {
                    response.getWriter().write("error:voucher_inactive");
                } else if (voucher.getExpiryDate().before(new java.util.Date())) {
                    response.getWriter().write("error:voucher_expired");
                } else if (voucher.getUsedCount() >= voucher.getUsageLimit()) {
                    response.getWriter().write("error:voucher_limit_reached");
                } else if (totalAmount < voucher.getMinOrderAmount()) {
                    response.getWriter().write("error:min_order_not_met:" + String.format("%.0f", voucher.getMinOrderAmount()));
                } else {
                    response.getWriter().write("error:voucher_invalid");
                }
                return;
            }
            
            // Calculate discount
            long discount = Math.min(
                (totalAmount * voucher.getDiscountPercent()) / 100,
                (long) voucher.getMaxDiscountAmount()
            );
            long finalAmount = totalAmount - discount;
            
            // Trả về: discount_amount:final_amount:voucher_id:discount_percent
            response.getWriter().write("success:" + discount + ":" + finalAmount + ":" + voucher.getVoucherID() + ":" + voucher.getDiscountPercent());
            
        } catch (NumberFormatException e) {
            response.getWriter().write("error:invalid_amount");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("error:system_error");
        }
    }
    
    // Validate voucher code format
    private boolean isValidVoucherCode(String code) {
        // Cho phép chữ cái, số, dấu gạch ngang và underscore, độ dài từ 3-20 ký tự
        return Pattern.matches("^[A-Z0-9_-]{3,20}$", code);
    }
    
    private void createOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");
        
        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Get form data
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            String voucherIdStr = request.getParameter("voucherId");
            String discountPercentStr = request.getParameter("discountPercent");
            long finalAmount = Long.parseLong(request.getParameter("finalAmount"));
            long discountAmount = Long.parseLong(request.getParameter("discountAmount"));
            
            // Get selected cart items
            String selectedItemIds = (String) session.getAttribute("selectedCartItemIds");
            List<Integer> cartItemIds = Arrays.stream(selectedItemIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            
            List<CartItem> selectedCartItems = cartItemDAO.getCartItemsByIds(cartItemIds);
            
            // Get address for snapshot
            Address address = addressDAO.getAddressById(addressId);
            String addressSnapshot = address.getProvinceName() + ", " + 
                                   address.getDistrictName() + ", " + 
                                   address.getWardName() + ", " + 
                                   address.getAddressDetails();
            
            // Create order
            Order order = new Order();
            order.setCustomerID(customer.getCustomerID());
            order.setTotalAmount(finalAmount);
            
            // Set discount percent từ voucher (không phải discount amount)
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
                // Create order details and update product quantities
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
                    customerVoucherDAO.createCustomerVoucher(customer.getCustomerID(), voucherId);
                    voucherDAO.incrementVoucherUsage(voucherId);
                }
                
                // Clear cart
                cartItemDAO.clearCartByCustomerId(customer.getCustomerID());
                
                session.setAttribute("message", "Order created successfully!");
                response.sendRedirect("Home");
                
            } else {
                session.setAttribute("message", "Failed to create order");
                response.sendRedirect("CreateOrderServlet");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Error creating order");
            response.sendRedirect("CreateOrderServlet");
        }
    }
}