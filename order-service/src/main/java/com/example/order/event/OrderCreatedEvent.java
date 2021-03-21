package com.example.order.event;

import com.example.order.model.OrderLine;
import com.example.order.model.PurchaseOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

public class OrderCreatedEvent implements ExportedEvent<String, JsonNode> {

    private final static ObjectMapper mapper = new ObjectMapper();

    private final long id;
    private final JsonNode order;
    private final Instant timestamp;

    private OrderCreatedEvent(long id, JsonNode order) {
        this.id = id;
        this.order = order;
        this.timestamp = Instant.now();
    }

    public static OrderCreatedEvent of(PurchaseOrder order) {
        ObjectNode asJson = mapper.createObjectNode()
                .put("id", order.getId())
                .put("customerId", order.getCustomerId())
                .put("orderDate", order.getOrderDate().toString());

        ArrayNode items = asJson.putArray("lineItems");

        for (OrderLine orderLine : order.getLineItems()) {
            ObjectNode orderLineAsJson = mapper.createObjectNode()
                    .put("id", orderLine.getId())
                    .put("item", orderLine.getItem())
                    .put("quantity", orderLine.getQuantity())
                    .put("totalPrice", orderLine.getTotalPrice())
                    .put("status", orderLine.getStatus().name());

            items.add(orderLineAsJson);
        }

        return new OrderCreatedEvent(order.getId(), asJson);
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(id);
    }

    @Override
    public String getAggregateType() {
        return "Order";
    }

    @Override
    public String getType() {
        return "OrderCreated";
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public JsonNode getPayload() {
        return order;
    }
}
