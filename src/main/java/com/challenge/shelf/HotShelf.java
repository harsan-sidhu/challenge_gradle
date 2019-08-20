package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HotShelf extends BasicShelf {

    static final double DECAY_RATE = 1;

    private static final int HOT_SHELF_CAPACITY = 15;

    public HotShelf(ConcurrentLinkedQueue<Delivery> hotShelf) {
        super(hotShelf);
    }

    @Override
    public String getType() {
        return "hot";
    }

    @Override
    public int capacity() {
        return HOT_SHELF_CAPACITY;
    }

    @Override
    public double decayMultiplier() {
        return 1;
    }
}
