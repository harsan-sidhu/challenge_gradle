package com.challenge.dispatcher;

import com.challenge.order.Delivery;
import com.challenge.kitchen.Kitchen;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeliveryPickupDispatcher {

    private final Kitchen kitchen;

    public DeliveryPickupDispatcher(Kitchen kitchen) {
        this.kitchen = kitchen;

    }

    public synchronized void dispatchPickupForOrder(
            ScheduledExecutorService executor, Delivery delivery, int timeToPickUpOrder) {
        executor.schedule(
                () -> {
                    kitchen.removeOrderFromShelves(delivery);
                    executor.shutdown();
                },
                timeToPickUpOrder,
                TimeUnit.SECONDS);

    }
}
