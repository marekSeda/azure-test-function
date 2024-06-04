package com.functions;

import com.functions.data.MenuRecord;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.awt.*;
import java.time.LocalDate;
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
        @TableOutput(name="menuRecord", tableName="HW3MenuData", connection="MyStorage") OutputBinding<MenuRecord[]> menuRecords,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        MenuRecord menuRecordOut = new MenuRecord();
        menuRecordOut.setPartitionKey(LocalDate.now().toString());
        menuRecordOut.setRowKey("name");
        menuRecordOut.setName("Test" + count++);
        menuRecords.setValue(new MenuRecord[]{menuRecordOut});
        return request.createResponseBuilder(HttpStatus.OK).body("Added to table storage").build();
    }

    public static String getJavaVersion() {
        return String.join(" - ", System.getProperty("java.home"), System.getProperty("java.version"));
    }
}
