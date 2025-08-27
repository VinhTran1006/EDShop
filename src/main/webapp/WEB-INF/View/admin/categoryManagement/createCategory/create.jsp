<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String createSuccess = (String) request.getAttribute("createSuccess");
    String createError = (String) request.getAttribute("createError");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create Category</title>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            body {
                background: linear-gradient(135deg, #e9f1ff, #f8f9fa);
                font-family: 'Segoe UI', sans-serif;
                margin: 0;
                padding: 0;
            }

            .container {
                padding: 30px;
            }

            .card {
                border-radius: 15px;
                box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
                border: none;
                max-width: 800px;
                margin: 0 auto;
                background: #fff;
            }

            .card-header {
                border-radius: 15px 15px 0 0 !important;
                padding: 1rem 1.5rem;
                background: linear-gradient(90deg, #0d6efd, #007bff);
                color: white;
                font-size: 1.3rem;
                font-weight: 600;
                text-align: center;
            }

            .card-body {
                padding: 2rem;
            }

            .form-section {
                margin-bottom: 2rem;
            }

            .section-title {
                color: #212529;
                border-left: 4px solid #0d6efd;
                padding-left: 10px;
                margin-bottom: 1rem;
                font-weight: 600;
                font-size: 1.1rem;
            }

            .form-label {
                font-weight: 600;
                margin-bottom: 0.25rem;
                display: block;
            }

            .form-control {
                border-radius: 8px;
                padding: 0.6rem 0.8rem;
                border: 1px solid #ced4da;
                transition: all 0.2s;
                width: 100%;
            }

            .form-control:focus {
                border-color: #0d6efd;
                box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
                outline: none;
            }

            .btn {
                border-radius: 8px;
                padding: 0.6rem 1.5rem;
                font-weight: 500;
                transition: all 0.2s;
                cursor: pointer;
            }

            .btn-success {
                background-color: #28a745;
                border: none;
                color: white;
            }

            .btn-success:hover {
                background-color: #218838;
            }

            .btn-secondary {
                background-color: #6c757d;
                border: none;
                color: white;
            }

            .btn-secondary:hover {
                background-color: #5a6268;
            }

            .btn-danger {
                background-color: #dc3545;
                border: none;
                color: white;
                margin-left: 10px;
                padding: 0.5rem 1rem;
            }

            .btn-danger:hover {
                background-color: #c82333;
            }

            .group-container {
                background: #f8f9fa;
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 15px;
                margin-bottom: 15px;
            }

            .group-container label {
                font-weight: 600;
                margin-bottom: 5px;
                display: block;
            }

            .text-end {
                text-align: right;
            }

            span[style*="color:red"] {
                display: block;
                margin-bottom: 5px;
                font-size: 0.9rem;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="card shadow">
                <div class="card-header">
                    <h4 class="mb-0">➕ Add New Category</h4>
                </div>
                <div class="card-body">
                    <form action="CreateCategory" method="post">
                        <input type="hidden" id="groupCount" name="groupCount" value="0"/>

                        <c:if test="${not empty sessionScope.existCategory}">
                            <span style="color:red">${sessionScope.existCategory}</span>
                        </c:if>
                        <c:remove var="existCategory" scope="session"/>

                        <div class="form-section">
                            <h5 class="section-title">Category Name</h5>
                            <c:if test="${not empty sessionScope.errorCategoryName}">
                                <span style="color:red">${sessionScope.errorCategoryName}</span>
                            </c:if>
                            <c:remove var="errorCategoryName" scope="session"/>
                            <input type="text" id="categoryName" name="categoryName"
                                   class="form-control supplier-input" placeholder="Enter category name...">
                        </div>

                        <div class="form-section">
                            <h5 class="section-title">Image URL Logo</h5>
                            <c:if test="${not empty sessionScope.errorUrl}">
                                <span style="color:red">${sessionScope.errorUrl}</span>
                            </c:if>
                            <c:remove var="errorUrl" scope="session"/>
                            <input type="text" id="ImgURLLogo" name="ImgURLLogo"
                                   class="form-control supplier-input" placeholder="Paste image URL here...">
                        </div>

                        <div class="form-section">
                            <h5 class="section-title">Attributes</h5>
                            <div id="groupWrapper"></div>
                            <button type="button" onclick="addAttribute()" class="btn btn-secondary mt-2">+ Add Attribute</button>
                        </div>

                        <div class="text-end mt-4">
                            <button type="submit" class="btn btn-success me-2">✅ Add Category</button>
                            <a href="CategoryView" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script>
            let groupCount = 0;

            function addAttribute() {
                groupCount++;
                document.getElementById("groupCount").value = groupCount;

                const groupDiv = document.createElement('div');
                groupDiv.classList.add('group-container');

                groupDiv.innerHTML =
                        '<label>Detail Group Name:</label>' +
                        '<input type="text" name="groups[' + groupCount + '][name]" ' +
                        'class="form-control" required maxlength="500" placeholder="Enter attribute group name"/> ' +
                        '<button type="button" onclick="removeGroup(this)" class="btn btn-danger">❌ Remove</button>';

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
