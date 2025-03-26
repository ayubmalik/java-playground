package ayub.virtualthreads;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VirtualThreadReverseService implements ReverseService {

    private final StringReverser reverser;

    public VirtualThreadReverseService(StringReverser reverser) {
        this.reverser = reverser;
    }

    @Override
    public List<String> reverse(List<String> sources) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = submitAll(executor, sources);
            return futures.stream()
                    .map(this::getOrThrow)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getOrThrow(Future<String> f) {
        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Future<String>> submitAll(ExecutorService executor, List<String> sources) {
        return sources.stream()
                .map(s -> executor.submit(() -> reverser.reverse(s)))
                .toList();
    }
}
