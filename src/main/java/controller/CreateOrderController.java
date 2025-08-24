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

@WebServlet(name = "CreateOrderController", urlPatterns = {"/CreateOrderController"})
public class CreateOrderController extends HttpServlet {
    
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
            
            // Get customer vouchers
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
            
            request.getRequestDispatcher("WEB-INF/View/customer/homePage/orderManagement/CreateOrder.jsp").forward(request, response);
            
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
            long totalAmount = Long.parseLong(totalAmountStr);
            Voucher voucher = voucherDAO.validateVoucherForCustomer(voucherCode, customer.getCustomerID(), totalAmount);
            
            if (voucher != null) {
                // Calculate discount
                long discount = Math.min(
                    (totalAmount * voucher.getDiscountPercent()) / 100,
                    (long) voucher.getMaxDiscountAmount()
                );
                long finalAmount = totalAmount - discount;
                
                response.getWriter().write("success:" + discount + ":" + finalAmount + ":" + voucher.getVoucherID());
            } else {
                response.getWriter().write("error:invalid_voucher");
            }
            
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
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            // Get form data
            int addressId = Integer.parseInt(request.getParameter("addressId"));
            String voucherIdStr = request.getParameter("voucherId");
            long finalAmount = Long.parseLong(request.getParameter("finalAmount"));
            long discount = Long.parseLong(request.getParameter("discount"));
            
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
            order.setDiscount((int) discount);
            order.setAddressSnapshot(addressSnapshot);
            order.setAddressID(addressId);
            order.setStatus("Pending");
            
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
                    
                    // Update product quantity
                    productDAO.updateProductQuantity(item.getProductID(), -item.getQuantity());
                }
                
                // Save voucher usage if voucher was used
                if (voucherIdStr != null && !voucherIdStr.isEmpty()) {
                    int voucherId = Integer.parseInt(voucherIdStr);
                    customerVoucherDAO.createCustomerVoucher(customer.getCustomerID(), voucherId);
                    voucherDAO.incrementVoucherUsage(voucherId);
                }
                
                // Clear cart
                cartItemDAO.clearCartByCustomerId(customer.getCustomerID());
                
                session.setAttribute("message", "Order created successfully! Order ID: " + orderId);
                response.sendRedirect("Home");
                
            } else {
                session.setAttribute("message", "Failed to create order");
                response.sendRedirect("CreateOrderController");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Error creating order");
            response.sendRedirect("CreateOrderController");
        }
    }
}