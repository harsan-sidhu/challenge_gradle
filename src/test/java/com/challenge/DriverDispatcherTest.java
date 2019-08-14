package com.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executors;

import static org.mockito.Mockito.verify;

class DriverDispatcherTest {

    private DriverDispatcher driverDispatcher;

    @Mock
    private Kitchen kitchen;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        driverDispatcher = new DriverDispatcher(kitchen);
    }

    @Test
    void dispatchForOrder_removeOrderFromShelves() throws InterruptedException {
        Order order = new Order();

        driverDispatcher.dispatchForOrder(Executors.newSingleThreadScheduledExecutor(), order, 0);
        Thread.sleep(500);

        verify(kitchen).removeOrderFromShelves(order);
    }
}