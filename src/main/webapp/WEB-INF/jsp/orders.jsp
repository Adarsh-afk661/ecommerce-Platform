<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - GoCart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .order-card {
            border: 1px solid #eee;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            transition: box-shadow 0.3s ease;
        }
        .order-card:hover {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .status-badge {
            font-size: 0.85rem;
            padding: 0.5rem 1rem;
            border-radius: 20px;
        }
        .status-pending {
            background: #ffc107;
            color: #000;
        }
        .status-delivered {
            background: #28a745;
            color: #fff;
        }
        .status-cancelled {
            background: #dc3545;
            color: #fff;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand fw-bold" href="/">🛒 GoCart</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="/shop">Shop</a></li>
                    <li class="nav-item"><a class="nav-link" href="/pricing">Pricing</a></li>
                    <li class="nav-item"><a class="nav-link" href="/cart">Cart</a></li>
                    <li class="nav-item"><a class="nav-link active" href="/orders">Orders</a></li>
                    <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <div class="bg-light py-4">
        <div class="container">
            <h1>My Orders</h1>
            <p class="text-muted">Track your purchases and manage returns</p>
        </div>
    </div>

    <!-- Orders List -->
    <div class="container my-5">
        <div class="order-card">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h6>Order #ORD-2024-001</h6>
                    <p class="text-muted mb-2">Placed on Nov 20, 2024</p>
                    <p class="mb-0"><strong>Items:</strong> Wireless Headphones (1x ₹1999)</p>
                </div>
                <div class="col-md-3">
                    <p><strong>Total:</strong> ₹2099</p>
                    <span class="status-badge status-delivered">Delivered</span>
                </div>
                <div class="col-md-3 text-end">
                    <button class="btn btn-sm btn-primary mb-2">View Details</button>
                    <button class="btn btn-sm btn-outline-primary">Reorder</button>
                </div>
            </div>
        </div>

        <div class="order-card">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h6>Order #ORD-2024-002</h6>
                    <p class="text-muted mb-2">Placed on Nov 18, 2024</p>
                    <p class="mb-0"><strong>Items:</strong> USB-C Cable (2x ₹299)</p>
                </div>
                <div class="col-md-3">
                    <p><strong>Total:</strong> ₹648</p>
                    <span class="status-badge status-pending">In Transit</span>
                </div>
                <div class="col-md-3 text-end">
                    <button class="btn btn-sm btn-primary mb-2">View Details</button>
                    <button class="btn btn-sm btn-outline-primary">Track</button>
                </div>
            </div>
        </div>

        <div class="order-card">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h6>Order #ORD-2024-003</h6>
                    <p class="text-muted mb-2">Placed on Nov 15, 2024</p>
                    <p class="mb-0"><strong>Items:</strong> Phone Screen Protector (3x ₹199)</p>
                </div>
                <div class="col-md-3">
                    <p><strong>Total:</strong> ₹618</p>
                    <span class="status-badge status-delivered">Delivered</span>
                </div>
                <div class="col-md-3 text-end">
                    <button class="btn btn-sm btn-primary mb-2">View Details</button>
                    <button class="btn btn-sm btn-outline-primary">Reorder</button>
                </div>
            </div>
        </div>

        <c:if test="${empty orders}">
            <div class="alert alert-info">
                <p>No orders yet. <a href="/shop">Start shopping</a></p>
            </div>
        </c:if>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-4 mt-5">
        <p>&copy; 2024 GoCart. All rights reserved.</p>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
