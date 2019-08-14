package com.challenge;

import com.challenge.order.Order;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderValueCalculator {
    private final Kitchen kitchen;

    public OrderValueCalculator(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    public void beginContinuouslyCalculatingOrder(ScheduledExecutorService executor, Order placedOrder) {
        executor.scheduleAtFixedRate(
                () -> {
                    if (!kitchen.isOrderOnShelves(placedOrder)) {
                        executor.shutdown();
                    } else {
                        long orderAge = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - placedOrder.getOrderTimeStamp());
                        double decayRate = kitchen.isOrderOnOverFlowShelf(placedOrder) ? placedOrder.getDecayRate() * 2 : placedOrder.getDecayRate();
                        double value = (placedOrder.getShelfLife() - orderAge) - (decayRate * orderAge);
                        placedOrder.setValue(value / placedOrder.getShelfLife());

                        if (value <= 0) {
                            kitchen.removeOrderFromShelves(placedOrder);
                        }
                    }
                },
                0,
                1,
                TimeUnit.SECONDS);
    }

    //Todo move formula to method for sharing and testing purposes
}
