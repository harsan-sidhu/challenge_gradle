package com.challenge.shelf;

import com.challenge.order.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ColdShelf extends BasicShelf {

    private static final int COLD_SHELF_CAPACITY = 15;

    public ColdShelf(ConcurrentLinkedQueue<Order> coldShelf) {
        super(coldShelf);
    }

    @Override
    public synchronized String getType() {
        return "cold";
    }

    @Override
    public int capacity() {
        return COLD_SHELF_CAPACITY;
    }
}
