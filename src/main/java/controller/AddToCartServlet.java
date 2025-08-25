package controller;

import dao.CartItemDAO;
import dao.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.Customer;
import model.Product;

@WebServlet(name = "AddToCartServlet", urlPatterns = {"/AddToCartServlet"})
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Customer user = (Customer) session.getAttribute("user");
        
        // Kiểm tra đăng nhập
        if (user == null) {
            response.sendRedirect("Login");
            return;
        }
        
        try {
            // Lấy thông tin từ form
            String productIdStr = request.getParameter("productId");
            String categoryIdStr = request.getParameter("categoryId");
            
            if (productIdStr == null || categoryIdStr == null) {
                response.sendRedirect("Home");
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            int categoryId = Integer.parseInt(categoryIdStr);
            int customerId = user.getCustomerID();
            int quantityToAdd = 1; // Mỗi lần add sẽ tăng 1
            
            // Khởi tạo DAO
            ProductDAO productDAO = new ProductDAO();
            CartItemDAO cartItemDAO = new CartItemDAO();
            
            // Lấy thông tin sản phẩm
            Product product = productDAO.getProductByID(productId);
            
            if (product == null || !product.isIsActive()) {
                // Sản phẩm không tồn tại hoặc không còn hoạt động
                response.sendRedirect("ProductDetail?productId=" + productId + "&categoryId=" + categoryId + "&error=product_not_found");
                return;
            }
            
            // Kiểm tra số lượng tồn kho
            if (product.getQuantity() <= 0) {
                // Hết hàng
                response.sendRedirect("ProductDetail?productId=" + productId + "&categoryId=" + categoryId + "&checkquantity=1");
                return;
            }
            
            // Kiểm tra số lượng hiện tại trong cart
            CartItem existingCartItem = cartItemDAO.getCartItem(customerId, productId);
            int currentQuantityInCart = 0;
            
            if (existingCartItem != null) {
                currentQuantityInCart = existingCartItem.getQuantity();
            }
            
            // Kiểm tra xem số lượng sau khi thêm có vượt quá tồn kho không
            int totalQuantityAfterAdd = currentQuantityInCart + quantityToAdd;
            
            if (totalQuantityAfterAdd > product.getQuantity()) {
                // Số lượng vượt quá tồn kho
                response.sendRedirect("ProductDetail?productId=" + productId + "&categoryId=" + categoryId + "&checkquantity=1");
                return;
            }
            
            // Thêm vào giỏ hàng
            boolean success = cartItemDAO.addToCart(customerId, productId, quantityToAdd);
            
            if (success) {
                // Thành công
                response.sendRedirect("ProductDetail?productId=" + productId + "&categoryId=" + categoryId + "&successcreate=1");
            } else {
                // Thất bại
                response.sendRedirect("ProductDetail?productId=" + productId + "&categoryId=" + categoryId + "&error=add_failed");
            }
            
        } catch (NumberFormatException e) {
            // Lỗi parse số
            response.sendRedirect("home");
        } catch (Exception e) {
            // Lỗi khác
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}