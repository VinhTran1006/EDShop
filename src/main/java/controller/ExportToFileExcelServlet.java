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
import org.apache.poi.ss.usermodel.*;


@WebServlet("/ExportToFileExcelServlet")
public class ExportToFileExcelServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy param filter
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

        // 2. Convert Date → Timestamp
        Timestamp fromTs = (from != null) ? new Timestamp(from.getTime()) : null;
        Timestamp toTs = (to != null) ? new Timestamp(to.getTime()) : null;

        // 3. Lấy dữ liệu
        ImportStockDAO stockDAO = new ImportStockDAO();
        ImportStockDetailDAO detailDAO = new ImportStockDetailDAO();

        List<ImportStock> importStocks = stockDAO.getImportHistoryFiltered(fromTs, toTs, supplierId);

        Map<Integer, ImportStock> importStockMap = new HashMap<>();
        List<ImportStockDetail> allDetails = new ArrayList<>();
        for (ImportStock imp : importStocks) {
            importStockMap.put(imp.getImportID(), imp);
            allDetails.addAll(detailDAO.getDetailsByImportId(imp.getImportID()));
        }

        // 4. Xuất Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ImportStockDetailReport.xlsx");

        try ( XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Import Stock Details");

            // ===== Style =====
            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Cell style (normal text)
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            // Number style
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.cloneStyleFrom(textStyle);
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            // Currency style
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.cloneStyleFrom(textStyle);
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0 \"₫\""));

            // Date style
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(textStyle);
            dateStyle.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm"));

            // ===== Header =====
            String[] headers = {
                "No.", "Import ID", "Import Date", "Supplier Name",
                "Product ID", "Product Name", "Quantity", "Unit Price",
                "Total Price", "Quantity Left", "Staff Name"
            };

            int rowNo = 0;
            Row row = sheet.createRow(rowNo++);
            row.setHeightInPoints(25);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== Data =====
            int stt = 1;
            for (ImportStockDetail detail : allDetails) {
                ImportStock imp = importStockMap.get(detail.getImportID());
                Product prod = detail.getProduct();
                Suppliers sup = (imp != null) ? imp.getSupplier() : null;

                row = sheet.createRow(rowNo++);
                int cellNum = 0;

                // No.
                Cell c0 = row.createCell(cellNum++);
                c0.setCellValue(stt++);
                c0.setCellStyle(numberStyle);

                // Import ID
                Cell c1 = row.createCell(cellNum++);
                c1.setCellValue(detail.getImportID());
                c1.setCellStyle(numberStyle);

                // Import Date
                Cell c2 = row.createCell(cellNum++);
                if (imp != null && imp.getImportDate() != null) {
                    c2.setCellValue(imp.getImportDate());
                    c2.setCellStyle(dateStyle);
                } else {
                    c2.setCellValue("");
                    c2.setCellStyle(textStyle);
                }

                // Supplier Name
                Cell c3 = row.createCell(cellNum++);
                c3.setCellValue(sup != null ? sup.getName() : "");
                c3.setCellStyle(textStyle);

                // Product ID
                Cell c4 = row.createCell(cellNum++);
                c4.setCellValue(prod != null ? prod.getProductID() : 0);
                c4.setCellStyle(numberStyle);

                // Product Name
                Cell c5 = row.createCell(cellNum++);
                c5.setCellValue(prod != null ? prod.getProductName() : "");
                c5.setCellStyle(textStyle);

                // Quantity
                Cell c6 = row.createCell(cellNum++);
                c6.setCellValue(detail.getStock());
                c6.setCellStyle(numberStyle);

                // Unit Price
                Cell c7 = row.createCell(cellNum++);
                c7.setCellValue(detail.getUnitPrice().doubleValue());
                c7.setCellStyle(currencyStyle);

                // Total Price
                BigDecimal total = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getStock()));
                Cell c8 = row.createCell(cellNum++);
                c8.setCellValue(total.doubleValue());
                c8.setCellStyle(currencyStyle);

                // Quantity Left
                Cell c9 = row.createCell(cellNum++);
                c9.setCellValue(detail.getStockLeft());
                c9.setCellStyle(numberStyle);

                // Staff Name
                Cell c10 = row.createCell(cellNum++);
                c10.setCellValue(imp != null ? imp.getFullName() : "");
                c10.setCellStyle(textStyle);
            }

            // ===== Auto size =====
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // Thêm padding cho đẹp
                int currentWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, currentWidth + 1000);
            }

            // 5. Xuất file
            workbook.write(response.getOutputStream());
        }
    }

}
