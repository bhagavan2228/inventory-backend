# Inventory Management System (Spring Boot)

A backend Inventory Management System built with Spring Boot that supports
product management, stock tracking, role-based access control, and stock
movement history.

The system is designed to simulate a real-world inventory backend used in
retail or warehouse environments.
## Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA
- Hibernate
- MySQL
- Maven
## Features

- User authentication using JWT
- Role-based authorization (ADMIN, STAFF, VIEWER)
- Product CRUD operations
- Automatic inventory creation for products
- Stock In / Stock Out operations
- Stock movement history tracking
- Low stock alerts
- Pagination and search functionality
## Roles & Access Control

| Role   | Permissions |
|------|------------|
| ADMIN | Full access (create, delete products, stock operations) |
| STAFF | Stock in / stock out, view products |
| VIEWER | Read-only access |

Security is implemented using Spring Security with JWT-based authentication.
## Setup & Run

### Prerequisites
- Java 17+
- MySQL
- Maven

### Database Configuration
Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
mvn clean install
mvn spring-boot:run
http://localhost:8084

---

# 🟣 STEP 1.6 — API ENDPOINTS (VERY IMPORTANT)

```md
## API Endpoints

### Authentication
| Method | Endpoint | Description |
|------|--------|------------|
| POST | /auth/login | Login and get JWT |

### Products
| Method | Endpoint | Role |
|------|--------|------|
| GET | /products | ADMIN, STAFF, VIEWER |
| POST | /products | ADMIN |
| DELETE | /products/{id} | ADMIN |
| GET | /products/search | ADMIN, STAFF, VIEWER |

### Inventory
| Method | Endpoint | Role |
|------|--------|------|
| POST | /products/{id}/stock-in | ADMIN, STAFF |
| POST | /products/{id}/stock-out | ADMIN, STAFF |
| GET | /products/{id}/stock-history | ADMIN, STAFF, VIEWER |
| GET | /products/low-stock | ADMIN, STAFF, VIEWER |
## Database Design

Main entities:
- Product
- Category
- Inventory
- StockMovement
- User

Each product has exactly one inventory record.
Stock movements are recorded for every stock-in and stock-out operation.
## Future Improvements

- Reporting APIs (sales & inventory summary)
- Docker support
- Swagger / OpenAPI documentation
- Cloud deployment (AWS / Render)
- Unit & integration tests
