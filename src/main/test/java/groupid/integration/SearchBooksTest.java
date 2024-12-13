package groupid.integration;
import groupid.setup.MockApiServer;
import groupid.setup.WireMockSetup;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SearchBooksTest {
    private MockApiServer mockApiServer;

    // Start the mock server and set up the stub before running the tests
    @BeforeClass
    public void setup() {
        mockApiServer = new MockApiServer();
        mockApiServer.startServer();  // Start the mock server
        
        // Set up WireMock stubs
        WireMockSetup.setupSearchBooksStub();
    }

    // Stop the mock server after the tests have finished
    @AfterClass
    public void tearDown() {
        mockApiServer.stopServer();  // Stop the mock server
    }

    // Test the serach book API
    @Test(description = "search books", groups = { "smoke", "regression",
    "books"})
    public void testSearchBooksSuccess() {
        String searchQuery = "Java";

        Response response = RestAssured.given()
                .contentType("application/json")
                .param("search", searchQuery).log().all()
                .get("http://localhost:8080/books");

        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");
        String responseBody = response.getBody().asString();
        System.out.println(responseBody);
        Assert.assertTrue(responseBody.contains("Effective Java"), "Response should contain 'Effective Java'");
        Assert.assertTrue(responseBody.contains("Clean Code"), "Response should contain 'Clean Code'");
    }

    @Test(description = "serch books with wrong query", groups = { "regression"})
    public void testSearchBooksNoResults() {
        // search query that returns no results
        String searchQuery = "NonExistentBook";

        Response response = RestAssured.given()
                .contentType("application/json")
                .param("search", searchQuery).log().all()
                .get("http://localhost:8080/books");

        // Assert response
        Assert.assertEquals(response.statusCode(), 200, "Expected status code 200");
        String responseBody = response.getBody().asString();
        Assert.assertEquals(responseBody, "[]", "Expected an empty array when no books are found.");
    }
}
