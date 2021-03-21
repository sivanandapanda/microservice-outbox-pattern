package com.example.order.event;

import com.example.order.model.OrderLineStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

public class OrderLineUpdatedEvent implements ExportedEvent<String, JsonNode> {

    private final static ObjectMapper mapper = new ObjectMapper();

    private final long orderId;
    private final long orderLineId;
    private final OrderLineStatus newStatus;
    private final OrderLineStatus oldStatus;
    private final Instant timestamp;

    public OrderLineUpdatedEvent(long orderId, long orderLineId, OrderLineStatus newStatus, OrderLineStatus oldStatus) {
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
        this.timestamp = Instant.now();
    }

    public static OrderLineUpdatedEvent of(long orderId, long orderLineId, OrderLineStatus newStatus, OrderLineStatus oldStatus) {
        return new OrderLineUpdatedEvent(orderId, orderLineId, newStatus, oldStatus);
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(orderId);
    }

    @Override
    public String getAggregateType() {
        return "Order";
    }

    @Override
    public JsonNode getPayload() {
        return mapper.createObjectNode()
                .put("orderId", orderId)
                .put("orderLineId", orderLineId)
                .put("oldStatus", oldStatus.name())
                .put("newStatus", newStatus.name());
    }

    @Override
    public String getType() {
        return "OrderLineUpdated";
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
