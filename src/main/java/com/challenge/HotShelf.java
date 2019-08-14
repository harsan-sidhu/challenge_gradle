package com.challenge;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HotShelf implements Shelf {

    private final ConcurrentLinkedQueue<Order> hotShelf;
    private final DefaultListModel<Order> hotShelfListModel;

    HotShelf(ConcurrentLinkedQueue<Order> hotShelf, DefaultListModel<Order> hotShelfListModel) {
        this.hotShelf = hotShelf;
        this.hotShelfListModel = hotShelfListModel;
    }

    @Override
    public synchronized String getType() {
        return "hot";
    }

    @Override
    public List<Order> getOrders() {
        return new ArrayList<>(hotShelf);
    }

    @Override
    public synchronized void addOrderToShelf(Order order) {
        hotShelf.add(order);
        SwingUtilities.invokeLater(() -> hotShelfListModel.addElement(order));

    }

    @Override
    public synchronized void removeOrderFromShelf(Order order) {
        hotShelf.remove(order);
        SwingUtilities.invokeLater(() -> hotShelfListModel.removeElement(order));

    }

    @Override
    public boolean contains(Order order) {
        return hotShelf.contains(order);
    }

    @Override
    public synchronized boolean isEmpty() {
        return hotShelf.isEmpty() && hotShelfListModel.isEmpty();
    }

    @Override
    public int size() {
        return hotShelf.size();
    }
}
