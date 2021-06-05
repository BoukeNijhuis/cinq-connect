package nl.cinqict;

import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
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

    @FunctionName("question")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {
            HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        return handleAnswer(request);
    }

    private HttpResponseMessage handleAnswer(HttpRequestMessage<Optional<String>> request) {

        // load replies
        Handler handler = new Handler();

        // parse query parameter
        String answer = request.getQueryParameters().get("answer");

        // no or empty query parameter
        if (answer == null) {
            answer = "null";
        } else {
            // normalize the input
            answer = answer.toLowerCase();
        }

        // lookup the reply
        ReplyObject replyObject = handler.handle(answer);

        // format the reply
        String body = Util.format(replyObject.reply);

//        sendRequest(replyObject);

        return request.createResponseBuilder(HttpStatus.OK).body(body).header("Content-Type", "text/html").build();
    }

    private void sendRequest(ReplyObject replyObject) {
        String url = "http://localhost:7071/api/last-question";
        BodyPublisher bodyPublisher = BodyPublishers.ofString(new Gson().toJson(replyObject));
        var request = HttpRequest.newBuilder(URI.create(url)).POST(bodyPublisher).build();
        try {
            var response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
            System.out.println("RESPONSE: " + response);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
