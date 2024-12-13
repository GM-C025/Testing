# Online Bookstore API Testing

## Project Overview

This project demonstrates the design, implementation, and automated testing of mock APIs for a hypothetical online bookstore. It includes the creation of mock API endpoints using **WireMock**, along with automated tests to validate these APIs using **TestNG** and **RestAssured**.

## Project Structure

The project structure is organized as follows:

### Folder Breakdown:

- **/main**: Contains the WireMock configuration and stub setup.
  - **/java/groupid/setup**: Contains the code that defines the mock APIs (like user registration, book search, etc.).
  - **/config**: Configuration files that define WireMock's behavior (e.g., port number, stubs).

- **/test/java/groupid/integration**: Contains all integration test files for validating the mock APIs, such as `UserRegistrationTest.java`, `SearchBooksTest.java`, etc.

## Installation

To set up this project locally, follow these steps:

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/online-bookstore-api-testing.git
cd online-bookstore-api-testing




