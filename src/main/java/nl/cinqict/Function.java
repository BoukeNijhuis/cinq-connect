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
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String input = request.getQueryParameters().get("answer");

        if (input == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a input on the query string like this: URL?answer=test").build();
        } else {
            Handler handler = new Handler();
            String output = handler.handle(input);
            return request.createResponseBuilder(HttpStatus.OK).body(output).build();
        }
    }
}
