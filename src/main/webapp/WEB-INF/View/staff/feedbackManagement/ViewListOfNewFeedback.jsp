<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Customer Feedback</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/sideBar.css">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            min-height: 100vh;
        }

        .container {
            background: transparent;
        }

        main.main-content {
            flex: 1;
            margin-left: 220px;
            min-height: 100vh;
            box-sizing: border-box;
            padding: 20px;
        }

        .wrapper {
            width: 100%;
            max-width: 100%;
            margin: 0 auto;
            margin-left: -30px;
            background: transparent;
        }

        .card {
            background: #ffffff;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            overflow: hidden;
            margin: 0 auto;
            max-width: 1200px;
            animation: slideInUp 0.5s ease-out;
        }

        .card-header {
            background: linear-gradient(135deg, #34495e, #2c3e50);
            color: #ffffff;
            padding: 5px 20px;
        }

        .card-header h1 {
            font-weight: 700;
            font-size: 2rem;
            letter-spacing: 0.5px;
            margin: 0;
            color: #ffffff;
        }

        .card-header i {
            margin-right: 10px;
            color: #3498db;
        }

        .card-body {
            padding: 30px;
        }

        /* Table Styling */
        .feedback-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            background: #ffffff;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }

        .feedback-table th,
        .feedback-table td {
            padding: 5px 20px;
            border-bottom: 1px solid #f1f3f4;
            text-align: center;
        }

        .feedback-table th {
            background: linear-gradient(135deg, #ecf0f1, #d5dbdb);
            color: #2c3e50;
            font-weight: 600;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .feedback-table td {
            font-size: 15px;
            color: #34495e;
        }

        .feedback-table tr:last-child th,
        .feedback-table tr:last-child td {
            border-bottom: none;
        }

        .feedback-table tbody tr:nth-child(even) {
            background: #f8f9fa;
        }

        .feedback-table tbody tr:hover {
            background: #e3f2fd;
            transform: scale(1.01);
            transition: all 0.3s ease;
        }

        /* New feedback row highlighting */
        .feedback-table .table-success {
            background: linear-gradient(135deg, #d4edda, #c3e6cb) !important;
            border-left: 4px solid #28a745;
        }

        .feedback-table .table-success:hover {
            background: linear-gradient(135deg, #c3e6cb, #b3d7b8) !important;
        }

        /* Status Badges */
        .badge {
            padding: 8px 16px;
            border-radius: 25px;
            font-weight: 600;
            font-size: 13px;
            color: #fff;
            display: inline-block;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
        }

        .bg-success {
            background: linear-gradient(135deg, #27ae60, #229954);
        }

        .bg-secondary {
            background: linear-gradient(135deg, #95a5a6, #7f8c8d);
        }

        /* Star Rating */
        .star-rating {
            display: inline-flex;
            gap: 2px;
        }

        .star-rating i {
            color: #f39c12;
            font-size: 16px;
            text-shadow: 0 1px 2px rgba(0,0,0,0.1);
        }

        .star-rating .far {
            color: #bdc3c7;
        }

        /* Buttons */
        .btn {
            padding: 10px 20px;
            font-weight: 600;
            font-size: 14px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }

        .btn-primary {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, #2980b9, #1f4e79);
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);
        }

        .btn-sm {
            padding: 8px 16px;
            font-size: 13px;
        }

        /* Alert Styling */
        .alert {
            background: #ffffff;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.1);
            padding: 30px;
            text-align: center;
            color: #7f8c8d;
            font-size: 1.1rem;
            max-width: 600px;
            margin: 0 auto;
        }

        .alert-warning {
            border-left: 4px solid #f39c12;
        }

        .alert i {
            font-size: 3rem;
            color: #bdc3c7;
            margin-bottom: 15px;
            display: block;
        }

        /* Section Header */
        .section-header {
            color: #2c3e50;
            font-weight: 700;
            font-size: 1.3rem;
            margin: 30px 0 20px 0;
            display: flex;
            align-items: center;
            border-left: 4px solid #3498db;
            padding-left: 15px;
        }

        .section-header i {
            margin-right: 10px;
            color: #3498db;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            main.main-content {
                margin-left: 0;
                padding: 15px;
            }

            .card {
                max-width: 100%;
                margin: 10px;
            }

            .card-body {
                padding: 20px;
            }

            .feedback-table th,
            .feedback-table td {
                padding: 10px 8px;
                font-size: 13px;
            }

            .card-header h1 {
                font-size: 1.5rem;
            }

            .btn {
                width: 100%;
                min-width: auto;
            }
        }

        /* Animation for page load */
        @keyframes slideInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Customer name styling */
        .customer-name {
            font-weight: 600;
            color: #2c3e50;
        }

        /* Action column */
        .action-column {
            min-width: 120px;
        }

        /* Index column */
        .index-column {
            font-weight: 600;
            color: #3498db;
            width: 60px;
        }
    </style>
</head>
<body>
<div class="container">
    <jsp:include page="../sideBar.jsp"/>
    <div class="wrapper">
        <main class="main-content">
            <jsp:include page="../header.jsp"/>
            
            <div class="card">
                <div class="card-header">
                    <h1><i class="fa-solid fa-comments"></i> Customer Feedback</h1>
                </div>

                <div class="card-body">
                    <c:if test="${empty ProductFeedback}">
                        <div class="alert alert-warning">
                            <i class="fa-solid fa-exclamation-triangle"></i>
                            <p>No customer feedback available at this time.</p>
                        </div>
                    </c:if>

                    <c:if test="${not empty ProductFeedback}">
                        <table class="feedback-table">
                            <thead>
                            <tr>
                                <th class="index-column">#</th>
                                <th>Customer Name</th>
                                <th>Status</th>
                                <th>Rating</th>
                                <th class="action-column">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${ProductFeedback}" var="fb" varStatus="loop">
                                <tr class="${!fb.isRead ? 'table-success' : ''}">
                                    <td class="index-column">${loop.index + 1}</td>
                                    <td class="customer-name">${fb.fullName}</td>
                                    <td>
                                        <span class="badge ${!fb.isRead ? 'bg-success' : 'bg-secondary'}">
                                            ${!fb.isRead ? 'New' : 'Seen'}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="star-rating">
                                            <c:forEach begin="1" end="${fb.star}" var="i">
                                                <i class="fas fa-star"></i>
                                            </c:forEach>
                                            <c:forEach begin="${fb.star + 1}" end="5" var="i">
                                                <i class="far fa-star"></i>
                                            </c:forEach>
                                        </div>
                                    </td>
                                    <td class="action-column">
                                        <a href="ViewFeedbackForStaff?feedbackID=${fb.feedbackID}" class="btn btn-primary btn-sm">
                                             View Details
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>