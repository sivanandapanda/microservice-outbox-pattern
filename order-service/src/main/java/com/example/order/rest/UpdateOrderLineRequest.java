package com.example.order.rest;

import com.example.order.model.OrderLineStatus;

public class UpdateOrderLineRequest {

    private OrderLineStatus newStatus;

    public OrderLineStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderLineStatus newStatus) {
        this.newStatus = newStatus;
    }
}