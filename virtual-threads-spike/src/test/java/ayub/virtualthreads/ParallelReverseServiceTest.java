package ayub.virtualthreads;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class ParallelReverseServiceTest {

    private final List<String> source = List.of("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog");
    private final List<String> expected = List.of("ehT", "kciuq", "nworb", "xof", "spmuj", "revo", "eht", "yzal", "god");

    private final ReverseService service = new ParallelReverseService(new SimpleReverser());

    @Test
    void reverse() {
        assertEquals(expected, service.reverse(source));
    }
}
