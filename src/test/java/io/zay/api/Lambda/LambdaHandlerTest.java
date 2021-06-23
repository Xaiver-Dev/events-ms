package io.zay.api.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.zay.api.Dto.EventDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.OffsetDateTime;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LambdaHandlerTest {
    @Mock
    private Context context;

    @Test
    public void handleRequestNotNull() {
        LambdaHandler handler = new LambdaHandler();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setPath("/path/to/resource");

        APIGatewayProxyResponseEvent responseEvent = handler.handleRequest(input, context);

        assertNotNull(responseEvent);
    }

    @Test
    public void handleRequestEventDto() throws IOException {
        LambdaHandler handler = new LambdaHandler();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        input.setPath("/path/to/resource");

        APIGatewayProxyResponseEvent responseEvent = handler.handleRequest(input, context);
        assertNotNull(responseEvent);

        String responseEventBody = responseEvent.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        EventDto eventDto = objectMapper.readValue(responseEventBody, EventDto.class);
        assertNotNull(eventDto);
        assertEquals("The Event is now!", eventDto.getDescription());
        assertEquals(OffsetDateTime.parse("2027-12-03T09:15:30Z"), eventDto.getDate());
    }

    }

