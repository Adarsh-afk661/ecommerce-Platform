# Architecture & Flow Diagrams

## 1. MVC Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER (JSP)                │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  checkout.jsp                                        │   │
│  │  ├─ Displays cart items                              │   │
│  │  ├─ Form for shipping address                        │   │
│  │  ├─ Payment method selection                         │   │
│  │  └─ Calls REST APIs (NO business logic)              │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              ↓↑
                      (REST API calls)
┌─────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                         │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  CheckoutController                                 │   │
│  │  ├─ POST /api/checkout/calculate                    │   │
│  │  ├─ POST /api/checkout/place-order                  │   │
│  │  ├─ GET /api/checkout/validate-product/:id          │   │
│  │  └─ GET /api/checkout/validate-coupon/:code         │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  OrderController                                     │   │
│  │  ├─ GET /api/orders                                 │   │
│  │  ├─ GET /api/orders/:id                             │   │
│  │  ├─ PUT /api/orders/:id                             │   │
│  │  └─ DELETE /api/orders/:id                          │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              ↓↑
                        (Method calls)
┌─────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  CheckoutService                                    │   │
│  │  ├─ calculateCheckout()          [Tax, Shipping]    │   │
│  │  ├─ validateProductAvailability() [Inventory check]│   │
│  │  └─ validateAndGetCoupon()       [Coupon validate]│   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  OrderService                                        │   │
│  │  ├─ createOrderWithInventoryManagement()            │   │
│  │  ├─ updateOrder()                                    │   │
│  │  └─ cancelOrderAndRestoreInventory()                │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  ProductService, CouponService, ...                 │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              ↓↑
                        (DAO calls)
┌─────────────────────────────────────────────────────────────┐
│                  DATA ACCESS LAYER (DAO)                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  OrderDAO [@Transactional]                          │   │
│  │  ├─ createOrderWithInventoryReduction()             │   │
│  │  │  └─ ATOMIC: Create + Reduce inventory            │   │
│  │  └─ cancelOrderAndRestoreInventory()                │   │
│  │     └─ ATOMIC: Cancel + Restore inventory           │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Repositories (OrderRepository, ProductRepository)  │   │
│  │  └─ CRUD operations                                 │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              ↓↑
                      (JPA/Hibernate)
┌─────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                           │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  NeonDB (PostgreSQL)                                │   │
│  │  ├─ order_table                                      │   │
│  │  ├─ order_item                                       │   │
│  │  ├─ product                                          │   │
│  │  ├─ coupon                                           │   │
│  │  └─ ... (other tables)                              │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

## 2. Order Placement Transaction Flow

