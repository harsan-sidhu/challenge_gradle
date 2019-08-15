package com.challenge.order;

public class OrderValueCalculator {

    public static double computeValueOfDelivery(Delivery delivery, long orderAge, double decayMultiplier) {
        Order order = delivery.getOrder();
        return ((order.getShelfLife() - orderAge) - ((order.getDecayRate() * decayMultiplier) * orderAge))/order.getShelfLife();
    }
}
