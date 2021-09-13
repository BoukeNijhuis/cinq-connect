package nl.cinqict;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HandlerTest {

    Handler handler;

    @BeforeEach
    public void setup() {
        handler = new Handler();
    }

    @Test
    public void happyFlow() throws Exception {
        String output = handler.handle("2").reply;
        assertEquals("What is the second decimal of pi?", output);
    }

    @Test
    public void unhappyFlow() throws Exception {
        String output = handler.handle("-1").reply;
        assertEquals("This answer is incorrect. Use the back button to go back to the question.", output);
    }
}
