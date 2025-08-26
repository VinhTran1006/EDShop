<%@page import="model.Attribute"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Attribute> attributeList = (List<Attribute>) request.getAttribute("attributeList");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Detail</title>
    </head>
    <body>
        <style>
            /* Toàn trang */
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f8f9fa;
                margin: 0;
                padding: 20px;
            }

            /* Bảng */
            .category-table {
                width: 80%;
                margin: 0 auto;
                border-collapse: collapse;
                background: #ffffff;
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }

            /* Hàng */
            .category-table tr {
                border-bottom: 1px solid #e9ecef;
                transition: background-color 0.2s ease-in-out;
            }

            .category-table tr:hover {
                background-color: #f1f7ff;
            }

            /* Cột tên */
            .category-name {
                width: 40%;
                padding: 12px 20px;
                font-weight: 600;
                color: #333;
                background-color: #f8f9fa;
                text-align: left;
                border-right: 1px solid #dee2e6;
            }

            /* Cột input */
            .attribute-values {
                padding: 12px 20px;
            }

            /* Input */
            .attribute-input {
                width: 100%;
                max-width: 300px;
                padding: 8px 12px;
                border: 1px solid #ced4da;
                border-radius: 6px;
                font-size: 14px;
                transition: border-color 0.2s ease, box-shadow 0.2s ease;
            }

            .attribute-input:focus {
                outline: none;
                border-color: #0d6efd;
                box-shadow: 0 0 4px rgba(13, 110, 253, 0.25);
            }

            /* Responsive */
            @media (max-width: 768px) {
                .category-table {
                    width: 95%;
                }

                .category-name {
                    width: 35%;
                    font-size: 14px;
                }

                .attribute-input {
                    max-width: 100%;
                    font-size: 13px;
                }
            }

        </style>
        <div style="width: 100%">
            <table class="category-table">

                <tbody id="detailGroup">
                    <%
                        if (attributeList != null && !attributeList.isEmpty()) {
                            for (Attribute a : attributeList) {
                    %>
                    <tr>
                        <td class="category-name">
                            <%= a.getAtrributeName()%>
                        </td>
                        <td class="attribute-values">
                            <input type="text"
                                   style="width: auto; max-width: 190px; background-color: none;"
                                   class="attribute-input"
                                   name="attribute_<%= a.getAttributeID()%>">
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </body>
</html>
