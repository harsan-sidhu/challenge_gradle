package com.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executors;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.*;

class OrderValueCalculatorTest {

    private OrderValueCalculator orderValueCalculator;

    @Mock
    private Kitchen kitchen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        orderValueCalculator = new OrderValueCalculator(kitchen);
    }

    @Test
    void beginContinuouslyCalculatingOrder_setValueOnOrder() {
        Order order = mock(Order.class);
        when(kitchen.isOrderOnShelves(order)).thenReturn(true);

        orderValueCalculator.beginContinuouslyCalculatingOrder(Executors.newSingleThreadScheduledExecutor(), order);

        verify(order).setValue(anyDouble());
    }

    @Test
    void whenOrderValueIsZero_beginContinuouslyCalculatingOrder_removeOrderFromShelves() {
        // TODO Write this test
    }
}