<%@page import="model.Customer"%>
<%@page import="model.Staff"%>
<%
    Customer cus = (Customer) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <!-- Import d?ng chung file CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/profile.css">
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <style>
            .form-control {
                border-radius: 10px;
                border: 2px solid #e9ecef;
                padding: 12px 15px;
                transition: all 0.3s ease;
                margin-bottom: 15px;
            }
            .form-control:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
            }
            .form-label {
                font-weight: 500;
                color: #333;
                margin-bottom: 5px;
            }
            .input-group {
                position: relative;
                margin-bottom: 15px;
            }
            .input-icon {
                position: absolute;
                left: 15px;
                top: 48%;
                transform: translateY(-50%);
                color: #6c757d;
                z-index: 5;
            }
            .form-control.with-icon {
                padding-left: 45px;
            }
            .input-group.password-group .input-icon {
                top: 38%;
                transform: translateY(-50%);
            }
            .toggle-password {
                position: absolute;
                right: 15px;
                top: 38%;
                transform: translateY(-50%);
                cursor: pointer;
            }
            .password-strength {
                width: 100%;
                height: 3px;
                background: #e9ecef;
                border-radius: 2px;
                margin-top: 5px;
                overflow: hidden;
            }
            .password-strength-bar {
                width: 0;
                height: 100%;
                transition: all 0.3s ease;
                border-radius: 2px;
            }
            .password-strength-bar.strength-weak {
                background: #ff6b6b;
                width: 33%;
            }
            .password-strength-bar.strength-medium {
                background: #ffa726;
                width: 66%;
            }
            .password-strength-bar.strength-strong {
                background: #11998e;
                width: 100%;
            }
        </style>

    </head>
    <body>
        <jsp:include page="/WEB-INF/View/customer/homePage/header.jsp" />
        <div class="main-account container-fluid">

            <jsp:include page="/WEB-INF/View/customer/sideBar.jsp" />
            <div class="profile-card">
                <div class="profile-header">
                    <h4>
                        <i class="bi bi-shield-lock me-2"></i>
                        Change Password
                    </h4>
                </div>
                <div class="profile-body">
                    <%
                    if (session != null) {
                        String error = (String) session.getAttribute("error");
                        String success = (String) session.getAttribute("success");
                        if (error != null) {
                %>
                <div class="alert alert-danger" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= error%>
                </div>
                <%
                        session.removeAttribute("error");
                    }
                    if (success != null) {
                %>
                <div class="alert alert-success" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> <%= success%>
                </div>
                <%
                            session.removeAttribute("success");
                        }
                    }
                %>
                    <form method="post" action="ChangePassword">
                        <div class="form-group position-relative">
                            <label for="oldPassword" class="form-label">
                                <i class="bi bi-lock me-2"></i>Old Password
                            </label>
                            <input type="password" class="form-control" name="oldPassword" id="oldPassword" required placeholder="Enter your current password">
                            <i class="bi bi-eye-slash toggle-password" style="position:absolute; right:15px; top:75%; transform:translateY(-50%); cursor:pointer;"></i>
                        </div>
                        <div class="input-group password-group">
                            <i class="bi bi-key input-icon"></i>
                            <input type="password" class="form-control with-icon" name="newPassword" id="newPassword" 
                                   placeholder="Enter your new password!" 
                                   required
                                   minlength="8" maxlength="30"
                                   title="Password must be 8?30 characters long, start with an uppercase letter, and contain at least one '@'.">
                            <i class="bi bi-eye-slash toggle-password"></i>
                            <div class="password-strength">
                                <div class="password-strength-bar" id="strengthBar"></div>
                            </div>
                        </div>

                        <div class="input-group password-group">
                            <i class="bi bi-key-fill input-icon"></i>
                            <input type="password" class="form-control with-icon" name="confirmPassword" id="confirmPassword" 
                                   placeholder="Confirm your new password!" 
                                   required
                                   minlength="8" maxlength="30"
                                   title="Password must be 8?30 characters long, start with an uppercase letter, and contain at least one '@'.">
                            <i class="bi bi-eye-slash toggle-password"></i>
                        </div>
                        <div class="profile-actions">
                            <a href="ViewProfile" class="btn-cancel">
                                <i class="bi bi-arrow-left me-1"></i> Back
                            </a>
                            <button type="submit" class="btn-update">
                                <i class="bi bi-shield-check me-2"></i>
                                Change Password
                            </button>
                        </div>
                    </form>
                </div>
            </div>

        </div>

        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const toggleIcons = document.querySelectorAll('.toggle-password');
                toggleIcons.forEach(icon => {
                    const input = icon.previousElementSibling;
                    icon.addEventListener('click', function (e) {
                        e.stopPropagation();
                        if (input.type === 'password') {
                            input.type = 'text';
                            icon.classList.remove('bi-eye-slash');
                            icon.classList.add('bi-eye');
                        } else {
                            input.type = 'password';
                            icon.classList.remove('bi-eye');
                            icon.classList.add('bi-eye-slash');
                        }
                    });
                    document.addEventListener('click', function (e) {
                        if (e.target !== input && e.target !== icon) {
                            input.type = 'password';
                            icon.classList.remove('bi-eye');
                            icon.classList.add('bi-eye-slash');
                        }
                    });
                });

                const newPasswordInput = document.getElementById('newPassword');
                const confirmPasswordInput = document.getElementById('confirmPassword');
                const strengthBar = document.getElementById('strengthBar');

                // Password strength
                newPasswordInput.addEventListener('input', function () {
                    const pwd = this.value || '';
                    let score = 0;
                    if (pwd.length >= 8)
                        score++;
                    if (/[A-Z]/.test(pwd))
                        score++;
                    if (/[a-z]/.test(pwd))
                        score++;
                    if (/\d/.test(pwd))
                        score++;
                    if (/[@]/.test(pwd))
                        score++;

                    strengthBar.className = 'password-strength-bar';
                    if (pwd.length === 0) {
                    } else if (score <= 2)
                        strengthBar.classList.add('strength-weak');
                    else if (score <= 4)
                        strengthBar.classList.add('strength-medium');
                    else
                        strengthBar.classList.add('strength-strong');
                });

                // Confirm password border color realtime
                confirmPasswordInput.addEventListener('input', function () {
                    const pwd = newPasswordInput.value || '';
                    const conf = this.value || '';
                    if (conf.length === 0) {
                        this.style.setProperty("border-color", "#e9ecef", "important");
                    } else if (pwd === conf) {
                        this.style.setProperty("border-color", "#28a745", "important");
                    } else {
                        this.style.setProperty("border-color", "#dc3545", "important");
                    }
                });

                // Form validation before submit
                const form = document.querySelector('form');
                form.addEventListener('submit', function (e) {
                    const pwdVal = newPasswordInput.value;
                    const confVal = confirmPasswordInput.value;
                    const pwdRegex = /^(?=.*@)[A-Z][A-Za-z0-9@]{7,254}$/; // same as register

                    if (!pwdRegex.test(pwdVal)) {
                        e.preventDefault();
                        alert("Password must be 8-255 characters, start with uppercase and contain '@'.");
                        newPasswordInput.focus();
                        return;
                    }

                    if (pwdVal !== confVal) {
                        e.preventDefault();
                        alert("Passwords do not match.");
                        confirmPasswordInput.focus();
                        return;
                    }
                });
            });
        </script>
    </body>
</html>
