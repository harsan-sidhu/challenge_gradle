package com.challenge;

import com.challenge.order.Delivery;
import com.challenge.dispatcher.DeliveryPickupDispatcher;
import com.challenge.kitchen.Kitchen;
import com.challenge.order.Order;
import org.apache.commons.math3.distribution.PoissonDistribution;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class OrderFulfiller {

    private final Lock lock = new ReentrantLock();

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

    synchronized void placeOrders(Queue<Order> orderQueue, PoissonDistribution poissonDistribution) {
        executorService.scheduleAtFixedRate(
                () -> {
                    if (orderQueue.isEmpty() && kitchen.isEmpty()) {
                        executorService.shutdown();
                    } else {
                        int orders = poissonDistribution.sample();
                        for (int i = 0; i < orders; i++) {
                            if (!orderQueue.isEmpty()) {
                                Order placedOrder = orderQueue.poll();

                                long orderTimeStamp = System.currentTimeMillis();
                                int timeToPickUpOrder = ThreadLocalRandom.current().nextInt(2, 10 + 1);
                                Delivery delivery = new Delivery(placedOrder, orderTimeStamp, timeToPickUpOrder);

                                if (kitchen.addOrderToShelves(delivery)) {
                                    deliveryPickupDispatcher.dispatchPickupForOrder(
                                            Executors.newSingleThreadScheduledExecutor(), delivery, timeToPickUpOrder);
                                }
                            }
                        }
                    }
                },
                /* initialDelay */ 0,
                /* period  */ 1,
                TimeUnit.SECONDS);
    }
}
