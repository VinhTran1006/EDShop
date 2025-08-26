

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
        <div class = "container col-md-12" style = "background-color: #FFFFFF; border-radius: 15px;">
            <div class = "row">
                <div class="col-md-12">
                    <table class="category-table">
                        <tbody id="detailGroup">
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
                                    <input type="text" 
                                           class="attribute-input"
                                           name="attribute_<%= proDetail.getAttibuteID()%>" 
                                           value="<%= proDetail.getAttributeValue()%>" 
                                           oninput="this.size = this.value.length || 1;" required>

                                    <%
                                                }
                                            }
                                        }
                                        if (!hasValue) {
                                    %>
                                    <input type="text"
                                           style="width: auto; max-width: 190px; background-color: none;"
                                           class="attribute-input"
                                           name="attribute_<%= a.getAttributeID()%>">
                                    <%
                                        }
                                    %>
                                </td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </body>
</html>
