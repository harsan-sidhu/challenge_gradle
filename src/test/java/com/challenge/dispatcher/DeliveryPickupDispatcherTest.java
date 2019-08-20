package com.challenge.dispatcher;

import com.challenge.kitchen.Kitchen;
import com.challenge.order.Delivery;
import com.challenge.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DeliveryPickupDispatcherTest {

    private DeliveryPickupDispatcher deliveryPickupDispatcher;

    @Mock private ScheduledExecutorService executor;
    @Mock private Kitchen kitchen;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        deliveryPickupDispatcher = new DeliveryPickupDispatcher(executor, kitchen);
    }

    @Test
    void dispatchPickupForOrder_removeOrderFromShelf() {
        Delivery delivery = new Delivery(mock(Order.class), 1, 1);

        deliveryPickupDispatcher.removeOrderFromShelf(delivery);

        verify(kitchen).removeOrderFromShelves(delivery);
    }


}