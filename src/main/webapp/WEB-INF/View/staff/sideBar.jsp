<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Sidebar - JSP Include Ready</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Namespace all sidebar styles to prevent conflicts */
        .edshop-sidebar {
            width: 220px;
            background: #232946 !important;
            color: #fff !important;
            min-height: 100vh;
            position: fixed !important;
            top: 0;
            left: 0;
            z-index: 9999 !important; /* Higher z-index to prevent being covered */
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
        }
        
        .edshop-sidebar .sidebar-header {
            padding: 20px;
            font-size: 1.3rem;
            font-weight: 700;
            letter-spacing: 1px;
            border-bottom: 1px solid #2e3652;
            display: flex;
            align-items: center;
            gap: 10px;
            color: #fff !important;
        }
        
        .edshop-sidebar .nav-menu {
            list-style: none !important;
            padding: 0 !important;
            margin: 0 !important;
        }
        
        .edshop-sidebar .nav-item {
            margin: 0 !important;
            list-style: none !important;
        }
        
        .edshop-sidebar .nav-link {
            display: flex !important;
            align-items: center;
            gap: 12px;
            padding: 14px 25px;
            color: #ffffff !important;
            text-decoration: none !important;
            font-size: 1rem;
            border-left: 4px solid transparent;
            transition: all 0.2s ease;
            width: 100%;
            box-sizing: border-box;
        }
        
        .edshop-sidebar .nav-link:hover,
        .edshop-sidebar .nav-link.active {
            background: #35377e !important;
            color: #fff !important;
            border-left: 4px solid #eebbc3 !important;
            text-decoration: none !important;
        }
        
        .edshop-sidebar .nav-link i {
            width: 16px;
            text-align: center;
            color: inherit !important;
        }
        
        /* Ensure main content has proper margin when sidebar is present */
        .edshop-main-content {
            margin-left: 220px !important;
            min-height: 100vh;
            transition: margin-left 0.2s ease;
        }
        
        /* Responsive design */
        @media (max-width: 768px) {
            .edshop-sidebar {
                transform: translateX(-220px);
                transition: transform 0.3s ease;
            }
            
            .edshop-sidebar.mobile-open {
                transform: translateX(0);
            }
            
            .edshop-main-content {
                margin-left: 0 !important;
            }
            
            .edshop-mobile-toggle {
                display: block !important;
                position: fixed;
                top: 15px;
                left: 15px;
                z-index: 10000;
                background: #232946;
                color: white;
                border: none;
                padding: 10px;
                border-radius: 5px;
                cursor: pointer;
            }
        }
        
        .edshop-mobile-toggle {
            display: none;
        }
        
        /* Demo content styles */
        .demo-content {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin: 20px;
        }
    </style>
</head>
<body>
    <!-- Mobile toggle button -->
    <button class="edshop-mobile-toggle" onclick="toggleSidebar()">
        <i class="fas fa-bars"></i>
    </button>

    <!-- Sidebar with namespace class -->
    <div class="edshop-sidebar" id="edshopSidebar">
        <div class="sidebar-header">
            <h2><i class="fas fa-store"></i> <span>EDShop</span></h2>
        </div>
        <ul class="nav-menu">
            <li class="nav-item">
                <a href="StaffDashboard" class="nav-link" data-page="staffdashboard">
                    <i class="fas fa-tachometer-alt"></i> <span>Dashboard</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="ViewOrderList" class="nav-link" data-page="vieworderlist">
                    <i class="fas fa-shopping-cart"></i> <span>Orders</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="ProductListForStaff" class="nav-link" data-page="productlistforstaff">
                    <i class="fas fa-box"></i> <span>Products</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="ImportStatistic" class="nav-link" data-page="importstatistic">
                    <i class="fas fa-warehouse"></i> <span>Stock</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="CustomerList" class="nav-link" data-page="customerlist">
                    <i class="fas fa-users"></i> <span>Customers</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="ViewListNewFeedback" class="nav-link" data-page="viewlistnewfeedback">
                    <i class="fas fa-comments"></i> <span>Feedback</span>
                </a>
            </li>
        </ul>
    </div>
    
    <script>
        // Namespace JavaScript functions to prevent conflicts
        window.EDShopSidebar = {
            init: function() {
                this.setActiveNavItem();
                this.setupEventListeners();
            },
            
            setActiveNavItem: function() {
                const path = window.location.pathname.toLowerCase();
                const navLinks = document.querySelectorAll(".edshop-sidebar .nav-link");
                
                // Remove all active classes first
                navLinks.forEach(link => link.classList.remove("active"));
                
                // Find and set active link
                navLinks.forEach(link => {
                    const href = link.getAttribute("href");
                    const dataPage = link.getAttribute("data-page");
                    
                    if (href && path.includes(href.toLowerCase())) {
                        link.classList.add("active");
                    } else if (dataPage && path.includes(dataPage.toLowerCase())) {
                        link.classList.add("active");
                    }
                });
            },
            
            setupEventListeners: function() {
                // Handle navigation clicks
                const navLinks = document.querySelectorAll(".edshop-sidebar .nav-link");
                navLinks.forEach(link => {
                    link.addEventListener("click", function(e) {
                        // Remove active from all links
                        navLinks.forEach(l => l.classList.remove("active"));
                        // Add active to clicked link
                        this.classList.add("active");
                    });
                });
            }
        };
        
        // Mobile toggle function
        function toggleSidebar() {
            const sidebar = document.getElementById("edshopSidebar");
            sidebar.classList.toggle("mobile-open");
        }
        
        // Initialize when DOM is ready
        document.addEventListener("DOMContentLoaded", function() {
            if (window.EDShopSidebar) {
                window.EDShopSidebar.init();
            }
        });
        
        // Re-initialize when page changes (for SPA-like behavior)
        window.addEventListener("popstate", function() {
            if (window.EDShopSidebar) {
                window.EDShopSidebar.setActiveNavItem();
            }
        });
    </script>
</body>
</html>