package com.functions;

import com.functions.data.Record;
import com.functions.data.RecordQueueDto;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    @FunctionName("ProcessQueueMessage")
    public void processQueueMessage(
        @QueueTrigger(name = "message",
            queueName = "scrape-request-queue",
            connection = "MyStorage") RecordQueueDto queueItem,
        @TableOutput(name = "menuRecord", tableName = "HW3RecordData", connection = "MyStorage") OutputBinding<Record[]> menuRecords,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        Record menuRecordOut = new Record();
        menuRecordOut.setPartitionKey(queueItem.getPlace());
        menuRecordOut.setRowKey(String.valueOf(System.currentTimeMillis()));
        menuRecordOut.setPlace(queueItem.getPlace());
        menuRecordOut.setTemperature(queueItem.getTemperature());
        menuRecords.setValue(new Record[]{menuRecordOut});

        if (queueItem.getTemperature() > 30) {
            context.getLogger().info("Temperature is too high, sending alert.");
        }
    }


    @FunctionName("HttpGetTableData")
    public HttpResponseMessage httpGetTableData(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.GET, HttpMethod.POST},
            route = "/{partitionKey}",
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("partitionKey") String partitionKey,
        @TableInput(name = "menuRecord", partitionKey = "{partitionKey}", tableName = "HW3RecordData", connection = "MyStorage") Record[] menuRecords,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        return request.createResponseBuilder(HttpStatus.OK).body(menuRecords).build();
    }

    @FunctionName("AddRequestToQueue")
    public HttpResponseMessage addRequestToQueue(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.GET},
            authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @QueueOutput(name = "message", queueName = "scrape-request-queue", connection = "MyStorage") OutputBinding<RecordQueueDto> message,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        String place = request.getQueryParameters().get("place");
        String temperature = request.getQueryParameters().get("temperature");
        if (place == null || temperature == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a place and temperature on the query string").build();
        }
        RecordQueueDto messageValue = new RecordQueueDto();
        messageValue.setPlace(place);
        messageValue.setTemperature(Long.parseLong(temperature));
        message.setValue(messageValue);
        return request.createResponseBuilder(HttpStatus.OK).body("Request added to queue").build();
    }
}
