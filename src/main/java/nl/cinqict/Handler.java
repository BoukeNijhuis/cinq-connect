package nl.cinqict;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Handler {

    private static final Object NOT_FOUND = "NOT_FOUND";
    private Map<String, String> map;

    public String handle(String input) {
        if (map == null) {
            try {
                InputStream inputStream = this.getClass().getResource("/replies.json").openStream();
                Replies questions = new Gson().fromJson(new InputStreamReader(inputStream), Replies.class);
                map = questions.getHashMap();
            } catch (IOException e) {
                return "could not load the questions: " + e.getMessage();
            }
        }
        String output = map.get(input);

        // no answer found
        if (output == null) {
            output = map.get(NOT_FOUND);
        }

        return output;
    }
}
