package groupid.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import groupid.setup.GlobalUtil;
import groupid.setup.MockApiServer;
import groupid.setup.WireMockSetup;

public class CheckoutTest {
    private MockApiServer mockApiServer;

    // Start the mock server and set up the stub before running the tests
    @BeforeClass
    public void setup() {
        mockApiServer = new MockApiServer();
        mockApiServer.startServer();  // Start the mock server
        
        // Set up WireMock stubs
        WireMockSetup.setupCheckoutStub();
    }

    // Stop the mock server after the tests have finished
    @AfterClass
    public void tearDown() {
        mockApiServer.stopServer();  // Stop the mock server
    }

    @Test(description = "checkout", groups = { "smoke", "regression",
    "checkout"},dependsOnGroups = {"userLogin","addCart"})
    public void testCheckoutWithValidToken() {
        String requestBody = "{\n" +
                             "  \"paymentMethod\": \"creditCard\",\n" +
                             "  \"billingAddress\": \"123 Main St, Springfield, IL\",\n" +
                             "  \"shippingAddress\": \"123 Main St, Springfield, IL\"\n" +
                             "}";

        // Send POST request with valid Bearer token
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+ GlobalUtil.token)  // Add valid Authorization header
                .body(requestBody)
                .post("http://localhost:8080/users/"+GlobalUtil.userId+"/checkout");

        // Assert that the response contains the success message
        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200 for successful checkout");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Checkout successful!"), "Response should contain success message");
        Assert.assertTrue(responseBody.contains("orderId"), "Response should contain order ID");
        Assert.assertTrue(responseBody.contains("totalAmount"), "Response should contain total amount");
    }

    @Test(description = "checkout with invalid token", groups = {"regression"},dependsOnGroups = {"userLogin","addCart"})
    public void testCheckoutWithInvalidToken() {
        String requestBody = "{\n" +
                             "  \"paymentMethod\": \"creditCard\",\n" +
                             "  \"billingAddress\": \"123 Main St, Springfield, IL\",\n" +
                             "  \"shippingAddress\": \"123 Main St, Springfield, IL\"\n" +
                             "}";

        // Send POST request to with invalid Bearer token
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer invalidToken456")  // Add invalid Authorization header
                .body(requestBody)
                .post("http://localhost:8080/users/"+GlobalUtil.userId+"/checkout");

        // Assert that the response contains the error message
        Assert.assertEquals(response.statusCode(), 401, "Expected status code 401 for invalid token");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Unauthorized - Invalid token. Checkout failed."), 
                          "Response should contain error message for invalid token");
    }
}
