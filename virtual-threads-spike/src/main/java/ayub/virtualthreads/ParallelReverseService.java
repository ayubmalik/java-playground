package ayub.virtualthreads;

import java.util.List;

public class ParallelReverseService implements ReverseService {
    private final SimpleReverser reverser;

    public ParallelReverseService(SimpleReverser reverser) {
        this.reverser = reverser;
    }

    @Override
    public List<String> reverse(List<String> sources) {
        return sources.parallelStream().
                map(reverser::reverse)
                .toList();
    }
}
