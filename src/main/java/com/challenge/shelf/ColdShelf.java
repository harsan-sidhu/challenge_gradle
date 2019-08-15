package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ColdShelf extends BasicShelf {

    private static final int COLD_SHELF_CAPACITY = 15;

    public ColdShelf(ConcurrentLinkedQueue<Delivery> coldShelf) {
        super(coldShelf);
    }

    @Override
    public synchronized String getType() {
        return "cold";
    }

    @Override
    public synchronized int capacity() {
        return COLD_SHELF_CAPACITY;
    }

    @Override
    public synchronized double decayMultiplier() {
        return 1;
    }
}
