<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pricing - GoCart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .pricing-card {
            transition: transform 0.3s ease;
            border: 2px solid #eee;
        }
        .pricing-card:hover {
            transform: translateY(-10px);
            border-color: #667eea;
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.2);
        }
        .pricing-card.featured {
            border-color: #667eea;
            transform: scale(1.05);
        }
        .price {
            font-size: 2rem;
            color: #667eea;
            font-weight: bold;
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
                    <li class="nav-item"><a class="nav-link active" href="/pricing">Pricing</a></li>
                    <li class="nav-item"><a class="nav-link" href="/cart">Cart</a></li>
                    <li class="nav-item"><a class="nav-link" href="/orders">Orders</a></li>
                    <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero -->
    <div class="bg-light py-5 text-center">
        <div class="container">
            <h1 class="mb-3">Store Seller Pricing</h1>
            <p class="lead text-muted">Choose the perfect plan for your online store</p>
        </div>
    </div>

    <!-- Pricing Cards -->
    <div class="container my-5">
        <div class="row g-4">
            <!-- Starter Plan -->
            <div class="col-md-4">
                <div class="card pricing-card">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-3">Starter</h5>
                        <div class="price mb-3">₹0<span class="text-muted" style="font-size: 1rem;">/month</span></div>
                        <ul class="list-unstyled mb-4">
                            <li class="mb-2">✓ 10 Products</li>
                            <li class="mb-2">✓ Basic Store</li>
                            <li class="mb-2">✓ Email Support</li>
                            <li class="mb-2">✓ 2% Transaction Fee</li>
                        </ul>
                        <button class="btn btn-outline-primary w-100">Get Started</button>
                    </div>
                </div>
            </div>

            <!-- Professional Plan -->
            <div class="col-md-4">
                <div class="card pricing-card featured">
                    <div class="badge bg-primary position-absolute top-0 start-50 translate-middle">Popular</div>
                    <div class="card-body p-4">
                        <h5 class="card-title mb-3">Professional</h5>
                        <div class="price mb-3">₹499<span class="text-muted" style="font-size: 1rem;">/month</span></div>
                        <ul class="list-unstyled mb-4">
                            <li class="mb-2">✓ Unlimited Products</li>
                            <li class="mb-2">✓ Advanced Store</li>
                            <li class="mb-2">✓ Priority Support</li>
                            <li class="mb-2">✓ 1% Transaction Fee</li>
                            <li class="mb-2">✓ Marketing Tools</li>
                        </ul>
                        <button class="btn btn-primary w-100">Choose Plan</button>
                    </div>
                </div>
            </div>

            <!-- Enterprise Plan -->
            <div class="col-md-4">
                <div class="card pricing-card">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-3">Enterprise</h5>
                        <div class="price mb-3">₹2499<span class="text-muted" style="font-size: 1rem;">/month</span></div>
                        <ul class="list-unstyled mb-4">
                            <li class="mb-2">✓ Unlimited Everything</li>
                            <li class="mb-2">✓ White Label Store</li>
                            <li class="mb-2">✓ 24/7 Support</li>
                            <li class="mb-2">✓ 0.5% Transaction Fee</li>
                            <li class="mb-2">✓ API Access</li>
                            <li class="mb-2">✓ Custom Domain</li>
                        </ul>
                        <button class="btn btn-outline-primary w-100">Contact Sales</button>
                    </div>
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
