package com.challenge.shelf;

import com.challenge.order.Order;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface Shelf {

    String getType();

    ConcurrentLinkedQueue<Order> getOrders();

    boolean add(Order order);

    boolean remove(Order order);

    boolean contains(Order order);

    boolean isEmpty();

    int size();

    int capacity();

}
