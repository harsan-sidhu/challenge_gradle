package com.challenge;

import com.challenge.dispatcher.DeliveryPickupDispatcher;
import com.challenge.kitchen.Kitchen;
import com.challenge.order.Delivery;
import com.challenge.order.Order;
import org.apache.commons.math3.distribution.PoissonDistribution;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

class OrderFulfiller {

    private final ScheduledExecutorService executorService;
    private final Kitchen kitchen;
    private final DeliveryPickupDispatcher deliveryPickupDispatcher;

    OrderFulfiller(ScheduledExecutorService executorService,
                   Kitchen kitchen,
                   DeliveryPickupDispatcher deliveryPickupDispatcher) {
        this.executorService = executorService;
        this.kitchen = kitchen;
        this.deliveryPickupDispatcher = deliveryPickupDispatcher;
    }

    void placeOrders(Queue<Order> orderQueue, PoissonDistribution poissonDistribution) {
        executorService.scheduleAtFixedRate(
                () -> placeOrderOrShutdown(orderQueue, poissonDistribution),
                /* initialDelay */ 0,
                /* period  */ 1,
                TimeUnit.SECONDS);
    }

    private void placeOrderOrShutdown(Queue<Order> orderQueue, PoissonDistribution poissonDistribution) {
        if (orderQueue.isEmpty() && kitchen.isEmpty()) {
            executorService.shutdown();
        } else {
            int orders = poissonDistribution.sample();
            for (int i = 0; i < orders; i++) {
                if (!orderQueue.isEmpty()) {
                    Order placedOrder = orderQueue.poll();

                    // TODO Careful with using the system clock directly. It'll make testing hard. Consider creating a Clock interface, and creating an implementation for testing and for this app.
                    long orderTimeStamp = System.currentTimeMillis();
                    int timeToPickUpOrder = ThreadLocalRandom.current().nextInt(2, 10 + 1);
                    Delivery delivery = new Delivery(placedOrder, orderTimeStamp, timeToPickUpOrder);

                    if (kitchen.addOrderToShelves(delivery)) {
                        deliveryPickupDispatcher.dispatchPickupForOrder(delivery, timeToPickUpOrder);
                    }
                }
            }
        }

    }
}
