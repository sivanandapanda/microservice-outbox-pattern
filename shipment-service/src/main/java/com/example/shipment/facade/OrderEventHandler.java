package com.example.shipment.facade;

import com.example.shipment.messagelog.MessageLog;
import com.example.shipment.service.ShipmentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@ApplicationScoped
public class OrderEventHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventHandler.class);

    @Inject
    private MessageLog log;

    @Inject
    private ShipmentService shipmentService;

    public void onOrderEvent(UUID eventId, String eventType, String key, String event, Instant ts) {
        if(log.alreadyProcessed(eventId)) {
            LOGGER.info("Event with UUID {} was already retrieved, ignoring it", eventId);
            return;
        }

        JsonNode eventPayload = deserialize(event);

        LOGGER.info("Received 'Order' event -- key: {}, event id: '{}', event type: '{}', ts: '{}'", key, eventId, eventType, ts);

        if (eventType.equals("OrderCreated")) {
            shipmentService.orderCreated(eventPayload);
        }
        else if (eventType.equals("OrderLineUpdated")) {
            shipmentService.orderLineUpdated(eventPayload);
        }
        else {
            LOGGER.warn("Unkown event type");
        }

        log.processed(eventId);

    }

    private JsonNode deserialize(String event) {
        JsonNode eventPayload;

        try {
            String unescaped = objectMapper.readValue(event, String.class);
            eventPayload = objectMapper.readTree(unescaped);
        }
        catch (IOException e) {
            throw new RuntimeException("Couldn't deserialize event", e);
        }

        return eventPayload.has("schema") ? eventPayload.get("payload") : eventPayload;
    }

}
