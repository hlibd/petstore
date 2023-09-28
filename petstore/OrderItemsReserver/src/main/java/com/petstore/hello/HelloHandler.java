package com.petstore.hello;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.petstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Azure Functions with HTTP Trigger.
 */
@Component
public class HelloHandler {
    /**
     * This function listens at endpoint "/api/HelloHandler". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HelloHandler
     * 2. curl {your host}/api/HelloHandler?name=HTTP%20Query
     */
    @Autowired
    private Hello hello;
    @FunctionName("HelloHandler")
    public HttpResponseMessage run(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<User>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        User user = request.getBody()
                .filter(u -> u.getName() != null)
                .orElseGet(() -> new User(request.getQueryParameters().getOrDefault("name", "world")));
        context.getLogger().info("Greeting user name: " + user.getName());
        return request.createResponseBuilder(HttpStatus.OK)
                .body(hello.apply(user))
                .header("Content-Type", "application/json")
                .build();
    }
}
