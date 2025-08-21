package controller;

import dao.CartItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateCartServlet", urlPatterns = {"/UpdateCart"})
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        try {
            String action = request.getParameter("action");
            String accountIdRaw = request.getParameter("accountId");
            String cartItemIdRaw = request.getParameter("cartItemId");
            String quantityRaw = request.getParameter("quantity");

           

            int cartItemId = Integer.parseInt(cartItemIdRaw);
            int quantity = Integer.parseInt(quantityRaw);

            // Ensure quantity is not negative
            if (quantity < 1) {
                quantity = 1;
            }

            // Update quantity in database
            CartItemDAO cartDAO = new CartItemDAO();
            boolean updated = cartDAO.updateCartItemQuantity(cartItemId, quantity);

            // Log update result
            System.out.println("Update result for cartItemId " + cartItemId + ": " + updated);

            if (updated) {
                response.getWriter().print("success"); // Dùng print thay vì write
            } else {
                response.getWriter().print("error:update_failed");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid input: " + e.getMessage());
            response.getWriter().write("error");
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            response.getWriter().write("error");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles cart item quantity updates";
    }
}
