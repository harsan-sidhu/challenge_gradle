package com.challenge;

import com.challenge.dispatcher.DeliveryPickupDispatcher;
import com.challenge.kitchen.Kitchen;
import com.challenge.order.AutoValueGsonFactory;
import com.challenge.order.Order;
import com.challenge.order.OrderType;
import com.challenge.shelf.*;
import com.challenge.ui.OrderFulfillerUI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.math3.distribution.PoissonDistribution;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {

    public static void main(String[] args) throws IOException {
        ConcurrentLinkedQueue<Order> orderQueue = parseJSONAndQueueOrders();

        List<BasicShelf> shelves
                = Arrays.asList(
                new HotShelf(new ConcurrentLinkedQueue<>()),
                new ColdShelf(new ConcurrentLinkedQueue<>()),
                new FrozenShelf(new ConcurrentLinkedQueue<>()),
                new OverflowShelf(
                        new ConcurrentLinkedQueue<>(),
                        new PriorityQueue<>(new OverflowShelf.DeliveryPriorityComparator()),
                        new PriorityQueue<>(new OverflowShelf.DeliveryPriorityComparator()),
                        new PriorityQueue<>(new OverflowShelf.DeliveryPriorityComparator())));

        OrderFulfillerUI ui = new OrderFulfillerUI(getConfigurationForShelves(shelves));
        ui.show();

        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(4);
        Kitchen kitchen = new Kitchen(shelves, ui);
        OrderFulfiller orderFulfiller
                = new OrderFulfiller(
                executor,
                kitchen,
                new DeliveryPickupDispatcher(executor, kitchen));
        orderFulfiller.placeOrders(orderQueue, new PoissonDistribution(3.25));
    }

    private static List<OrderType> getConfigurationForShelves(List<BasicShelf> shelves) {
        List<OrderType> shelfConfiguration = new ArrayList<>();
        for (Shelf shelf : shelves) {
            shelfConfiguration.add(shelf.getType());
        }
        return shelfConfiguration;
    }

    private static ConcurrentLinkedQueue<Order> parseJSONAndQueueOrders() throws FileNotFoundException {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create()).create();
        return new ConcurrentLinkedQueue<>(
                Arrays.asList(gson.fromJson(new FileReader("orders.json"), Order[].class)));

    }


}
