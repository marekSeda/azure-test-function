package com.functions;

import com.functions.data.MenuRecord;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    public static int count = 1;

    @FunctionName("HttpExample")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.GET, HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }

    @FunctionName("HttpTestAddToTableStorage")
    public HttpResponseMessage httpTestAddToTableStorage(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.GET, HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("partitionKey") String partitionKey,
        @BindingName("rowKey") String rowKey,
        @TableOutput(name = "person", partitionKey = "{partitionKey}", rowKey = "{rowKey}", tableName = "%MyTableName%") OutputBinding<MenuRecord> person,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        MenuRecord menuRecord = new MenuRecord();
        menuRecord.setPartitionKey(partitionKey);
        menuRecord.setRowKey(rowKey);
        menuRecord.setName("Test" + count++);
        person.setValue(menuRecord);
        return request.createResponseBuilder(HttpStatus.OK).body("Added to table storage").build();
    }

    public static String getJavaVersion() {
        return String.join(" - ", System.getProperty("java.home"), System.getProperty("java.version"));
    }
}
