package ayub.virtualthreads;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringBuilderReverserTest {

    @Test
    void reverse() {
        assertEquals("olleH", new StringBuilderReverser().reverse("Hello"));
        assertEquals("!mum olleH", new StringBuilderReverser().reverse("Hello mum!"));
    }

}