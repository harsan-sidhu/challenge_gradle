package com.challenge.shelf;

import com.challenge.order.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FrozenShelf extends BasicShelf {

    private static final int FROZEN_SHELF_CAPACITY = 15;

    public FrozenShelf(ConcurrentLinkedQueue<Order> frozenShelf) {
        super(frozenShelf);
    }

    @Override
    public synchronized String getType() {
        return "frozen";
    }

    @Override
    public int capacity() {
        return FROZEN_SHELF_CAPACITY;
    }
}
