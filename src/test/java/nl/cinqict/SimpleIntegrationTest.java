package nl.cinqict;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SimpleIntegrationTest {

    private static final String URL = "/post";
    private static WireMockServer server;

    @BeforeAll
    public static void startWireMock() {
        server = new WireMockServer(new WireMockConfiguration().port(9999).notifier(new ConsoleNotifier(false)));
        server.start();

        server.stubFor(post(URL).willReturn(aResponse().withStatus(200)));
    }

    @AfterEach
    public void resetWireMock() {
        server.resetRequests();
    }

    @AfterAll
    public static void stopWireMock() {
        server.stop();
    }

    @Test
    public void noAnswer() {
        assertEquals("Start the introduction by clicking <a href=\"/api/question?answer=intro\">here</a>.", callFunction(null));
        server.verify(0, postRequestedFor(urlEqualTo(URL)));
    }

    @Test
    public void question1() {
        assertEquals("What is the second decimal of pi?", callFunction("2"));
        server.verify(0, postRequestedFor(urlEqualTo(URL)));
    }

    @Test
    public void question2() {
        assertEquals("In what year became covid-19 a pandemic?", callFunction("4"));
        server.verify(0, postRequestedFor(urlEqualTo(URL)));
    }

    @Test
    public void question3() {
        assertEquals("This is the end of the introduction!", callFunction("2019"));
        server.verify(0, postRequestedFor(urlEqualTo(URL)));
    }

    @Test
    public void question4() {
        assertEquals("During a test Bouke discovers a bug. He knows commit cafebabe was correct, so he applies a binary search to find the commit that caused the bug. In case the middle commit will result in a decimal value, Bouke will always round down. How often will he have to checkout in order to find the offending commit?", callFunction("start"));
        server.verify(0, postRequestedFor(urlEqualTo(URL)));
    }

    @Test
    public void question5() {
        assertEquals("How many times can you find the words listed in file A in file B?", callFunction("11"));
        server.verify(1, postRequestedFor(urlEqualTo(URL)));
    }

    @Test
    public void invalidAnswer() {
        assertEquals("This answer is incorrect. Use the back button to go back to the question.", callFunction("1"));
        server.verify(0, postRequestedFor(urlEqualTo(URL)));
    }

    private String callFunction(String input) {

        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        if (input != null) {
            queryParams.put("answer", input);
        }
        doReturn(queryParams).when(req).getQueryParameters();

        final Optional<String> queryBody = Optional.empty();
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret = new Question().run(req, context);

        return (String) ret.getBody();
    }
}
