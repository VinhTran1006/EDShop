
<%@page import="java.util.List"%>
<%@page import="model.Attribute"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body></body>
</html>

<script>
    function confirmDelete(attributeID, categoryId) {
        Swal.fire({
            title: 'Are you sure?',
            text: "This atrribute has been deleted.",
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

