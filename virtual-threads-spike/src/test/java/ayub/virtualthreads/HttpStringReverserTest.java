package ayub.virtualthreads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpStringReverserTest {

    private final ReverseServer server = new ReverseServer();

    private final HttpStringReverser client = new HttpStringReverser("http://localhost:9090");

    @BeforeEach
    void setup() {
        server.start();
    }

    @AfterEach
    void cleanup() {
        server.stop();
    }

    @ParameterizedTest
    @MethodSource("reverseParams")
    void reverseParameterized(TestParam param) {
        var actual = client.reverse(param.input());
        assertEquals(param.expected(), actual);
    }

    static Stream<TestParam> reverseParams() {
        return Stream.of(
                new TestParam("example", "elpmaxe"),
                new TestParam("java", "avaj"),
                new TestParam("virtual", "lautriv"),
                new TestParam("threads", "sdaerht"),
                new TestParam("hello world!", "!dlrow olleh")
        );
    }

    record TestParam(String input, String expected) {
    }
}