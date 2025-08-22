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
        <link rel="stylesheet" href="Css/productDetail1.css">
    </head>
    <body>
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
