package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.OrderType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Basic implementation of a {@link Shelf} that handles most core functionality shared by different shelves
 */
public abstract class BasicShelf implements Shelf {

    private final ConcurrentLinkedQueue<Delivery> shelf;

    BasicShelf(ConcurrentLinkedQueue<Delivery> orders) {
        this.shelf = orders;
    }

    @Override
    public synchronized List<Delivery> getOrders() {
        return new ArrayList<>(shelf);
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

    public abstract OrderType getType();

    public abstract int capacity();

    public abstract double decayMultiplier();
}
