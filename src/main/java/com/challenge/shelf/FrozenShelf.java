package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.OrderType;

import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.FROZEN;

public class FrozenShelf extends BasicShelf {

    static final double DECAY_RATE = 1;

    private static final int FROZEN_SHELF_CAPACITY = 15;

    public FrozenShelf(ConcurrentLinkedQueue<Delivery> frozenShelf) {
        super(frozenShelf);
    }

    @Override
    public synchronized OrderType getType() {
        return FROZEN;
    }

    @Override
    public synchronized int capacity() {
        return FROZEN_SHELF_CAPACITY;
    }

    @Override
    public synchronized double decayMultiplier() {
        return 1;
    }
}
