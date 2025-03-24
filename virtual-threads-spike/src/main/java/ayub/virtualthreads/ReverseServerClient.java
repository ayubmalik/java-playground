package ayub.virtualthreads;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ReverseServerClient implements Reverser {

    private final String baseUrl;
    private final HttpClient client;

    public ReverseServerClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(30))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Override
    public String reverse(String source) {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseUrl + "/api/reverse/" + encode(source, UTF_8)))
                .build();
        try {
            var response = client.send(request, BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
