package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.OrderType;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.COLD;

/**
 * A {@link BasicShelf} that stores {@link OrderType#COLD} Deliveries
 */
public class ColdShelf extends BasicShelf {

    static final double DECAY_RATE = 1;

    private static final int COLD_SHELF_CAPACITY = 15;

    public ColdShelf(ConcurrentLinkedQueue<Delivery> coldShelf) {
        super(coldShelf);
    }

    @Override
    public OrderType getType() {
        return COLD;
    }

    @Override
    public int capacity() {
        return COLD_SHELF_CAPACITY;
    }

    @Override
    public double decayMultiplier() {
        return 1;
    }
}
