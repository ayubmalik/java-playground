package ayub.virtualthreads;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpReverser implements Reverser {

    private final String baseUrl;
    private final HttpClient client;

    public HttpReverser(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(30))
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Override
    public String reverser(String source) {
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(baseUrl + "/api/reverse/" + source))
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
