package com.example.order.event;

import com.example.order.model.PurchaseOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.debezium.outbox.quarkus.ExportedEvent;

import java.time.Instant;

public class InvoiceCreatedEvent implements ExportedEvent<String, JsonNode> {

    private final static ObjectMapper mapper = new ObjectMapper();

    private final long id;
    private final JsonNode order;
    private final Instant timestamp;

    private InvoiceCreatedEvent(long id, JsonNode order) {
        this.id = id;
        this.order = order;
        this.timestamp = Instant.now();
    }

    public static InvoiceCreatedEvent of(PurchaseOrder order) {
        ObjectNode asJson = mapper.createObjectNode()
                .put("id", order.getId())
                .put("invoiceDate", order.getOrderDate().toString())
                .put("invoiceValue", order.getTotalValue());

        return new InvoiceCreatedEvent(order.getId(), asJson);
    }

    @Override
    public String getAggregateId() {
        return String.valueOf(id);
    }

    @Override
    public String getAggregateType() {
        return "Customer";
    }

    @Override
    public String getType() {
        return "InvoiceCreated";
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
