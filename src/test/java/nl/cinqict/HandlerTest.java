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
        assertEquals("Wat is de tiende decimaal van pi?", output);
    }

    @Test
    public void unhappyFlow() throws Exception {
        String output = handler.handle("-1").reply;
        assertEquals("Dit antwoord is incorrect. Gebruik de Terug knop om terug te gaan naar de vraag.", output);
    }
}
