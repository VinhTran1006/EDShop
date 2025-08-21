package controller;

import dao.AddressDAO;
import dao.CartItemDAO;
import dao.ImportStockDetailDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.PaymentsDAO;
import dao.VoucherDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Address;
import model.CartItem;
import model.Customer;
import model.Product;
import model.Voucher;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/CheckoutServlet"})
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("user");
        Customer customer = (Customer) session.getAttribute("cus");
        String action = request.getParameter("action");
        if (action == null) {
            action = "checkout";
        }
        if (user == null || customer == null) {
            response.sendRedirect("Login");
            return;
        }

        if (action.equalsIgnoreCase("checkout")) {
            String selectedIdsParam = request.getParameter("selectedCartItemIds");
            List<CartItem> selectedItems = new ArrayList<>();

            if (selectedIdsParam != null && !selectedIdsParam.trim().isEmpty()) {
                String[] idArray = selectedIdsParam.split(",");
                CartItemDAO cartDAO = new CartItemDAO();

                for (String idStr : idArray) {
                    try {
                        int cartItemId = Integer.parseInt(idStr.trim());
                        CartItem item = cartDAO.getCartItemById(cartItemId);
                        if (item != null && item.getProduct() != null) {
                            selectedItems.add(item);
                        }
                    } catch (NumberFormatException e) {
                        Logger.getLogger(CheckoutServlet.class.getName()).log(Level.WARNING, "Invalid cartItemId: {0}", idStr);
                    }
                }
            }

            if (selectedItems.isEmpty()) {
                session.setAttribute("message", "No product is chosen to pay.");
                request.getRequestDispatcher("/WEB-INF/View/customer/cartManagement/viewCart.jsp").forward(request, response);
                return;
            }
            AddressDAO addressDAO = new AddressDAO();
            List<Address> addresses = addressDAO.getAddressesByCustomerId(customer.getId());
            if (addresses == null || addresses.isEmpty()) {
                session.setAttribute("message", "No address found. Please add a new address.");
                response.sendRedirect("AddAddress");
                return;
            }

            // Lấy địa chỉ được chọn từ session (nếu có) hoặc địa chỉ mặc định
            Address selectedAddress = (Address) session.getAttribute("selectedAddress");
            if (selectedAddress == null) {
               
                selectedAddress = addressDAO.getDefaultAddressByCustomerId(customer.getId());
                if (selectedAddress != null) {
                    session.setAttribute("selectedAddress", selectedAddress);
                    Logger.getLogger(CheckoutServlet.class.getName()).log(Level.INFO,
                            "Default address found for CustomerID {0}: {1}",
                            new Object[]{customer.getId(), selectedAddress.getAddressDetails()});
                } else {
                    Logger.getLogger(CheckoutServlet.class.getName()).log(Level.WARNING,
                            "No default address found for CustomerID: {0}", customer.getId());
                }
            }

            session.setAttribute("selectedItems", selectedItems);
            session.setAttribute("selectedCartItemIds", selectedIdsParam);
            request.setAttribute("selectedItems", selectedItems);
            request.setAttribute("defaultAddress", selectedAddress);
            request.getRequestDispatcher("/WEB-INF/View/customer/cartManagement/checkout.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("voucher")) {
            String voucherCode = request.getParameter("voucherCode");
            String voucherId = request.getParameter("voucherId");
            String selectedIdsParam = request.getParameter("selectedCartItemIds");

            VoucherDAO voucherDAO = new VoucherDAO();
            Voucher voucher = null;

            if (voucherId != null && !voucherId.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(voucherId);
                    voucher = voucherDAO.getVoucherById(id);
                    if (voucher != null && customer != null) {
                      //  voucher = voucherDAO.getVoucherByCodeForCustomer(voucher.getCode(), customer.getId());
                    }
                } catch (NumberFormatException e) {
                    Logger.getLogger(CheckoutServlet.class.getName()).log(Level.WARNING, "Invalid voucherId: {0}", voucherId);
                    session.setAttribute("errorMessage", "Invalid voucher ID.");
                    response.sendRedirect("voucher.jsp?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                    return;
                }
            } else if (voucherCode != null) {
                if (customer != null) {
               //     voucher = voucherDAO.getVoucherByCodeForCustomer(voucherCode, customer.getId());
                } else {
                    voucher = voucherDAO.getVoucherByCode(voucherCode);
                }
            }

            if (voucher != null) {
                long totalAmount = calculateTotalAmount((List<CartItem>) session.getAttribute("selectedItems"));
                if (!voucher.isActive() || voucher.getExpiryDate().before(new Date())) {
                    session.setAttribute("errorMessage", "Voucher is not active or has expired.");
                } else if (totalAmount < voucher.getMinOrderAmount()) {
                    session.setAttribute("errorMessage", "Total order amount does not meet the minimum requirement of " + voucher.getMinOrderAmount() + " VND.");
                } else if (voucher.getUsedCount() >= voucher.getUsageLimit() && voucher.getUsageLimit() > 0) {
                    session.setAttribute("errorMessage", "Voucher has exceeded its usage limit.");
                } else {
                    session.setAttribute("appliedVoucher", voucher);
                    session.setAttribute("message", "Voucher applied successfully!");
                    response.sendRedirect("CheckoutServlet?action=checkout&selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                    return;
                }
            } else {
                session.setAttribute("errorMessage", "Voucher code is not valid or you do not have the right to use it.");
            }

            // If validation fails, redirect back to voucher.jsp with error
            response.sendRedirect("/WEB-INF/View/customer/cartManagement/voucherOrder.jsp?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("user");
        Customer customer = (Customer) session.getAttribute("cus");
        CartItemDAO cartDAO = new CartItemDAO();
        OrderDAO orderDAO = new OrderDAO();
        VoucherDAO voucherDAO = new VoucherDAO();
        OrderDetailDAO orderDetailsDAO = new OrderDetailDAO();
        PaymentsDAO paymentsDAO = new PaymentsDAO();
        ImportStockDetailDAO importStockDetailDAO = new ImportStockDetailDAO();

        if (user == null || customer == null) {
            response.sendRedirect("Login");
            return;
        }

        String selectedIdsParam = request.getParameter("selectedCartItemIds");
        List<CartItem> selectedItems = new ArrayList<>();

        if (selectedIdsParam != null && !selectedIdsParam.trim().isEmpty()) {
            String[] idArray = selectedIdsParam.split(",");

            for (String idStr : idArray) {
                try {
                    int cartItemId = Integer.parseInt(idStr.trim());
                    CartItem item = cartDAO.getCartItemById(cartItemId);
                    if (item != null && item.getProduct() != null) {
                        selectedItems.add(item);
                    }
                } catch (NumberFormatException e) {
                    Logger.getLogger(CheckoutServlet.class.getName()).log(Level.WARNING, "Invalid cartItemId: {0}", idStr);
                }
            }
        }

        if (selectedItems.isEmpty()) {
            session.setAttribute("message", "No product is chosen to pay.");
            response.sendRedirect("CheckoutServlet?selectedCartItemIds="
                    + (selectedIdsParam != null ? selectedIdsParam : ""));
            return;
        }
        try {
            // Lấy dữ liệu từ form
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String addressIdStr = request.getParameter("addressId");
            String totalAmountStr = request.getParameter("totalAmount");
            String totalPromotionStr = request.getParameter("totalPromotion");
            Voucher appliedVoucher = (Voucher) session.getAttribute("appliedVoucher");

            // Kiểm tra dữ liệu đầu vào
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("FullName is required");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone is required");
            }
            if (addressIdStr == null || addressIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("AddressID is required");
            }
            if (totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
                throw new IllegalArgumentException("TotalAmount is required");
            }

            // Ép kiểu addressId
            int addressId;
            try {
                addressId = Integer.parseInt(addressIdStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid AddressID: " + addressIdStr);
            }

            // Lấy AddressSnapshot từ AddressID
            AddressDAO addressDAO = new AddressDAO();
            Address address = addressDAO.getAddressById(addressId);
            if (address == null) {
                throw new IllegalArgumentException("Invalid AddressID: " + addressIdStr);
            }
            String addressSnapshot = address.getProvinceName() + ", "
                    + address.getDistrictName() + ", "
                    + address.getWardName() + ", "
                    + address.getAddressDetails();

            // Ép kiểu totalAmount
            long totalAmount;
            try {
                totalAmount = Long.parseLong(totalAmountStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid totalAmount: " + totalAmountStr);
            }

            // Ép kiểu totalPromotion
            long totalPromotion = 0;
            if (totalPromotionStr != null && !totalPromotionStr.trim().isEmpty()) {
                try {
                    totalPromotion = Long.parseLong(totalPromotionStr);
                } catch (NumberFormatException e) {
                    Logger.getLogger(CheckoutServlet.class.getName()).log(Level.WARNING,
                            "Invalid totalPromotion, defaulting to 0: {0}", totalPromotionStr);
                }
            }

            // Kiểm tra totalPromotion hợp lệ
            if (totalPromotion < 0 || totalPromotion > totalAmount) {
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.WARNING,
                        "Invalid totalPromotion: {0}, totalAmount: {1}",
                        new Object[]{totalPromotion, totalAmount});
                session.setAttribute("message", "Promotional value is invalid.");
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Kiểm tra tính hợp lệ của totalAmount
            long calculatedTotal = calculateTotalAmount(selectedItems);
            if (calculatedTotal != totalAmount) {
                session.setAttribute("message",
                        "The total money does not match the product list. Expected: " + calculatedTotal + ", Received: " + totalAmount);
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Kiểm tra CustomerID
            if (customer.getId() <= 0) {
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE,
                        "Invalid CustomerID: {0}", customer.getId());
                session.setAttribute("message", "Customer information is invalid.");
                response.sendRedirect("Login");
                return;
            }

            // Chuẩn bị dữ liệu cho createOrder
            String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String deliveredDate = null; // Đơn hàng mới
            int status = 1; // Đang xử lý
            long finalTotalAmount = totalAmount - totalPromotion;
            int discount = totalAmount > 0 ? (int) ((totalPromotion * 100) / totalAmount) : 0;

            // Kiểm tra dữ liệu trước khi insert
            boolean isValidOrder = true;
            StringBuilder validationMessage = new StringBuilder("Order validation errors: ");
            if (customer.getId() <= 0) {
                isValidOrder = false;
                validationMessage.append("CustomerID is invalid; ");
            }
            if (fullName == null || fullName.trim().isEmpty()) {
                isValidOrder = false;
                validationMessage.append("FullName is null or empty; ");
            }
            if (phone == null || phone.trim().isEmpty()) {
                isValidOrder = false;
                validationMessage.append("Phone is null or empty; ");
            }
            if (addressSnapshot == null || addressSnapshot.trim().isEmpty()) {
                isValidOrder = false;
                validationMessage.append("AddressSnapshot is null or empty; ");
            }
            if (orderDate == null || orderDate.trim().isEmpty()) {
                isValidOrder = false;
                validationMessage.append("OrderDate is null or empty; ");
            }
            if (finalTotalAmount < 0) {
                isValidOrder = false;
                validationMessage.append("TotalAmount is negative; ");
            }

            // Ghi log dữ liệu
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.INFO,
                    "Order data: CustomerID={0}, FullName={1}, Phone={2}, AddressID={3}, AddressSnapshot={4}, OrderDate={5}, TotalAmount={6}, Discount={7}",
                    new Object[]{customer.getId(), fullName, phone, addressId, addressSnapshot, orderDate, finalTotalAmount, discount});

            // Nếu dữ liệu không hợp lệ, trả về lỗi
            if (!isValidOrder) {
                Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, validationMessage.toString());
                session.setAttribute("message", "Valid order data:" + validationMessage.toString());
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Gọi createOrder với AddressID
            int orderID = orderDAO.createOrder(customer.getId(), fullName, addressSnapshot, phone,
                    orderDate, deliveredDate, status, finalTotalAmount, discount, addressId);
            if (orderID <= 0) {
                session.setAttribute("message", "Can't create orders! Please check the information.");
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Thêm chi tiết đơn hàng
            boolean orderDetailsSuccess = orderDetailsDAO.addOrderDetails(orderID, selectedItems);
            if (!orderDetailsSuccess) {
                session.setAttribute("message", "Error when adding order details!");
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Thêm thanh toán
            boolean paymentSuccess = paymentsDAO.addPayment(orderID, finalTotalAmount, "COD", "PENDING");
            if (!paymentSuccess) {
                session.setAttribute("message", "Error when adding payment information!");
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Xóa các mục trong giỏ hàng
            List<Integer> cartItemIds = new ArrayList<>();
            for (CartItem item : selectedItems) {
                cartItemIds.add(item.getCartItemID());
            }
            boolean cartItemsDeleted = cartDAO.deleteMultipleCartItemsByIntegerIds(cartItemIds);
            if (!cartItemsDeleted) {
                session.setAttribute("message", "Error when deleting the product from the cart!");
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

            // Cập nhật tồn kho
            boolean stockUpdated = importStockDetailDAO.updateStockForOrder(selectedItems);
            if (!stockUpdated) {
                session.setAttribute("message", "Error when updating inventory!");
                response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
                return;
            }

          

            // Xóa session attributes
            session.removeAttribute("selectedItems");
            session.removeAttribute("selectedCartItemIds");
            session.removeAttribute("selectedAddress");
            session.setAttribute("message", "Đặt hàng thành công!");
            response.sendRedirect("ViewOrderOfCustomer");

        } catch (NumberFormatException e) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE,
                    "Invalid input data: {0}", e.getMessage());
            session.setAttribute("message", "Invalid input data: " + e.getMessage());
            response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
        } catch (IllegalArgumentException e) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE,
                    "Validation error: {0}", e.getMessage());
            session.setAttribute("message", "Authentication error: " + e.getMessage());
            response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
        } catch (Exception e) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE,
                    "Error processing order: {0}", e.getMessage());
            session.setAttribute("message", "Error when processing orders: " + e.getMessage());
            response.sendRedirect("CheckoutServlet?selectedCartItemIds=" + (selectedIdsParam != null ? selectedIdsParam : ""));
        }
    }

    private long calculateTotalAmount(List<CartItem> cartItems) {
        long totalAmount = 0;
        if (cartItems != null && !cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
                Product product = item.getProduct();
                if (product != null) {
                    BigDecimal unitPrice = product.getPrice();
                    BigDecimal discount = BigDecimal.valueOf(product.getDiscount());
                    BigDecimal discountFactor = BigDecimal.ONE.subtract(discount.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP));
                    BigDecimal discountedPrice = unitPrice.multiply(discountFactor);
                    BigDecimal itemTotal = discountedPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                    totalAmount += itemTotal.longValue();
                }
            }
        }
        return totalAmount;
    }
}
