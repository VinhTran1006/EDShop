
<%@page import="model.Attribute"%>
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                <c:if test="${not empty sessionScope.existCategory}">
                    <span style="color:red">${sessionScope.existCategory}</span>
                </c:if>
                <c:remove var="existCategory" scope="session"/>
            </div>


            <!--            <== Category name==>-->
            <% if (category != null) {%>
            <div class="divCategoryInput">
                <div class="form-row" style = "margin-bottom: 2%">
                    <label for="categoryName_<%= category.getCategoryId()%>">Category Name:</label>
                    <c:if test="${not empty sessionScope.errorCategoryName}">
                        <span style="color:red">${sessionScope.errorCategoryName}</span>
                    </c:if>
                    <c:remove var="errorCategoryName" scope="session"/>
                    <input type="text"
                           id="categoryName"
                           name="categoryName"
                           value="<%= category.getCategoryName()%>"
                           class="input-category" required/>
                </div>

                <div class="form-row">
                    <label for="ImgURLLogo<%= category.getCategoryId()%>">ImgURLLogo:</label>
                    <c:if test="${not empty sessionScope.errorUrl}">
                        <span style="color:red">${sessionScope.errorUrl}</span>
                    </c:if>
                    <c:remove var="errorUrl" scope="session"/>
                    <input type="text"
                           id="ImgURLLogo:"
                           name="ImgURLLogo"
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
                    <a href="<%= request.getContextPath()%>/DeleteAttribute?attributeID=<%= a.getAttributeID()%>&categoryID=<%= category.getCategoryId()%>" 
                       class="btn btn-danger">Delete</a>




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
            <button type="submit" class="btn btn-primary" id="submit">
                <i class="fas fa-edit"></i> Add Attribute
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

    window.onload = function () {
    <% if ("1".equals(success)) { %>
        Swal.fire({
            icon: 'success',
            title: 'Deleted!',
            text: 'The category has been updated.',
            timer: 2000
        }).then(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        });
    <% } else if ("1".equals(error)) { %>
        Swal.fire({
            icon: 'error',
            title: 'Failed!',
            text: 'Could not update the category.',
            timer: 2000
        }).then(() => {
            window.history.replaceState({}, document.title, window.location.pathname);
        });
    <% }%>
    };

    function confirmDelete(attributeID, categoryId) {
        Swal.fire({
            title: 'Are you sure?',
            text: "This category has been deleted.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Delete',
            cancelButtonText: 'Cancel'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = '<%= request.getContextPath()%>/DeleteAttribute?attributeID=' + attributeID + '&categoryID=' + categoryId;
            }
        });
    }
</script>



