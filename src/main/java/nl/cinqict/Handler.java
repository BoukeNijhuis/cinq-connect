package nl.cinqict;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Handler {

    private Map<String, String> map = new HashMap<>();

    public Handler() {
        setup();
    }

    private void setup() {

        try {

            InputStream inputStream = this.getClass().getResource("/questions.json").openStream();
            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(new InputStreamReader(inputStream));
            JsonArray jsonArray = (JsonArray) jsonObject.get("map");

            // ugly!

            for (int i = 0; i < jsonArray.size(); i++) {

                Map<String, String> pair = jsonArray.getMap(i);
                System.out.println(pair);
                map.put(pair.get("oldAnswer"), pair.get("newQuestion"));
            }

        }  catch (IOException | JsonException e) {
            e.printStackTrace();
        }

        System.out.println(map);
    }

    public String handle(String input) {
        return map.get(input);
    }
}
