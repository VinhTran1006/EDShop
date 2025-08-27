
<%@page import="model.Attribute"%>
<%@page import="model.Product"%>
<%@page import="model.ProductDetail"%>

<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Attribute> attributeList = (List<Attribute>) request.getAttribute("attributeList");
    List<ProductDetail> productDetailList = (List<ProductDetail>) request.getAttribute("productDetailList");
    Product product = (Product) request.getAttribute("product");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link rel="stylesheet" href="Css/productDetail1.css">
    </head>
    <body>


        <div style = "display: flex">

        </div>
        <div class = "container col-md-12" style = "background-color: #FFFFFF; border-radius: 15px;">
            <div class = "row">
                <div class="col-md-12">
                    <table class="category-table">


                        <tbody>
                            <%
                                if (attributeList != null && !attributeList.isEmpty()) {
                                    for (Attribute a : attributeList) {
                                        boolean hasValue = false;
                            %>
                            <tr>
                                <td class="category-name">
                                    <%= a.getAtrributeName()%>
                                </td>
                                <td class="attribute-values">
                                    <%
                                        if (productDetailList != null) {
                                            for (ProductDetail proDetail : productDetailList) {
                                                if (proDetail.getAttibuteID() == a.getAttributeID()) {
                                                    hasValue = true;
                                    %>
                                    <div class="attribute-item"><%= proDetail.getAttributeValue()%></div>
                                    <%
                                                }
                                            }
                                        }
                                        if (!hasValue) {
                                    %>
                                    <div class="attribute-item">No data</div>
                                    <%
                                        }
                                    %> 
                                </td>

                            </tr>
                            <%                                } // end for
                            } else {
                            %>
                            <tr>
                                <td colspan="2">No attributes available</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>



                    </table>
                </div>
            </div>
        </div>

    </body>
</html>

<script>
    function toggleDetails(index) {
        const detailGroup = document.getElementById("detailGroup" + index);
        const arrowIcon = document.getElementById("arrow" + index);

        if (detailGroup.classList.contains("hidden")) {
            detailGroup.classList.remove("hidden");
            arrowIcon.innerText = "▲"; // hoặc dùng ▾ nếu thích
        } else {
            detailGroup.classList.add("hidden");
            arrowIcon.innerText = "▼";
        }
    }
</script>

<style>
    /* Reset */
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }

    body {
        font-family: "Segoe UI", Tahoma, sans-serif;
        background-color: #F2F4F7;
        color: #333;
    }

    /* Container chính */
    .divAll {
        background-color: #F2F4F7;
        min-height: 100vh;
        padding: 20px;
    }

    .container {
        background: #fff;
        padding: 24px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        margin-bottom: 20px;
    }

    /* --- Title --- */
    h1.display-5 {
        font-size: 2rem;
        color: #222;
    }

    h3 {
        margin-top: 12px;
        font-size: 1.25rem;
        font-weight: 600;
        color: #444;
    }

    /* --- Bảng thông số kỹ thuật --- */
    .category-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 16px;
        background: #fff;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    }

    .category-table tr {
        border-bottom: 1px solid #eee;
    }
    .category-table tr:last-child {
        border-bottom: none;
    }

    .category-table td {
        padding: 10px 14px;
        font-size: 14px;
        vertical-align: middle;
        color: #333;
    }

    .category-name {
        width: 35%;
        font-weight: 600;
        color: #222;
        background: #f3f4f6;
    }

    .attribute-item {
        display: inline-block;
        padding: 4px 8px;
        margin: 2px;
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        border-radius: 6px;
        font-size: 13px;
    }

    /* --- Buttons --- */
    .btn-back {
        color: #fff !important;
        background-color: #6c757d;
        border: none;
        padding: 8px 18px;
        border-radius: 6px;
        font-size: 14px;
        text-decoration: none;
        transition: all 0.3s ease;
    }
    .btn-back:hover {
        background-color: #5c636a;
        transform: translateY(-2px);
    }
    .category-table td .attribute-item{
        color: #000 !important;
    }

</style>
