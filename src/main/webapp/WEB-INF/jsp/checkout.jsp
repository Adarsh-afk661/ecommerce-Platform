<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - GoCart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        .form-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .loading {
            display: none;
        }
        .spinner-border {
            margin-right: 8px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand fw-bold" href="/">🛒 GoCart</a>
        </div>
    </nav>

    <!-- Page Header -->
    <div class="bg-light py-4">
        <div class="container">
            <h1>Checkout</h1>
        </div>
    </div>

    <!-- Checkout Content -->
    <div class="container my-5">
        <div class="row">
            <div class="col-md-7">
                <!-- Shipping Address -->
                <div class="form-section">
                    <h5 class="mb-4">Shipping Address</h5>
                    <form id="addressForm">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">First Name</label>
                                <input type="text" class="form-control" id="firstName" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Last Name</label>
                                <input type="text" class="form-control" id="lastName" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" required>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">City</label>
                                <input type="text" class="form-control" id="city" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">State</label>
                                <input type="text" class="form-control" id="state" required>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">PIN Code</label>
                                <input type="text" class="form-control" id="pinCode" required>
                            </div>
                        </div>
                    </form>
                </div>

                <!-- Payment Method -->
                <div class="form-section">
                    <h5 class="mb-4">Payment Method</h5>
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="radio" name="payment" id="card" value="CARD" checked>
                        <label class="form-check-label" for="card">
                            Credit/Debit Card
                        </label>
                    </div>
                    <div class="form-check mb-3">
                        <input class="form-check-input" type="radio" name="payment" id="upi" value="UPI">
                        <label class="form-check-label" for="upi">
                            UPI
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="radio" name="payment" id="cod" value="COD">
                        <label class="form-check-label" for="cod">
                            Cash on Delivery
                        </label>
                    </div>
                </div>
            </div>

            <div class="col-md-5">
                <!-- Order Summary -->
                <div class="form-section">
                    <h5 class="mb-4">Order Summary</h5>
                    
                    <!-- Items List -->
                    <div id="itemsList"></div>
                    
                    <hr>
                    
                    <!-- Coupon Code -->
                    <div class="mb-3">
                        <label class="form-label">Apply Coupon Code (Optional)</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="couponCode" placeholder="Enter coupon code">
                            <button class="btn btn-outline-secondary" type="button" id="applyCouponBtn" onclick="applyCoupon()">
                                Apply
                            </button>
                        </div>
                        <small id="couponMessage" class="text-muted"></small>
                    </div>
                    
                    <hr>
                    
                    <!-- Price Details -->
                    <div class="d-flex justify-content-between mb-2">
                        <span>Subtotal:</span>
                        <span id="subtotal">₹0.00</span>
                    </div>
                    
                    <div class="d-flex justify-content-between mb-2" id="discountRow" style="display: none;">
                        <span>Discount:</span>
                        <span id="discount" class="text-success">₹0.00</span>
                    </div>
                    
                    <div class="d-flex justify-content-between mb-2">
                        <span>Tax (5%):</span>
                        <span id="tax">₹0.00</span>
                    </div>
                    
                    <div class="d-flex justify-content-between mb-3">
                        <span>Shipping:</span>
                        <span id="shipping" class="text-success">FREE</span>
                    </div>
                    
                    <hr>
                    
                    <div class="d-flex justify-content-between fw-bold">
                        <span>Total:</span>
                        <span class="h5" id="total">₹0.00</span>
                    </div>
                    
                    <button class="btn btn-primary w-100 mt-3" id="placeOrderBtn" onclick="placeOrder()">
                        Place Order
                    </button>
                    <button type="button" class="btn btn-outline-secondary w-100 mt-2" onclick="window.location.href='/cart'">
                        Back to Cart
                    </button>
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
        // Get cart items from localStorage or pass from backend
        let cartItems = JSON.parse(localStorage.getItem('cart')) || [];
        let checkoutData = null;

        // Initialize page
        document.addEventListener('DOMContentLoaded', function() {
            if (cartItems.length === 0) {
                alert('Your cart is empty!');
                window.location.href = '/shop';
                return;
            }
            
            displayCartItems();
            calculateCheckout();
        });

        /**
         * Display cart items in the order summary
         */
        function displayCartItems() {
            const itemsList = document.getElementById('itemsList');
            itemsList.innerHTML = '';

            cartItems.forEach((item, index) => {
                const itemTotal = item.price * item.quantity;
                itemsList.innerHTML += `
                    <div class="mb-3">
                        <div class="d-flex justify-content-between mb-1">
                            <span>${item.productName || 'Product'}</span>
                            <span>₹${item.price.toFixed(2)}</span>
                        </div>
                        <small class="text-muted">Quantity: ${item.quantity} × ₹${item.price.toFixed(2)} = ₹${itemTotal.toFixed(2)}</small>
                    </div>
                `;
            });
        }

        /**
         * Calculate checkout using backend API
         */
        function calculateCheckout() {
            // Disable UI while calculating
            setUILoading(true);

            const checkoutRequest = {
                items: cartItems.map(item => ({
                    productId: item.productId,
                    productName: item.productName,
                    price: item.price,
                    quantity: item.quantity,
                    storeId: item.storeId
                })),
                couponCode: document.getElementById('couponCode').value || null
            };

            fetch('/api/checkout/calculate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(checkoutRequest)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        throw new Error(error.message || 'Checkout calculation failed');
                    });
                }
                return response.json();
            })
            .then(data => {
                checkoutData = data;
                updateCheckoutDisplay(data);
                setUILoading(false);
            })
            .catch(error => {
                console.error('Error calculating checkout:', error);
                alert('Error calculating checkout: ' + error.message);
                setUILoading(false);
            });
        }

        /**
         * Update checkout display with calculated values
         */
        function updateCheckoutDisplay(data) {
            document.getElementById('subtotal').textContent = '₹' + (data.subtotal || 0).toFixed(2);
            document.getElementById('tax').textContent = '₹' + (data.tax || 0).toFixed(2);
            document.getElementById('shipping').textContent = data.shipping === 0 ? 'FREE' : '₹' + data.shipping.toFixed(2);
            document.getElementById('total').textContent = '₹' + (data.total || 0).toFixed(2);

            // Show discount if applicable
            if (data.discount && data.discount > 0) {
                document.getElementById('discountRow').style.display = 'flex';
                document.getElementById('discount').textContent = '₹' + data.discount.toFixed(2);
            } else {
                document.getElementById('discountRow').style.display = 'none';
            }
        }

        /**
         * Apply coupon code
         */
        function applyCoupon() {
            const couponCode = document.getElementById('couponCode').value.trim();
            if (!couponCode) {
                alert('Please enter a coupon code');
                return;
            }

            // Recalculate with coupon
            calculateCheckout();
        }

        /**
         * Place order
         */
        function placeOrder() {
            // Validate form
            const firstName = document.getElementById('firstName').value.trim();
            const lastName = document.getElementById('lastName').value.trim();
            const email = document.getElementById('email').value.trim();
            const address = document.getElementById('address').value.trim();
            const city = document.getElementById('city').value.trim();
            const state = document.getElementById('state').value.trim();
            const pinCode = document.getElementById('pinCode').value.trim();

            if (!firstName || !lastName || !email || !address || !city || !state || !pinCode) {
                alert('Please fill in all address fields');
                return;
            }

            if (!checkoutData) {
                alert('Please recalculate checkout first');
                calculateCheckout();
                return;
            }

            // Get selected payment method
            const paymentMethod = document.querySelector('input[name="payment"]:checked').value;

            // Prepare order request
            const orderRequest = {
                order: {
                    userId: localStorage.getItem('userId') || 'guest_' + Date.now(),
                    storeId: cartItems[0]?.storeId || 'default_store',
                    addressId: 'addr_' + Date.now(), // Would be created separately in production
                    paymentMethod: paymentMethod,
                    total: checkoutData.total,
                    isCouponUsed: checkoutData.couponCode ? true : false,
                    coupon: checkoutData.appliedCoupon ? JSON.stringify(checkoutData.appliedCoupon) : '{}'
                },
                items: cartItems.map(item => ({
                    productId: item.productId,
                    quantity: item.quantity,
                    price: item.price
                }))
            };

            // Disable button during submission
            const placeOrderBtn = document.getElementById('placeOrderBtn');
            placeOrderBtn.disabled = true;
            placeOrderBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Processing...';

            // Call backend API to place order
            fetch('/api/checkout/place-order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderRequest)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        throw new Error(error.message || 'Order placement failed');
                    });
                }
                return response.json();
            })
            .then(data => {
                // Clear cart
                localStorage.removeItem('cart');
                
                // Show success message
                alert('Order placed successfully! Order ID: ' + data.orderId);
                
                // Redirect to orders page
                window.location.href = '/orders';
            })
            .catch(error => {
                console.error('Error placing order:', error);
                alert('Error placing order: ' + error.message);
                
                // Re-enable button
                placeOrderBtn.disabled = false;
                placeOrderBtn.innerHTML = 'Place Order';
            });
        }

        /**
         * Set UI loading state
         */
        function setUILoading(isLoading) {
            const placeOrderBtn = document.getElementById('placeOrderBtn');
            if (isLoading) {
                placeOrderBtn.disabled = true;
                placeOrderBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Calculating...';
            } else {
                placeOrderBtn.disabled = false;
                placeOrderBtn.innerHTML = 'Place Order';
            }
        }
    </script>
</body>
</html>
