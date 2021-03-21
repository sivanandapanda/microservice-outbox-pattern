package com.example.order.service;

import com.example.order.event.InvoiceCreatedEvent;
import com.example.order.event.OrderCreatedEvent;
import com.example.order.event.OrderLineUpdatedEvent;
import com.example.order.model.EntityNotFoundException;
import com.example.order.model.OrderLineStatus;
import com.example.order.model.PurchaseOrder;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Event<ExportedEvent> event;

    @Transactional
    public PurchaseOrder addOrder(PurchaseOrder order) {
        order = entityManager.merge(order);

        event.fire(OrderCreatedEvent.of(order));
        event.fire(InvoiceCreatedEvent.of(order));

        return order;
    }

    @Transactional
    public  PurchaseOrder updateOrderLine(long orderId, long orderLineId, OrderLineStatus newStatus) {
        PurchaseOrder order = entityManager.find(PurchaseOrder.class, orderId);
        if(order == null) {
            throw new EntityNotFoundException("Order with id " + orderId + " could not be found");
        }

        OrderLineStatus oldStatus = order.updateOrderLine(orderLineId, newStatus);
        event.fire(OrderLineUpdatedEvent.of(orderId, orderLineId, newStatus, oldStatus));

        return order;
    }

}
