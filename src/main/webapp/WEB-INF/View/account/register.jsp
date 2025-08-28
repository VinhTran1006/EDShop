<%-- 
    Document   : register
    Created on : Jun 11, 2025, 4:10:00 PM
    Author     : pc
--%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <style>
            .login-container {
                background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 20px 0;
            }

            .login-card {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-radius: 20px;
                box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
                padding: 40px;
                width: 100%;
                max-width: 500px;
            }

            .login-title {
                color: #333;
                font-weight: 600;
                margin-bottom: 30px;
                text-align: center;
            }

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

            .btn-register {
                background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
                border: none;
                border-radius: 10px;
                padding: 12px 30px;
                font-weight: 600;
                width: 100%;
                transition: all 0.3s ease;
                margin-bottom: 15px;
                color: white;
            }

            .btn-register:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(17, 153, 142, 0.3);
                color: white;
            }

            .btn-login {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border: none;
                border-radius: 10px;
                padding: 12px 30px;
                font-weight: 600;
                width: 100%;
                transition: all 0.3s ease;
                margin-bottom: 20px;
                color: white;
            }

            .btn-login:hover {
                transform: translateY(-2px);
                box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
                color: white;
            }

            .login-link {
                color: #667eea;
                text-decoration: none;
                font-size: 14px;
                transition: all 0.3s ease;
                display: block;
                text-align: center;
            }

            .login-link:hover {
                color: #764ba2;
                text-decoration: underline;
            }

            .divider {
                text-align: center;
                margin: 20px 0;
                position: relative;
                color: #6c757d;
            }

            .divider::before {
                content: '';
                position: absolute;
                top: 50%;
                left: 0;
                right: 0;
                height: 1px;
                background: #e9ecef;
            }

            .divider span {
                background: rgba(255, 255, 255, 0.95);
                padding: 0 15px;
            }

            .error-message {
                background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
                color: white;
                padding: 15px;
                border-radius: 10px;
                margin-top: 10px;
                margin-bottom: 20px;
                text-align: center;
                font-weight: 500;
            }

            .input-group {
                position: relative;
                margin-bottom: 15px;
            }

            .input-icon {
                position: absolute;
                left: 15px;
                top: 46%;
                transform: translateY(-50%);
                color: #6c757d;
                z-index: 5;
            }

            .form-control.with-icon {
                padding-left: 45px;
            }

            .input-group.password-group .input-icon {
                top: 38%;              /* trung tâm chính input */
                transform: translateY(-50%);
            }

            .form-row {
                display: flex;
                gap: 15px;
            }

            .form-row .form-group {
                flex: 1;
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
        <div class="login-container">
            <div class="login-card">
                <h2 class="login-title">
                    <i class="bi bi-person-plus-fill me-2"></i>
                    Create New Account
                </h2>
                <%
                    String error = (String) session.getAttribute("error");
                    String phoneVal = (String) session.getAttribute("tempPhone");
                    String emailVal = (String) session.getAttribute("tempEmail");
                    String fullNameVal = (String) session.getAttribute("tempFullName");
                %>

                <% if (error != null) {%>
                <div class="error-message">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <%= error%>
                </div>
                <%
                        session.removeAttribute("error"); // clear flash
                    }%>

                <form method="post" action="Register">
                    <div class="input-group">
                        <i class="bi bi-telephone-fill input-icon"></i>
                        <input type="tel" name="phone" class="form-control with-icon"
                               pattern="^\d{10}$" maxlength="10" placeholder="Enter your phone number"
                               required value="<%= phoneVal != null ? phoneVal : ""%>">
                    </div>

                    <div class="input-group">
                        <i class="bi bi-envelope-fill input-icon"></i>
                        <input type="email" name="email" class="form-control with-icon"
                               placeholder="Enter your email address" required
                               value="<%= emailVal != null ? emailVal : ""%>">
                    </div>

                    <div class="input-group">
                        <i class="bi bi-person-fill input-icon"></i>
                        <input type="text" name="fullName" class="form-control with-icon"
                               pattern="^[\p{L}\s]{2,255}$" placeholder="Enter your full name" required
                               value="<%= fullNameVal != null ? fullNameVal : ""%>">
                    </div>

                    <div class="input-group password-group">
                        <i class="bi bi-lock-fill input-icon"></i>
                        <input type="password" name="password" class="form-control with-icon" placeholder="Create a password" required id="password">
                        <i class="bi bi-eye-slash toggle-password" style="position:absolute; right:15px; top:40%; transform:translateY(-50%); cursor:pointer;"></i>
                        <div class="password-strength">
                            <div class="password-strength-bar" id="strengthBar"></div>
                        </div>
                    </div>

                    <div class="input-group password-group">
                        <i class="bi bi-shield-lock-fill input-icon"></i>
                        <input type="password" name="confirmPassword" class="form-control with-icon" placeholder="Confirm your password" required id="confirmPassword">
                        <i class="bi bi-eye-slash toggle-password" style="position:absolute; right:15px; top:40%; transform:translateY(-50%); cursor:pointer;"></i>
                    </div>

                    <button type="submit" class="btn btn-register">
                        <i class="bi bi-person-plus me-2"></i>
                        Create Account
                    </button>

                    <div class="divider">
                        <span>Already have an account?</span>
                    </div>

                    <a href="Login" class="btn btn-login">
                        <i class="bi bi-box-arrow-in-right me-2"></i>
                        Sign In
                    </a>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const toggleIcons = document.querySelectorAll('.toggle-password');

                toggleIcons.forEach(icon => {
                    const input = icon.previousElementSibling; // input ngay tr??c icon

                    icon.addEventListener('click', function (e) {
                        e.stopPropagation(); // tránh s? ki?n click lan ra ngoài
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

                    // Click ra ngoài input s? hide password
                    document.addEventListener('click', function (e) {
                        if (e.target !== input && e.target !== icon) {
                            input.type = 'password';
                            icon.classList.remove('bi-eye');
                            icon.classList.add('bi-eye-slash');
                        }
                    });
                });

                const form = document.querySelector('form');
                const phoneInput = document.querySelector('input[name="phone"]');
                const fullNameInput = document.querySelector('input[name="fullName"]');
                const passwordInput = document.getElementById('password');
                const confirmPasswordInput = document.getElementById('confirmPassword');
                const strengthBar = document.getElementById('strengthBar');

                const phoneRegex = /^\d{10}$/;
                const nameRegex = /^[\p{L}\s]{2,255}$/u;
                const passwordRegex = /^(?=.*@)[A-Z][A-Za-z0-9@]{7,254}$/;

                if (form) {
                    form.addEventListener('submit', function (e) {
                        const errors = [];

                        const phoneVal = phoneInput ? phoneInput.value.trim() : '';
                        const nameVal = fullNameInput ? fullNameInput.value.trim() : '';
                        const pwdVal = passwordInput ? passwordInput.value : '';
                        const confVal = confirmPasswordInput ? confirmPasswordInput.value : '';

                        // Phone validation
                        if (phoneInput) {
                            if (!phoneRegex.test(phoneVal)) {
                                errors.push("Phone number must be exactly 10 digits.");
                            }
                        }

                        // Full name validation
                        if (fullNameInput) {
                            if (nameVal.length === 0 || !nameRegex.test(nameVal)) {
                                errors.push("Full name cannot be empty and must not contain numbers or special characters.");
                            }
                        }

                        // Password validation (the one required by backend)
                        if (!passwordRegex.test(pwdVal)) {
                            errors.push("Password must be 8-255 characters, start with an uppercase letter, and contain at least one '@'.");
                        }

                        // Confirm password
                        if (pwdVal !== confVal) {
                            errors.push("Passwords do not match.");
                        }

                        if (errors.length > 0) {
                            e.preventDefault();
                            alert(errors.join("\n"));

                            if (phoneInput && !phoneRegex.test(phoneVal)) {
                                phoneInput.focus();
                                return;
                            }
                            if (fullNameInput && (nameVal.length === 0 || !nameRegex.test(nameVal))) {
                                fullNameInput.focus();
                                return;
                            }
                            if (!passwordRegex.test(pwdVal) && passwordInput) {
                                passwordInput.focus();
                                return;
                            }
                            if (pwdVal !== confVal && confirmPasswordInput) {
                                confirmPasswordInput.focus();
                                return;
                            }
                        }
                    });
                }

                // Password strength indicator (visual helper only)
                if (passwordInput && strengthBar) {
                    passwordInput.addEventListener('input', function () {
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

                        // Reset classes
                        strengthBar.className = 'password-strength-bar';

                        if (pwd.length === 0) {
                            // no bar
                        } else if (score <= 2) {
                            strengthBar.classList.add('strength-weak');
                        } else if (score <= 4) {
                            strengthBar.classList.add('strength-medium');
                        } else {
                            strengthBar.classList.add('strength-strong');
                        }
                    });
                }

                // Realtime confirm password border color
                if (confirmPasswordInput && passwordInput) {
                    confirmPasswordInput.addEventListener('input', function () {
                        const pwd = passwordInput.value || '';
                        const conf = this.value || '';
                        if (conf.length === 0) {
//                            this.style.borderColor = '#e9ecef';
                            this.style.setProperty("border-color", "#e9ecef", "important");
                        } else if (pwd === conf) {
                            this.style.setProperty("border-color", "#28a745", "important");
                        } else {
//                            this.style.borderColor = '#dc3545';
                            this.style.setProperty("border-color", "#dc3545", "important");
                        }
                    });
                }
            });
        </script>
        <jsp:include page="/WEB-INF/View/customer/homePage/footer.jsp" />
    </body>
</html>