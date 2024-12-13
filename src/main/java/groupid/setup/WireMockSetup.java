package groupid.setup;

import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockSetup {

    public static void setupUserRegistrationStub() {
        // Set up a mock response for the user registration endpoint
        stubFor(post(urlEqualTo("/users/register"))
        .withRequestBody(containing("\"email\": \"testuser@example.com\"")) // Match request body for user registration
        .willReturn(aResponse()
                .withStatus(201) 
                .withHeader("Content-Type", "application/json")
                .withBody("{\n" +
                          "  \"message\": \"User registered successfully\",\n" +
                          "  \"userId\": 12345\n" +
                          "}")));

        // Mock response for user already exists (error case)
        stubFor(post(urlEqualTo("/users/register"))
                .withRequestBody(containing("\"email\": \"existinguser@example.com\"")) // Match email that already exists
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"error\": \"User already exists\"\n" +
                                "}")));
    }

    public static void setupUserLoginStub() {
        // Set up a mock response for the user login endpoint
        String validUsername = "Geetanjali_Malik";
        String validPassword = "securePassword123";
        WireMock.configureFor("localhost", 8080);
        WireMock.stubFor(post(urlEqualTo("/users/login"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{ \"status\": \"success\", \"message\": \"Login successful.\", \"userId\": \"12345\", \"authToken\": \""
                                        + GlobalUtil.generateJwtToken(validUsername) + "\" }")));

        // Mock response for invalid login (wrong credentials)
        WireMock.stubFor(post(urlEqualTo("/users/login"))
                .withRequestBody(matchingJsonPath("$.username", notMatching(validUsername)))
                .withRequestBody(matchingJsonPath("$.password", notMatching(validPassword)))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"status\": \"error\",\n" +
                                "  \"message\": \"Invalid credentials.\"\n" +
                                "}")));
    }

    // WireMock stub for /books?search=query endpoint
    public static void setupSearchBooksStub() {
        // Mock successful search response
        stubFor(get(urlPathMatching("/books"))
                .withQueryParam("search", matching(".*")) // Match any query string
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[\n" +
                                "  {\n" +
                                "    \"id\": 1,\n" +
                                "    \"title\": \"Effective Java\",\n" +
                                "    \"author\": \"Joshua Bloch\",\n" +
                                "    \"price\": 45.99\n" +
                                "  },\n" +
                                "  {\n" +
                                "    \"id\": 2,\n" +
                                "    \"title\": \"Clean Code\",\n" +
                                "    \"author\": \"Robert C. Martin\",\n" +
                                "    \"price\": 40.00\n" +
                                "  }\n" +
                                "]")));

        // Mock response for no search results found
        stubFor(get(urlPathMatching("/books"))
                .withQueryParam("search", matching(".*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]"))); // Empty array for no results
    }

     // WireMock stub for /users/{userId}/cart
     public static void setupAddToCartStub() {
        // Mocking successful add to cart with valid Bearer token
        stubFor(post(urlMatching("/users/"+GlobalUtil.userId+"/cart"))
                .withHeader("Authorization", matching("Bearer "+ GlobalUtil.token)) // Expecting a valid token
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                  "  \"message\": \"Item added to cart successfully\",\n" +
                                  "  \"cart\": {\n" +
                                  "    \"userId\": 12345,\n" +
                                  "    \"items\": [{\n" +
                                  "      \"bookId\": 101,\n" +
                                  "      \"quantity\": 1\n" +
                                  "    }]\n" +
                                  "  }\n" +
                                  "}")));

        // Mocking failed add to cart with invalid Bearer token
        stubFor(post(urlMatching("/users/"+GlobalUtil.userId+"/cart"))
                .withHeader("Authorization", matching("Bearer invalidToken456")) // Invalid token
                .willReturn(aResponse()
                        .withStatus(401) 
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                  "  \"error\": \"Unauthorized - Invalid token\"\n" +
                                  "}")));
    }

    // WireMock stub for /users/{userId}/checkout
    public static void setupCheckoutStub() {
        // Mocking successful checkout with valid Bearer token
        stubFor(post(urlMatching("/users/"+GlobalUtil.userId+"/checkout"))
                .withHeader("Authorization", matching("Bearer "+ GlobalUtil.token)) // Expecting a valid token
                .willReturn(aResponse()
                        .withStatus(200) 
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                  "  \"message\": \"Checkout successful!\",\n" +
                                  "  \"order\": {\n" +
                                  "    \"orderId\": 98765,\n" +
                                  "    \"totalAmount\": 99.99,\n" +
                                  "    \"items\": [{\n" +
                                  "      \"bookId\": 101,\n" +
                                  "      \"quantity\": 1\n" +
                                  "    }]\n" +
                                  "  }\n" +
                                  "}")));

        // Mocking failed checkout with invalid Bearer token
        stubFor(post(urlMatching("/users/"+GlobalUtil.userId+"/checkout"))
                .withHeader("Authorization", matching("Bearer invalidToken456")) // Invalid token
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                  "  \"error\": \"Unauthorized - Invalid token. Checkout failed.\"\n" +
                                  "}")));
    }

}
