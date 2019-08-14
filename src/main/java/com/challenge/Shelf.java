package com.challenge;

import java.util.List;

public interface Shelf {

    String getType();

    List<Order> getOrders();

    void addOrderToShelf(Order order);

    void removeOrderFromShelf(Order order);

    boolean contains(Order order);

    boolean isEmpty();

    int size();

    // TODO Consider Capacity then each shelf defines its own

}
