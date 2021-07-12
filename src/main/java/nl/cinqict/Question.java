package nl.cinqict;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import nl.cinqict.Replies.ReplyObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

public class Question {

    private static final int FIRST_QUESTION_ORDER = 1;
    private static final String CORRECT_ANSWERS_URL = "URL";
    private static final String QUERY_PARAMETER_ANSWER_KEY = "answer";
    private static final String KEY_FOR_FIRST_QUESTION = "FIRST_QUESTION";

    String correctAnswersURL;

    @FunctionName("question")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {
            HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
                                   final ExecutionContext context) {
        return handleRequest(request);
    }

    private HttpResponseMessage handleRequest(HttpRequestMessage<Optional<String>> request) {

        // load replies
        Handler handler = new Handler();

        // get the correct answers URL
        correctAnswersURL = handler.handle(CORRECT_ANSWERS_URL).reply;

        // parse query parameter
        String answer = request.getQueryParameters().get(QUERY_PARAMETER_ANSWER_KEY);

        // no or empty query parameter
        if (answer == null) {
            answer = KEY_FOR_FIRST_QUESTION;
        } else {
            // normalize the input
            answer = answer.toLowerCase();
        }

        // lookup the reply
        ReplyObject replyObject = handler.handle(answer);

        // format the reply
        String body = Util.format(replyObject.reply);

        // only send correct answers
        if (replyObject.order > FIRST_QUESTION_ORDER) {
            sendRequest(answer);
        }

        return request.createResponseBuilder(HttpStatus.OK).body(body).header("Content-Type", "text/html").build();
    }

    private void sendRequest(String answer) {
        BodyPublisher bodyPublisher = BodyPublishers.ofString(answer);
        var request = HttpRequest.newBuilder(URI.create(correctAnswersURL)).POST(bodyPublisher).build();
        try {
            var response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
            System.out.println("RESPONSE: " + response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
