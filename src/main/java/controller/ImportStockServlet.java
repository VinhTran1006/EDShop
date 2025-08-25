package controller;

import dao.ImportStockDAO;
import dao.ImportStockDetailDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import model.ImportStock;
import model.ImportStockDetail;
import model.Product;
import model.Staff;
import model.Suppliers;

@WebServlet(name = "ImportStockServlet", urlPatterns = {"/ImportStock"})
public class ImportStockServlet extends HttpServlet {

    SupplierDAO supplierDAO = new SupplierDAO();
    ProductDAO productDAO = new ProductDAO();
    ImportStockDAO importStockDAO = new ImportStockDAO();
    ImportStockDetailDAO importStockDetailDAO = new ImportStockDetailDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImportStatisticServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ImportStatisticServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String status = request.getParameter("status");
        if (status != null && status.equals("cancel")) {
            session.removeAttribute("selectedProducts");
            session.setAttribute("success", "Stock import completed successfully!");
            response.sendRedirect("ImportStock?success=imported");
            return;
        }

        List<Suppliers> suppliers = supplierDAO.getAllSuppliers();
        request.setAttribute("suppliers", suppliers);
        session.setAttribute("suppliers", suppliers);

        ArrayList<Product> products = (ArrayList<Product>) session.getAttribute("products");
        if (products == null) {
            products = (ArrayList<Product>) productDAO.getProductListAdmin();
            session.setAttribute("products", products);
        }

        request.getRequestDispatcher("/WEB-INF/View/staff/stockManagement/importStock.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // --- Lấy session ---
        List<ImportStockDetail> detailList = (List<ImportStockDetail>) session.getAttribute("selectedProducts");
        if (detailList == null) {
            detailList = new ArrayList<>();
        }

        List<Product> products = (List<Product>) session.getAttribute("products");
        if (products == null) {
            products = productDAO.getProductListAdmin();
            session.setAttribute("products", products);
        }

        // --- Chọn supplier ---
        String supplierIdRaw = request.getParameter("supplierId");
        if (supplierIdRaw != null && !supplierIdRaw.trim().isEmpty()) {
            try {
                int supplierId = Integer.parseInt(supplierIdRaw.trim());
                Suppliers supplier = supplierDAO.getSupplierById(supplierId);
                session.setAttribute("supplier", supplier);

                session.setAttribute("products", productDAO.getProductListAdmin());
                session.setAttribute("selectedProducts", new ArrayList<ImportStockDetail>());
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid supplier ID.");
            }
            response.sendRedirect("ImportStock");
            return;
        }

// --- Thêm sản phẩm ---
        String productIdRaw = request.getParameter("productId");
        String quantityRaw = request.getParameter("importQuantity");
        String priceRaw = request.getParameter("unitPrice");

        if (productIdRaw != null && quantityRaw != null && priceRaw != null
                && !productIdRaw.trim().isEmpty() && !quantityRaw.trim().isEmpty() && !priceRaw.trim().isEmpty()) {
            try {
                int productId = Integer.parseInt(productIdRaw.trim());
                int quantity = Integer.parseInt(quantityRaw.trim());

                // Parse price an toàn
                String normalizedPrice = priceRaw.replaceAll("[^\\d.]", "").trim();
                BigDecimal price = new BigDecimal(normalizedPrice);

                Product product = productDAO.getProductByID(productId);

                // Validate import price < 90% sale price
                if (product.getPrice() != null) {
                    BigDecimal maxImportPrice = product.getPrice().multiply(BigDecimal.valueOf(0.9));
                    if (price.compareTo(maxImportPrice) >= 0) {
                        session.setAttribute("error", "Import price must be less than 90% of sale price ("
                                + maxImportPrice.toPlainString() + " VND).");

                        // luôn tính lại totalAmount
                        BigDecimal totalAmount = detailList.stream()
                                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getStock())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        session.setAttribute("totalAmount", totalAmount);
                        response.sendRedirect("ImportStock");
                        return;
                    }
                }

                // Kiểm tra trùng
                boolean isContained = detailList.stream().anyMatch(d -> d.getProductID() == productId);

                if (!isContained) {
                    ImportStockDetail detail = new ImportStockDetail();
                    detail.setProduct(product);
                    detail.setProductID(productId);
                    detail.setStock(quantity);
                    detail.setStockLeft(quantity);
                    detail.setUnitPrice(price);

                    detailList.add(detail);
                    products.removeIf(p -> p.getProductID() == productId);

                    session.setAttribute("selectedProducts", detailList);
                    session.setAttribute("products", products);

                    // ✅ luôn tính lại totalAmount sau khi thêm
                    BigDecimal totalAmount = detailList.stream()
                            .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getStock())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    session.setAttribute("totalAmount", totalAmount);

                } else {
                    session.setAttribute("error", "Product already selected.");
                }

                response.sendRedirect("ImportStock");
                return;

            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid input value.");
                response.sendRedirect("ImportStock");
                return;
            }
        }

