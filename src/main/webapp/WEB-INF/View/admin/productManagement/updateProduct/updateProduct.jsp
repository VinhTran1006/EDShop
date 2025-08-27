<%
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!-- Bootstrap CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Fontawesome CDN -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
<html>

    <head>
        <style>


            .main-content {
                margin-left: 40px;
                padding: 30px;
                background-color: #f8f9fa;


                min-height: auto;
                height: auto;
                overflow: visible;
            }


        </style>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>

    <body>

        <div class="main-content">
            <div>
                <jsp:include page="/WEB-INF/View/admin/productManagement/updateProduct/productTitle.jsp" />
            </div>

            <form method="post" action="AdminUpdateProduct" enctype="multipart/form-data">

                <div style = "display: flex; gap: 1%;">

                    <div style = "width: 55%; border-radius: 12px; background-color: #ffffff; max-height: 220px;">
                        <div>
                            <jsp:include page="/WEB-INF/View/admin/productManagement/updateProduct/imgProduct.jsp" />
                        </div>

                        <div style = "margin-top: 5%;">
                            <jsp:include page="/WEB-INF/View/admin/productManagement/updateProduct/updateDetail.jsp" />
                            <button type="submit" class="btn btn-primary" style="margin-top: 10px;">Save</button>
                        </div>
                    </div>

                    <div style = "width: 44%;">
                        <jsp:include page="/WEB-INF/View/admin/productManagement/updateProduct/updateInformation.jsp" />
                    </div>
                </div>

            </form>
        </div>
    </body>

    <script>
        function previewSelectedImage(event, imgId) {
            const input = event.target;

            if (!input || !input.files || !input.files[0]) {
                console.error("Input or input.files is undefined!");
                return;
            }

            const preview = document.getElementById(imgId);
            if (!preview) {
                console.error("Preview image element not found: " + imgId);
                return;
            }

            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        }

        window.onload = function () {
        <% if ("1".equals(success)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Update!',
                text: 'The product has been update.',
                timer: 2000,
                showConfirmButton: false
            }).then(() => {

                window.history.replaceState({}, document.title, window.location.pathname);
            });
        <% } else if ("1".equals(error)) { %>
            Swal.fire({
                icon: 'error',
                title: 'Failed!',
                text: 'Could not update the product.',
                timer: 2000,
                showConfirmButton: false
            }).then(() => {

                window.history.replaceState({}, document.title, window.location.pathname);
            });
        <% }%>
        };


    </script>
</html>
