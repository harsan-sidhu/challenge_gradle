package com.challenge.shelf;

import static org.junit.Assert.assertTrue;

import com.challenge.TestUtils;
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

import static org.mockito.Mockito.*;

public class OverflowShelfTest {

    private OverflowShelf overflowShelf;

    @Mock private ConcurrentLinkedQueue<Delivery> overflowShelfInternal;
    @Mock private PriorityQueue<Delivery> hotOrderPriorityQueue;
    @Mock private PriorityQueue<Delivery> coldOrderPriorityQueue;
    @Mock private PriorityQueue<Delivery> frozenOrderPriorityQueue;

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
        Delivery delivery = new Delivery(atRiskOrder, 1, 10);

        overflowShelf.add(delivery);

        verify(hotOrderPriorityQueue).add(delivery);
    }

    @Test
    void whenColdOrderValueAtPickupOnOverFlowIsLessThanZero_add_addToColdOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Sushi", OrderType.COLD, 15, .8);
        Delivery delivery = new Delivery(atRiskOrder, 1, 10);

        overflowShelf.add(delivery);

        verify(coldOrderPriorityQueue).add(delivery);
    }

    @Test
    void whenFrozenOrderValueAtPickupOnOverFlowIsLessThanZero_add_addToFrozenOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Icy", OrderType.FROZEN, 15, .8);
        Delivery delivery = new Delivery(atRiskOrder, 1, 10);

        overflowShelf.add(delivery);

        verify(frozenOrderPriorityQueue).add(delivery);
    }

    @Test
    void whenOrderValueAtPickupOnOverFlowIsMoreThanZero_add_doNotAddToOrderPriorityQueue() {
        Order atRiskOrder = Order.create("Beef Hash", OrderType.HOT, 300, .6);
        Delivery delivery = new Delivery(atRiskOrder, 1, 4);

        overflowShelf.add(delivery);

        verify(hotOrderPriorityQueue, never()).add(delivery);
    }

    @Test
    void whenRemoveHotOrder_remove_removeHotOrderFromShelf() {
        Delivery delivery = new Delivery(TestUtils.createHotOrder(), 1, 1);

        overflowShelf.remove(delivery);

        verify(hotOrderPriorityQueue).remove(delivery);
    }

    @Test
    void whenRemoveColdOrder_remove_removeColdOrderFromShelf() {
        Delivery delivery = new Delivery(TestUtils.createColdOrder(), 1, 1);

        overflowShelf.remove(delivery);

        verify(coldOrderPriorityQueue).remove(delivery);
    }

    @Test
    void whenRemoveFrozenOrder_remove_removeFrozenOrderFromShelf() {
        Delivery delivery = new Delivery(TestUtils.createFrozenOrder(), 1, 1);

        overflowShelf.remove(delivery);

        verify(frozenOrderPriorityQueue).remove(delivery);
    }

    @Test
    void whenHotOrderType_removeHighestPriorityOrder_removeDeliveryFromQueue() {
        Delivery delivery = new Delivery(TestUtils.createHotOrder(), 1, 1);
        when(hotOrderPriorityQueue.poll()).thenReturn(delivery);

        Delivery highestPriorityDelivery = overflowShelf.removeHighestPriorityOrder(OrderType.HOT);

        Assert.assertEquals(highestPriorityDelivery, delivery);
    }

    @Test
    void whenColdOrderType_removeHighestPriorityOrder_removeDeliveryFromQueue() {
        Delivery delivery = new Delivery(TestUtils.createColdOrder(), 1, 1);
        when(coldOrderPriorityQueue.poll()).thenReturn(delivery);

        Delivery highestPriorityDelivery = overflowShelf.removeHighestPriorityOrder(OrderType.COLD);

        Assert.assertEquals(highestPriorityDelivery, delivery);
    }

    @Test
    void whenFrozenOrderType_removeHighestPriorityOrder_removeDeliveryFromQueue() {
        Delivery delivery = new Delivery(TestUtils.createFrozenOrder(), 1, 1);
        when(frozenOrderPriorityQueue.poll()).thenReturn(delivery);

        Delivery highestPriorityDelivery = overflowShelf.removeHighestPriorityOrder(OrderType.FROZEN);

        Assert.assertEquals(highestPriorityDelivery, delivery);
    }
}