```
┌──────────────────────────────────────────────────────────────────┐
│ STEP 1: USER INITIATES ORDER PLACEMENT                           │
│ - User fills shipping address                                    │
│ - Selects payment method                                         │
│ - Clicks "Place Order" button                                    │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 2: CHECKOUT.JSP VALIDATES FORM                              │
│ - Validates all required fields                                  │
│ - Prevents empty/invalid submissions                             │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 3: SEND POST /api/checkout/place-order                      │
│ Request body:                                                    │
│  {                                                               │
│    "order": {                                                    │
│      "userId": "user_123",                                       │
│      "storeId": "store_1",                                       │
│      "total": 47250,                                             │
│      ...                                                         │
│    },                                                            │
│    "items": [                                                    │
│      {"productId": "prod_1", "quantity": 1, "price": 50000}     │
│    ]                                                             │
│  }                                                               │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 4: CHECKOUTCONTROLLER.placeOrder() RECEIVES REQUEST         │
│ - Validates request body                                         │
│ - Validates all items exist and in stock                         │
│ - Returns 400 if validation fails                                │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 5: ORDERSERVICE.createOrderWithInventoryManagement()        │
│ - Ensures user exists                                            │
│ - Calls OrderDAO with order + items                              │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 6: ORDERDAO.createOrderWithInventoryReduction()             │
│ @Transactional boundary starts here                              │
│                                                                  │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │ TRANSACTION BEGINS                                          │ │
│ │                                                             │ │
│ │ 1. VALIDATE INVENTORY                                      │ │
│ │    └─ For each item: Check product exists and stock      │ │
│ │       If insufficient: throw RuntimeException             │ │
│ │                                                             │ │
│ │ 2. CREATE ORDER RECORD                                     │ │
│ │    └─ new Order(id, total, status=ORDER_PLACED, ...)     │ │
│ │    └─ entityManager.persist(order)                       │ │
│ │    └─ entityManager.flush()                              │ │
│ │                                                             │ │
│ │ 3. CREATE ORDER ITEMS                                      │ │
│ │    ├─ For each item:                                      │ │
│ │    │  ├─ new OrderItem(product, quantity, price)        │ │
│ │    │  ├─ item.setOrder(order)                            │ │
│ │    │  └─ entityManager.persist(item)                     │ │
│ │    └─ entityManager.flush()                              │ │
│ │                                                             │ │
│ │ 4. REDUCE INVENTORY                                        │ │
│ │    ├─ For each item:                                      │ │
│ │    │  ├─ Get Product from DB                             │ │
│ │    │  ├─ product.reduceQuantity(item.quantity)           │ │
│ │    │  │  ├─ quantity -= item.quantity                    │ │
│ │    │  │  └─ if quantity <= 0: inStock = false            │ │
│ │    │  └─ entityManager.merge(product)                    │ │
│ │    └─ entityManager.flush()                              │ │
│ │                                                             │ │
│ │ 5. COMMIT TRANSACTION                                      │ │
│ │    └─ All changes written to database atomically         │ │
│ │                                                             │ │
│ │ SUCCESS PATH:                                              │ │
│ │ ├─ Order created ✓                                        │ │
│ │ ├─ Order items created ✓                                  │ │
│ │ ├─ Inventory reduced ✓                                    │ │
│ │ └─ Database consistent ✓                                  │ │
│ │                                                             │ │
│ │ ERROR PATH (any step fails):                              │ │
│ │ ├─ Exception thrown ✗                                     │ │
│ │ ├─ AUTOMATIC ROLLBACK ↩️                                  │ │
│ │ ├─ All changes reverted                                   │ │
│ │ └─ Database unchanged ✓                                   │ │
│ │                                                             │ │
│ └─────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 7: RETURN RESPONSE                                          │
│                                                                  │
│ SUCCESS (201 CREATED):                                          │
│  {                                                               │
│    "orderId": "order_abc123",                                   │
│    "message": "Order placed successfully",                      │
│    "total": 47250                                               │
│  }                                                               │
│                                                                  │
│ ERROR - Insufficient Inventory (400 BAD REQUEST):               │
│  {                                                               │
│    "message": "Insufficient inventory for product: prod_1",    │
│    "statusCode": 400                                            │
│  }                                                               │
│                                                                  │
│ ERROR - Product Not Found (400 BAD REQUEST):                    │
│  {                                                               │
│    "message": "Product not found: prod_1",                     │
│    "statusCode": 400                                            │
│  }                                                               │
│                                                                  │
│ ERROR - Database Error (500 INTERNAL SERVER ERROR):             │
│  {                                                               │
│    "message": "Error placing order: ...",                       │
│    "statusCode": 500                                            │
│  }                                                               │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│ STEP 8: CHECKOUT.JSP RECEIVES RESPONSE                           │
│ - If success: Clear cart, redirect to /orders                   │
│ - If error: Display error message, allow retry                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## 3. Success vs Failure Scenarios

### Scenario A: Successful Order (Happy Path)

```
Database State BEFORE Order:
┌────────────────┐  ┌──────────────┐
│ Product: prod1 │  │ Orders Table │
│ quantity: 100  │  │ (empty)      │
└────────────────┘  └──────────────┘

Order Request:
  - Product: prod1, Quantity: 1, Price: 50000

Transaction Steps:
  [BEGIN TRANSACTION]
  1. Validate: prod1 exists? ✓ quantity >= 1? ✓ (100 >= 1)
  2. Create Order: id=order_1, total=50000
  3. Create OrderItem: product=prod1, qty=1, price=50000
  4. Reduce Inventory: prod1.quantity = 100 - 1 = 99
  [COMMIT]

Database State AFTER Order:
┌────────────────┐  ┌──────────────────────────┐
│ Product: prod1 │  │ Orders Table             │
│ quantity: 99   │  │ - order_1 (50000)        │
└────────────────┘  │ OrderItems:              │
                    │ - order_1, prod1, qty=1  │
                    └──────────────────────────┘

Result: ✅ CONSISTENT (Order + Inventory match)
```

---

### Scenario B: Insufficient Inventory (Failure)

```
Database State BEFORE Order:
┌────────────────┐  ┌──────────────┐
│ Product: prod1 │  │ Orders Table │
│ quantity: 2    │  │ (empty)      │
└────────────────┘  └──────────────┘

Order Request:
  - Product: prod1, Quantity: 10, Price: 50000

