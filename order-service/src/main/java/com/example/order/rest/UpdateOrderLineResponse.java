package com.example.order.rest;

import com.example.order.model.OrderLineStatus;

public class UpdateOrderLineResponse {

    private final OrderLineStatus oldStatus;
    private final OrderLineStatus newStatus;

    public UpdateOrderLineResponse(OrderLineStatus oldStatus, OrderLineStatus newStatus) {
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public OrderLineStatus getOldStatus() {
        return oldStatus;
    }

    public OrderLineStatus getNewStatus() {
        return newStatus;
    }
}