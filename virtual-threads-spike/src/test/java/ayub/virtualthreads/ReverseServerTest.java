package ayub.virtualthreads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ReverseServerTest {

    private ReverseServer server;

    @BeforeEach
    void setUp() {
        server = new ReverseServer();
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void reverse() {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:9090/api/reverse/hello"))
                .build();

        try (var client = HttpClient.newHttpClient()) {
            var result = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals("olleh", result.body());
        } catch (IOException | InterruptedException e) {
            fail(e.getMessage());
        }
    }
}