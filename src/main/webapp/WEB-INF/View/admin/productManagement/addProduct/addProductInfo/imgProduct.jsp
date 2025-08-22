<%@page import="model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Product product = (Product) request.getAttribute("product");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Update Product Images</title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container py-4">

    <!-- ẢNH CHÍNH -->
    <h5>Main Image</h5>
    <div style="
         width: 100%;
         height: 300px;
         border-radius: 15px;
         overflow: hidden;
         background-color: #f8f9fa;
         display: flex;
         justify-content: center;
         align-items: center;
         padding: 10px;">
        <label for="fileInputMain" style="width: 100%; cursor: pointer; margin: 0; display: block;">
            <img id="previewMainImage"
                 src="https://redthread.uoregon.edu/files/original/affd16fd5264cab9197da4cd1a996f820e601ee4.png"
                 style="width: 100%; max-height: 100%; object-fit: contain; border-radius: 10px;"
                 alt="Click to change image" title="Click to change image">
        </label>
        <input type="file" name="fileMain" id="fileInputMain" accept="image/*"
               style="display: none;" onchange="previewSelectedImage(this, 'previewMainImage')">
    </div>

    <!-- ẢNH PHỤ -->
    <h5 class="mt-4">Sub Images</h5>
    <div class="row g-3">
        <% for (int i = 1; i <= 3; i++) { %>
        <div class="col-md-4">
            <div style="
                 width: 100%;
                 height: 200px;
                 border-radius: 15px;
                 overflow: hidden;
                 background-color: #f8f9fa;
                 display: flex;
                 justify-content: center;
                 align-items: center;
                 padding: 10px;">
                <label for="fileInputSub<%=i%>" style="width: 100%; cursor: pointer; margin: 0; display: block;">
                    <img id="previewSubImage<%=i%>"
                         src="https://via.placeholder.com/200x150?text=Sub+Image+<%=i%>"
                         style="width: 100%; max-height: 100%; object-fit: contain; border-radius: 10px;"
                         alt="Click to change image" title="Click to change image">
                </label>
                <input type="file" name="fileSub<%=i%>" id="fileInputSub<%=i%>" accept="image/*"
                       style="display: none;" onchange="previewSelectedImage(this, 'previewSubImage<%=i%>')">
            </div>
        </div>
        <% } %>
    </div>

</body>

<script>
    // Preview ảnh khi chọn
    function previewSelectedImage(input, imgId) {
        const preview = document.getElementById(imgId);
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    // SweetAlert thông báo
    window.onload = function () {
        const urlParams = new URLSearchParams(window.location.search);
        const success = urlParams.get("success");
        const error = urlParams.get("error");
        if (success === "1") {
            Swal.fire({
                icon: 'success',
                title: 'Update Successful!',
                text: 'The product images have been updated.',
                timer: 2000,
                showConfirmButton: false
            });
        }
        if (error === "1") {
            Swal.fire({
                icon: 'error',
                title: 'Update Failed!',
                text: 'Unable to update the product. Please try again.',
                timer: 2000,
                showConfirmButton: false
            });
        }
    };
</script>
</html>
