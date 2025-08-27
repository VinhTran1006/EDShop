
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String selectedFilter = (String) request.getAttribute("selectedFilter");
    if (selectedFilter == null) {
        selectedFilter = "All"; // giá trị mặc định
    }
%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Management</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f9fafb;
                margin: 0;
                padding: 0;
            }

            h1 {
                text-align: center;
                font-size: 2.5rem;
                margin-top: 30px;
                color: #222;
            }

            .header-row {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 20px;
                margin: 40px auto;
                flex-wrap: wrap;
            }

            .header-row p {
                font-size: 1.6rem;
                font-weight: 600;
                color: #333;
                margin: 0;
            }

            /* Search form */
            .search-form {
                display: flex;
                align-items: center;
                background: #fff;
                padding: 6px 12px;
                border-radius: 8px;
                border: 1px solid #ddd;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            }

            .search-form input {
                border: none;
                outline: none;
                padding: 8px 12px;
                font-size: 15px;
                flex: 1;
                min-width: 200px;
            }

            .search-form .search-btn {
                background: #2563eb;
                color: #fff;
                border: none;
                padding: 8px 16px;
                border-radius: 6px;
                cursor: pointer;
                font-weight: 500;
                transition: background 0.2s ease;
            }

            .search-form .search-btn:hover {
                background: #1e40af;
            }

            /* Filter */
            .filter-form {
                display: flex;
                align-items: center;
                gap: 10px;
                background: #fff;
                padding: 6px 12px;
                border-radius: 8px;
                border: 1px solid #ddd;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            }

            .filter-form label {
                font-weight: 600;
                color: #444;
            }

            .filter-form select {
                padding: 6px 12px;
                font-size: 15px;
                border: 1px solid #ccc;
                border-radius: 6px;
                outline: none;
                cursor: pointer;
                transition: border 0.2s ease;
            }

            .filter-form select:hover {
                border-color: #2563eb;
            }
        </style>
    </head>

    <body>
        <h1>Product Management</h1>

        <div class="header-row">
            <p>
                <%= (selectedFilter != null && !selectedFilter.isEmpty()) ? (selectedFilter + " ") : "All"%> Product
            </p>

            <form class="search-form" method="get" action="ProductListForStaff">
                <input
                    type="text"
                    name="keyword"
                    placeholder="Find by name..."
                    value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>"
                    />
                <button type="submit" class="search-btn">Search</button>
            </form>

            <form class="filter-form" action="ProductListForStaff" method="get">
                <label for="filter">Filter:</label>
                <select name="filter" id="filter" onchange="this.form.submit()">
                    <option value="All" <%= "All".equals(selectedFilter) ? "selected" : ""%>>All products</option>
                    <option value="Featured" <%= "Featured".equals(selectedFilter) ? "selected" : ""%>>Featured</option>
                    <option value="Bestseller" <%= "Bestseller".equals(selectedFilter) ? "selected" : ""%>>Bestseller</option>
                    <option value="New" <%= "New".equals(selectedFilter) ? "selected" : ""%>>New</option>
                </select>
            </form>
        </div>
    </body>
</html>

