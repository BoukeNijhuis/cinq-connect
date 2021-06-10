package nl.cinqict;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import nl.cinqict.Replies.ReplyObject;

import java.util.Optional;

public class Question {

    @FunctionName("question")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {
            HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        return handleRequest(request);
    }

    private HttpResponseMessage handleRequest(HttpRequestMessage<Optional<String>> request) {

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

        return request.createResponseBuilder(HttpStatus.OK).body(body).header("Content-Type", "text/html").build();
    }
}
