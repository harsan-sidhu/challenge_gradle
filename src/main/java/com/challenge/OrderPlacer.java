package com.challenge;

import org.apache.commons.math3.distribution.PoissonDistribution;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class OrderPlacer {

    private final ScheduledExecutorService executorService;
    private final Kitchen kitchen;
    private final DriverDispatcher driverDispatcher;
    private final OrderValueCalculator orderValueCalculator;

    public OrderPlacer(ScheduledExecutorService executorService, Kitchen kitchen, OrderValueCalculator orderValueCalculator, DriverDispatcher driverDispatcher) {
        this.executorService = executorService;
        this.kitchen = kitchen;
        this.orderValueCalculator = orderValueCalculator;
        this.driverDispatcher = driverDispatcher;
    }

    public void placeOrders(Queue<Order> orderQueue, PoissonDistribution poissonDistribution) {
        executorService.scheduleAtFixedRate(
                () -> {
                    if (orderQueue.isEmpty() && kitchen.isEmpty()) {
                        executorService.shutdown();
                    } else {
                        int orders = poissonDistribution.sample();
                        for (int i = 0; i < orders; i++) {
                            if (!orderQueue.isEmpty()) {
                                // Add Order to Shelves
                                // TODO Consider addOrderToShelves returning a boolean, then not calculating or dispatching if false
                                Order placedOrder = orderQueue.poll();
                                placedOrder.setOrderTimeStamp(System.currentTimeMillis());
                                kitchen.addOrderToShelves(placedOrder);

                                // Calculate Value of Order
                                orderValueCalculator.beginContinuouslyCalculatingOrder(new ScheduledThreadPoolExecutor(50), placedOrder);

                                // Dispatch Driver To Get Order
                                int timeToPickUpOrder = ThreadLocalRandom.current().nextInt(2, 10 + 1);
                                placedOrder.setTimeToPickupOrder(timeToPickUpOrder);
                                driverDispatcher.dispatchForOrder(new ScheduledThreadPoolExecutor(50), placedOrder, timeToPickUpOrder);
                            }
                        }
                    }
                },
                /* initialDelay */ 0,
                /* period  */ 1,
                TimeUnit.SECONDS);

    }
}
