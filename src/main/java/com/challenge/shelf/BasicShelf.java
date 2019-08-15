package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class BasicShelf implements Shelf {

    private final ConcurrentLinkedQueue<Delivery> shelf;

    BasicShelf(ConcurrentLinkedQueue<Delivery> orders) {
        this.shelf = orders;
    }

    @Override
    public synchronized ConcurrentLinkedQueue<Delivery> getOrders() {
        return new ConcurrentLinkedQueue<>(shelf);
    }

    @Override
    public synchronized boolean add(Delivery order) {
        if (size() < capacity()) {
            shelf.add(order);
            order.setShelf(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean remove(Delivery order) {
        if (contains(order)) {
            shelf.remove(order);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized boolean contains(Delivery order) {
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

    @Override
    public synchronized void maybeTrashSpoiledOrders() {
        for (Delivery delivery : shelf) {
            if (delivery.getValue() <= 0) {
                remove(delivery);
            }
        }
    }

    public abstract String getType();

    public abstract int capacity();

    public abstract double decayMultiplier();
}
