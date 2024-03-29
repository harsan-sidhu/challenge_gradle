package com.challenge.kitchen;

import com.challenge.order.Delivery;
import com.challenge.order.OrderType;
import com.challenge.shelf.BasicShelf;
import com.challenge.shelf.OverflowShelf;
import com.challenge.shelf.Shelf;
import com.challenge.ui.DispatcherUICallback;

import java.util.ArrayList;
import java.util.List;

import static com.challenge.order.OrderType.*;

/**
 * Class containing all the shelves with {@link Delivery}'s
 *
 * This class is responsible for the following
 * 1) Adding and removing deliveries from shelves
 * 2) Trashing spoiled orders
 * 3) Notifying the UI when new data is available.
 */
public class Kitchen {

    private final Shelf hotShelf;
    private final Shelf coldShelf;
    private final Shelf frozenShelf;
    private final OverflowShelf overFlowShelf;
    private final DispatcherUICallback uiCallback;

    public Kitchen(List<BasicShelf> shelves, DispatcherUICallback dispatcherUICallback) {
        hotShelf = shelves.get(0);
        coldShelf = shelves.get(1);
        frozenShelf = shelves.get(2);
        overFlowShelf = (OverflowShelf) shelves.get(3);
        this.uiCallback = dispatcherUICallback;
    }

    /**
     * @param order {@link Delivery} to add to a shelf
     *
     * @return true/false whether the order was added to a shelf
     */
    public synchronized boolean addOrderToShelves(Delivery order) {
        boolean wasAddedToShelf;

        maybeTrashSpoiledOrders();

        switch (order.getOrder().getTemp()) {
            case HOT:
                wasAddedToShelf = hotShelf.add(order);
                break;
            case COLD:
                wasAddedToShelf = coldShelf.add(order);
                break;
            case FROZEN:
                wasAddedToShelf = frozenShelf.add(order);
                break;
            default:
                throw new IllegalStateException("Unrecognized OrderType: " + order.getOrder().getTemp());
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

    /**
     * Given an order, attempt to remove it. When a delivery is removed see if we move an "at risk" delivery from the
     * overflow shelf.
     *
     * @param order {@link Delivery} to remove from a shelf
     *
     * @return true/false whether the order was removed to a shelf
     */
    public synchronized boolean removeOrderFromShelves(Delivery order) {
        boolean wasRemovedFromShelf;
        OrderType removedOrderType;

        maybeTrashSpoiledOrders();

        switch (order.getOrder().getTemp()) {
            case HOT:
                wasRemovedFromShelf = hotShelf.remove(order);
                removedOrderType = HOT;
                break;
            case COLD:
                wasRemovedFromShelf = coldShelf.remove(order);
                removedOrderType = COLD;
                break;
            case FROZEN:
                wasRemovedFromShelf = frozenShelf.remove(order);
                removedOrderType = FROZEN;
                break;
            default:
                throw new IllegalStateException("Unrecognized OrderType: " + order.getOrder().getTemp());
        }

        if (!wasRemovedFromShelf) {
            wasRemovedFromShelf = overFlowShelf.remove(order);
        } else {
            Delivery deliveryToMoveOffOverflowShelf = overFlowShelf.removeHighestPriorityOrder(removedOrderType);
            if (deliveryToMoveOffOverflowShelf != null) {
                addOrderToShelves(deliveryToMoveOffOverflowShelf);
            }
        }

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

    /**
     * @return true/false based on if there are any orders left.
     */
    public synchronized boolean isEmpty() {
        return hotShelf.isEmpty()
                && coldShelf.isEmpty()
                && frozenShelf.isEmpty()
                && overFlowShelf.isEmpty();
    }
}
