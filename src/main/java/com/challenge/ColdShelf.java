package com.challenge;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ColdShelf implements Shelf {

    private final ConcurrentLinkedQueue<Order> coldShelf;
    private final DefaultListModel<Order> coldShelfListModel;

    ColdShelf(ConcurrentLinkedQueue<Order> coldShelf, DefaultListModel<Order> coldShelfListModel) {
        this.coldShelf = coldShelf;
        this.coldShelfListModel = coldShelfListModel;
    }

    @Override
    public synchronized String getType() {
        return "cold";
    }

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(coldShelf);
    }

    @Override
    public synchronized void addOrderToShelf(Order order) {
        coldShelf.add(order);
        SwingUtilities.invokeLater(() -> coldShelfListModel.addElement(order));

    }

    @Override
    public synchronized void removeOrderFromShelf(Order order) {
        coldShelf.remove(order);
        SwingUtilities.invokeLater(() -> coldShelfListModel.removeElement(order));

    }


    @Override
    public boolean contains(Order order) {
        return coldShelf.contains(order);
    }

    @Override
    public synchronized boolean isEmpty() {
        return coldShelf.isEmpty() && coldShelfListModel.isEmpty();
    }

    @Override
    public int size() {
        return coldShelf.size();
    }
}
