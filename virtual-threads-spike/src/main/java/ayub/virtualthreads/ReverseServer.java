package ayub.virtualthreads;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ReverseServer {

    private final HttpServer server;

    public ReverseServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(9090), 1);
            this.server.createContext("/api/reverse", exchange -> {

                // Extract the last part of the path (after /api/reverse/)
                String[] parts = exchange.getRequestURI().getPath().split("/api/reverse/");
                String response;
                if (parts.length == 2 && !parts[1].isEmpty()) {
                    // Reverse the last part
                    String textToReverse = decode(parts[1], UTF_8);
                    response = new StringBuilder(textToReverse).reverse().toString();
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "Please provide a valid text to reverse in the format /api/reverse/<text>";
                    exchange.sendResponseHeaders(200, response.length());
                }

                // Send the response back to the client
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }

            });

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create server", e);
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
