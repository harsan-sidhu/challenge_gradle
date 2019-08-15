package com.challenge.order;

public class OrderValueCalculator {

    /*
    TODO: Here's a fun GOTCHA, that a lot of people miss. If your order is on the decay shelf for 20 seconds decaying at 2x
     then moves to a cold shelf at decays at 1x for another 10 seconds, and the value of the order when you first received it is 100, with a normal decay rate of 1.0
     what is the value of the delivery? (Shelf life is 100)
     Most people calculate this wrong because they forget moving shelves means the original decay your product suffered from being on the wrong shelf is carried with them.
     Consider this in your calculation. It is a GOTCHA that is not obvious.
     */
    public static double computeValueOfDelivery(Delivery delivery, long orderAge, double decayMultiplier) {
        Order order = delivery.getOrder();
        return ((order.getShelfLife() - orderAge) - ((order.getDecayRate() * decayMultiplier) * orderAge))/order.getShelfLife();
    }
}
