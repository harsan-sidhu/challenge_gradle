package com.challenge.order;

import com.challenge.shelf.Shelf;

import java.util.concurrent.TimeUnit;

public class Delivery {

    private final Order order;
    private final long orderPlacedTimeStamp;
    private final int pickupTime;
    private Shelf shelf;

    public Delivery(Order order, long orderPlacedTimeStamp, int pickupTime) {
        this.order = order;
        this.orderPlacedTimeStamp = orderPlacedTimeStamp;
        this.pickupTime = pickupTime;
    }

    public Order getOrder() {
        return order;
    }

    private long getOrderPlacedTimeStamp() {
        return orderPlacedTimeStamp;
    }

    public int getPickupTime() {
        return pickupTime;
    }

    public double getValue() {
        long orderAge = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getOrderPlacedTimeStamp());
        return OrderValueCalculator.computeValueOfDelivery(this, orderAge, shelf.decayMultiplier());
    }

    public synchronized void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public synchronized Shelf getShelf() {
        return shelf;
    }
}
