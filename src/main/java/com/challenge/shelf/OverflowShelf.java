package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.Order;
import com.challenge.order.OrderType;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.OVERFLOW;

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
        if (calculateOrderValueAtPickupOnOverFlowShelf(order) <= 0) {
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
        return super.add(order);
    }

    private double calculateOrderValueAtPickupOnOverFlowShelf(Delivery delivery) {
        Order order = delivery.getOrder();
        int orderAgeAtPickup = delivery.getPickupTime();
        return ((order.getShelfLife() - orderAgeAtPickup) - ((order.getDecayRate() * decayMultiplier()) * orderAgeAtPickup))/order.getShelfLife();
    }

    @Override
    public synchronized boolean remove(Delivery order) {
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

        return super.remove(order);
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

    public Delivery removeHighestPriorityOrder(OrderType type) {
        Delivery highestPriorityOrderToMove = null;

        switch (type) {
            case HOT:
                return hotOrderPriorityQueue.poll();
            case COLD:
                return coldOrderPriorityQueue.poll();
            case FROZEN:
                return frozenOrderPriorityQueue.poll();
            default:
                return highestPriorityOrderToMove;
        }
    }

    public static class DeliveryPriorityComparator implements Comparator<Delivery> {
        @Override
        public int compare(Delivery o1, Delivery o2) {
            return (int) (calculateOrderValueOnTargetShelf(o1) - calculateOrderValueOnTargetShelf(o2));
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
