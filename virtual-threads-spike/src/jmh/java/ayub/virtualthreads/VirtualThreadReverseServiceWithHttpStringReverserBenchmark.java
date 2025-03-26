package ayub.virtualthreads;

import org.openjdk.jmh.annotations.*;

import java.util.List;

@State(Scope.Benchmark)
public class VirtualThreadReverseServiceWithHttpStringReverserBenchmark {

    private final ReverseServer server = new ReverseServer();

    private final ReverseService service = new VirtualThreadReverseService(new HttpStringReverser("http://localhost:8080"));
    
    @Setup(Level.Trial)
    public void setup() {
        server.start();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        server.stop();
    }

    @Benchmark
    public void baseline() {
        // empty
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    public List<String> reverse() {
        return service.reverse(List.of("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"));
    }

}
