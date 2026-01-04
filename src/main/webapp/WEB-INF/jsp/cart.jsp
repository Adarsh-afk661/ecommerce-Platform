<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - GoCart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .cart-item {
            border-bottom: 1px solid #eee;
            padding: 20px 0;
        }
        .summary-box {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
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
                    <li class="nav-item"><a class="nav-link active" href="/cart">Cart</a></li>
                    <li class="nav-item"><a class="nav-link" href="/orders">Orders</a></li>
                    <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <div class="bg-light py-4">
        <div class="container">
            <h1>Shopping Cart</h1>
        </div>
    </div>

    <!-- Cart Content -->
    <div class="container my-5">
        <div class="row">
            <div class="col-md-8">
                <div class="cart-item">
                    <div class="row align-items-center">
                        <div class="col-md-2">
                            <img src="https://via.placeholder.com/100" class="img-fluid" alt="Product">
                        </div>
                        <div class="col-md-4">
                            <h6>Product Name</h6>
                            <p class="text-muted">Item ID: 12345</p>
                        </div>
                        <div class="col-md-2 text-center">
                            <input type="number" class="form-control" value="1" min="1" style="width: 70px;">
                        </div>
                        <div class="col-md-2 text-center">
                            <p class="fw-bold">₹999</p>
                        </div>
                        <div class="col-md-2 text-end">
                            <button class="btn btn-sm btn-danger">Remove</button>
                        </div>
                    </div>
                </div>

                <div class="mt-4">
                    <a href="/shop" class="btn btn-outline-primary">Continue Shopping</a>
                </div>
            </div>

            <div class="col-md-4">
                <div class="summary-box">
                    <h5 class="mb-3">Order Summary</h5>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Subtotal:</span>
                        <span>₹999</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Shipping:</span>
                        <span class="text-success">Free</span>
                    </div>
                    <div class="d-flex justify-content-between mb-2">
                        <span>Tax:</span>
                        <span>₹50</span>
                    </div>
                    <hr>
                    <div class="d-flex justify-content-between fw-bold">
                        <span>Total:</span>
                        <span class="h5">₹1049</span>
                    </div>
                    <a href="/checkout" class="btn btn-primary w-100 mt-3">Proceed to Checkout</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-4 mt-5">
        <p>&copy; 2024 GoCart. All rights reserved.</p>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
