package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.Order;
import com.challenge.order.OrderType;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.OVERFLOW;

/**
 * A {@link BasicShelf} that can store any {@link OrderType}.
 */
public class OverflowShelf extends BasicShelf {

    private static final int OVERFLOW_SHELF_CAPACITY = 20;

    private final PriorityQueue<Delivery> hotOrderPriorityQueue;
    private final PriorityQueue<Delivery> coldOrderPriorityQueue;
    private final PriorityQueue<Delivery> frozenOrderPriorityQueue;

    public OverflowShelf(
            ConcurrentLinkedQueue<Delivery> overFlowShelf,
            PriorityQueue<Delivery> hotOrderPriorityQueue,
            PriorityQueue<Delivery> coldOrderPriorityQueue,
            PriorityQueue<Delivery> frozenOrderPriorityQueue) {
        super(overFlowShelf);

        this.hotOrderPriorityQueue = hotOrderPriorityQueue;
        this.coldOrderPriorityQueue = coldOrderPriorityQueue;
        this.frozenOrderPriorityQueue = frozenOrderPriorityQueue;
    }

    @Override
    public synchronized boolean add(Delivery order) {
        boolean wasAdded = super.add(order);
        if (wasAdded && calculateOrderValueAtPickupOnOverFlowShelf(order) <= 0) {
            switch (order.getOrder().getTemp()) {
                case HOT:
                    hotOrderPriorityQueue.add(order);
                    break;
                case COLD:
                    coldOrderPriorityQueue.add(order);
                    break;
                case FROZEN:
                    frozenOrderPriorityQueue.add(order);
                    break;
            }
        }
        return wasAdded;
    }

    private double calculateOrderValueAtPickupOnOverFlowShelf(Delivery delivery) {
        Order order = delivery.getOrder();
        int orderAgeAtPickup = delivery.getPickupTime();
        return ((order.getShelfLife() - orderAgeAtPickup) - ((order.getDecayRate() * decayMultiplier()) * orderAgeAtPickup))/order.getShelfLife();
    }

    @Override
    public synchronized boolean remove(Delivery order) {
        boolean wasRemoved = super.remove(order);
        if (wasRemoved) {
            switch (order.getOrder().getTemp()) {
                case HOT:
                    hotOrderPriorityQueue.remove(order);
                    break;
                case COLD:
                    coldOrderPriorityQueue.remove(order);
                    break;
                case FROZEN:
                    frozenOrderPriorityQueue.remove(order);
                    break;
            }
        }

        return wasRemoved;
    }

    @Override
    public OrderType getType() {
        return OVERFLOW;
    }

    @Override
    public int capacity() {
        return OVERFLOW_SHELF_CAPACITY;
    }

    @Override
    public double decayMultiplier() {
        return 2;
    }

  /**
   * For a given {@link OrderType} on this shelf, retreive the order that is high priority.
   *
   * Currently, we dispatched a specific driver for a specific order.
   *
   * Given this, when we assign the driver with an ETA for an order pickup, we can calculate for any order its normalized
   * value upon pick up. This is because the order age will equal the ETA.
   *
   * As a result, any time an order is added to the overflow shelf, we perform the above
   * calculation to deem whether the item is "at risk", it's normalized value would reach zero if
   * left on that shelf. If this is the case, it'll be added to a Priority Queue sorted by the value
   * it would have if moved to its temperature appropriate shelf. When an order is removed we then
   * ask the overflow shelf to give us the most "at risk" order for the shelf an item was just
   * removed from, then add it to the temperate appropriate shelf.
   *
   * @param type {@link OrderType} of the order to return.
   * @return {@link Delivery} most likely of spoiling.
   */
  public synchronized Delivery removeHighestPriorityOrder(OrderType type) {
        Delivery highestPriorityOrderToMove;

        switch (type) {
            case HOT:
                highestPriorityOrderToMove = hotOrderPriorityQueue.poll();
                break;
            case COLD:
                highestPriorityOrderToMove = coldOrderPriorityQueue.poll();
                break;
            case FROZEN:
                highestPriorityOrderToMove = frozenOrderPriorityQueue.poll();
                break;
            default:
                throw new IllegalStateException("Unrecognized OrderType: " + type);
        }

        if (highestPriorityOrderToMove != null) {
            remove(highestPriorityOrderToMove);
        }

        return highestPriorityOrderToMove;
    }

    /**
     * Compares orders if they were placed on their temperature specific shelf taking into account their current decayed
     * values.
     */
    public static class DeliveryPriorityComparator implements Comparator<Delivery> {
        @Override
        public int compare(Delivery o1, Delivery o2) {
            return Double.compare(calculateOrderValueOnTargetShelf(o1), calculateOrderValueOnTargetShelf(o2));
        }
    }

    private static double calculateOrderValueOnTargetShelf(Delivery delivery) {
        Order order = delivery.getOrder();
        int orderAgeAtPickup = delivery.getPickupTime();
        double targetShelfDecayRateMultiplier = getTargetShelfDecayRateMultiplier(delivery);

        return ((order.getShelfLife() - orderAgeAtPickup) - (((order.getDecayRate() * targetShelfDecayRateMultiplier) * orderAgeAtPickup) + delivery.getDecayedValue()))/order.getShelfLife();
    }

    private static double getTargetShelfDecayRateMultiplier(Delivery delivery) {
        switch(delivery.getOrder().getTemp()) {
            case HOT:
                return HotShelf.DECAY_RATE;
            case COLD:
                return ColdShelf.DECAY_RATE;
            case FROZEN:
                return FrozenShelf.DECAY_RATE;
            default:
                throw new IllegalStateException("Unrecognized OrderType: " + delivery.getOrder().getTemp());
        }
    }
}
