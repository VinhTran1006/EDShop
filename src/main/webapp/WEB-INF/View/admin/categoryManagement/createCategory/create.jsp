<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String createSuccess = (String) request.getAttribute("createSuccess");
    String createError = (String) request.getAttribute("createError");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create Category</title>
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
                                <h4>Add New Category</h4>
                            </div>
                            <div class="card-body">
                                <form action="CreateCategory" method="post">
                                    <input type="hidden" id="groupCount" name="groupCount" value="0"/>

                                    <c:if test="${not empty sessionScope.existCategory}">
                                        <div class="alert alert-danger">${sessionScope.existCategory}</div>
                                    </c:if>
                                    <c:remove var="existCategory" scope="session"/>

                                    <table class="info-table">
                                        <tr>
                                            <th>Category Name</th>
                                            <td>
                                                <c:if test="${not empty sessionScope.errorCategoryName}">
                                                    <span style="color:red">${sessionScope.errorCategoryName}</span>
                                                </c:if>
                                                <c:remove var="errorCategoryName" scope="session"/>
                                                <input type="text" id="categoryName" name="categoryName"
                                                       class="form-control supplier-input" placeholder="Enter category name...">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Image URL Logo</th>
                                            <td>
                                                <c:if test="${not empty sessionScope.errorUrl}">
                                                    <span style="color:red">${sessionScope.errorUrl}</span>
                                                </c:if>
                                                <c:remove var="errorUrl" scope="session"/>
                                                <input type="text" id="ImgURLLogo" name="ImgURLLogo"
                                                       class="form-control supplier-input" placeholder="Paste image URL here...">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Attributes</th>
                                            <td>
                                                <div id="groupWrapper"></div>
                                                <button type="button" onclick="addAttribute()" class="btn btn-secondary mt-2">
                                                    <i class="fa-solid fa-plus"></i> Add Attribute
                                                </button>
                                            </td>
                                        </tr>
                                    </table>

                                    <div class="form-controls mt-4">
                                        <button type="submit" class="btn btn-success">
                                            Add Category
                                        </button>
                                        <a href="CategoryView" class="btn btn-secondary">
                                            Cancel
                                        </a>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script>
            let groupCount = 0;

            function addAttribute() {
                groupCount++;
                document.getElementById("groupCount").value = groupCount;

                const groupDiv = document.createElement('div');
                groupDiv.classList.add('group-container', 'mt-2');

                groupDiv.innerHTML =
                        '<label>Detail Group Name:</label>' +
                        '<input type="text" name="groups[' + groupCount + '][name]" ' +
                        'class="form-control" required maxlength="500" placeholder="Enter attribute group name"/> ' +
                        '<button type="button" onclick="removeGroup(this)" class="btn btn-danger mt-1">' +
                        '<i class="fa-solid fa-trash"></i> Remove</button>';

                document.getElementById('groupWrapper').appendChild(groupDiv);
            }

            function removeGroup(btn) {
                btn.parentElement.remove();
            }

            window.onload = function () {
            <% if ("1".equals(createSuccess)) { %>
                Swal.fire({
                    icon: 'success',
                    title: 'Success!',
                    text: 'Category has been added.',
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    window.history.replaceState({}, document.title, window.location.pathname);
                });
            <% } else if ("1".equals(createError)) { %>
                Swal.fire({
                    icon: 'error',
                    title: 'Failed!',
                    text: 'Could not add category.',
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    window.history.replaceState({}, document.title, window.location.pathname);
                });
            <% }%>
            };
        </script>
    </body>
</html>
