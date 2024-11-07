## How to run the service

Make sure you have Java installed on your system. 

Navigate to the Application Directory (OrderService)
   
Then run these below commands. 
```
./gradlew build
java -jar build/libs/OrderService-0.0.1-SNAPSHOT.jar
```

----



### Service Documentation: Order and Product Management System

This documentation provides an overview of the design, and API endpoints for the **Order Management System** and **Product Management System**. It details the REST API endpoints, their purpose, expected inputs, and outputs. The service handles the creation of orders, updating the status of orders, and managing product information, including adding products, updating their details, and reducing product quantities when orders are placed.

---

### **Overview**

The system utilizes the following key components:

- **Order Service**: Handles the business logic for creating and processing orders, including validation and status transitions.
- **Product Service**: Handles the business logic for managing products, including adding new products, updating product information, and reducing product quantities.

Both services interact with their respective repositories to persist and fetch data.

---

### **API Endpoints**

#### **Order API**

**Base URL**: `/api/orders`


1. **Add Order**

   - **URL**: `/api/orders/`
   - **Method**: `POST`
   - **Description**: Creates a new order by processing the provided order request.
   - **Request Body**:
     ```json
     {
       "productId": 1,
       "quantity": 2
     }
     ```
   - **Response**:
     - **Status Code**: `201 Created`
     - **Body**:
       ```json
       {
		    "id": 4,
		    "product": {
		        "id": 1,
		        "name": "TV pro",
		        "price": 10000.0,
		        "quantity": 99998
		    },
		    "quantity": 1,
		    "price": 10000.0,
		    "status": "NEW"
		}
       ```

2. **Update Order Status**

   - **URL**: `/api/orders/{orderId}/status`
   - **Method**: `PUT`
   - **Description**: Updates the status of an existing order.
   - **Request Parameters**:
     - `orderId`: The ID of the order to be updated.
     - `status`: The new status to set for the order (can be `NEW`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
   - **Response**:
     - **Status Code**: `200 OK`
     - **Body**:
       ```json
       {
		    "id": 1,
		    "product": {
		        "id": 1,
		        "name": "TV pro",
		        "price": 10000.0,
		        "quantity": 99998
		    },
		    "quantity": 1,
		    "price": 10000.0,
		    "status": "PROCESSING"
		}
       ```

#### **Product API**

**Base URL**: `/api/products`

1. **Add Product**

   - **URL**: `/api/products/`
   - **Method**: `POST`
   - **Description**: Adds a new product to the inventory.
   - **Request Body**:
     ```json
     {
       "name": "Product Name",
       "price": 99.99,
       "quantity": 100
     }
     ```
   - **Response**:
     - **Status Code**: `201 Created`
     - **Body**:
       ```json
       {
         "id": 1,
         "name": "Product Name",
         "price": 99.99,
         "quantity": 100
       }
       ```

2. **Update Product**

   - **URL**: `/api/products/{id}`
   - **Method**: `PUT`
   - **Description**: Updates the details of an existing product.
   - **Request Parameters**:
     - `id`: The ID of the product to update.
   - **Request Body**:
     ```json
     {
       "name": "Updated Product Name",
       "price": 79.99,
       "quantity": 150
     }
     ```
   - **Response**:
     - **Status Code**: `200 OK`
     - **Body**:
       ```json
       {
         "id": 1,
         "name": "Updated Product Name",
         "price": 79.99,
         "quantity": 150
       }
       ```

---

### **Service Design and Business Logic**

#### **Order Service**
The `OrderService` handles the business logic for creating and updating orders:

- **processOrderRequest(OrderRequest orderRequest)**: 
  - This method processes a new order request by validating the quantity, reducing the available stock of the ordered product, calculating the total price, and creating a new order.
  - If the requested quantity is less than 1, an exception is thrown.
  
- **updateOrderStatus(Long orderId, OrderStatus newStatus)**: 
  - This method allows updating the status of an order, validating that status transitions are allowed (e.g., an order in `NEW` status can only move to `PROCESSING` or `CANCELLED`).
  - An exception is thrown if an invalid status transition is attempted.

#### **Product Service**
The `ProductService` is responsible for managing products, including adding new products, updating product details, and reducing the stock of a product when an order is placed:

- **addProduct(ProductRequest productRequest)**: 
  - Adds a new product to the inventory, validating the request before saving the product.
  
- **updateProduct(Long id, ProductRequest productRequest)**: 
  - Updates an existing product's details (e.g., name, price, or quantity) based on the provided product request.
  
- **reduceProductQuantity(Long productId, int quantity)**: 
  - Reduces the available stock of a product by the specified quantity, ensuring that there is sufficient stock to fulfill the order. If not, an exception is thrown.

#### **Data Validation**
Both services include checks to validate the input data:

- **OrderService**:
  - Validates that the quantity is greater than zero before processing an order.
  - Ensures the status transition is valid before updating an order's status.

- **ProductService**:
  - Ensures that product names are not empty, prices and quantities are not negative, and sufficient stock is available before reducing quantities.

---

### **Error Handling**

The system uses `IllegalArgumentException` for invalid inputs or operations:

- Invalid product quantity or price.
- Insufficient product stock.
- Invalid status transitions for orders.
- Product or order not found by ID.

Error responses are returned with appropriate HTTP status codes and error messages.

---

### **Future Enhancements**

1. **User Integration**: Add a `Users` table to link each order to a specific user, enabling user-specific order tracking and management.

2. **Multiple Products per Order**: Allow orders to contain multiple products by introducing an `OrderItem` entity, enabling users to place more complex orders.

3. **Specific Exception Handling**: Implement more granular exceptions (e.g., `ProductNotFoundException`, `InsufficientStockException`) for better error handling and clarity.

4. **Product Service as a Microservice**: Move the product management service to its own microservice for better scalability and maintainability.

5. **Notification Service**: Introduce a notification service to send updates to users and sellers about order status and inventory changes.

6. **Logging**: Implement centralized logging (e.g., with ELK Stack or AWS CloudWatch) to monitor and troubleshoot system behavior.

7. **Unit Tests**: Develop unit tests for core logic to ensure reliability and catch regressions early.

8. **Environment Variables for DB Credentials**: Move database connection details to environment variables or services like AWS KMS for better security and easier configuration management.
