package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HotShelf extends BasicShelf {

    private static final int HOT_SHELF_CAPACITY = 15;

    public HotShelf(ConcurrentLinkedQueue<Delivery> hotShelf) {
        super(hotShelf);
    }

    @Override
    public synchronized String getType() {
        return "hot";
    }

    @Override
    public synchronized int capacity() {
        return HOT_SHELF_CAPACITY;
    }

    @Override
    public synchronized double decayMultiplier() {
        return 1;
    }
}
