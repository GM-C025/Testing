package groupid.integration;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import groupid.setup.GlobalUtil;
import groupid.setup.MockApiServer;
import groupid.setup.WireMockSetup;

import static com.github.tomakehurst.wiremock.client.WireMock.getSettings;

import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class UserLoginTest {

    private MockApiServer mockApiServer;

    // Start the mock server and set up the stub before running the tests
    @BeforeClass
    public void setup() {
        mockApiServer = new MockApiServer();
        mockApiServer.startServer(); // Start the mock server

        // Set up WireMock stubs
        WireMockSetup.setupUserLoginStub();
    }

    // Stop the mock server after the tests have finished
    @AfterClass
    public void tearDown() {
        mockApiServer.stopServer(); // Stop the mock server
    }

    @Test(description = "login user", groups = { "smoke", "regression",
    "userLogin"},dependsOnGroups = "UserRegistration")
    public void testUserLoginSuccessful() {
        // Send a login request with correct credentials
        String loginRequestBody = "{ \"username\": \"Geetanjali_Malik\", \"password\": \"securePassword123\" }";

            Response response = RestAssured.given()
            .baseUri("http://localhost:8080")
            .basePath("/users/login")
            .header("Content-Type", "application/json")
            .body(loginRequestBody)
            .when()
            .post()
            .then()
            .statusCode(200)  // Validate that the response status code is 200
            .extract()
            .response();

        // Validate the response contains a JWT token
        GlobalUtil.token = response.jsonPath().getString("authToken");
        GlobalUtil.userId = response.jsonPath().getString("userId");
        Assert.assertNotNull(GlobalUtil.token, "JWT token should not be null");
    }

    @Test(description = "login user with invalid credentials", groups = { "regression"},dependsOnGroups = "UserRegistration")
    public void testInvalidLogin() {
        // login request payload with invalid credentials
        String loginRequestBody = "{ \"username\": \"john\", \"password\": \"wrongPassword\" }";

        Response response = RestAssured.given()
            .baseUri("http://localhost:8080")
            .basePath("/users/login")
            .header("Content-Type", "application/json")
            .body(loginRequestBody).log().all()
            .when()
            .post()
            .then().log().all()
            .statusCode(401)  // Validate that the response status code is 200
            .extract()
            .response();

        // Validate the error message
        String errorMessage = response.jsonPath().getString("message");
        Assert.assertEquals(errorMessage, "Invalid credentials.", "Expected error message is 'Invalid credentials.'");
    }

}
