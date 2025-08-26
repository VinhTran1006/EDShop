<html>
    <head>
        <title>Sidebar + Category View</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/supplierList5.css">
        
         <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <!-- Fontawesome CDN -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
        <!-- Sidebar CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">

        <!-- Dashboard CSS -->

       

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    </head>
    <body>
        
        <div class="container">
            <!-- Sidebar bên trái -->
            <div class="sidebar">
                  <jsp:include page="/WEB-INF/View/admin/sideBar.jsp"/>
            </div>

            <div>
                <jsp:include page="/WEB-INF/View/admin/categoryManagement/viewCategoryList/CategoryView.jsp" />
            </div>
        </div>
    </body>
</html>