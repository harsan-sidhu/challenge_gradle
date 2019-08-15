package com.challenge.kitchen;

import com.challenge.order.Delivery;
import com.challenge.order.OrderValueCalculator;
import com.challenge.shelf.BasicShelf;
import com.challenge.shelf.Shelf;
import com.challenge.ui.DispatcherUICallback;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Kitchen {

    private final Shelf hotShelf;
    private final Shelf coldShelf;
    private final Shelf frozenShelf;
    private final Shelf overFlowShelf;
    private final DispatcherUICallback uiCallback;

    //TODO: I would just initalize your shelves in this constructor. No need to pass them in, unless you want to test other scenarios.
    public Kitchen(List<BasicShelf> shelves, DispatcherUICallback dispatcherUICallback) {
        hotShelf = shelves.get(0);
        coldShelf = shelves.get(1);
        frozenShelf = shelves.get(2);
        overFlowShelf = shelves.get(3);
        this.uiCallback = dispatcherUICallback;
    }

    public synchronized boolean addOrderToShelves(Delivery order) {
        boolean wasAddedToShelf = false;

        maybeTrashSpoiledOrders();

        //TODO: Consider an enum instead of using strings. It'll make things easier later if you add new types.
        switch (order.getOrder().getTemp()) {
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

    private void maybeTrashSpoiledOrders() {
        hotShelf.maybeTrashSpoiledOrders();
        coldShelf.maybeTrashSpoiledOrders();
        frozenShelf.maybeTrashSpoiledOrders();
        overFlowShelf.maybeTrashSpoiledOrders();
    }

    public synchronized boolean removeOrderFromShelves(Delivery order) {
        boolean wasRemovedFromShelf = false;

        maybeTrashSpoiledOrders();

        switch (order.getOrder().getTemp()) {
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

    public synchronized boolean isEmpty() {
        return hotShelf.isEmpty()
                && coldShelf.isEmpty()
                && frozenShelf.isEmpty()
                && overFlowShelf.isEmpty();
    }

    // Calculate if the decay factor will make a difference.
    // Start by getting orders we know will hit 0 given the current
    // Take driver pickup time and replace with age, because the order age will be the time it takes for the driver to pickup
    // and recalculate without decay rate * 2 Take postive value
    // Take closest to 0
    // Add driver pickup time to model
    // TO DO consider moving to different class
    private void maybeMoveOverFlowOrders() {
        if (!overFlowShelf.isEmpty()) {
            /*
            TODO: I would consider moving this to the overflow shelf and make the API something like: public Delivery removeHighestPriorityOrder(TYPE).
             That would make your code much cleaner, and put the responsibility where it belongs.
             Also, bonus, while you're doing this, you can trash spoiled orders.
             Consider adding to these priority queues on the addition of items in overflow instead of at the moment you're calling maybeMoveOverFlowOrders.
             */
            PriorityQueue<Delivery> hotShelfPriorityQueue = new PriorityQueue<>();
            PriorityQueue<Delivery> coldShelfPriorityQueue = new PriorityQueue<>();
            PriorityQueue<Delivery> frozenShelfPriorityQueue = new PriorityQueue<>();

            /*
            TODO: Taking my suggestion above into account, you can also delete the getOrders method. That method is not thread safe in this situation.
             The reason is that synchronized synchronizes only on the instance of the object, so the moment you pull the order list out, you've lost thread safety in the shelf, so if it mutates
             you're in for a bad time. While this is super edge casey, it's also a bad code smell, so reconsider it.
            */
            for (Delivery overFlowOrder : overFlowShelf.getOrders()) {
                if (calculateOrderValueAtPickup(overFlowOrder, overFlowOrder.getShelf().decayMultiplier()) < 0) {
                    switch (overFlowOrder.getOrder().getTemp()) {
                        case "hot":
                            if (calculateOrderValueAtPickup(overFlowOrder, hotShelf.decayMultiplier()) > 0) {
                                hotShelfPriorityQueue.add(overFlowOrder);
                            }
                            break;
                        case "cold":
                            if (calculateOrderValueAtPickup(overFlowOrder, coldShelf.decayMultiplier()) > 0) {
                                coldShelfPriorityQueue.add(overFlowOrder);
                            }
                            break;
                        case "frozen":
                            if (calculateOrderValueAtPickup(overFlowOrder, frozenShelf.decayMultiplier()) > 0) {
                                frozenShelfPriorityQueue.add(overFlowOrder);
                            }
                            break;
                    }
                }
            }

            while (hotShelf.size() < hotShelf.capacity() && !hotShelfPriorityQueue.isEmpty()) {
                Delivery order = hotShelfPriorityQueue.poll();
                overFlowShelf.remove(order);
                hotShelf.add(order);
            }
            while (coldShelf.size() < coldShelf.capacity() && !coldShelfPriorityQueue.isEmpty()) {
                Delivery order = coldShelfPriorityQueue.poll();
                overFlowShelf.remove(order);
                coldShelf.add(order);
            }
            while (frozenShelf.size() < frozenShelf.capacity() && !frozenShelfPriorityQueue.isEmpty()) {
                Delivery order = frozenShelfPriorityQueue.poll();
                overFlowShelf.remove(order);
                frozenShelf.add(order);
            }
        }
    }

    private static double calculateOrderValueAtPickup(Delivery order, double shelfDecayMultiplier) {
        return OrderValueCalculator.computeValueOfDelivery(order, order.getPickupTime(), shelfDecayMultiplier);
    }
}
