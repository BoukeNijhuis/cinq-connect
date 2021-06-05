package nl.cinqict;

import java.util.Optional;

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

// TODO: add delay
public class LastQuestion {

    private static ReplyObject lastReplyObject;

    @FunctionName("last-question")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = { HttpMethod.GET,
            HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<ReplyObject>> request,
            final ExecutionContext context) {

        // return the last question in case of GET
        if (HttpMethod.GET.equals(request.getHttpMethod())) {
            return handleGet(request);
        }

        // if no GET, then it is a POST
        return handlePost(request);
    }

    private HttpResponseMessage handleGet(HttpRequestMessage<Optional<ReplyObject>> request) {
        String body = "There is no known last question yet!";

        if (lastReplyObject != null) {
            body = lastReplyObject.reply;
        }

        body = Util.format(body);

        return request.createResponseBuilder(HttpStatus.OK).body(body).header("Content-Type", "text/html").build();
    }

    private HttpResponseMessage handlePost(HttpRequestMessage<Optional<ReplyObject>> request) {
        Optional<ReplyObject> body = request.getBody();

        if (body.isPresent()) {
            ReplyObject replyObject = body.get();
            System.out.println("ONTVANGEN: " + new Gson().toJson(replyObject));

            // initialize last reply object OR only replace when higher order
            if (lastReplyObject == null || replyObject.order > lastReplyObject.order) {
                lastReplyObject = replyObject;
            }
        }

        return request.createResponseBuilder(HttpStatus.OK).build();
    }
}
