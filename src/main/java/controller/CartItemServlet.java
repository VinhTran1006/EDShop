package controller;

import dao.CartItemDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.CartItem;
import model.Product;

@WebServlet(name = "CartItemServlet", urlPatterns = {"/CartItem"})
public class CartItemServlet extends HttpServlet {

    private CartItemDAO cartItemDAO = new CartItemDAO();
    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            response.sendRedirect("Login");
            return;
        }

        try {
            // Lấy danh sách cart items của customer
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomerId(customer.getCustomerID());
            
            // Validate và update cart items dựa trên stock hiện tại
            validateAndUpdateCartItems(cartItems, session);

            // Set attribute để JSP có thể sử dụng
            request.setAttribute("cartItems", cartItems);

            // Forward đến trang view cart
            request.getRequestDispatcher("WEB-INF/View/customer/homePage/cartManagement/viewCart.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Có lỗi xảy ra khi tải giỏ hàng!");
            response.sendRedirect("Home");
        }
    }

    /**
     * Validate cart items against current stock and update quantities if necessary
     */
    private void validateAndUpdateCartItems(List<CartItem> cartItems, HttpSession session) {
        StringBuilder warningMessage = new StringBuilder();
        boolean hasUpdates = false;

        for (CartItem item : cartItems) {
            try {
                Product currentProduct = productDAO.getProductByID(item.getProductID());
                
                if (currentProduct == null || !currentProduct.isIsActive()) {
                    // Sản phẩm không còn tồn tại hoặc bị vô hiệu hóa
                    cartItemDAO.removeCartItem(item.getCartItemID());
                    warningMessage.append("Product '").append(item.getProduct().getProductName())
                                 .append("' has been removed from your cart as it's no longer available.\n");
                    hasUpdates = true;
                    continue;
                }

                // Update product info in cart item
                item.setProduct(currentProduct);

                // Kiểm tra số lượng trong kho
                if (currentProduct.getQuantity() == 0) {
                    // Sản phẩm hết hàng
                    warningMessage.append("Product '").append(currentProduct.getProductName())
                                 .append("' is out of stock.\n");
                    hasUpdates = true;
                } else if (item.getQuantity() > currentProduct.getQuantity()) {
                    // Số lượng trong cart vượt quá số lượng có sẵn
                    cartItemDAO.updateCartItemQuantity(item.getCartItemID(), currentProduct.getQuantity());
                    item.setQuantity(currentProduct.getQuantity());
                    warningMessage.append("Quantity for '").append(currentProduct.getProductName())
                                 .append("' has been reduced to ").append(currentProduct.getQuantity())
                                 .append(" (maximum available).\n");
                    hasUpdates = true;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (hasUpdates && warningMessage.length() > 0) {
            session.setAttribute("cartWarning", warningMessage.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            response.getWriter().write("error:not_logged_in");
            return;
        }

        switch (action != null ? action : "") {
            case "saveSelectedItems":
                handleSaveSelectedItems(request, response, session);
                break;

            case "updateQuantity":
                handleUpdateQuantity(request, response, session, customer.getCustomerID());
                break;

            case "removeItem":
                handleRemoveItem(request, response, session, customer.getCustomerID());
                break;

            case "clearAll":
                handleClearAll(request, response, session, customer.getCustomerID());
                break;

            case "validateCheckout":
                handleValidateCheckout(request, response, session, customer.getCustomerID());
                break;

            default:
                doGet(request, response);
                break;
        }
    }

    private void handleSaveSelectedItems(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        String selectedCartItemIds = request.getParameter("selectedCartItemIds");
        session.setAttribute("selectedCartItemIds", selectedCartItemIds);
        response.getWriter().write("success");
    }

    private void handleValidateCheckout(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, int customerId) throws IOException {
        try {
            String selectedItemIds = request.getParameter("selectedCartItemIds");
            if (selectedItemIds == null || selectedItemIds.trim().isEmpty()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"message\": \"No items selected\"}");
                return;
            }

            String[] itemIds = selectedItemIds.split(",");
            StringBuilder errorMessage = new StringBuilder();
            boolean hasErrors = false;

            for (String itemId : itemIds) {
                try {
                    int cartItemId = Integer.parseInt(itemId.trim());
                    CartItem cartItem = cartItemDAO.getCartItemById(cartItemId);
                    
                    if (cartItem == null || cartItem.getCustomerID() != customerId) {
                        continue;
                    }

                    Product product = productDAO.getProductByID(cartItem.getProductID());
                    if (product == null || !product.isIsActive()) {
                        errorMessage.append("Product '").append(cartItem.getProduct().getProductName())
                                   .append("' is no longer available.\n");
                        hasErrors = true;
                        continue;
                    }

                    if (product.getQuantity() == 0) {
                        errorMessage.append("Product '").append(product.getProductName())
                                   .append("' is out of stock.\n");
                        hasErrors = true;
                    } else if (cartItem.getQuantity() > product.getQuantity()) {
                        errorMessage.append("Product '").append(product.getProductName())
                                   .append("' only has ").append(product.getQuantity())
                                   .append(" items available, but you have ")
                                   .append(cartItem.getQuantity()).append(" in cart.\n");
                        hasErrors = true;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            if (hasErrors) {
                response.getWriter().write("{\"success\": false, \"message\": \"" + 
                    errorMessage.toString().replace("\"", "\\\"").replace("\n", "\\n") + "\"}");
            } else {
                response.getWriter().write("{\"success\": true}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"System error occurred\"}");
        }
    }

    private void handleUpdateQuantity(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, int customerId) throws IOException {
        try {
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
            int newQuantity = Integer.parseInt(request.getParameter("quantity"));

            // Validation số lượng tối thiểu
            if (newQuantity < 1) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("error:invalid_quantity");
                return;
            }

            // Lấy thông tin cart item hiện tại
            CartItem currentItem = cartItemDAO.getCartItemById(cartItemId);
            if (currentItem == null || currentItem.getCustomerID() != customerId) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("error:item_not_found");
                return;
            }

            // Kiểm tra số lượng sản phẩm có sẵn
            Product product = productDAO.getProductByID(currentItem.getProductID());
            if (product == null || !product.isIsActive()) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("error:product_not_available");
                return;
            }

            if (product.getQuantity() == 0) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("error:product_out_of_stock");
                return;
            }

            if (newQuantity > product.getQuantity()) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("error:insufficient_stock:" + product.getQuantity());
                return;
            }

            // Cập nhật số lượng
            boolean success = cartItemDAO.updateCartItemQuantity(cartItemId, newQuantity);

            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            if (success) {
                response.getWriter().write("success");
            } else {
                response.getWriter().write("error:update_failed");
            }

        } catch (NumberFormatException e) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("error:invalid_parameters");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("error:system_error");
        }
    }

    private void handleRemoveItem(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, int customerId) throws IOException, ServletException {
        try {
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));

            // Kiểm tra quyền sở hữu
            CartItem item = cartItemDAO.getCartItemById(cartItemId);
            if (item == null || item.getCustomerID() != customerId) {
                session.setAttribute("message", "Không thể xóa sản phẩm này!");
                response.sendRedirect("CartItem");
                return;
            }

            boolean success = cartItemDAO.removeCartItem(cartItemId);

            if (success) {
                session.setAttribute("message", "Product removed from cart successfully!");
            } else {
                session.setAttribute("message", "Có lỗi xảy ra khi xóa sản phẩm!");
            }

            response.sendRedirect("CartItem");

        } catch (NumberFormatException e) {
            session.setAttribute("message", "Tham số không hợp lệ!");
            response.sendRedirect("CartItem");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Có lỗi xảy ra!");
            response.sendRedirect("CartItem");
        }
    }

    private void handleClearAll(HttpServletRequest request, HttpServletResponse response,
            HttpSession session, int customerId) throws IOException, ServletException {
        try {
            boolean success = cartItemDAO.clearCartByCustomerId(customerId);

            if (success) {
                session.setAttribute("message", "All products removed from cart!");
            } else {
                session.setAttribute("message", "Có lỗi xảy ra khi xóa giỏ hàng!");
            }

            response.sendRedirect("CartItem");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Có lỗi xảy ra!");
            response.sendRedirect("CartItem");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing cart items with stock validation";
    }
}