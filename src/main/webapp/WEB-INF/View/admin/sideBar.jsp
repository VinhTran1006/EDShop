
<div class="sidebar-overlay" id="sidebarOverlay"></div>
<nav class="sidebar" id="sidebar">
    <div class="sidebar-header">
        <h2><i class="fas fa-store"></i> <span>TShop</span></h2>
    </div>
    <ul class="nav-menu">
        <li class="nav-item">

            <a href="AdminDashboard" class="nav-link">

                <i class="fas fa-tachometer-alt"></i> <span>Dashboard</span>
            </a>
        </li>
        <li class="nav-item">

            <a href="StaffList" class="nav-link">

                <i class="fas fa-user-tie"></i> <span>Staff Management</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="AdminProduct" class="nav-link">
                <i class="fas fa-box"></i> <span>Product Management</span>
            </a>
        </li>
        <li class="nav-item">

            <a href="CategoryView" class="nav-link">

                <i class="fas fa-tags"></i> <span>Category Management</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="Voucher" class="nav-link">
                <i class="fas fa-ticket-alt"></i> <span>Voucher Management</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="ViewSupplier" class="nav-link">
                <i class="fas fa-truck"></i> <span>Supplier Management</span>
            </a>
        </li>
        <li class="nav-item">

            <a href="ManageStatistic" class="nav-link">
                <i class="fa fa-bar-chart"></i> <span>Manage Statistics</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="ViewOrderList" class="nav-link">
                <i class="fas fa-shopping-cart"></i> Orders
            </a>
        </li>
        <li class="nav-item">
            <a href="ImportStatistic" class="nav-link">
                <i class="fas fa-warehouse"></i> Stock
            </a>
        </li>
        <li class="nav-item">
            <a href="CustomerList" class="nav-link">
                <i class="fas fa-users"></i> Customers
            </a>
        </li>
        <li class="nav-item">
            <a href="ViewListNewFeedback" class="nav-link">
                <i class="fas fa-comments"></i> Feedback
            </a>
        </li>
    </ul>

</nav>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const path = window.location.pathname.toLowerCase();
        const navLinks = document.querySelectorAll(".nav-link");

        navLinks.forEach(link => {
            const href = link.getAttribute("href").toLowerCase();
            if (path.includes(href)) {
                document.querySelectorAll(".nav-link").forEach(l => l.classList.remove("active"));
                link.classList.add("active");
            }
        });
    });
</script>
