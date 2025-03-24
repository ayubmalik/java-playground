package ayub.virtualthreads;

import static org.junit.jupiter.api.Assertions.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class HttpReverserTest {

    private WireMockServer wireMock;

    @BeforeEach
    void setup() {
        
        wireMock = new WireMockServer(8080);
        wireMock.start();
        configureFor("localhost", 8080);


        stubFor(get(urlPathMatching("/api/reverse/.*"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("{{ request.path.[2] | reverse }}")
                        .withTransformers("response-template")));
    }

    @Test
    void reverse_ShouldReturnStubbedResponse() {
        var httpReverser = new HttpReverser(wireMock.baseUrl());

        // Execute reverse call
        var response = httpReverser.reverser("test");

        // Verify the response matches the stubbed response
        assertEquals("stubbed-response", response);
    }

    @AfterEach
    void cleanup() {
        wireMock.stop();
    }

}