package controller;

import dao.CartItemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "RemoveCartItemServlet", urlPatterns = {"/RemoveCartItem"})
public class RemoveCartItemServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String accountIdRaw = request.getParameter("accountId");

        if ("deleteMultiple".equals(action)) {
            try {
                String selectedItems = request.getParameter("selectedItems");
                if (selectedItems == null || selectedItems.isEmpty()) {
                    session.setAttribute("message", "No items selected for deletion.");
                    response.sendRedirect("CartList?accountId=" + accountIdRaw);
                    return;
                }

                List<String> itemIds = Arrays.asList(selectedItems.split(","));
                CartItemDAO cartDAO = new CartItemDAO();
                boolean isSuccess = cartDAO.deleteMultipleCartItems(itemIds);

                if (isSuccess) {
                    response.sendRedirect("CartList?accountId=" + accountIdRaw + "&successdeletem=1");
                } else {
                    response.sendRedirect("CartList?accountId=" + accountIdRaw + "&errordeletem=1");
                }
            } catch (Exception e) {
                session.setAttribute("message", "Error deleting cart items.");
                response.sendRedirect("CartList?accountId=" + accountIdRaw);
            }
        } else {
            session.setAttribute("message", "Invalid action.");
            response.sendRedirect("CartList?accountId=" + accountIdRaw);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        String accountIdRaw = request.getParameter("accountId");

        if ("remove".equals(action)) {
            try {
                int cartItemId = Integer.parseInt(request.getParameter("id"));
                CartItemDAO cartDAO = new CartItemDAO();
                boolean isSuccess = cartDAO.deleteCartItem(cartItemId);

                if (isSuccess) {
                    response.sendRedirect("CartList?accountId=" + accountIdRaw + "&successdeletes=1");
                } else {
                    response.sendRedirect("CartList?accountId=" + accountIdRaw + "&errordeletes=1");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("message", "Invalid cart item ID.");
                response.sendRedirect("CartList?accountId=" + accountIdRaw);
            }
        } else {
            session.setAttribute("message", "Invalid action.");
            response.sendRedirect("CartList?accountId=" + accountIdRaw);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to remove items from cart";
    }
}