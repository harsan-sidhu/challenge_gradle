package com.challenge.shelf;

import com.challenge.order.Order;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BasicShelf implements Shelf {

    private final ConcurrentLinkedQueue<Order> shelf;

    public BasicShelf(ConcurrentLinkedQueue<Order> orders) {
        this.shelf = orders;
    }

    @Override
    public synchronized ConcurrentLinkedQueue<Order> getOrders() {
        return new ConcurrentLinkedQueue<>(shelf);
    }

    @Override
    public synchronized boolean add(Order order) {
        if (size() < capacity() && order.getTemp().equals(getType())) {
            shelf.add(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean remove(Order order) {
        if (contains(order)) {
            shelf.remove(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean contains(Order order) {
        return shelf.contains(order);
    }

    @Override
    public synchronized boolean isEmpty() {
        return shelf.isEmpty();
    }

    @Override
    public synchronized int size() {
        return shelf.size();
    }

    public abstract String getType();

    public abstract int capacity();
}
