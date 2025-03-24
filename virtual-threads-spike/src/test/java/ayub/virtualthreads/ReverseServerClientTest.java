package ayub.virtualthreads;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReverseServerClientTest {

    private final ReverseServer server = new ReverseServer();

    private final ReverseServerClient client = new ReverseServerClient("http://localhost:9090");

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

    static Stream<? extends Arguments> reverseParams() {
        return Stream.of(
                new TestParam("example", "elpmaxe"),
                new TestParam("java", "avaj"),
                new TestParam("virtual", "lautriv"),
                new TestParam("threads", "sdaerht"),
                new TestParam("hello world!", "!dlrow olleh")
        ).map(Arguments::of);
    }

    record TestParam(String input, String expected) {
    }
}