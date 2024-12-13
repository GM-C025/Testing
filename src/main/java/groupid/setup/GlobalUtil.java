package groupid.setup;

import java.util.UUID;

public class GlobalUtil {
    // Utility method to generate a simple JWT token for mock purposes
    public static String generateJwtToken(String username) {
        // Here we're simply using a UUID for the mock token.
        // In a real scenario, you would use a JWT library to sign and generate a token.
        return UUID.randomUUID().toString();
    }

    // variables

    public static String token = "";
    public static String userId = "";
}
