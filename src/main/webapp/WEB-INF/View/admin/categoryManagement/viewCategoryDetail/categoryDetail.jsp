
<%@page import="java.util.List"%>
<%@page import="model.Category"%>
<%@page import="model.Attribute"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%

    Category category = (Category) request.getAttribute("category");
    List<Attribute> attributeList = (List<Attribute>) request.getAttribute("attributeList");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Category Detail</title>
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
                    <div class="container mt-5">
                        <div class="card shadow">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-folder-open"></i> Category Detail</h4>
                            </div>
                            <div class="card-body">
                                <!-- Category Info -->
                                <div class="text-center mb-4">
                                    <%
                                        if (category != null && category.getIsActive()) {
                                    %>
                                    <img src="<%= category.getImgUrlLogo()%>" 
                                         alt="Category Logo" 
                                         class="img-thumbnail mb-3" 
                                         style="max-width: 150px;">
                                    <h3><%= category.getCategoryName()%></h3>
                                    <%
                                        }
                                    %>
                                </div>

                                <!-- Technical Specs -->
                                <div class="technical-specs">
                                    <h5><i class="fa-solid fa-list"></i> Technical Specifications</h5>
                                    <table class="info-table">
                                        <tbody>
                                            <%
                                                if (attributeList != null && !attributeList.isEmpty()) {
                                                    int groupIndex = 0;
                                                    for (Attribute a : attributeList) {
                                            %>
                                            <tr class="group-header" id="group<%= groupIndex%>">
                                                <td colspan="2">
                                                    <strong><%= a.getAtrributeName()%></strong>
                                                </td>
                                            </tr>
                                            <%
                                                    groupIndex++;
                                                }
                                            } else {
                                            %>
                                            <tr>
                                                <td colspan="2" class="no-data-message text-center text-muted">
                                                    No data available
                                                </td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                        </tbody>
                                    </table>
                                </div>

                                <div class="form-controls mt-4">
                                    <a href="CategoryView" class="btn btn-secondary">
                                        <i class="fa-solid fa-arrow-left"></i> Back to Category List
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
    </body>
</html>
