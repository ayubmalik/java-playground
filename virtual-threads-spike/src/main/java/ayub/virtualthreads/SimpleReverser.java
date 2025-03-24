package ayub.virtualthreads;

public class SimpleReverser implements Reverser {

    @Override
    public String reverser(String source) {
        var sb = new StringBuilder(source);
        return sb.reverse().toString();
    }
}
