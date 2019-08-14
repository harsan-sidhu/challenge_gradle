package com.challenge;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FrozenShelf implements Shelf {

    private final ConcurrentLinkedQueue<Order> frozenShelf;
    private final DefaultListModel<Order> frozenShelfListModel;

    FrozenShelf(ConcurrentLinkedQueue<Order> frozenShelf, DefaultListModel<Order> frozenShelfListModel) {
        this.frozenShelf = frozenShelf;
        this.frozenShelfListModel = frozenShelfListModel;
    }

    @Override
    public synchronized String getType() {
        return "frozen";
    }

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(frozenShelf);
    }

    @Override
    public synchronized void addOrderToShelf(Order order) {
        frozenShelf.add(order);
        SwingUtilities.invokeLater(() -> frozenShelfListModel.addElement(order));

    }

    @Override
    public synchronized void removeOrderFromShelf(Order order) {
        frozenShelf.remove(order);
        SwingUtilities.invokeLater(() -> frozenShelfListModel.removeElement(order));

    }


    @Override
    public boolean contains(Order order) {
        return frozenShelf.contains(order);
    }

    @Override
    public synchronized boolean isEmpty() {
        return frozenShelf.isEmpty() && frozenShelfListModel.isEmpty();
    }

    @Override
    public int size() {
        return frozenShelf.size();
    }
}
