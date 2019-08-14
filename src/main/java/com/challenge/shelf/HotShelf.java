package com.challenge.shelf;

import com.challenge.order.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HotShelf extends BasicShelf {

    private static final int HOT_SHELF_CAPACITY = 15;

    public HotShelf(ConcurrentLinkedQueue<Order> hotShelf) {
        super(hotShelf);
    }

    @Override
    public synchronized String getType() {
        return "hot";
    }

    @Override
    public int capacity() {
        return HOT_SHELF_CAPACITY;
    }
}
