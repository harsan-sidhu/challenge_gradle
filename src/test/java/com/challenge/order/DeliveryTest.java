package com.challenge.order;

import com.challenge.clock.Clock;
import com.challenge.shelf.HotShelf;
import com.challenge.shelf.OverflowShelf;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.challenge.order.OrderType.HOT;
import static org.mockito.Mockito.when;

class DeliveryTest {

    private Delivery delivery;

    @Mock Clock clock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void whenOnHotShelf_getValue_returnProperValue() {
        HotShelf hotShelf = new HotShelf(new ConcurrentLinkedQueue<>());
        Order order = Order.create("Beef Hash", HOT, 30, 0.74);
        delivery = new Delivery(order, 1, 5, clock);
        delivery.setShelf(hotShelf);
        when(clock.getCurrentTimeInSeconds()).thenReturn(10L);

        double orderValue = delivery.getValue();

        Assert.assertEquals(0.478, orderValue, 0);
    }

    @Test
    void whenOnOverflowShelf_getValue_returnProperValue() {
        OverflowShelf overflowShelf = createOverflowShelf();
        Order order = Order.create("Beef Hash", HOT, 30, 0.74);
        delivery = new Delivery(order, 1, 5, clock);
        delivery.setShelf(overflowShelf);
        when(clock.getCurrentTimeInSeconds()).thenReturn(10L);

        double orderValue = delivery.getValue();

        Assert.assertEquals(0.256, orderValue, 0);
    }

    private OverflowShelf createOverflowShelf() {
        return new OverflowShelf(
                new ConcurrentLinkedQueue<>(),
                new PriorityQueue<>(),
                new PriorityQueue<>(),
                new PriorityQueue<>());
    }
}
