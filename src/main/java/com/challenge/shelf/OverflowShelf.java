package com.challenge.shelf;

import com.challenge.order.Delivery;
import com.challenge.order.Order;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OverflowShelf extends BasicShelf {

    private static final int OVERFLOW_SHELF_CAPACITY = 20;

    private final PriorityQueue<Delivery> hotShelfPriorityQueue;

    public OverflowShelf(ConcurrentLinkedQueue<Delivery> overFlowShelf) {
        super(overFlowShelf);

        hotShelfPriorityQueue = new PriorityQueue<>(new DeliveryPriorityComparator());
    }

    @Override
    public synchronized boolean add(Delivery order) {
        if (calculateOrderValueAtPickupOnOverFlowShelf(order) <= 0) {
            hotShelfPriorityQueue.add(order);
        }
        return super.add(order);
    }

    @Override
    public String getType() {
        return "overflow";
    }

    @Override
    public int capacity() {
        return OVERFLOW_SHELF_CAPACITY;
    }

    @Override
    public double decayMultiplier() {
        return 2;
    }

    /*public Delivery removeHighestPriorityOrder(String type) {
        switch (type) {
            case "hot":
                return hotShelfPriorityQueue.poll();
            case "cold":
                return co
        }
    }*/


    private double calculateOrderValueAtPickupOnOverFlowShelf(Delivery delivery) {
        Order order = delivery.getOrder();
        int orderAgeAtPickup = delivery.getPickupTime();
        return ((order.getShelfLife() - orderAgeAtPickup) - ((order.getDecayRate() * decayMultiplier()) * orderAgeAtPickup))/order.getShelfLife();
    }

    private static double calculateOrderValueOnTargetShelf(Delivery delivery) {
        Order order = delivery.getOrder();
        int orderAgeAtPickup = delivery.getPickupTime();
        double targetShelfDecayRateMultiplier = getTargetShelfDecayRateMultiplier(delivery);

        return ((order.getShelfLife() - orderAgeAtPickup) - (((order.getDecayRate() * targetShelfDecayRateMultiplier) * orderAgeAtPickup) + delivery.getDecayedValue()))/order.getShelfLife();
    }

    private static double getTargetShelfDecayRateMultiplier(Delivery delivery) {
        switch(delivery.getOrder().getTemp()) {
            case "hot":
                return HotShelf.DECAY_RATE;
            case "cold":
                return ColdShelf.DECAY_RATE;
            case "frozen":
                return FrozenShelf.DECAY_RATE;
            default:
                // TODO Throw exception instead
                return -1;

        }
    }

    private static class DeliveryPriorityComparator implements Comparator<Delivery> {
        @Override
        public int compare(Delivery o1, Delivery o2) {
            return (int) (calculateOrderValueOnTargetShelf(o1) - calculateOrderValueOnTargetShelf(o2));
        }
    }


}
