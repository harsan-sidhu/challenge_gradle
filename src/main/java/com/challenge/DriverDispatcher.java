package com.challenge;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DriverDispatcher {

    private final Kitchen kitchen;

    public DriverDispatcher(Kitchen kitchen) {
        this.kitchen = kitchen;

    }

    public synchronized void dispatchForOrder(
            ScheduledExecutorService executor, Order orderToPickup, int timeToPickUpOrder) {
        executor.schedule(
                () -> {
                    kitchen.removeOrderFromShelves(orderToPickup);
                    executor.shutdown();
                },
                timeToPickUpOrder,
                TimeUnit.SECONDS);

    }
}
