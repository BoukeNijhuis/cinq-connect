package nl.cinqict;

import java.util.ArrayList;
import java.util.HashMap;

public class Questions {

    private ArrayList<Tuple> map = new ArrayList<>();

    public HashMap<String, String> getHashMap() {
        var result = new HashMap<String, String>();
        for (Tuple t: map) {
            result.put(t.oldAnswer, t.newQuestion);
        }
        return result;
    }

    private class Tuple {
        String oldAnswer;
        String newQuestion;
    }
}
