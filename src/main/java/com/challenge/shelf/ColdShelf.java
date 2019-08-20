package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ColdShelf extends BasicShelf {

    static final double DECAY_RATE = 1;

    private static final int COLD_SHELF_CAPACITY = 15;

    public ColdShelf(ConcurrentLinkedQueue<Delivery> coldShelf) {
        super(coldShelf);
    }

    @Override
    public String getType() {
        return "cold";
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
