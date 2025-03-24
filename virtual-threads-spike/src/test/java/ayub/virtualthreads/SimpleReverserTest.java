package ayub.virtualthreads;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SimpleReverserTest {

    @Test
    void reverse() {
        assertEquals("olleH", new SimpleReverser().reverser("Hello"));
        assertEquals("!mum olleH", new SimpleReverser().reverser("Hello mum!"));
    }

}