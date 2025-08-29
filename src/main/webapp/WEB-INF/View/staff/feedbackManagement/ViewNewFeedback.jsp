<%-- 
    Document   : ViewNewFeedback
    Created on : Mar 2, 2025, 10:17:23 AM
    Author     : Vinhntce181630
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Reviews</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/admin.css">

        <!-- SweetAlert2 -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            .main-content{
                padding-top: 60px !important
            }
        </style>
    </head>
    <body>
        <div class="container">
            <jsp:include page="../sideBar.jsp" />
            <jsp:include page="../header.jsp" />
            <div class="wrapper">
                <main class="main-content">
                    <div class="container">
                        <div class="card">
                            <div class="card-header">
                                <h4><i class="fa-solid fa-star"></i> Customer Reviews</h4>
                            </div>

                            <div class="card-body">
                                <div class="review-card">
                                    <h5 class="section-header">
                                        <i class="fa-solid fa-box"></i> 
                                        [Product ID: ${Product.productID}] "${Product.productName}" - 
                                        <fmt:formatNumber value="${Product.price}" type="number" groupingUsed="true" />đ
                                    </h5>

                                    <table class="info-table">
                                        <tr>
                                            <th>Reviewed by:</th>
                                            <td><strong>${cus.fullName}</strong></td>
                                        </tr>
                                        <tr>
                                            <th>Review Date:</th>
                                            <td><fmt:formatDate value="${rate.createdDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        </tr>
                                        <tr>
                                            <th>Rating:</th>
                                            <td>
                                                <div class="star-rating">
                                                    <c:forEach var="i" begin="1" end="5">
                                                        <c:choose>
                                                            <c:when test="${i <= rate.star}">
                                                                <i class="fa fa-star"></i>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <i class="fa fa-star text-muted"></i>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Comment:</th>
                                            <td>
                                                <p id="comment-${rate.feedbackID}" data-original="${rate.comment}" 
                                                   class="${rate.isActive ? '' : 'hidden-feedback'}">
                                                    ${rate.isActive ? rate.comment : "This feedback was hidden for some reason."}
                                                </p>
                                            </td>
                                        </tr>
                                        <c:if test="${not empty rate.reply}">
                                            <tr>
                                                <th>Staff Reply:</th>
                                                <td>
                                                    <div class="reply-container">
                                                        <p>${rate.reply}</p>
                                                        <small class="text-muted">
                                                            by Staff: ${st.fullName}, 
                                                            at <fmt:formatDate value="${rate.replyDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                        </small>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </table>

                                    <div class="management-section">
                                        <div class="form-controls">
                                            <!-- Toggle Visibility Button -->
                                            <button id="toggle-btn-${rate.feedbackID}" 
                                                    class="btn ${rate.isActive ? 'btn-warning' : 'btn-success'}"
                                                    onclick="toggleVisibility(${rate.feedbackID}, ${rate.isActive ? 1 : 0})">
                                                ${rate.isActive ? "Hide" : "Show"}
                                            </button>

                                            <!-- Reply Button -->
                                            <c:if test="${empty rate.reply}">
                                                <button class="btn btn-primary"
                                                        onclick="toggleReplyForm(${rate.feedbackID})">
                                                    Reply
                                                </button>
                                            </c:if>

                                            <!-- Update/Delete Reply Buttons -->
                                            <c:if test="${not empty rate.reply}">
                                                <button class="btn btn-primary" 
                                                        onclick="openUpdateModal(${rate.feedbackID}, '${rate.reply}')">
                                                    <i class="fa fa-edit"></i> Update Reply
                                                </button>

                                                <button class="btn btn-danger" 
                                                        onclick="openDeleteModal(${rate.feedbackID})">
                                                    <i class="bi bi-trash"></i> Delete Reply
                                                </button>
                                            </c:if>
                                        </div>

                                        <!-- Reply Form -->
                                        <div id="replyForm-${rate.feedbackID}" class="reply-form mt-2">
                                            <form method="POST" action="ReplyFeedback">
                                                <input type="hidden" name="feedbackID" value="${rate.feedbackID}">
                                                <textarea required="true" name="answer" class="form-control" placeholder="Write your reply..."></textarea>
                                                <button type="submit" class="btn btn-success mt-2">
                                                    Submit Reply
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <!-- Success Modal -->
        <div id="successModal" class="modal">
            <div class="modal-content">
                <h3>Action Successful!</h3>
                <p>You will be redirected shortly...</p>
            </div>
        </div>

        <script>
            // Hàm hiển thị popup thành công và tự động chuyển hướng sau 2 giây
            function showSuccessAndRedirect() {
                // Hiển thị modal thành công
                var successModal = new bootstrap.Modal(document.getElementById('successModal'));
                successModal.show();

                // Chuyển hướng sau 2 giây
                setTimeout(function () {
                    window.location.href = "ViewListFeedback"; // Cập nhật URL nếu cần
                }, 2000);
            }

            // Hàm xử lý khi submit trả lời
            function submitReply(feedbackID) {
                let replyText = document.querySelector("#replyForm-" + feedbackID + " textarea").value;
                let xhr = new XMLHttpRequest();
                xhr.open("POST", "ReplyFeedbackServlet", true);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        let response = xhr.responseText.trim();
                        if (response === "Success") {
                            showSuccessAndRedirect(); // Hiển thị popup thành công và chuyển hướng
                        } else {
                            alert("Failed to submit the reply. Please try again.");
                        }
                    }
                };
                xhr.send("feedbackID=" + feedbackID + "&answer=" + encodeURIComponent(replyText));
            }

            function toggleReplyForm(feedbackID) {
                let form = document.getElementById("replyForm-" + feedbackID);
                form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
            }

            function toggleVisibility(feedbackID, currentStatus) {
                // 1 = Show, 0 = Hide
                let newStatus = currentStatus === 1 ? 0 : 1;

                let xhr = new XMLHttpRequest();
                xhr.open("POST", "UpdateStatusComment", true);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        let btn = document.getElementById("toggle-btn-" + feedbackID);
                        let comment = document.getElementById("comment-" + feedbackID);

                        if (newStatus === 1) { // chuyển sang Show
                            comment.innerHTML = comment.getAttribute("data-original");
                            comment.classList.remove("hidden-feedback");

                            btn.innerHTML = '<i class="fa fa-eye-slash"></i> Hide';
                            btn.classList.remove("btn-success");
                            btn.classList.add("btn-warning");
                        } else { // chuyển sang Hide
                            comment.setAttribute("data-original", comment.innerHTML);
                            comment.innerHTML = "This feedback was hidden for some reason.";
                            comment.classList.add("hidden-feedback");

                            btn.innerHTML = '<i class="fa fa-eye"></i> Show';
                            btn.classList.remove("btn-warning");
                            btn.classList.add("btn-success");
                        }

                        // Cập nhật onclick mới với giá trị thực
                        btn.setAttribute("onclick", "toggleVisibility(" + feedbackID + ", " + newStatus + ")");
                    }
                };

                xhr.send("feedbackID=" + feedbackID + "&isActive=" + newStatus);
            }

            function openDeleteModal(feedbackID) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this reply!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#d33',
                    cancelButtonColor: '#6c757d',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Gửi request xoá nếu xác nhận
                        let xhr = new XMLHttpRequest();
                        xhr.open("POST", "DeleteReply", true);
                        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === 4) {
                                if (xhr.status === 200 && xhr.responseText.trim() === "Success") {
                                    Swal.fire({
                                        icon: 'success',
                                        title: 'Deleted!',
                                        text: 'The reply has been deleted.',
                                        timer: 1500,
                                        showConfirmButton: false
                                    }).then(() => {
                                        location.reload(); // Reload lại trang
                                    });
                                } else {
                                    Swal.fire({
                                        icon: 'error',
                                        title: 'Failed',
                                        text: 'Something went wrong. Please try again.'
                                    });
                                }
                            }
                        };

                        xhr.send("feedbackID=" + feedbackID);
                    }
                });
            }

            function openUpdateModal(feedbackID, currentText) {
                if (!feedbackID) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Invalid feedback ID'
                    });
                    return;
                }
                Swal.fire({
                    title: 'Update Reply',
                    input: 'textarea',
                    inputLabel: 'Your reply',
                    inputValue: currentText,
                    showCancelButton: true,
                    confirmButtonText: 'Save',
                    cancelButtonText: 'Cancel',
                    inputValidator: (value) => {
                        if (!value.trim()) {
                            return 'Reply cannot be empty!';
                        }
                    }
                }).then((result) => {
                    if (result.isConfirmed) {
                        const updatedText = result.value.trim();
                        let xhr = new XMLHttpRequest();
                        xhr.open("POST", "UpdateReply", true);
                        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === 4) {
                                if (xhr.status === 200 && xhr.responseText.trim() === "Success") {
                                    Swal.fire({
                                        icon: 'success',
                                        title: 'Updated!',
                                        text: 'The reply has been updated.',
                                        timer: 1500,
                                        showConfirmButton: false
                                    }).then(() => {
                                        location.reload();
                                    });
                                } else {
                                    Swal.fire({
                                        icon: 'error',
                                        title: 'Failed',
                                        text: 'Failed to update reply: ' + xhr.responseText
                                    });
                                }
                            }
                        };
                        xhr.send("feedbackID=" + encodeURIComponent(feedbackID)
                                + "&answer=" + encodeURIComponent(updatedText));
                    }
                });
            }

            <% String success = request.getParameter("success");
                String feedbackID = request.getParameter("feedbackID"); // lấy từ URL
            %>
            window.onload = function () {
            <% if ("success".equals(success)) {%>
                Swal.fire({
                    icon: 'success',
                    title: 'Reply Submitted!',
                    text: 'Your reply was sent successfully.',
                    confirmButtonText: 'OK'
                }).then(() => {
                    window.location.href = '/EDShop/ViewFeedbackForStaff?feedbackID=<%=feedbackID%>';
                });
            <% } else if ("failed".equals(success)) { %>
                Swal.fire({
                    icon: 'error',
                    title: 'Reply Failed!',
                    text: 'Failed to submit your reply.',
                    confirmButtonText: 'OK'
                }).then(() => {
                    location.reload();
                });
            <% } else if ("error".equals(success)) {%>
                Swal.fire({
                    icon: 'error',
                    title: 'Server Error!',
                    text: 'Something went wrong while processing your reply.',
                    confirmButtonText: 'OK'
                }).then(() => {
                    window.location.href = '/EDShop/ViewFeedbackForStaff?feedbackID=<%=feedbackID%>';
                });
            <% } else if ("nostaff".equals(success)) {%>
                Swal.fire({
                    icon: 'warning',
                    title: 'Permission Denied!',
                    text: 'Staff ID not found for this account.',
                    confirmButtonText: 'OK'
                }).then(() => {
                    window.location.href = '/EDShop/ViewFeedbackForStaff?feedbackID=<%=feedbackID%>';
                });
            <% }%>
            };
        </script>
    </body>
</html>

