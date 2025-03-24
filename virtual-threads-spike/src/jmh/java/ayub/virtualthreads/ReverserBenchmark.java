package ayub.virtualthreads;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ReverserBenchmark {

    private final SimpleReverser reverser = new SimpleReverser();

    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    public String reverse() {
        return reverser.reverser("Hello, world!");
    }

}
