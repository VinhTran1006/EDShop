package controller;

import dao.CartItemDAO;
import dao.ProductDAO; // Cần thêm ProductDAO để check số lượng sản phẩm
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
    private ProductDAO productDAO = new ProductDAO(); // Cần thêm ProductDAO

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("cus");

        if (customer == null) {
            // Nếu chưa đăng nhập, chuyển về trang login
            response.sendRedirect("Login");
            return;
        }

        try {
            // Lấy danh sách cart items của customer
            List<CartItem> cartItems = cartItemDAO.getCartItemsByCustomerId(customer.getCustomerID());

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
                session.setAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
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
                session.setAttribute("message", "Đã xóa tất cả sản phẩm khỏi giỏ hàng!");
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
        return "Servlet for managing cart items";
    }
}
