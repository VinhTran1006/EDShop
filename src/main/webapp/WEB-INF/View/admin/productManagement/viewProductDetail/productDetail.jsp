
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
                                        
                                    %> 
                                </td>
                            </tr>
                            <%
                                } // end for
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
    .btn-back {
        color: #fff;
        background-color: #6c757d;
        border: 1px solid #6c757d;
        padding: 10px 20px;
        border-radius: 6px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .btn-back:hover {
        background-color: #5c636a;
        border-color: #565e64;
    }

    .btn-success, .btn-back {
        padding: 8px 16px;  /* trước là 10px 20px */
        font-size: 14px;
    }
</style>
