package com.challenge.dispatcher;

import com.challenge.kitchen.Kitchen;
import com.challenge.order.Delivery;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for removing a delivery from the kitchen.
 */
public class DeliveryPickupDispatcher {

    private final ScheduledExecutorService executor;
    private final Kitchen kitchen;

    public DeliveryPickupDispatcher(ScheduledExecutorService executor, Kitchen kitchen) {
        this.executor = executor;
        this.kitchen = kitchen;

    }

    /**
     * Given the timeToPickUpOrder remove the given delivery.
     *
     * @param delivery {@link Delivery} to be removed/picked up.
     * @param timeToPickUpOrder Time it'll take to pick up the order.
     */
    public void dispatchPickupForOrder(Delivery delivery, int timeToPickUpOrder) {
        executor.schedule(
                () -> removeOrderFromShelf(delivery),
                timeToPickUpOrder,
                TimeUnit.SECONDS);
    }

    // Separated for easier testability
    void removeOrderFromShelf(Delivery delivery) {
        kitchen.removeOrderFromShelves(delivery);
    }
}