// --- Xử lý action delete/save ---
        String action = request.getParameter("action");
        if (action != null) {
            try {
                if ("delete".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    Iterator<ImportStockDetail> iterator = detailList.iterator();
                    while (iterator.hasNext()) {
                        ImportStockDetail d = iterator.next();
                        if (d.getProduct().getProductID() == productId) {
                            products.add(productDAO.getProductByID(productId));
                            iterator.remove();
                            break;
                        }
                    }
                } else if ("save".equals(action)) {
                    int productId = Integer.parseInt(request.getParameter("productEditedId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    String normalizedPrice = request.getParameter("price").replaceAll("[^\\d.]", "").trim();
                    BigDecimal price = new BigDecimal(normalizedPrice);

                    Product product = productDAO.getProductByID(productId);

                    // Validate lại rule < 90% sale price
                    if (product.getPrice() != null) {
                        BigDecimal maxImportPrice = product.getPrice().multiply(BigDecimal.valueOf(0.9));
                        if (price.compareTo(maxImportPrice) >= 0) {
                            session.setAttribute("error", "Import price must be less than 90% of sale price ("
                                    + maxImportPrice.toPlainString() + " VND).");
                            response.sendRedirect("ImportStock");
                            return;
                        }
                    }

                    for (ImportStockDetail d : detailList) {
                        if (d.getProduct().getProductID() == productId) {
                            d.setStock(quantity);
                            d.setStockLeft(quantity);
                            d.setUnitPrice(price);
                            break;
                        }
                    }
                }

                // Sắp xếp
                detailList.sort(Comparator.comparingInt(ImportStockDetail::getProductID));
                products.sort(Comparator.comparingInt(Product::getProductID));

                session.setAttribute("selectedProducts", detailList);
                session.setAttribute("products", products);

                // tính lại totalAmount sau mỗi thao tác
                BigDecimal totalAmount = detailList.stream()
                        .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getStock())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                session.setAttribute("totalAmount", totalAmount);

                response.sendRedirect("ImportStock");
                return;

            } catch (NumberFormatException e) {
                session.setAttribute("error", "Cannot process product data.");
                response.sendRedirect("ImportStock");
                return;
            }
        }

        // --- Lưu import stock vào DB ---
        Suppliers supplier = (Suppliers) session.getAttribute("supplier");
        if (supplier != null && !detailList.isEmpty()) {

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (ImportStockDetail d : detailList) {
                totalAmount = totalAmount.add(d.getUnitPrice().multiply(BigDecimal.valueOf(d.getStock())));
            }

            Staff staff = (Staff) session.getAttribute("staff");
            int staffId = (staff != null) ? staff.getStaffID() : 0;

            // --- Tạo phiếu nhập ---
            ImportStock importStock = new ImportStock(staffId, supplier.getSupplierID(), totalAmount);

            int importId = importStockDAO.createImportStock(importStock);

            if (importId <= 0) {
                session.setAttribute("error", "Cannot create import stock receipt.");
                response.sendRedirect("ImportStock");
                return;
            }

            // --- Lưu chi tiết nhập và cập nhật tồn kho ---
            for (ImportStockDetail d : detailList) {
                d.setImportID(importId);
                importStockDetailDAO.createImportStockDetail(d);

                Product p = d.getProduct();
                int newStock = p.getQuantity() + d.getStock();
                productDAO.increaseStock(p.getProductID(), newStock);
            }

            importStockDAO.importStock(importId);

            session.removeAttribute("selectedProducts");
            session.removeAttribute("supplier");
            session.removeAttribute("products");

            response.sendRedirect("ImportStock?success=imported");
            return;
        }

        response.sendRedirect("ImportStock?error=1");
    }

    @Override
    public String getServletInfo() {
        return "Import stock controller";
    }
}
