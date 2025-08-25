package controller;

import dao.ImportStockDAO;
import dao.ImportStockDetailDAO;
import model.ImportStock;
import model.ImportStockDetail;
import model.Product;
import model.Suppliers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/ExportToFileExcelServlet")
public class ExportToFileExcelServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy param từ form filter (tương tự JSP)
        String fromStr = request.getParameter("from");
        String toStr = request.getParameter("to");
        String supplierIdStr = request.getParameter("supplierId");

        Date from = null;
        Date to = null;
        Integer supplierId = null;

        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (fromStr != null && !fromStr.isEmpty()) {
                from = sdfInput.parse(fromStr);
            }
            if (toStr != null && !toStr.isEmpty()) {
                to = sdfInput.parse(toStr);
            }
            if (supplierIdStr != null && !supplierIdStr.isEmpty()) {
                supplierId = Integer.parseInt(supplierIdStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Convert Date → Timestamp cho DAO
        Timestamp fromTs = (from != null) ? new Timestamp(from.getTime()) : null;
        Timestamp toTs = (to != null) ? new Timestamp(to.getTime()) : null;

        // 3. Lấy danh sách import stock đã filter giống JSP
        ImportStockDAO stockDAO = new ImportStockDAO();
        ImportStockDetailDAO detailDAO = new ImportStockDetailDAO();

        List<ImportStock> importStocks = stockDAO.getImportHistoryFiltered(fromTs, toTs, supplierId);

        // 4. Lấy tất cả chi tiết từ import stock
        Map<Integer, ImportStock> importStockMap = new HashMap<>();
        List<ImportStockDetail> allDetails = new ArrayList<>();
        for (ImportStock imp : importStocks) {
            importStockMap.put(imp.getImportID(), imp);
            allDetails.addAll(detailDAO.getDetailsByImportId(imp.getImportID()));
        }

        // 5. Tạo Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ImportStockDetailReport.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("ImportStockDetails");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 6. Header
        String[] headers = {"No.", "Import ID", "Import Date", "Supplier Name", "Product ID",
            "Product Name", "Quantity", "Unit Price", "Total Price", "Quantity Left", "Staff Name"};

        int rowNo = 0;
        XSSFRow row = sheet.createRow(rowNo++);
        int cellNum = 0;
        for (String header : headers) {
            row.createCell(cellNum++).setCellValue(header);
        }

        // 7. Data
        int stt = 1;
        for (ImportStockDetail detail : allDetails) {
            ImportStock imp = importStockMap.get(detail.getImportID());
            Product prod = detail.getProduct();
            Suppliers sup = (imp != null) ? imp.getSupplier() : null;

            cellNum = 0;
            row = sheet.createRow(rowNo++);

            row.createCell(cellNum++).setCellValue(stt++);
            row.createCell(cellNum++).setCellValue(detail.getImportID());
            row.createCell(cellNum++).setCellValue(imp != null && imp.getImportDate() != null ? sdf.format(imp.getImportDate()) : "");
            row.createCell(cellNum++).setCellValue(sup != null ? sup.getName() : "");
            row.createCell(cellNum++).setCellValue(prod != null ? String.valueOf(prod.getProductID()) : "");
            row.createCell(cellNum++).setCellValue(prod != null ? prod.getProductName() : "");
            row.createCell(cellNum++).setCellValue(detail.getStock());
            row.createCell(cellNum++).setCellValue(detail.getUnitPrice().doubleValue());
            BigDecimal total = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getStock()));
            row.createCell(cellNum++).setCellValue(total.doubleValue());
            row.createCell(cellNum++).setCellValue(detail.getStockLeft());
            row.createCell(cellNum++).setCellValue(imp != null ? imp.getFullName() : "");
        }

        // 8. Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 9. Ghi ra response
        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
