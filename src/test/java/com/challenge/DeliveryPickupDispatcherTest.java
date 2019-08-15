package com.challenge;

import com.challenge.dispatcher.DeliveryPickupDispatcher;
import com.challenge.kitchen.Kitchen;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


// Add tests after implementation complete
class DeliveryPickupDispatcherTest {

    private DeliveryPickupDispatcher deliveryPickupDispatcher;

    @Mock
    private Kitchen kitchen;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        deliveryPickupDispatcher = new DeliveryPickupDispatcher(kitchen);
    }


}