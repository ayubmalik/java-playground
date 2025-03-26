package ayub.virtualthreads;

import org.openjdk.jmh.annotations.*;

import java.util.List;

@State(Scope.Benchmark)
public class AsyncReverseServiceWithStringBuilderReverserBenchmark {

    private final ReverseService service = new AsyncReverseService(new StringBuilderReverser());

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
