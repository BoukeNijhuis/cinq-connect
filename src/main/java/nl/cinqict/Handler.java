package nl.cinqict;

import com.google.gson.Gson;
import nl.cinqict.Replies.ReplyObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Handler {

    private static final Object NOT_FOUND = "NOT_FOUND";

    public ReplyObject handle(String input) {

        Map<String, ReplyObject> map;
        try (InputStream inputStream = getInputStream()) {
            Replies replies = getReplies(inputStream);
            map = replies.getHashMap();
        } catch (IOException e) {
            ReplyObject questionObject = new ReplyObject();
            questionObject.reply = "could not load the questions: " + e.getMessage();
            return questionObject;
        }
        ReplyObject questionObject = map.get(input);

        // no answer found
        if (questionObject == null) {
            questionObject = map.get(NOT_FOUND);
        }

        return questionObject;
    }

    private Replies getReplies(InputStream inputStream) {
        return new Gson().fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), Replies.class);
    }

    private InputStream getInputStream() throws IOException {
        return this.getClass().getResource("replies.json").openStream();
    }
}
