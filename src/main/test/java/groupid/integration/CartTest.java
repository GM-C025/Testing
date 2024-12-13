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

public class CartTest {

    private MockApiServer mockApiServer;

    // Start the mock server and set up the stub before running the tests
    @BeforeClass
    public void setup() {
        mockApiServer = new MockApiServer();
        mockApiServer.startServer();  // Start the mock server
        
        // Set up WireMock stubs
        WireMockSetup.setupAddToCartStub();
    }

    // Stop the mock server after the tests have finished
    @AfterClass
    public void tearDown() {
        mockApiServer.stopServer();  // Stop the mock server
    }

    @Test(description = "add to cart", groups = { "smoke", "regression",
    "addCart"},dependsOnGroups = "userLogin")
    public void testAddToCartWithValidToken() {
        // Define the JSON request body for adding an item to the cart
        String requestBody = "{\n" +
                             "  \"bookId\": 101,\n" +
                             "  \"quantity\": 1\n" +
                             "}";

        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer "+ GlobalUtil.token)  // Add valid Authorization header
                .body(requestBody)
                .post("http://localhost:8080/users/"+GlobalUtil.userId+"/cart"); 

        // Assert that the response contains the success message
        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200 for valid token");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Item added to cart successfully"), "Response should contain success message");
        Assert.assertTrue(responseBody.contains("cart"), "Response should contain the cart details");
    }

    @Test(description = "add to cart with invalid token", groups = {"regression"},dependsOnGroups = "userLogin")
    public void testAddToCartWithInvalidToken() {
        String requestBody = "{\n" +
                             "  \"bookId\": 101,\n" +
                             "  \"quantity\": 1\n" +
                             "}";

        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer invalidToken456")  // Add invalid Authorization header
                .body(requestBody)
                .post("http://localhost:8080/users/"+GlobalUtil.userId+"/cart"); 

        // Assert that the response contains the error message
        Assert.assertEquals(response.statusCode(), 401, "Expected status code 401 for invalid token");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Unauthorized - Invalid token"), "Response should contain error message for invalid token");
    }
}
