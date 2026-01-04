# 🛒 GoCart - Full-Stack Java E-Commerce Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-green?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-NeonDB-blue?style=for-the-badge&logo=postgresql)
![Transactions](https://img.shields.io/badge/JDBC-Transactions-red?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A comprehensive full-stack multi-vendor e-commerce platform with JDBC Transaction Management, NeonDB PostgreSQL, and Strict MVC Architecture**

[Quick Start](#-quick-start) • [Installation](#-installation--setup) • [Features](#-features) • [Architecture](#-architecture) • [API Documentation](#-rest-api-endpoints) • [Deployment](#-deployment)

</div>

---

## 📋 Table of Contents

1. [⚡ Quick Start](#-quick-start)
2. [🛠️ Installation & Setup](#-installation--setup)
3. [✨ Features](#-features)
4. [🏗️ Architecture](#-architecture)
5. [💾 Database Configuration](#-database-configuration)
6. [🔌 REST API Endpoints](#-rest-api-endpoints)
7. [📁 Project Structure](#-project-structure)
8. [🧪 Testing](#-testing)
9. [📦 Deployment](#-deployment)
10. [🔐 Transaction Management](#-transaction-management)
11. [📚 Documentation](#-documentation)
12. [🤝 Contributing](#-contributing)
13. [📜 License](#-license)

---

## ✨ Features

### For Customers

✅ **Product Browsing** - Browse products by categories and stores
✅ **Shopping Cart** - Add/remove items with quantity management
✅ **Checkout** - Secure order placement with address management
✅ **Order Tracking** - View order history and status
✅ **User Authentication** - Register and login accounts
✅ **Ratings & Reviews** - Rate and review purchased products

### For Store Vendors

✅ **Store Management** - Create and manage your online store
✅ **Product Management** - Add, edit, delete products
✅ **Order Management** - View and manage customer orders
✅ **Sales Dashboard** - Analytics and sales tracking
✅ **Pricing Plans** - Multiple subscription tiers
✅ **Coupon Management** - Create discount coupons

### For Administrators

✅ **Admin Dashboard** - Platform-wide analytics
✅ **Vendor Approval** - Approve/reject store registrations
✅ **Store Management** - Oversee all stores
✅ **User Management** - Manage platform users
✅ **Commission Tracking** - Monitor revenue and commissions
✅ **Coupon Management** - Global coupon administration

---

## 🏗️ Architecture

```bash
┌─────────────────────────────────────────────┐
│     Frontend (JSP/Servlet Views)            │
│  - Home, Shop, Product Detail               │
│  - Cart, Checkout, Orders                   │
│  - Login, Register, Pricing                 │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│  Spring Boot Controllers & Services         │
│  - HomeController (Web Pages)               │
│  - REST Controllers (API)                   │
│  - Business Logic Services                  │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│  Spring Data JPA & Repositories             │
│  - Entity Mapping (Hibernate)               │
│  - Custom Queries                           │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│  PostgreSQL (NeonDB Serverless)             │
│  - User Accounts                            │
│  - Products & Stores                        │
│  - Orders & Ratings                         │
│  - Addresses & Coupons                      │
└─────────────────────────────────────────────┘
```

---

## ⚡ Quick Start

Get the application running in 3 minutes:

### Step 1: Set Environment Variables (Windows PowerShell)
```powershell
cd c:\Users\preet\Downloads\java3rd\ecommerce-java
.\setup-env.ps1
```

The script will:
- ✅ Set DATABASE_URL from your NeonDB credentials
- ✅ Set DB_USERNAME and DB_PASSWORD  
- ✅ Configure SERVER_PORT (8080)
- ✅ Ask if you want to run Maven

### Step 2: Run Application
```powershell
mvn spring-boot:run
```

Or if using the JAR:
```powershell
mvn clean package
java -jar target/gocart-0.1.0.jar
```

### Step 3: Access Application
- **Home Page:** http://localhost:8080/
- **Checkout:** http://localhost:8080/checkout
- **API Base:** http://localhost:8080/api/

**That's it! Application is running.** 🎉

---

## 🛠️ Installation & Setup

### Prerequisites

- **Java 17+** - Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/)
  ```powershell
  java --version
  ```

- **Maven 3.9+** - Download from [maven.apache.org](https://maven.apache.org/)
  ```powershell
  mvn --version
  ```

- **NeonDB Account** - Free at [neon.tech](https://neon.tech)
  - Create project and get connection string
  - Credentials: `neondb_owner` / password from dashboard

- **Git** (Optional) - For version control

### Full Installation Guide

#### 1️⃣ **Get NeonDB Connection Details**

1. Go to https://neon.tech
2. Sign in and create a project
3. In your project, go to **Connection details**
4. Copy the connection string (looks like: `postgresql://...@ep-xxx.neon.tech/neondb`)
5. Note username: `neondb_owner` and password from reset dialog

#### 2️⃣ **Navigate to Project Directory**

```powershell
cd c:\Users\preet\Downloads\java3rd\ecommerce-java
```

#### 3️⃣ **Configure Environment Variables Using Setup Script**

**Option A: Interactive Setup (Recommended)**
```powershell
.\setup-env.ps1
```

The script will guide you and ask if you want to run Maven automatically.

**Option B: Manual Setup**
```powershell
$env:DATABASE_URL = "postgresql://neondb_owner:PASSWORD@ep-xxx.neon.tech/neondb?sslmode=require"
$env:DB_USERNAME = "neondb_owner"
$env:DB_PASSWORD = "YOUR_PASSWORD"
$env:SERVER_PORT = "8080"
```

Replace with your actual NeonDB credentials.

**Option C: Quick Setup**
```powershell
.\setup-env-quick.ps1
```

(Edit the script first with your actual credentials)

#### 4️⃣ **Build the Project**

```powershell
mvn clean package -DskipTests
```

This will:
- Download all dependencies
- Compile Java source code
- Create executable JAR at `target/gocart-0.1.0.jar`
- Takes 2-5 minutes first time

#### 5️⃣ **Run the Application**

**Using Maven:**
```powershell
mvn spring-boot:run
```

**Using JAR:**
```powershell
java -jar target/gocart-0.1.0.jar
```

**Output should show:**
```
[INFO] GoCartApplication          : Starting GoCartApplication v0.1.0
[INFO] GoCartApplication          : Started GoCartApplication in 5.234 seconds
```

#### 6️⃣ **Verify Application is Running**

Open browser and visit:
- Home: http://localhost:8080/
- Shop: http://localhost:8080/shop
- Checkout: http://localhost:8080/checkout
- API: http://localhost:8080/api/products

**✅ Application is live!**

---

### 🛠️ Tech Stack

#### Backend
- **Framework:** Spring Boot 3.1.5
- **Language:** Java 17+
- **ORM:** Hibernate with Spring Data JPA
- **Build Tool:** Maven 3.9+
- **View Layer:** JSP with JSTL

#### Frontend
- **Template Engine:** JSP/JSTL
- **CSS:** Bootstrap 5.3
- **JavaScript:** Vanilla JS
- **Icons:** Bootstrap Icons

#### Database
- **Database:** PostgreSQL (via NeonDB)
- **Connection Pool:** HikariCP (10 connections)
- **Transaction Management:** Spring @Transactional
- **Isolation Level:** READ_COMMITTED

#### Additional
- **Logging:** SLF4J
- **Validation:** Hibernate Validator
- **JSON:** Jackson

---

### 📦 Database Configuration (NeonDB)

#### Connection Details
```yaml
# application.yml automatically reads from environment variables:
datasource:
  url: ${DATABASE_URL}                    # NeonDB connection string
  username: ${DB_USERNAME}                # neondb_owner
  password: ${DB_PASSWORD}                # Your password
  hikari:
    maximum-pool-size: 10                 # Connection pool size
    auto-commit: false                    # Transaction control
    transaction-isolation: TRANSACTION_READ_COMMITTED
```

#### What Happens on Startup
1. **Connection:** Spring connects to NeonDB using HikariCP
2. **Schema Creation:** Hibernate creates tables (if not exist) via `ddl-auto: update`
3. **Ready:** Application starts serving at http://localhost:8080

#### Tables Auto-Created
- `order_table` - Orders
- `order_item` - Order line items
- `product` - Products with inventory
- `coupon` - Discount coupons
- `user_table` - User accounts
- `store` - Stores
- `address` - Delivery addresses
- `rating` - Product ratings

---

### 🔄 Transaction Management Features

✅ **ACID Transactions** - All-or-nothing order placement
✅ **Atomic Operations** - Order + Inventory reduced together  
✅ **Automatic Rollback** - Errors revert all changes
✅ **Data Consistency** - No partial orders in database
✅ **Inventory Tracking** - Real-time stock management

**See [Transactions](#-transaction-management) section for details.**

---

## � Web Pages & Routes

| Page | Route | Purpose |
|------|-------|---------|
| **Home** | `/` | Landing page with featured products |
| **Shop** | `/shop` | Browse products with filters and search |
| **Product Detail** | `/product-detail?id={id}` | View detailed product info |
| **Cart** | `/cart` | Manage shopping cart items |
| **Checkout** | `/checkout` | Order finalization with summary |
| **Login** | `/login` | User authentication |
| **Register** | `/register` | User registration |
| **Orders** | `/orders` | View order history |
| **Pricing** | `/pricing` | Pricing and delivery information |
| **Health Check** | `/health` | System status and database connectivity |

---

## 🔐 Transaction Management

### What Are Transactions?

Transactions ensure data consistency by treating multiple database operations as a single unit:
- ✅ **All succeed** together (COMMIT)
- ❌ **All fail** together (ROLLBACK)
- No partial updates allowed

### Order Placement Atomicity

When a customer places an order, the system **atomically**:

```
START TRANSACTION
├─ Create Order record
├─ Create OrderItem records (line items)
├─ Reduce Product inventory
├─ Apply Coupon discount (if valid)
└─ END TRANSACTION
```

**If ANY step fails:**
- Order is not created
- Inventory is not reduced
- Database remains consistent

**Without transactions:** A power failure could create order but not reduce inventory = data corruption

### Implementation Details

#### OrderDAO.java (Transaction Boundary)
```java
@Transactional  // Spring manages commit/rollback
public Order createOrderWithInventoryReduction(
    User user,
    List<CartItem> cartItems,
    Coupon coupon) {
    
    // 1. Validate stock
    for (CartItem item : cartItems) {
        Product product = findProduct(item.getId());
        if (product.getQuantity() < item.getQuantity()) {
            throw new InsufficientStockException();  // ROLLBACK triggered
        }
    }
    
    // 2. Create order
    Order order = new Order();
    order.setUser(user);
    
    // 3. Create line items & reduce inventory
    for (CartItem item : cartItems) {
        OrderItem orderItem = new OrderItem(item, order);
        orderItemRepository.save(orderItem);
        
        Product product = findProduct(item.getId());
        product.reduceQuantity(item.getQuantity());  // Atomic update
        productRepository.save(product);
    }
    
    // 4. Apply coupon
    if (coupon != null) {
        order.setCoupon(coupon);
    }
    
    // 5. Save order
    orderRepository.save(order);
    
    // All operations succeed or all rollback
    return order;
}
```

#### Transaction Configuration (application.yml)
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
  datasource:
    hikari:
      auto-commit: false                # Explicit transaction control
      transaction-isolation: TRANSACTION_READ_COMMITTED
```

### Error Handling

```java
try {
    Order order = orderDAO.createOrderWithInventoryReduction(user, items, coupon);
    return ResponseEntity.ok(new OrderResponse("Order placed", order));
    
} catch (InsufficientStockException e) {
    // Transaction automatically rolled back
    return ResponseEntity.badRequest()
        .body(new ErrorResponse("Item out of stock"));
        
} catch (InvalidCouponException e) {
    // Transaction automatically rolled back
    return ResponseEntity.badRequest()
        .body(new ErrorResponse("Invalid coupon"));
}
```

### Isolation Levels

**READ_COMMITTED** (Default):
- Prevents "dirty reads" (reading uncommitted data)
- Allows non-repeatable reads
- Good for e-commerce (fast + safe enough)

```
Transaction 1: Read Price = $100
Transaction 2: Update Price = $150 (COMMITS)
Transaction 1: Read Price = $150 (NEW value) ✓ Non-repeatable
```

### Testing Transactions

#### Unit Test Example
```java
@Test
@Transactional
void testOrderCreationRollsBackOnInsufficientStock() {
    // Given
    User user = createUser();
    Product lowStockProduct = createProduct(quantity=1);
    List<CartItem> cartItems = Arrays.asList(
        new CartItem(lowStockProduct, quantity=5)  // Request 5, only 1 available
    );
    
    // When & Then
    assertThrows(InsufficientStockException.class, () -> {
        orderDAO.createOrderWithInventoryReduction(user, cartItems, null);
    });
    
    // Verify rollback: order not created, inventory unchanged
    assertTrue(orderRepository.findAll().isEmpty());
    assertEquals(1, lowStockProduct.getQuantity());
}
```

#### Integration Test
```java
@Test
@Transactional
void testCompleteCheckoutFlow() {
    // 1. Place order
    OrderResponse response = checkoutController.placeOrder(checkoutRequest);
    assertEquals(HttpStatus.OK, response.getStatus());
    
    // 2. Verify inventory reduced
    Product product = productRepository.findById(productId);
    assertEquals(originalQuantity - 2, product.getQuantity());
    
    // 3. Verify order created with items
    Order order = orderRepository.findById(response.getOrderId());
    assertEquals(1, order.getOrderItems().size());
}
```

### Common Issues & Solutions

**Issue:** `HibernateException: Detecting repeated column in mapping`
```
**Solution:** Ensure @Transactional is on service/DAO method, not controller
```

**Issue:** Orders created but inventory not reduced
```
**Solution:** Verify @Transactional annotation exists and database supports transactions
```

**Issue:** Slow order placement
```
**Solution:** Use SELECT ... FOR UPDATE to lock rows during transaction
```

---

## 🔌 REST API Endpoints

### Base URL: `http://localhost:8080/api`

### Users API
```
GET    /api/users              - Get all users
POST   /api/users              - Create new user
GET    /api/users/{id}         - Get user by ID
PUT    /api/users/{id}         - Update user
DELETE /api/users/{id}         - Delete user
```

### Products API
```
GET    /api/products           - Get all products
POST   /api/products           - Create product
GET    /api/products/{id}      - Get product by ID
PUT    /api/products/{id}      - Update product
DELETE /api/products/{id}      - Delete product
```

### Orders API
```
GET    /api/orders             - Get all orders
POST   /api/orders             - Create order
GET    /api/orders/{id}        - Get order by ID
PUT    /api/orders/{id}        - Update order
DELETE /api/orders/{id}        - Delete order
GET    /api/orders/user/{userId} - Get user's orders
```

### Stores API
```
GET    /api/stores             - Get all stores
POST   /api/stores             - Create store
GET    /api/stores/{id}        - Get store by ID
PUT    /api/stores/{id}        - Update store
DELETE /api/stores/{id}        - Delete store
GET    /api/stores/{username}  - Get store by username
```

### Ratings API
```
GET    /api/ratings            - Get all ratings
POST   /api/ratings            - Create rating
GET    /api/ratings/{id}       - Get rating by ID
PUT    /api/ratings/{id}       - Update rating
DELETE /api/ratings/{id}       - Delete rating
```

### Addresses API
```
GET    /api/addresses          - Get all addresses
POST   /api/addresses          - Create address
GET    /api/addresses/{id}     - Get address by ID
PUT    /api/addresses/{id}     - Update address
DELETE /api/addresses/{id}     - Delete address
```

### Coupons API
```
GET    /api/coupons            - Get all coupons
POST   /api/coupons            - Create coupon
GET    /api/coupons/{code}     - Get coupon by code
PUT    /api/coupons/{id}       - Update coupon
DELETE /api/coupons/{id}       - Delete coupon
```

### Example Requests

**Create a User:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "name": "John Doe",
    "phone": "9876543210"
  }'
```

**Get All Products:**
```bash
curl http://localhost:8080/api/products
```

**Create an Order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-uuid",
    "storeId": "store-uuid",
    "totalAmount": 1500,
    "status": "PENDING"
  }'
```

---

## 💾 Database Schema

### Tables

**Users Table**
- id (UUID, Primary Key)
- email (String, Unique)
- name (String)
- phone (String)
- created_at (Timestamp)
- updated_at (Timestamp)

**Products Table**
- id (UUID, Primary Key)
- store_id (UUID, Foreign Key)
- name (String)
- description (Text)
- price (Decimal)
- stock (Integer)
- category (String)
- created_at (Timestamp)

**Stores Table**
- id (UUID, Primary Key)
- owner_id (UUID, Foreign Key → Users)
- name (String)
- username (String, Unique)
- description (Text)
- created_at (Timestamp)

**Orders Table**
- id (UUID, Primary Key)
- user_id (UUID, Foreign Key → Users)
- store_id (UUID, Foreign Key → Stores)
- total_amount (Decimal)
- status (Enum: PENDING, CONFIRMED, DELIVERED, CANCELLED)
- created_at (Timestamp)

**Order Items Table**
- id (UUID, Primary Key)
- order_id (UUID, Foreign Key → Orders)
- product_id (UUID, Foreign Key → Products)
- quantity (Integer)
- price (Decimal)

**Ratings Table**
- id (UUID, Primary Key)
- user_id (UUID, Foreign Key → Users)
- product_id (UUID, Foreign Key → Products)
- rating (Integer, 1-5)
- comment (Text)
- created_at (Timestamp)

**Addresses Table**
- id (UUID, Primary Key)
- user_id (UUID, Foreign Key → Users)
- street (String)
- city (String)
- state (String)
- pin_code (String)
- is_default (Boolean)

**Coupons Table**
- id (UUID, Primary Key)
- code (String, Unique)
- discount_percent (Integer)
- max_uses (Integer)
- uses (Integer)
- valid_from (Date)
- valid_to (Date)

---

## 📁 Project Structure

```
gocart/
├── src/main/
│   ├── java/com/gocart/
│   │   ├── GoCartApplication.java          # Entry point
│   │   ├── controller/
│   │   │   ├── HomeController.java         # Web page routes
│   │   │   ├── UserController.java         # REST API
│   │   │   ├── ProductController.java
│   │   │   ├── OrderController.java
│   │   │   ├── StoreController.java
│   │   │   ├── RatingController.java
│   │   │   ├── AddressController.java
│   │   │   └── CouponController.java
│   │   ├── entity/                         # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Product.java
│   │   │   ├── Order.java
│   │   │   ├── Store.java
│   │   │   ├── Rating.java
│   │   │   ├── Address.java
│   │   │   ├── Coupon.java
│   │   │   └── OrderItem.java
│   │   ├── repository/                     # Spring Data JPA
│   │   │   ├── UserRepository.java
│   │   │   ├── ProductRepository.java
│   │   │   ├── OrderRepository.java
│   │   │   └── ...
│   │   ├── service/                        # Business Logic
│   │   │   ├── UserService.java
│   │   │   ├── ProductService.java
│   │   │   ├── OrderService.java
│   │   │   └── ...
│   │   ├── dto/                            # Data Transfer Objects
│   │   ├── exception/                      # Custom Exceptions
│   │   ├── config/                         # Configuration
│   │   │   └── WebConfig.java              # CORS & Web Configuration
│   │   └── util/                           # Utilities
│   ├── resources/
│   │   ├── application.yml                 # Configuration
│   │   └── db/migration/                   # Database migrations
│   └── webapp/WEB-INF/jsp/
│       ├── index.jsp                       # Home page
│       ├── shop.jsp                        # Shop page
│       ├── product-detail.jsp
│       ├── cart.jsp
│       ├── checkout.jsp
│       ├── orders.jsp
│       ├── login.jsp
│       ├── register.jsp
│       └── pricing.jsp
├── pom.xml                                 # Maven dependencies
├── .env                                    # Environment variables
├── .env.example                            # Environment template
└── README.md                               # This file
```

---

## 🔧 Configuration

### application.yml
```yaml
spring:
  application:
    name: gocart
  mvc:
    view:
      prefix: /WEB-INF/jsp/
      suffix: .jsp
  jpa:
    hibernate:
      ddl-auto: update
  datasource:
    url: ${DATABASE_URL}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5

server:
  port: ${SERVER_PORT:8080}
  servlet:
    context-path: /
```

### Environment Variables
```
DATABASE_URL=jdbc:postgresql://host:port/db?user=X&password=Y&sslmode=require
SERVER_PORT=8080
```

---

## 🧪 Testing

### Test Database Connection
```bash
curl http://localhost:8080/api/health
```

### Test a Web Page
```bash
curl http://localhost:8080/
```

### Test REST API
```bash
curl http://localhost:8080/api/products
```

---

## 📦 Deployment

### Create Executable JAR
```bash
mvn clean package
```

Output: `target/gocart-0.1.0.jar`

### Run JAR
```bash
java -jar gocart-0.1.0.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/gocart-0.1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build Docker image:
```bash
docker build -t gocart:latest .
```

Run Docker container:
```bash
docker run -p 8080:8080 \
  -e DATABASE_URL="jdbc:postgresql://..." \
  gocart:latest
```

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

See [CONTRIBUTING.md](./CONTRIBUTING.md) for details.

---

## 📜 License

This project is licensed under the MIT License - see [LICENSE.md](./LICENSE.md) file for details.

---

## 📞 Support

- **Issues:** [GitHub Issues](https://github.com/GreatStackDev/goCart/issues)
- **Documentation:** See this README
- **Email:** support@gocart.example.com

---

## 🎯 Roadmap

- [ ] Add payment gateway integration (Razorpay, Stripe)
- [ ] Implement email notifications
- [ ] Add JWT authentication
- [ ] Create mobile app
- [ ] Add inventory management
- [ ] Implement advanced analytics
- [ ] Add multi-currency support
- [ ] Create Swagger API documentation

---

<div align="center">

**Built with ❤️ using Java Spring Boot**

⭐ Star us on GitHub if you like this project!

</div>
