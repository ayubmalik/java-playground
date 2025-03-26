package ayub.virtualthreads;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class StringBuilderReverserBenchmark {

    private final StringReverser reverser = new StringBuilderReverser();

    @Benchmark
    public String reverse() {
        return reverser.reverse("Hello, world!");
    }

}