Transaction Steps:
  [BEGIN TRANSACTION]
  1. Validate: prod1 exists? ✓ quantity >= 10? ✗ (2 < 10)
  2. RuntimeException thrown
  [AUTOMATIC ROLLBACK] ↩️

Database State AFTER ROLLBACK:
┌────────────────┐  ┌──────────────┐
│ Product: prod1 │  │ Orders Table │
│ quantity: 2    │  │ (empty)      │
└────────────────┘  └──────────────┘

Result: ✅ CONSISTENT (No partial changes)
Response: 400 BAD REQUEST
  "Insufficient inventory for product: prod_1"
```

---

### Scenario C: Database Error During Commit (Failure)

```
Database State BEFORE Order:
┌────────────────┐  ┌──────────────┐
│ Product: prod1 │  │ Orders Table │
│ quantity: 100  │  │ (empty)      │
└────────────────┘  └──────────────┘

Order Request:
  - Product: prod1, Quantity: 1, Price: 50000

Transaction Steps:
  [BEGIN TRANSACTION]
  1. Validate: prod1 exists? ✓ quantity >= 1? ✓
  2. Create Order: id=order_1, total=50000
  3. Create OrderItem: product=prod1, qty=1
  4. Reduce Inventory: prod1.quantity = 99
  5. FLUSH to database → Database error (network issue)
  [AUTOMATIC ROLLBACK] ↩️

Database State AFTER ROLLBACK:
┌────────────────┐  ┌──────────────┐
│ Product: prod1 │  │ Orders Table │
│ quantity: 100  │  │ (empty)      │
└────────────────┘  └──────────────┘

Result: ✅ CONSISTENT (All changes reverted)
Response: 500 INTERNAL SERVER ERROR
  "Error placing order: Connection timeout"
```

---

## 4. Checkout Calculation Flow

```
User enters checkout page
        ↓
JavaScript loads cart from localStorage
        ↓
User applies coupon code (optional)
        ↓
POST /api/checkout/calculate
    {
      items: [
        {productId, productName, price, quantity, storeId},
        ...
      ],
      couponCode: "SAVE10"
    }
        ↓
CheckoutService.calculateCheckout()
    ├─ 1. Calculate Subtotal
    │  └─ For each item: price × quantity
    │     Total: $999 + $500 = $1,499
    │
    ├─ 2. Validate & Apply Coupon
    │  ├─ Check coupon exists
    │  ├─ Check not expired
    │  ├─ Check minimum order value
    │  └─ Calculate discount
    │     SAVE10 = 10% → $149.90
    │
    ├─ 3. Calculate Subtotal After Discount
    │  └─ $1,499 - $149.90 = $1,349.10
    │
    ├─ 4. Calculate Tax (5%)
    │  └─ $1,349.10 × 5% = $67.46 (rounded)
    │
    ├─ 5. Calculate Shipping
    │  ├─ If subtotal >= $100: FREE
    │  └─ Else: Standard shipping cost
    │     $1,349.10 > $100 → FREE
    │
    └─ 6. Calculate Total
       └─ $1,349.10 + $67.46 + $0 = $1,416.56
        ↓
Response:
{
  "subtotal": 1499.00,
  "discount": 149.90,
  "subtotalAfterDiscount": 1349.10,
  "tax": 67.46,
  "shipping": 0.00,
  "total": 1416.56,
  "couponCode": "SAVE10",
  "appliedCoupon": {
    "code": "SAVE10",
    "discount": 0.1,
    "description": "Save 10%",
    "expiresAt": "2025-12-31"
  }
}
        ↓
checkout.jsp displays formatted prices
        ↓
User clicks "Place Order"
```

---

## 5. Data Consistency Guarantee

```
KEY PRINCIPLE: ACID Transactions
═════════════════════════════════

A - Atomicity
  ├─ Order creation and inventory reduction are ONE unit
  ├─ Either both succeed or both fail
  └─ No partial orders in database
        ✓ GUARANTEED by @Transactional

C - Consistency
  ├─ Database rules always enforced
  ├─ Order must have items
  ├─ Inventory can't go negative
  └─ All FK constraints maintained
        ✓ GUARANTEED by transaction isolation

I - Isolation
  ├─ Concurrent orders don't interfere
  ├─ One order's transaction doesn't affect another
  └─ READ_COMMITTED prevents dirty reads
        ✓ GUARANTEED by transaction isolation level

D - Durability
  ├─ Once committed, changes survive crashes
  ├─ PostgreSQL writes to disk
  └─ No data loss
        ✓ GUARANTEED by PostgreSQL


