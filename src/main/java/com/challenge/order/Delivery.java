package com.challenge.order;

import com.challenge.shelf.Shelf;

import java.util.concurrent.TimeUnit;

public class Delivery {

    private final Order order;
    private final long orderPlacedTimeStamp;
    private final int pickupTime;
    private Shelf shelf;
    private double decayedValue;

    public Delivery(Order order, long orderPlacedTimeStamp, int pickupTime) {
        this.order = order;
        this.orderPlacedTimeStamp = orderPlacedTimeStamp;
        this.pickupTime = pickupTime;

        decayedValue = 0.0;
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
        Order order = getOrder();
        return ((order.getShelfLife() - getAge()) - computeDecay())/order.getShelfLife();
    }

    public synchronized void setShelf(Shelf shelf) {
        this.shelf = shelf;
        decayedValue = computeDecay();
    }

    public synchronized Shelf getShelf() {
        return shelf;
    }

    private long getAge() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getOrderPlacedTimeStamp());
    }

    public double getDecayedValue() {
        return decayedValue;
    }

    private double computeDecay() {
        return (order.getDecayRate() * shelf.decayMultiplier() * getAge()) + decayedValue;
    }
}
