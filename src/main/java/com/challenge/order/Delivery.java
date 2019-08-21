package com.challenge.order;

import com.challenge.clock.Clock;
import com.challenge.shelf.Shelf;

/**
 * Wraps an order when the order is placed and contains information to help calculate the value of the Delivery.
 */
public class Delivery {

    private final Order order;
    private final long orderPlacedTimeStamp;
    private final int pickupTime;
    private final Clock clock;

    private Shelf shelf;
    private double decayedValue;

    public Delivery(Order order, long orderPlacedTimeStamp, int pickupTime, Clock clock) {
        this.order = order;
        this.orderPlacedTimeStamp = orderPlacedTimeStamp;
        this.pickupTime = pickupTime;
        this.clock = clock;

        decayedValue = 0.0;
    }

    public Order getOrder() {
        return order;
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
        return clock.getCurrentTimeInSeconds() - orderPlacedTimeStamp;
    }

    public double getDecayedValue() {
        return decayedValue;
    }

    private double computeDecay() {
        return (order.getDecayRate() * shelf.decayMultiplier() * getAge()) + decayedValue;
    }
}
