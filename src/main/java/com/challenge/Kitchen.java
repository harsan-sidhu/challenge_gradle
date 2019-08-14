package com.challenge;

import com.challenge.order.Order;
import com.challenge.shelf.BasicShelf;
import com.challenge.shelf.Shelf;
import com.challenge.ui.DispatcherUICallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class Kitchen {

    private final Shelf hotShelf;
    private final Shelf coldShelf;
    private final Shelf frozenShelf;
    private final Shelf overFlowShelf;
    private final DispatcherUICallback uiCallback;

    public Kitchen(List<BasicShelf> shelves, DispatcherUICallback dispatcherUICallback) {
        hotShelf = shelves.get(0);
        coldShelf = shelves.get(1);
        frozenShelf = shelves.get(2);
        overFlowShelf = shelves.get(3);
        this.uiCallback = dispatcherUICallback;
    }

    public synchronized boolean addOrderToShelves(Order order) {
        boolean wasAddedToShelf = false;

        switch (order.getTemp()) {
            case "hot":
                wasAddedToShelf = hotShelf.add(order);
                break;
            case "cold":
                wasAddedToShelf = coldShelf.add(order);
                break;
            case "frozen":
                wasAddedToShelf = frozenShelf.add(order);
                break;
        }

        if (!wasAddedToShelf) {
            wasAddedToShelf = overFlowShelf.add(order);
        }

        updateUi();

        return wasAddedToShelf;
    }

    public synchronized boolean removeOrderFromShelves(Order order) {
        boolean wasRemovedFromShelf = false;

        switch (order.getTemp()) {
            case "hot":
                wasRemovedFromShelf = hotShelf.remove(order);
                break;
            case "cold":
                wasRemovedFromShelf = coldShelf.remove(order);
                break;
            case "frozen":
                wasRemovedFromShelf = frozenShelf.remove(order);
                break;
        }

        if (!wasRemovedFromShelf) {
            wasRemovedFromShelf = overFlowShelf.remove(order);
        }

        maybeMoveOverFlowOrders();

        updateUi();

        return wasRemovedFromShelf;

    }

    private synchronized void updateUi() {
        List<Shelf> shelvesToDisplay = new ArrayList<>();
        shelvesToDisplay.add(hotShelf);
        shelvesToDisplay.add(coldShelf);
        shelvesToDisplay.add(frozenShelf);
        shelvesToDisplay.add(overFlowShelf);

        uiCallback.onDataUpdated(shelvesToDisplay);
    }

    /*

    reconsider if this is needed
     */
    public synchronized boolean isOrderOnShelves(Order order) {
        return hotShelf.contains(order)
                || coldShelf.contains(order)
                || frozenShelf.contains(order)
                || overFlowShelf.contains(order);

    }

    public synchronized boolean isOrderOnOverFlowShelf(Order order) {
        return overFlowShelf.contains(order);
    }

    public synchronized boolean isEmpty() {
        return hotShelf.isEmpty()
                && coldShelf.isEmpty()
                && frozenShelf.isEmpty()
                && overFlowShelf.isEmpty();
    }

    public synchronized List<String> getShelfTypes() {
        return Arrays.asList(hotShelf.getType(), coldShelf.getType(), frozenShelf.getType(), overFlowShelf.getType());
    }

    // TODO Clean up javadoc
    // Calculate if the decay factor will make a difference.
    // Start by getting orders we know will hit 0 given the current
    // Take driver pickup time and replace with age, because the order age will be the time it takes for the driver to pickup
    // and recalculate without decay rate * 2 Take postive value
    // Take closest to 0
    // Add driver pickup time to model
    // TO DO consider moving to different class
    private synchronized void maybeMoveOverFlowOrders() {
        if (!overFlowShelf.isEmpty()) {
            PriorityQueue<Order> hotShelfPriorityQueue = new PriorityQueue<>();
            PriorityQueue<Order> coldShelfPriorityQueue = new PriorityQueue<>();
            PriorityQueue<Order> frozenShelfPriorityQueue = new PriorityQueue<>();
            for (Order overFlowOrder : overFlowShelf.getOrders()) {
                if (calculateOrderValueAtPickup(overFlowOrder, true) < 0) {
                    switch (overFlowOrder.getTemp()) {
                        case "hot":
                            if (calculateOrderValueAtPickup(overFlowOrder, false) > 0) {
                                hotShelfPriorityQueue.add(overFlowOrder);
                            }
                            break;
                        case "cold":
                            if (calculateOrderValueAtPickup(overFlowOrder, false) > 0) {
                                coldShelfPriorityQueue.add(overFlowOrder);
                            }
                            break;
                        case "frozen":
                            if (calculateOrderValueAtPickup(overFlowOrder, false) > 0) {
                                frozenShelfPriorityQueue.add(overFlowOrder);
                            }
                            break;
                    }
                }
            }

            while (hotShelf.size() < 15 && !hotShelfPriorityQueue.isEmpty()) {
                Order order = hotShelfPriorityQueue.poll();
                removeOrderFromShelves(order);
                addOrderToShelves(order);
            }
            while (coldShelf.size() < 15 && !coldShelfPriorityQueue.isEmpty()) {
                Order order = coldShelfPriorityQueue.poll();
                removeOrderFromShelves(order);
                addOrderToShelves(order);
            }
            while (frozenShelf.size() < 15 && !frozenShelfPriorityQueue.isEmpty()) {
                Order order = frozenShelfPriorityQueue.poll();
                removeOrderFromShelves(order);
                addOrderToShelves(order);
            }
        }
    }

    private static double calculateOrderValueAtPickup(Order order, boolean isOverFlow) {
        int orderAgeAtPickup = order.getTimeToPickupOrder();
        double decayRate = isOverFlow ? order.getDecayRate() * 2 : order.getDecayRate();
        double value = (order.getShelfLife() - orderAgeAtPickup) - (decayRate * orderAgeAtPickup);
        return value/order.getShelfLife();
    }
}
