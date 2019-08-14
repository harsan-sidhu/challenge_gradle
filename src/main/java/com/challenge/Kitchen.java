package com.challenge;

import java.util.List;
import java.util.PriorityQueue;

public class Kitchen {

    private final Shelf hotShelf;
    private final Shelf coldShelf;
    private final Shelf frozenShelf;
    private final Shelf overFlowShelf;

    public Kitchen(List<Shelf> shelves) {
        hotShelf = shelves.get(0);
        coldShelf = shelves.get(1);
        frozenShelf = shelves.get(2);
        overFlowShelf = shelves.get(3);
    }

    public synchronized void addOrderToShelves(Order order) {
        boolean wasAddedToShelf = false;

        switch (order.getTemp()) {
            case "hot":
                if (hotShelf.size() < 15) {
                    hotShelf.addOrderToShelf(order);
                    wasAddedToShelf = true;
                }
                break;
            case "cold":
                if (coldShelf.size() < 15) {
                    coldShelf.addOrderToShelf(order);
                    wasAddedToShelf = true;
                }
                break;
            case "frozen":
                if (frozenShelf.size() < 15) {
                    frozenShelf.addOrderToShelf(order);
                    wasAddedToShelf = true;
                }
                break;
        }

        if (!wasAddedToShelf && overFlowShelf.size() < 20) {
            overFlowShelf.addOrderToShelf(order);
        }
    }

    public synchronized void removeOrderFromShelves(Order order) {
        boolean wasRemovedFromShelf = false;

        switch (order.getTemp()) {
            case "hot":
                if (hotShelf.contains(order)) {
                    hotShelf.removeOrderFromShelf(order);
                    wasRemovedFromShelf = true;
                }
                break;
            case "cold":
                if (coldShelf.contains(order)) {
                    coldShelf.removeOrderFromShelf(order);
                    wasRemovedFromShelf = true;
                }
                break;
            case "frozen":
                if (frozenShelf.contains(order)) {
                    frozenShelf.removeOrderFromShelf(order);
                    wasRemovedFromShelf = true;
                }
                break;
        }

        if (!wasRemovedFromShelf && overFlowShelf.contains(order)) {
            overFlowShelf.removeOrderFromShelf(order);
        }

        maybeMoveOverFlowOrders();

    }

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
