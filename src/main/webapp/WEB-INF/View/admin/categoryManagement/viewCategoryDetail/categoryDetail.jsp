
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%@page import="model.Attribute"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%

    Category category = (Category) request.getAttribute("category");
    List<Attribute> attributeList = (List<Attribute>) request.getAttribute("attributeList");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Category Detail</title>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            body {
                background-color: #f5f6fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                color: #333;
            }

            .container {
                width: 90%;
                max-width: 1200px;
                margin: 30px auto;
            }

            /* Header */
            .header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 25px;
            }

            .header h1 {
                font-size: 32px;
                font-weight: 700;
                color: #2c3e50;
            }

            /* Category card */
            .category-card {
                display: flex;
                align-items: center;
                gap: 20px;
                margin-bottom: 30px;
                padding: 20px;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 3px 8px rgba(0,0,0,0.08);
            }

            .category-card img {
                width: 120px;
                height: 120px;
                object-fit: contain;
                border-radius: 8px;
                border: 1px solid #eee;
                padding: 10px;
                background: #fafafa;
            }

            .category-card h2 {
                font-size: 22px;
                font-weight: 600;
                color: #34495e;
            }

            /* Technical specs box */
            .technical-specs {
                background-color: #ffffff;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            }

            .technical-specs h2 {
                font-size: 24px;
                margin-bottom: 15px;
                font-weight: 600;
                color: #2c3e50;
                border-left: 5px solid #0d6efd;
                padding-left: 12px;
            }

            /* Table style */
            .category-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
                background: #fff;
                border-radius: 8px;
                overflow: hidden;
            }

            .category-table th,
            .category-table td {
                padding: 14px;
                border-bottom: 1px solid #e9ecef;
                text-align: left;
                font-size: 15px;
            }

            .category-table tr:last-child td {
                border-bottom: none;
            }

            /* Group header */
            .group-header {
                background-color: #f1f3f5;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .group-header:hover {
                background-color: #e9ecef;
            }

            .group-header h2 {
                margin: 0;
                font-size: 18px;
                font-weight: 600;
                color: #495057;
            }

            .arrow-icon {
                font-size: 18px;
                margin-left: 10px;
                transition: transform 0.3s ease;
            }

            /* Khi mở thì quay mũi tên */
            .group-header.open .arrow-icon {
                transform: rotate(180deg);
            }

            /* No data */
            .no-data-message {
                text-align: center;
                padding: 15px;
                font-size: 15px;
                color: #888;
            }

        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>Category Detail</h1>
            </div>

            <!-- Category Cards -->
            <div class="category-card">
                <%
                    if (category != null) {

                        if (category.getIsActive()) {

                %>

                <img src="<%= category.getImgUrlLogo()%>" alt="Category Logo">
                <h2><%= category.getCategoryName()%></h2>

                <%
                        }
                    }
                %>
            </div>

            <div class="technical-specs">
                <h2>Technical Specifications</h2>
                <table class="category-table">
                    <tbody>
                        <%
                            if (attributeList != null) {
                                int groupIndex = 0;
                                for (Attribute a : attributeList) {
                        %>
                        <tr class="group-header" id="group<%= groupIndex%>">
                            <td colspan="2">
                                <div style="display: flex; justify-content: space-between; align-items: center;">
                                    <h2><%= a.getAtrributeName()%></h2>

                                </div>
                            </td>
                        </tr>
                        <%
                                groupIndex++;
                            }
                        } else {
                        %>
                        <tr><td colspan="2" class="no-data-message">No data</td></tr>
                        <%
                            }
                        %>

                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
