package com.challenge.dispatcher;

import com.challenge.kitchen.Kitchen;
import com.challenge.order.Delivery;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeliveryPickupDispatcher {

    private final ScheduledExecutorService executor;
    private final Kitchen kitchen;

    public DeliveryPickupDispatcher(ScheduledExecutorService executor, Kitchen kitchen) {
        this.executor = executor;
        this.kitchen = kitchen;

    }

    public void dispatchPickupForOrder(Delivery delivery, int timeToPickUpOrder) {
        executor.schedule(
                () -> removeOrderFromShelf(delivery),
                timeToPickUpOrder,
                TimeUnit.SECONDS);
    }

    void removeOrderFromShelf(Delivery delivery) {
        kitchen.removeOrderFromShelves(delivery);
    }
}
