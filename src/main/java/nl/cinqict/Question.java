package nl.cinqict;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import nl.cinqict.Replies.ReplyObject;

import java.util.Optional;

public class Question {

    public static final String QUERY_PARAM_ANSWER = "answer";
    public static final String NO_ANSWER_FOUND = "null";

    @FunctionName("question")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {
            HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
                                   final ExecutionContext context) {

        return handleRequest(request);
    }

    private HttpResponseMessage handleRequest(HttpRequestMessage<Optional<String>> request) {

        // load replies
        Handler handler = new Handler();

        // parse query parameter
        String answer = request.getQueryParameters().get(QUERY_PARAM_ANSWER);

        // no or empty query parameter
        if (answer == null) {
            answer = NO_ANSWER_FOUND;
        } else {
            // normalize the input
            answer = answer.toLowerCase();
        }

        // lookup the reply
        ReplyObject replyObject = handler.handle(answer);

        return request.createResponseBuilder(HttpStatus.OK)
                .body(replyObject.reply)
                .header("Content-Type", "text/html")
                .build();
    }
}
