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
import model.Account;

@WebFilter(urlPatterns = {
    "/StaffDashboard",
    "/ImportStock",
    "/ImportStockHistory",
    "/ViewOrderList",
    "/ViewOrderDetail",
    "/CustomerList",
    "/AssignVoucher",
    "/ImportStatistic",
    "/ImportHistoryDetail",
    "/ViewListNewFeedback",
    "/ViewFeedBackForStaff"
})
public class StaffFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Khởi tạo filter nếu cần
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // false -> không tạo mới nếu chưa có

        // Chưa login
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/LoginStaff");
            return;
        }

        // Lấy account để kiểm tra role
        Staff staff = (Staff) session.getAttribute("user");
        if (acc == null || acc.getRole().equalsIgnoreCase("staff") == false) {
            res.sendRedirect(req.getContextPath() + "/LoginStaff");
            return;
        }

        // Nếu pass hết → cho đi tiếp
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Xóa tài nguyên nếu cần
    }
}
