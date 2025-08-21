package controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Staff;

@WebFilter(urlPatterns = {
    "/AddPromotionServlet",
    "/AdminAddProductDetail",
    "/AdminCreateProduct",
    "/AdminDashboard",
    "/StaffDeleteProduct",
    "/AdminProduct",
    "/AdminUpdateProduct",
    "/AdminViewProductDetail",
    "/CategoryDetail",
    "/CategoryView",
    "/ChangePasswordStaff",
    "/CreateCategory",
    "/CreateStaffServlet",
    "/CreateSupplier",
    "/DeleteStaffServlet",
    "/InventoryStatistic",
    "/ManageStatistic",
    "/RevenueStatistic",
    "/StaffList",
    "/UpdateCategory",
    "/UpdateStaffServlet",
    "/UpdateSupplier",
    "/ViewSupplier"
        
})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional: filter initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false); // avoid creating new session
        Staff staff = (session != null) ? (Staff) session.getAttribute("admin") : null;


        if (staff == null || !"admin".equalsIgnoreCase(staff.getRole())) {
            // Not logged in or not an admin
            res.sendRedirect(req.getContextPath() + "/LoginAdmin");
            return;
        }

        // Logged in as admin
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Optional: cleanup code
    }
}