BEFORE @Transactional:
─────────────────────

Order 1: 
  CREATE order_1 ✓
  CREATE order_item_1 ✓
  REDUCE inventory_1 ✓

Order 2 (same time):
  CREATE order_2 ✓
  CREATE order_item_2 ✓
  REDUCE inventory_2 ✓

Network error during order_1 inventory reduction:
  database still has order_1 + order_item_1
  but inventory NOT reduced!
  → INCONSISTENT STATE! 😞


AFTER @Transactional:
───────────────────

Order 1:
  [BEGIN TRANSACTION]
  CREATE order_1
  CREATE order_item_1
  REDUCE inventory_1
  [COMMIT] → All written together ✓
           → Or [ROLLBACK] → All reverted ✓

Network error during COMMIT:
  [AUTOMATIC ROLLBACK]
  order_1 NOT created
  inventory NOT changed
  → CONSISTENT STATE! 😊
```

---

## 6. Component Interaction Sequence

```
User          Browser        Server               Database
 │              │              │                      │
 ├──────────────→│ Fill form    │                      │
 │              │              │                      │
 │              │──Place Order→ │                      │
 │              │  POST /place  │                      │
 │              │              │ Validate items       │
 │              │              │ Check inventory      │
 │              │              │                      │
 │              │              │─ BEGIN TRANS ───────→│
 │              │              │ Create order        │
 │              │              │─────────────────────→│
 │              │              │ Create items        │
 │              │              │─────────────────────→│
 │              │              │ Reduce inventory    │
 │              │              │─────────────────────→│
 │              │              │ COMMIT ────────────→│
 │              │              │←── Success ─────────│
 │              │              │                      │
 │              │←─ 201 Created │                      │
 │              │  {orderId}    │                      │
 │←─ Success ───│              │                      │
 │              │              │                      │
```

---

## 7. Database Transaction Timeline

```
Time  Process A (User 1)           Process B (User 2)
────────────────────────────────────────────────────
 T0   BEGIN TRANSACTION             BEGIN TRANSACTION
      
 T1   CREATE Order A1               CREATE Order B1
      
 T2   READ Product qty=100          READ Product qty=100
      
 T3   CREATE OrderItem A1           CREATE OrderItem B1
      
 T4   UPDATE qty=99                 UPDATE qty=99
      
 T5   COMMIT                        COMMIT
      
 T6   ✓ Order A complete            ✓ Order B complete
      ✓ Inventory=98                ✓ Inventory=98
      (both reduced correctly)
      
      
ISOLATION LEVEL: READ_COMMITTED
────────────────────────────────
- Process A can't see B's uncommitted changes
- Prevents dirty reads
- Both see qty=100 before reducing
- After both COMMIT, qty=98 (correct)

ISOLATION LEVEL: SERIALIZABLE (stronger)
───────────────────────────────────────
- One process locks Product row
- Other waits for lock
- No possibility of both reading same qty
- Slower but safer
```

---

## Summary Diagram

```
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃            GOCART E-COMMERCE TRANSACTION FLOW            ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

┌─────────────────────────────────┐
│  JSP (Presentation)             │
│  - checkout.jsp                 │
│  - Pure UI (no logic)           │
└────────────┬────────────────────┘
             │ HTTP REST API calls
             ↓
┌─────────────────────────────────┐
│  Controller (Request Handler)   │
│  - CheckoutController           │
│  - Route + Basic validation     │
└────────────┬────────────────────┘
             │ Call service methods
             ↓
┌─────────────────────────────────┐
│  Service (Business Logic)       │
│  - CheckoutService              │
│  - OrderService                 │
│  - Calculations, rules          │
└────────────┬────────────────────┘
             │ Call DAO methods
             ↓
┌─────────────────────────────────┐
│  DAO (Transaction Manager)      │
│  - OrderDAO [@Transactional]    │
│  - ATOMIC operations            │
│  - Commit/Rollback logic        │
└────────────┬────────────────────┘
             │ JPA/Hibernate
             ↓
┌─────────────────────────────────┐
│  Database (NeonDB PostgreSQL)   │
│  - ACID guarantee               │
│  - Data persistence             │
└─────────────────────────────────┘

KEY FEATURES:
═════════════
✓ Strict separation of concerns
✓ Business logic in services
✓ Transactions in DAO layer
✓ Automatic rollback on errors
✓ Inventory consistency maintained
✓ REST API for frontend communication
```

---

**Document Version:** 1.0  
**Last Updated:** December 18, 2025
