package com.challenge.shelf;

import com.challenge.TestUtils;
import com.challenge.clock.Clock;
import com.challenge.order.Delivery;
import com.challenge.order.Order;
import com.challenge.order.OrderType;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.*;
import static org.mockito.Mockito.*;

class OverflowShelfTest {

    private OverflowShelf overflowShelf;

    @Mock private ConcurrentLinkedQueue<Delivery> overflowShelfInternal;
    @Mock private PriorityQueue<Delivery> hotOrderPriorityQueue;
    @Mock private PriorityQueue<Delivery> coldOrderPriorityQueue;
    @Mock private PriorityQueue<Delivery> frozenOrderPriorityQueue;
    @Mock private Clock clock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        overflowShelf
                = new OverflowShelf(
                overflowShelfInternal,
                hotOrderPriorityQueue,
                coldOrderPriorityQueue,
                frozenOrderPriorityQueue);
    }

    @Test
    void whenHotOrderValueAtPickupOnOverFlowIsLessThanZero_add_addToHotOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Beef Hash", OrderType.HOT, 15, .8);
        Delivery delivery = new Delivery(atRiskOrder, 1, 10, clock);

        overflowShelf.add(delivery);

        verify(hotOrderPriorityQueue).add(delivery);
    }

    @Test
    void whenColdOrderValueAtPickupOnOverFlowIsLessThanZero_add_addToColdOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Sushi", COLD, 15, .8);
        Delivery delivery = new Delivery(atRiskOrder, 1, 10, clock);

        overflowShelf.add(delivery);

        verify(coldOrderPriorityQueue).add(delivery);
    }

    @Test
    void whenFrozenOrderValueAtPickupOnOverFlowIsLessThanZero_add_addToFrozenOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Icy", FROZEN, 15, .8);
        Delivery delivery = new Delivery(atRiskOrder, 1, 10, clock);

        overflowShelf.add(delivery);

        verify(frozenOrderPriorityQueue).add(delivery);
    }

    @Test
    void whenOrderValueAtPickupOnOverFlowIsMoreThanZero_add_doNotAddToOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Beef Hash", HOT, 300, .6);
        Delivery delivery = new Delivery(atRiskOrder, 1, 4, clock);

        overflowShelf.add(delivery);

        verify(hotOrderPriorityQueue, never()).add(delivery);
    }

    @Test
    void whenRemoveHotOrder_remove_removeHotOrderFromShelf() {
        Delivery delivery = new Delivery(TestUtils.createHotOrder(), 1, 1, clock);
        overflowShelf.add(delivery);
        when(overflowShelfInternal.contains(delivery)).thenReturn(true);

        overflowShelf.remove(delivery);

        verify(hotOrderPriorityQueue).remove(delivery);
    }

    @Test
    void whenRemoveColdOrder_remove_removeColdOrderFromShelf() {
        Delivery delivery = new Delivery(TestUtils.createColdOrder(), 1, 1, clock);
        overflowShelf.add(delivery);
        when(overflowShelfInternal.contains(delivery)).thenReturn(true);

        overflowShelf.remove(delivery);

        verify(coldOrderPriorityQueue).remove(delivery);
    }

    @Test
    void whenRemoveFrozenOrder_remove_removeFrozenOrderFromShelf() {
        Delivery delivery = new Delivery(Order.create("Icy", FROZEN, 9, 0.6), 1, 10, clock);
        overflowShelf.add(delivery);
        when(overflowShelfInternal.contains(delivery)).thenReturn(true);

        overflowShelf.remove(delivery);

        verify(frozenOrderPriorityQueue).remove(delivery);
    }

    @Test
    void whenHotOrderType_removeHighestPriorityOrder_removeDeliveryFromQueue() {
        Delivery delivery = new Delivery(TestUtils.createHotOrder(), 1, 1, clock);
        when(hotOrderPriorityQueue.poll()).thenReturn(delivery);

        Delivery highestPriorityDelivery = overflowShelf.removeHighestPriorityOrder(HOT);

        Assert.assertEquals(highestPriorityDelivery, delivery);
    }

    @Test
    void whenColdOrderType_removeHighestPriorityOrder_removeDeliveryFromQueue() {
        Delivery delivery = new Delivery(TestUtils.createColdOrder(), 1, 1, clock);
        when(coldOrderPriorityQueue.poll()).thenReturn(delivery);

        Delivery highestPriorityDelivery = overflowShelf.removeHighestPriorityOrder(COLD);

        Assert.assertEquals(highestPriorityDelivery, delivery);
    }

    @Test
    void whenFrozenOrderType_removeHighestPriorityOrder_removeDeliveryFromQueue() {
        Delivery delivery = new Delivery(TestUtils.createFrozenOrder(), 1, 1, clock);
        when(frozenOrderPriorityQueue.poll()).thenReturn(delivery);

        Delivery highestPriorityDelivery = overflowShelf.removeHighestPriorityOrder(FROZEN);

        Assert.assertEquals(highestPriorityDelivery, delivery);
    }
}
