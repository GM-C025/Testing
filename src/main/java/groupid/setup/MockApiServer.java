package groupid.setup;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;

public class MockApiServer {

    private WireMockServer wireMockServer;

    public void startServer() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8080)); // Mock server on port 8080
        wireMockServer.start();
    }

    public void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop(); // Clean shutdown of the server
        }
    }

    public WireMockServer getWireMockServer() {
        return wireMockServer;
    }
}
