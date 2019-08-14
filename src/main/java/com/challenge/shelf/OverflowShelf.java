package com.challenge.shelf;

import com.challenge.order.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverflowShelf extends BasicShelf {

    private static final int OVERFLOW_SHELF_CAPACITY = 20;

    public OverflowShelf(ConcurrentLinkedQueue<Order> overFlowShelf) {
        super(overFlowShelf);
    }

    @Override
    public synchronized String getType() {
        return "overflow";
    }

    @Override
    public int capacity() {
        return OVERFLOW_SHELF_CAPACITY;
    }
}
