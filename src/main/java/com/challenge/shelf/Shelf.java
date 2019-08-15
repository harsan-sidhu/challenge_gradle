package com.challenge.shelf;

import com.challenge.order.Delivery;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface Shelf {

    String getType();

    ConcurrentLinkedQueue<Delivery> getOrders();

    boolean add(Delivery order);

    boolean remove(Delivery order);

    boolean contains(Delivery order);

    boolean isEmpty();

    int size();

    int capacity();

    void maybeTrashSpoiledOrders();

    double decayMultiplier();
}
