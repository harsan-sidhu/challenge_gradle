package com.challenge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.math3.distribution.PoissonDistribution;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException {
        // Parse and Queue Orders
        ConcurrentLinkedQueue<Order> orderQueue = parseJSONAndQueueOrders();

        // Initialize Shelves Data
        DefaultListModel<Order> hotShelfListModel = new DefaultListModel<>();
        DefaultListModel<Order> coldShelfListModel = new DefaultListModel<>();
        DefaultListModel<Order> frozenShelfListModel = new DefaultListModel<>();
        DefaultListModel<Order> overflowShelfListModel = new DefaultListModel<>();
        Kitchen kitchen = initShelves(hotShelfListModel, coldShelfListModel, frozenShelfListModel, overflowShelfListModel);

        // Begin Processing Orders
        OrderPlacer orderPlacer
                = new OrderPlacer(
                new ScheduledThreadPoolExecutor(50),
                kitchen,
                new OrderValueCalculator(kitchen),
                new DriverDispatcher(kitchen));
        orderPlacer.placeOrders(orderQueue, new PoissonDistribution(3.25));

        // Show All Shelves In GUI
        showGUI(hotShelfListModel, coldShelfListModel, frozenShelfListModel, overflowShelfListModel);
    }

    private static ConcurrentLinkedQueue<Order> parseJSONAndQueueOrders() throws FileNotFoundException {
        Gson gson = new GsonBuilder().create();
        return new ConcurrentLinkedQueue<>(
                Arrays.asList(gson.fromJson(new FileReader("orders.json"), Order[].class)));

    }

    private static Kitchen initShelves(
            DefaultListModel<Order> hotShelfListModel,
            DefaultListModel<Order> coldShelfListModel,
            DefaultListModel<Order> frozenShelfListModel,
            DefaultListModel<Order> overflowShelfListModel) {
        HotShelf hotShelf = new HotShelf(new ConcurrentLinkedQueue<>(), hotShelfListModel);
        ColdShelf coldShelf = new ColdShelf(new ConcurrentLinkedQueue<>(), coldShelfListModel);
        FrozenShelf frozenShelf = new FrozenShelf(new ConcurrentLinkedQueue<>(), frozenShelfListModel);
        OverflowShelf overFlowShelf = new OverflowShelf(new ConcurrentLinkedQueue<>(), overflowShelfListModel);

        return new Kitchen(Arrays.asList(hotShelf, coldShelf, frozenShelf, overFlowShelf));
    }

    private static void showGUI(
            DefaultListModel<Order> hotShelfListModel,
            DefaultListModel<Order> coldShelfListModel,
            DefaultListModel<Order> frozenShelfListModel,
            DefaultListModel<Order> overflowShelfListModel) {
        JPanel panel = new JPanel(new GridLayout(2,4,10,0));

        panel.add(new JLabel("Hot Shelf"));
        panel.add(new JLabel("Cold Shelf"));
        panel.add(new JLabel("Frozen Shelf"));
        panel.add(new JLabel("Overflow Shelf"));

        panel.add(new JList<>(hotShelfListModel));
        panel.add(new JList<>(coldShelfListModel));
        panel.add(new JList<>(frozenShelfListModel));
        panel.add(new JList<>(overflowShelfListModel));

        JFrame jFrame = new JFrame();
        jFrame.add(panel);
        jFrame.setSize(1000, 800);
        jFrame.setVisible(true);
    }
}
