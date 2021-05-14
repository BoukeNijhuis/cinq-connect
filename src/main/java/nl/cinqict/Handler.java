package nl.cinqict;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Handler {

    private Map<String, String> map;

    public String handle(String input) {
        if (map == null) {
            try {
                InputStream inputStream = this.getClass().getResource("/questions.json").openStream();
                Questions questions = new Gson().fromJson(new InputStreamReader(inputStream), Questions.class);
                map = questions.getHashMap();
            } catch (IOException e) {
                return "could not load the questions: " + e.getMessage();
            }
        }
        return map.get(input);
    }
}
