# API Specifications for Online Bookstore

## 1. User Registration
- **Endpoint**: `POST /users/register`
- **Description**: Registers a new user.
- **Request Schema**:
    ```json
    {
      "username": "john_doe",
      "password": "securePassword123",
      "email": "john.doe@example.com"
    }
    ```
- **Response (Success)**:
    ```json
    {
      "status": "success",
      "message": "User successfully registered.",
      "userId": "12345"
    }
    ```
- **Response (Error)**:
    ```json
    {
      "status": "error",
      "message": "Username already exists."
    }
    ```
- **Authentication**: None required for registration.

---

## 2. User Login
- **Endpoint**: `POST /users/login`
- **Description**: Logs a user into the system.
- **Request Schema**:
    ```json
    {
      "username": "john_doe",
      "password": "securePassword123"
    }
    ```
- **Response (Success)**:
    ```json
    {
      "status": "success",
      "message": "Login successful.",
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTIzNDUiLCJleHBpcmVkX2FjdGl2ZSI6Il9N...}"
    }
    ```
- **Response (Error)**:
    ```json
    {
      "status": "error",
      "message": "Invalid credentials."
    }
    ```
- **Authentication**: None required for login.

---

## 3. Search Books
- **Endpoint**: `GET /books?search={query}`
- **Description**: Searches for books based on a query parameter.
- **Query Parameters**:
    - `search`: (string) Search query for books.
  
- **Response (Success)**:
    ```json
    {
      "status": "success",
      "books": [
        {
          "id": "123",
          "title": "The Great Gatsby",
          "author": "F. Scott Fitzgerald",
          "price": 9.99
        },
        {
          "id": "124",
          "title": "1984",
          "author": "George Orwell",
          "price": 7.99
        }
      ]
    }
    ```

- **Response (Error)**:
    ```json
    {
      "status": "error",
      "message": "No books found."
    }
    ```
- **Authentication**: Optional.

---

## 4. Add to Cart
- **Endpoint**: `POST /users/{userId}/cart`
- **Description**: Adds a book to the user's cart.
- **Request Schema**:
    ```json
    {
      "bookId": "123",
      "quantity": 2
    }
    ```
- **Response (Success)**:
    ```json
    {
      "status": "success",
      "message": "Book added to cart."
    }
    ```
- **Response (Error)**:
    ```json
    {
      "status": "error",
      "message": "Book not found."
    }
    ```
- **Authentication**: Required (JWT token).

---

## 5. Checkout
- **Endpoint**: `POST /users/{userId}/checkout`
- **Description**: Completes the checkout process for the user.
- **Request Schema**:
    ```json
    {
      "paymentMethod": "credit_card",
      "billingAddress": "123 Main Street"
    }
    ```
- **Response (Success)**:
    ```json
    {
      "status": "success",
      "message": "Order placed successfully.",
      "orderId": "987654"
    }
    ```
- **Response (Error)**:
    ```json
    {
      "status": "error",
      "message": "Payment failed."
    }
    ```
- **Authentication**: Required (JWT token).
---

## General Error Codes
- **400 Bad Request**: The request could not be understood by the server due to malformed syntax.
- **401 Unauthorized**: Authentication is required and has either failed or not been provided.
- **403 Forbidden**: The request is understood, but it has been refused or access is not allowed.
- **404 Not Found**: The requested resource could not be found.
- **500 Internal Server Error**: A generic error message indicating an unexpected server issue.

---

## Authentication
For endpoints that require authentication (e.g., `Add to Cart`, `Checkout`), the system uses **JWT tokens**.
