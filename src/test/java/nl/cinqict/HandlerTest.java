package nl.cinqict;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandlerTest {

    @Test
    public void testHandler() throws Exception {

        Handler handler = new Handler();
        String output = handler.handle("2");
        assertEquals("Wat is de tiende decimaal van pi?", output);
    }
}
