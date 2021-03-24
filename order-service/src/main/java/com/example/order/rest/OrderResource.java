package com.example.order.rest;

import com.example.order.model.PurchaseOrder;
import com.example.order.service.OrderService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    @POST
    public OrderOperationResponse addOrder(CreateOrderRequest createOrderRequest) {
        PurchaseOrder order = createOrderRequest.toOrder();
        order = orderService.addOrder(order);
        return OrderOperationResponse.from(order);
    }

    @PUT
    @Path("{orderId}/lines/{orderLineId}")
    public OrderOperationResponse updateOrderLine(@PathParam("orderId") long orderId, @PathParam("orderLineId") long orderLineId, UpdateOrderLineRequest request) {
        PurchaseOrder updated = orderService.updateOrderLine(orderId, orderLineId, request.getNewStatus());
        return OrderOperationResponse.from(updated);
    }
}
