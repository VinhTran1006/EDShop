
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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">
    </head>
    <body>
        <div class="container">
            <jsp:include page="/WEB-INF/View/admin/sideBar.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container">
                        <div class="card shadow">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-pen-to-square"></i> Update Category</h4>
                            </div>
                            <div class="card-body">
                                <form method="post" action="UpdateCategory">
                                    <!-- Hidden categoryId -->
                                    <input type="hidden" name="categoryId" value="<%= category.getCategoryId()%>">

                                    <!-- Title + error -->
                                    <div class="mb-4">
                                        <h3 class="mb-1"><%= category.getCategoryName()%></h3>
                                        <c:if test="${not empty sessionScope.existCategory}">
                                            <span style="color:red">${sessionScope.existCategory}</span>
                                        </c:if>
                                        <c:remove var="existCategory" scope="session"/>
                                    </div>

                                    <% if (category != null) {%>
                                    <!-- Category Name -->
                                    <div class="form-section mb-3">
                                        <label for="categoryName" class="form-label fw-bold">Category Name:</label>
                                        <c:if test="${not empty sessionScope.errorCategoryName}">
                                            <span style="color:red">${sessionScope.errorCategoryName}</span>
                                        </c:if>
                                        <c:remove var="errorCategoryName" scope="session"/>
                                        <input type="text"
                                               id="categoryName"
                                               name="categoryName"
                                               value="<%= category.getCategoryName()%>"
                                               class="form-control supplier-input" required/>
                                    </div>

                                    <!-- Image URL -->
                                    <div class="form-section mb-4">
                                        <label for="ImgURLLogo" class="form-label fw-bold">Image URL Logo:</label>
                                        <c:if test="${not empty sessionScope.errorUrl}">
                                            <span style="color:red">${sessionScope.errorUrl}</span>
                                        </c:if>
                                        <c:remove var="errorUrl" scope="session"/>
                                        <input type="text"
                                               id="ImgURLLogo"
                                               name="ImgURLLogo"
                                               value="<%= category.getImgUrlLogo()%>"
                                               class="form-control supplier-input" required/>
                                    </div>
                                    <% } %>

                                    <!-- Technical Specifications -->
                                    <div class="technical-specs mb-4">
                                        <h5><i class="fa-solid fa-list"></i> Technical Specifications</h5>
                                        <hr>
                                        <%
                                            int groupIndex = -1;
                                            if (attributeList != null && !attributeList.isEmpty()) {
                                                groupIndex = 0;
                                                for (Attribute a : attributeList) {
                                        %>
                                        <div class="d-flex align-items-center mb-2" id="group<%= groupIndex%>">
                                            <!-- Hidden ID -->
                                            <input type="hidden" name="attributeID" value="<%= a.getAttributeID()%>" />
                                            <!-- Editable name -->
                                            <input type="text"
                                                   name="attributeName"
                                                   value="<%= a.getAtrributeName()%>"
                                                   class="form-control me-2" required/>
                                            <!-- Delete button -->
                                            <button type="button"
                                                    class="btn btn-danger"
                                                    onclick="confirmDelete('<%= a.getAttributeID()%>', '<%= category.getCategoryId()%>')">
                                                <i class="fa-solid fa-trash"></i> Delete
                                            </button>
                                        </div>
                                        <%
                                                groupIndex++;
                                            }
                                        } else {
                                        %>
                                        <p class="text-muted">No data</p>
                                        <%
                                            }
                                        %>
                                    </div>

                                    <!-- Action buttons -->
                                    <div class="text-end">
                                        <a href="CategoryView" class="btn btn-secondary me-2">
                                            Back
                                        </a>
                                        <% if (groupIndex > -1) { %>
                                        <button type="submit" class="btn btn-success">
                                            Save
                                        </button>
                                        <% } %>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

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
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    window.history.replaceState({}, document.title, window.location.pathname);
                });
            <% } else if ("1".equals(error)) { %>
                Swal.fire({
                    icon: 'error',
                    title: 'Error!',
                    text: 'Could not update the category. Please try again later.',
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    window.history.replaceState({}, document.title, window.location.pathname);
                });
            <% }%>
            };

            function confirmDelete(attributeID, categoryId) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "This attribute will be deleted permanently.",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#3085d6',
                    confirmButtonText: 'Delete',
                    cancelButtonText: 'Cancel'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = '<%= request.getContextPath()%>/DeleteAttribute?attributeID='
                                + attributeID + '&categoryID=' + categoryId;
                    }
                });
            }
        </script>
    </body>
</html>



