package com.challenge;

import com.challenge.dispatcher.Dispatcher;
import com.challenge.kitchen.Kitchen;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


// Add tests after implementation complete
class DispatcherTest {

    private Dispatcher dispatcher;

    @Mock
    private Kitchen kitchen;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        dispatcher = new Dispatcher(kitchen);
    }


}