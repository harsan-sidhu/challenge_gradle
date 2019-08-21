package com.challenge;

import com.challenge.clock.Clock;
import com.challenge.dispatcher.DeliveryPickupDispatcher;
import com.challenge.kitchen.Kitchen;
import com.challenge.order.Delivery;
import com.challenge.order.Order;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderFulfillerTest {

    private OrderFulfiller orderFulfiller;

    @Mock private ScheduledExecutorService executor;
    @Mock private Kitchen kitchen;
    @Mock private DeliveryPickupDispatcher dispatcher;
    @Mock private Clock clock;
    @Mock private PoissonDistribution poissonDistribution;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        orderFulfiller = new OrderFulfiller(executor, kitchen, dispatcher, clock);
    }

    @Test
    void whenOrdersAreQueued_placeOrderOrShutdown_placeOrder() {
        Order order = mock(Order.class);
        Queue<Order> orderQueue = new PriorityQueue();
        orderQueue.add(order);
        when(poissonDistribution.sample()).thenReturn(1);
        when(kitchen.addOrderToShelves(any())).thenReturn(true);

        orderFulfiller.placeOrderOrShutdown(orderQueue, poissonDistribution);

        verify(kitchen).addOrderToShelves(any(Delivery.class));
        verify(dispatcher).dispatchPickupForOrder(any(Delivery.class), anyInt());
    }

    @Test
    void whenNoOrdersAreQueued_placeOrderOrShutdown_shutdown() {
        when(kitchen.isEmpty()).thenReturn(true);

        orderFulfiller.placeOrderOrShutdown(new ConcurrentLinkedQueue<>(), poissonDistribution);

        verify(executor).shutdown();
        verifyZeroInteractions(dispatcher);
    }
}
