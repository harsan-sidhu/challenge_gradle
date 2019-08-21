package com.challenge;

import com.challenge.clock.Clock;
import com.challenge.dispatcher.DeliveryPickupDispatcher;
import com.challenge.kitchen.Kitchen;
import com.challenge.order.Delivery;
import com.challenge.order.Order;
import org.apache.commons.math3.distribution.PoissonDistribution;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class OrderFulfiller {

    private final ScheduledExecutorService executorService;
    private final Kitchen kitchen;
    private final DeliveryPickupDispatcher dispatcher;
    private final Clock clock;

    public OrderFulfiller(
            ScheduledExecutorService executorService,
            Kitchen kitchen,
            DeliveryPickupDispatcher dispatcher,
            Clock clock) {
        this.executorService = executorService;
        this.kitchen = kitchen;
        this.dispatcher = dispatcher;
        this.clock = clock;
    }

    public void placeOrders(Queue<Order> orderQueue, PoissonDistribution poissonDistribution) {
        executorService.scheduleAtFixedRate(
                () -> placeOrderOrShutdown(orderQueue, poissonDistribution),
                /* initialDelay */ 0,
                /* period  */ 1,
                TimeUnit.SECONDS);
    }

    public void placeOrderOrShutdown(Queue<Order> orderQueue, PoissonDistribution poissonDistribution) {
        if (orderQueue.isEmpty() && kitchen.isEmpty()) {
            executorService.shutdown();
        } else {
            int orders = poissonDistribution.sample();
            for (int i = 0; i < orders; i++) {
                if (!orderQueue.isEmpty()) {
                    Order placedOrder = orderQueue.poll();

                    long orderTimeStamp = clock.getCurrentTimeInSeconds();
                    int timeToPickUpOrder = ThreadLocalRandom.current().nextInt(2, 10 + 1);
                    Delivery delivery = new Delivery(placedOrder, orderTimeStamp, timeToPickUpOrder, clock);

                    if (kitchen.addOrderToShelves(delivery)) {
                        dispatcher.dispatchPickupForOrder(delivery, timeToPickUpOrder);
                    }
                }
            }
        }

    }
}
