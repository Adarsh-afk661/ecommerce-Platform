<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - GoCart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .product-image {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 400px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 2rem;
        }
        .price-section {
            margin: 20px 0;
        }
        .price-main {
            font-size: 2.5rem;
            color: #667eea;
            font-weight: bold;
        }
        .rating {
            color: #ffc107;
            margin: 10px 0;
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
                    <li class="nav-item"><a class="nav-link" href="/orders">Orders</a></li>
                    <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Product Detail -->
    <div class="container my-5">
        <div class="row">
            <div class="col-md-6">
                <div class="product-image">
                    ${product.name}
                </div>
            </div>
            <div class="col-md-6">
                <h1>${product.name}</h1>
                <div class="rating">
                    ★★★★☆ (4.5/5 - 128 reviews)
                </div>
                <div class="price-section">
                    <div class="price-main">₹${product.price}</div>
                    <p class="text-muted"><small>Stock: ${product.stock} available</small></p>
                </div>
                <div class="description mb-4">
                    <h5>Description</h5>
                    <p>${product.description}</p>
                </div>
                <div class="quantity-section mb-4">
                    <label for="qty">Quantity:</label>
                    <input type="number" id="qty" class="form-control" style="width: 100px;" value="1" min="1" max="${product.stock}">
                </div>
                <div class="action-buttons">
                    <button class="btn btn-primary btn-lg me-2" onclick="addToCart('${product.id}')">
                        🛒 Add to Cart
                    </button>
                    <button class="btn btn-outline-secondary btn-lg">
                        ❤ Wishlist
                    </button>
                </div>
                <div class="mt-4">
                    <p><strong>Category:</strong> Electronics</p>
                    <p><strong>Seller:</strong> GoCart Store</p>
                    <p><strong>Shipping:</strong> Free delivery on orders above ₹500</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Related Products -->
    <div class="bg-light py-5 mt-5">
        <div class="container">
            <h3 class="mb-4">Related Products</h3>
            <div class="row">
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Related Product 1</h5>
                            <p class="price-main">₹999</p>
                            <a href="#" class="btn btn-sm btn-primary">View</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-3 mb-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Related Product 2</h5>
                            <p class="price-main">₹1299</p>
                            <a href="#" class="btn btn-sm btn-primary">View</a>
                        </div>
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
    <script>
        function addToCart(productId) {
            const qty = document.getElementById('qty').value;
            alert(`Added ${qty} item(s) to cart!`);
        }
    </script>
</body>
</html>
