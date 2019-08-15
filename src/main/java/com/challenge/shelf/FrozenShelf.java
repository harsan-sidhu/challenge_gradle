package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FrozenShelf extends BasicShelf {

    private static final int FROZEN_SHELF_CAPACITY = 15;

    public FrozenShelf(ConcurrentLinkedQueue<Delivery> frozenShelf) {
        super(frozenShelf);
    }

    @Override
    public synchronized String getType() {
        return "frozen";
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
