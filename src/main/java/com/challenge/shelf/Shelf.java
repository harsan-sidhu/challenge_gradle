package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.OrderType;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface Shelf {

    OrderType getType();

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
