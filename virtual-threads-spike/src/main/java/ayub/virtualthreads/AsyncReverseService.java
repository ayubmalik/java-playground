package ayub.virtualthreads;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncReverseService implements ReverseService {
    private final SimpleReverser reverser;

    public AsyncReverseService(SimpleReverser reverser) {
        this.reverser = reverser;
    }

    @Override
    public List<String> reverse(List<String> sources) {
        var futures = sources.stream()
                .map(s -> supplyAsync(() -> reverser.reverse(s)))
                .toList();
        allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }
}
