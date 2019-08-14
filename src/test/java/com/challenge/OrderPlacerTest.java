package com.challenge;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderPlacerTest {

    @Mock
    private PoissonDistribution poissonDistribution;
    @Mock
    private Kitchen kitchen;
    @Mock
    private OrderValueCalculator orderValueCalculator;
    @Mock
    private DriverDispatcher driverDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void withOrder_placeOrder_samplePoissonDistribution() throws InterruptedException {
        OrderPlacer orderPlacer = new OrderPlacer(Executors.newSingleThreadScheduledExecutor(), kitchen, orderValueCalculator, driverDispatcher);
        Order order = new Order();
        Queue<Order> orders = new ConcurrentLinkedQueue<>();
        orders.add(order);
        when(poissonDistribution.sample()).thenReturn(1);

        // TODO Don't use Sleep
        orderPlacer.placeOrders(orders, poissonDistribution);
        Thread.sleep(500);

        verify(poissonDistribution).sample();
    }

    @Test
    void withOrder_placeOrder_addOrderToShelf() throws InterruptedException {
        OrderPlacer orderPlacer = new OrderPlacer(Executors.newSingleThreadScheduledExecutor(), kitchen, orderValueCalculator, driverDispatcher);
        Order order = new Order();
        Queue<Order> orders = new ConcurrentLinkedQueue<>();
        orders.add(order);
        when(poissonDistribution.sample()).thenReturn(1);

        // TODO Don't use Sleep
        orderPlacer.placeOrders(orders, poissonDistribution);
        Thread.sleep(500);

        verify(kitchen).addOrderToShelves(order);
    }

    @Test
    void withOrder_placeOrder_calculateOrder() throws InterruptedException {
        OrderPlacer orderPlacer = new OrderPlacer(Executors.newSingleThreadScheduledExecutor(), kitchen, orderValueCalculator, driverDispatcher);
        Order order = new Order();
        Queue<Order> orders = new ConcurrentLinkedQueue<>();
        orders.add(order);
        when(poissonDistribution.sample()).thenReturn(1);

        // TODO Don't use Sleep
        orderPlacer.placeOrders(orders, poissonDistribution);
        Thread.sleep(500);

        verify(orderValueCalculator).beginContinuouslyCalculatingOrder(any(), eq(order));
    }

    @Test
    void withOrder_placeOrder_dispatchDriver() throws InterruptedException {
        OrderPlacer orderPlacer = new OrderPlacer(Executors.newSingleThreadScheduledExecutor(), kitchen, orderValueCalculator, driverDispatcher);
        Order order = new Order();
        Queue<Order> orders = new ConcurrentLinkedQueue<>();
        orders.add(order);
        when(poissonDistribution.sample()).thenReturn(1);

        // TODO Don't use Sleep
        orderPlacer.placeOrders(orders, poissonDistribution);
        Thread.sleep(500);

        verify(driverDispatcher).dispatchForOrder(any(), eq(order), anyInt());
    }
}