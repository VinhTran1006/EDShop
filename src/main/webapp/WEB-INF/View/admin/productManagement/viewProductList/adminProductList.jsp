
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <!-- Fontawesome CDN -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
        <!-- Sidebar CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">

        <!-- Dashboard CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <title>JSP Page</title>
<!--        <style>
            .container {
                padding-left: 47px !important;
                padding-right: 47px !important;
            }
        </style>-->
    </head>
    <body>
        <div style = "display: flex"  >
            <div style = "width: 18.5%;">
                <jsp:include page="/WEB-INF/View/admin/sideBar.jsp"/>
            </div>

            <div class = "container" style = "flex: 1; margin-left: -12px ">
                <jsp:include page="/WEB-INF/View/admin/productManagement/viewProductList/fillterProductList.jsp" />
                <jsp:include page="/WEB-INF/View/admin/productManagement/viewProductList/productList.jsp" />
            </div>
        </div>
    </body>
    <%
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        String deletesuccess = request.getParameter("deletesuccess");
        String deleteerror = request.getParameter("deleteerror");
        String updatesuccess = request.getParameter("updatesuccess");
    %>

    <script>
        window.onload = function () {
        <% if ("1".equals(success)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Added!',
                text: 'The product has been added.',
                timer: 2000
            });
        <% } else if ("1".equals(error)) { %>
            Swal.fire({
                icon: 'error',
                title: 'Failed!',
                text: 'Could not add the product.',
                timer: 2000
            });
        <% } else if ("1".equals(deletesuccess)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Delete product successfully.',
                timer: 2000
            });
        <% } else if ("1".equals(deleteerror)) { %>
            Swal.fire({
                icon: 'error',
                title: 'Failed!',
                text: 'Could not delete the product.',
                timer: 2000
            });
        <% } else if ("1".equals(updatesuccess)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Update product successfully.',
                timer: 2000
            });
        <% }%>

            const url = new URL(window.location);
            ["success", "error", "deletesuccess", "deleteerror", "updatesuccess"]
                    .forEach(p => url.searchParams.delete(p));
            window.history.replaceState({}, document.title, url);
        };
    </script>

</html>