package nl.cinqict;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;


public class Function {

    @FunctionName("cinq-connect")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // load replies
        Handler handler = new Handler();

        // parse query parameter
        String input = request.getQueryParameters().get("answer");
 
        // no or empty query parameter
        if (input == null) {
            input = "null";
        } else {
            // normalize the input
            input = input.toLowerCase();
        }

        // lookup the reply
        String output = handler.handle(input);

        // format the reply
        output = "<pre>" + output + "</pre>";

        return request.createResponseBuilder(HttpStatus.OK).body(output).header("Content-Type", "text/html").build();
    }
}
