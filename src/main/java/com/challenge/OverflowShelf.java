package com.challenge;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverflowShelf implements Shelf {

    private final ConcurrentLinkedQueue<Order> overFlowShelf;
    private final DefaultListModel<Order> overFlowShelfListModel;

    OverflowShelf(ConcurrentLinkedQueue<Order> overFlowShelf, DefaultListModel<Order> overFlowShelfListModel) {
        this.overFlowShelf = overFlowShelf;
        this.overFlowShelfListModel = overFlowShelfListModel;
    }

    @Override
    public synchronized String getType() {
        return "overflow";
    }


    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(overFlowShelf);
    }

    @Override
    public synchronized void addOrderToShelf(Order order) {
        overFlowShelf.add(order);
        SwingUtilities.invokeLater(() -> overFlowShelfListModel.addElement(order));

    }

    @Override
    public synchronized void removeOrderFromShelf(Order order) {
        overFlowShelf.remove(order);
        SwingUtilities.invokeLater(() -> overFlowShelfListModel.removeElement(order));

    }


    @Override
    public boolean contains(Order order) {
        return overFlowShelf.contains(order);
    }

    @Override
    public synchronized boolean isEmpty() {
        return overFlowShelf.isEmpty() && overFlowShelfListModel.isEmpty();
    }

    @Override
    public int size() {
        return overFlowShelf.size();
    }
}
