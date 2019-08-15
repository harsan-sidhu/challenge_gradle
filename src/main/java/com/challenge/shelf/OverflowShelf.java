package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OverflowShelf extends BasicShelf {

    private static final int OVERFLOW_SHELF_CAPACITY = 20;

    public OverflowShelf(ConcurrentLinkedQueue<Delivery> overFlowShelf) {
        super(overFlowShelf);
    }

    @Override
    public synchronized String getType() {
        return "overflow";
    }

    @Override
    public synchronized int capacity() {
        return OVERFLOW_SHELF_CAPACITY;
    }

    @Override
    public synchronized double decayMultiplier() {
        return 2;
    }
}
