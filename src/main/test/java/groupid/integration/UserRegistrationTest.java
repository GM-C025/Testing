package groupid.integration;

import groupid.setup.MockApiServer;
import groupid.setup.WireMockSetup;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserRegistrationTest {

    private MockApiServer mockApiServer;

    // Start the mock server and set up the stub before running the tests
    @BeforeClass
    public void setup() throws InterruptedException {
        mockApiServer = new MockApiServer();
        mockApiServer.startServer(); // Start the mock server

        // Set up WireMock stubs (e.g., user registration endpoint)
        WireMockSetup.setupUserRegistrationStub();
    }

    // Stop the mock server after the tests have finished
    @AfterClass
    public void tearDown() {
        mockApiServer.stopServer(); // Stop the mock server
    }

    // Test the user registration API
    @Test(description = "user registration", groups = { "smoke", "regression",
    "UserRegistration"})
    public void testUserRegistration() {
        // Define the request body
        String requestBody = "{\n" +
                "  \"email\": \"testuser@example.com\",\n" +
                "  \"password\": \"securePassword123\",\n" +
                "  \"name\": \"Geetanjali_Malik\"\n" +
                "}";

        // Make a POST request to the mock server
        Response response = RestAssured.given()
                .baseUri("http://localhost:8080")
                .basePath("/users/register")
                .header("Content-Type", "application/json").log().all()
                .body(requestBody)
                .when()
                .post()
                .then().log().all()
                .statusCode(201) // Validate that the response status code is 200
                .extract()
                .response();

        // Validate the response body
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("User registered successfully"));
        Assert.assertTrue(responseBody.contains("userId"));
    }

    @Test(description = "user registration with existing user", groups = {"regression"})
    public void testUserRegistrationUserExistsError() {
        // Define the request body with an existing user
        String requestBody = "{\n" +
                "  \"email\": \"existinguser@example.com\",\n" +
                "  \"password\": \"securePassword123\",\n" +
                "  \"name\": \"Geetanjali_Malik\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri("http://localhost:8080")
                .basePath("/users/register")
                .header("Content-Type", "application/json").log().all()
                .body(requestBody)
                .when()
                .post()
                .then().log().all()
                .statusCode(400) // Validate that the response status code is 200
                .extract()
                .response();

        // Assert that the response contains the error message
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("User already exists"), "Response should contain error message");
    }
}
