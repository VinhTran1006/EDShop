

<%@page import="dao.BrandDAO"%>
<%@page import="dao.CategoryDAO"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.List"%>
<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Product> productList = (List<Product>) request.getAttribute("productList");

//    Category category = (Category) request.getAttribute("category");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="Css/staffProductList.css">

    </head>
    <body>
        <jsp:include page="/WEB-INF/View/staff/productManagement/deleteProduct/staffDeleteProduct.jsp" />
        <div class = "" style = "width: 80%; margin-left: 18.9%">

            <%if (productList != null) {%>
            <div style="overflow-x: auto; width: 100%;">
                <table class="table table-striped table-hover">
                    <tr class = "tableRow">
                        <td class ="tieuDe" style = "text-align: center">ID</td>
                        <td class ="tieuDe" style = "text-align: center">Product Name</td>
                        <td class ="tieuDe" style = "text-align: center">Price</td>
                        <td class ="tieuDe" style = "text-align: center">Category</td>
                        <td class ="tieuDe" style = "text-align: center">Brand</td>
                        <td class ="tieuDe" style = "text-align: center">Image</td>
                        <td class ="tieuDe" style = "text-align: center">Action</td>
                    </tr>
                    <%
                        Locale localeVN = new Locale("vi", "VN");
                        NumberFormat currencyVN = NumberFormat.getInstance(localeVN);

                        CategoryDAO catedao = new CategoryDAO();
                        BrandDAO branddao = new BrandDAO();
                        for (Product product : productList) {
                            String brandname = branddao.getBrandNameByBrandId(product.getBrandID());
                            String CategoryName = catedao.getCategoryNameByCategoryId(product.getCategoryID());
                            if (product != null) {
                                String giaFormatted = "______";
                                if (product.getPrice() != null) {
                                    giaFormatted = currencyVN.format(product.getPrice());
                                }
                    %>
                    <tr>
                        <td><%= product.getProductID()%></td>
                        <td><%= product.getProductName()%></td>
                        <td><%=giaFormatted%></td>
                        <td><%= CategoryName%></td>
                        <td><%= brandname%></td>



                        <%
                            String img = product.getImageUrl1();
                        %>
                        <td>
                            <img style = "width: 80%" src = "<%= (img != null) ? img : ""%>">
                        </td>

                        <td>
                            <div class="d-flex gap-1">
                                <a style = "text-align: center" href="StaffViewProductDetail?productId=<%= product.getProductID()%>" class="btn btn-warning" style="color: white;"><i class="bi bi-tools"></i> Detail</a>
                            </div>
                        </td>
                    </tr>

                    <%

                            }
                        }
                    %>
                </table>
                <%
                    } else {
                        out.println("No Data!");
                    }
                %>
            </div>
        </div>

    </body>
</html>


<%
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>

<script>
    window.onload = function () {
    <% if ("1".equals(success)) { %>
        Swal.fire({
            icon: 'success',
            title: 'Deleted!',
            text: 'The product has been hidden.',
            timer: 2000
        });
    <% } else if ("1".equals(error)) { %>
        Swal.fire({
            icon: 'error',
            title: 'Failed!',
            text: 'Could not hide the product.',
            timer: 2000
        });
    <% }%>
    };
</script>

