package com.example.shipment.service;

import com.example.shipment.model.Shipment;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
@Traced
public class ShipmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(value = Transactional.TxType.MANDATORY)
    public void orderCreated(JsonNode event) {
        LOGGER.info("Processing 'OrderCreated' event: {}", event);

        long orderId = event.get("id").asLong();
        long customerId = event.get("customerId").asLong();
        LocalDateTime orderDate = LocalDateTime.parse(event.get("orderDate").asText());

        entityManager.persist(new Shipment(customerId, orderId, orderDate));
    }

    @Transactional(value = Transactional.TxType.MANDATORY)
    public void orderLineUpdated(JsonNode event) {
        LOGGER.info("Processing 'OrderLineUpdated' event: {}", event);
    }
}
