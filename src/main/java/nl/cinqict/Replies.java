package nl.cinqict;

import java.util.ArrayList;
import java.util.HashMap;

public class Replies {

    private ArrayList<KeyValue> map = new ArrayList<>();

    public HashMap<String, String> getHashMap() {
        var result = new HashMap<String, String>();
        for (KeyValue t: map) {
            result.put(t.key, t.value);
        }
        return result;
    }

    private class KeyValue {
        String key;
        String value;
    }
}
