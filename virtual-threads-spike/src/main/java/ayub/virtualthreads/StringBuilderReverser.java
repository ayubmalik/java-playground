package ayub.virtualthreads;

public class StringBuilderReverser implements StringReverser {

    @Override
    public String reverse(String source) {
        var sb = new StringBuilder(source);
        return sb.reverse().toString();
    }
}
