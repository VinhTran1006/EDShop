
<%@page import="model.Attribute"%>
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Attribute> attributeList = (List<Attribute>) request.getAttribute("attributeList");
    Category category = (Category) request.getAttribute("category");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="Css/categoryDetail.css">
        <link rel="stylesheet" href="Css/updateCategory2.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body>
        <form method="post" action="UpdateCategory">
            <!-- Truyền categoryId ẩn -->
            <input type="hidden" name="categoryId" value="<%= category.getCategoryId()%>">
            <div style="display: flex; align-items: center; gap: 10px; margin-left: 21.5%; margin-bottom: 3%;  margin-top: 3%">
                <h1 class = "display-5 fw-bold" style="font-size: 320%; margin: 0;">Category</h1>
                <span style="font-size: 120%; color: gray; margin-top: 4%;">Update Category</span>
            </div>

            <!--            <== Category name==>-->
            <% if (category != null) {%>
            <div class="divCategoryInput">
                <div class="form-row" style = "margin-bottom: 2%">
                    <label for="categoryName_<%= category.getCategoryId()%>">Category Name:</label>
                    <input type="text"
                           id="categoryName"
                           name="categoryName"
                           value="<%= category.getCategoryName()%>"
                           class="input-category" required/>
                </div>

                <div class="form-row">
                    <label for="ImgURLLogo<%= category.getCategoryId()%>">ImgURLLogo:</label>
                    <input type="text"
                           id="ImgURLLogo:"
                           name="ImgURLLogo:"
                           value="<%= category.getImgUrlLogo()%>"
                           class="input-category" required/>
                </div>
            </div>
            <% } %>


            <div class="category-container" style = "width: 75%; margin-top: 1%">
                <h2 style = "font-size: 140%">Technical Specifications</h2>
                <hr>
                <%
                    int groupIndex = -1;
                    if (attributeList != null && !attributeList.isEmpty()) {
                        groupIndex = 0;
                        for (Attribute a : attributeList) {
                %>

                <div class="group-header" id="group<%= groupIndex%>">
                    <!-- ID ẩn để gửi về Servlet -->
                    <input type="hidden" name="attributeID" value="<%= a.getAttributeID()%>" />

                    <!-- Tên nhóm có thể chỉnh sửa -->
                    <input type="text"
                           name="attributeName"
                           value="<%= a.getAtrributeName()%>"
                           class="group-name-input" required/>
                    <button class="btn btn-danger" onclick="confirmDelete(<%= a.getAttributeID()%>)">Delete</button>
                </div>
                <%
                        groupIndex++;
                    }
                %>

                <%
                } else {
                %>
                <p class="no-data-message">No data</p>
                <%
                    }

                %>
            </div>
            <%  if (groupIndex > -1) {
            %>
            <a style = "margin-left: 81%;" href="CategoryView" class="btn btn-secondary" id="back"><i class="bi bi-arrow-return-left"></i> Back</a>
            <button type="submit" class="btn btn-primary" id="submit">
                <i class="fas fa-edit"></i> Save
            </button>

            <%
            } else {
            %>
            <a style = "margin-left: 55%;" href="CategoryView" class="btn btn-secondary" id="back"><i class="bi bi-arrow-return-left"></i> Back</a>
            <%
                }
            %>
        </form>



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
            title: 'Update Successful!',
            text: 'Your changes have been saved successfully.',
            showConfirmButton: true,
            confirmButtonText: 'OK',
            timer: 3000
        });
    <% } else if ("1".equals(error)) { %>
        Swal.fire({
            icon: 'error',
            title: 'Error Occurred!',
            text: 'The update could not be completed. Please try again later.',
            showConfirmButton: true,
            confirmButtonText: 'Try Again',
            timer: 3000
        });
    <% }%>
    };
</script>



