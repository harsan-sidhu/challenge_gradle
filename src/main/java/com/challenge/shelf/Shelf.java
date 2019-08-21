package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.OrderType;

import java.util.List;

public interface Shelf {

    /**
     * @return {@link OrderType} that this shelf holds
     */
    OrderType getType();

    /**
     * @return List of the current {@link Delivery}'s on this shelf
     */
    List<Delivery> getOrders();

    /**
     * @param order {@link Delivery} to attempt to add to this shelf.
     *
     * @return true/false whether that order was added or not.
     */
    boolean add(Delivery order);

    /**
     * @param order {@link Delivery} to attempt to remove from this shelf.
     *
     * @return true/false whether that order was removed or not.
     */
    boolean remove(Delivery order);

    /**
     * @param order {@link Delivery} to check whether is on this shelf.
     *
     * @return true/false whether the shelf contains the order.
     */
    boolean contains(Delivery order);

    /**
     * @return true/false whether this shelf is empty
     */
    boolean isEmpty();

    /**
     * @return Size of the shelf
     */
    int size();

    /**
     * @return Capacity of the shelf
     */
    int capacity();

    /**
     * Trash orders that have reached a value of 0.
     */
    void maybeTrashSpoiledOrders();

    /**
     *
     * @return  Decay rate of this shelf to be applied when calculating an order
     */
    double decayMultiplier();
}
