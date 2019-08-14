package com.challenge;

import com.challenge.order.Order;
import com.challenge.shelf.*;
import com.challenge.ui.DispatcherUICallback;
import com.challenge.ui.OrderFulfillerUI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.math3.distribution.PoissonDistribution;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException {
        // Parse and Queue Orders
        ConcurrentLinkedQueue<Order> orderQueue = parseJSONAndQueueOrders();


        List<BasicShelf> shelves
                = Arrays.asList(
                new HotShelf(new ConcurrentLinkedQueue<>()),
                new ColdShelf(new ConcurrentLinkedQueue<>()),
                new FrozenShelf(new ConcurrentLinkedQueue<>()),
                new OverflowShelf(new ConcurrentLinkedQueue<>()));

        OrderFulfillerUI ui = new OrderFulfillerUI(getConfigurationForShelves(shelves));

        Kitchen kitchen = new Kitchen(shelves, ui);

        // Begin Processing Orders
        OrderFulfiller orderFulfiller
                = new OrderFulfiller(
                new ScheduledThreadPoolExecutor(50),
                kitchen,
                new OrderValueCalculator(kitchen),
                new DriverDispatcher(kitchen));
        orderFulfiller.placeOrders(orderQueue, new PoissonDistribution(3.25));

        ui.show();
    }

    private static List<String> getConfigurationForShelves(List<BasicShelf> shelves) {
        List<String> shelfConfiguration = new ArrayList<>();
        for (Shelf shelf : shelves) {
            shelfConfiguration.add(shelf.getType());
        }
        return shelfConfiguration;
    }

    private static ConcurrentLinkedQueue<Order> parseJSONAndQueueOrders() throws FileNotFoundException {
        Gson gson = new GsonBuilder().create();
        return new ConcurrentLinkedQueue<>(
                Arrays.asList(gson.fromJson(new FileReader("orders.json"), Order[].class)));

    }

    /*private static void showGUI(
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
    }*/
}
