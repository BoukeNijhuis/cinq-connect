package nl.cinqict;

import java.util.ArrayList;
import java.util.HashMap;

public class Replies {

    private ArrayList<KeyValue> map = new ArrayList<>();

    public HashMap<String, ReplyObject> getHashMap() {
        var result = new HashMap<String, ReplyObject>();
        for (KeyValue t: map) {
            result.put(t.key, t.value);
        }
        return result;
    }

    private class KeyValue {
        String key;
        ReplyObject value;
    }

    public static class ReplyObject {
        // TODO: assign order based on the order in the file?
        int order;
        String reply;
    }
}
