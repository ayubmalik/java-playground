package ayub.virtualthreads;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class AsyncServiceBenchmark {

    private final AsyncReverseService service = new AsyncReverseService(new SimpleReverser());

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    public void baseline() {
        // empty
    }

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    public List<String> reverse() {
        return service.reverse(List.of("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"));
    }

}
