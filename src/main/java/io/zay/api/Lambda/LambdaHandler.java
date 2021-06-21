package io.zay.api.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.zay.api.Dto.EventDto;

import java.time.OffsetDateTime;
import java.util.HashMap;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("x-request-path", input.getPath());

        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent().withHeaders(headers);


        EventDto eventDto = new EventDto();
        eventDto.setDescription("The Event is now!");
        eventDto.setDate(OffsetDateTime.parse("2027-12-03T10:15:30+01:00"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            responseEvent.setBody(mapper.writeValueAsString(eventDto));
            return responseEvent
                    .withStatusCode(200);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return responseEvent
                    .withStatusCode(500)
                    .withBody("{}");
        }
    }
}