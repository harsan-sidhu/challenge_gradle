package com.challenge.kitchen;

import com.challenge.order.Delivery;
import com.challenge.shelf.ColdShelf;
import com.challenge.shelf.FrozenShelf;
import com.challenge.shelf.HotShelf;
import com.challenge.shelf.OverflowShelf;
import com.challenge.ui.DispatcherUICallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.challenge.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class KitchenTest {

    private Kitchen kitchen;

    @Mock private HotShelf hotShelf;
    @Mock private ColdShelf coldShelf;
    @Mock private FrozenShelf frozenShelf;
    @Mock private OverflowShelf overFlowShelf;
    @Mock private DispatcherUICallback uiCallback;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        kitchen = new Kitchen(Arrays.asList(hotShelf, coldShelf, frozenShelf, overFlowShelf), uiCallback);
    }

    @Test
    void withHotOrder_addOrderToShelves_placeOrderOnHotShelf() {
        Delivery delivery = new Delivery(createHotOrder(), 1, 1);

        kitchen.addOrderToShelves(delivery);

        verify(hotShelf).add(delivery);
    }

    @Test
    void withColdOrder_addOrderToShelves_placeOrderOnColdShelf() {
        Delivery delivery = new Delivery(createColdOrder(), 1, 1);

        kitchen.addOrderToShelves(delivery);

        verify(coldShelf).add(delivery);
    }

    @Test
    void withFrozenOrder_addOrderToShelves_placeOrderOnFrozenShelf() {
        Delivery delivery = new Delivery(createFrozenOrder(), 1, 1);

        kitchen.addOrderToShelves(delivery);

        verify(frozenShelf).add(delivery);
    }

    @Test
    void withHotOrderShelfAtCapacity_addOrderToShelves_placeOrderOnOverflowShelf() {
        Delivery delivery = new Delivery(createHotOrder(), 1, 1);
        when(hotShelf.capacity()).thenReturn(15);
        when(hotShelf.size()).thenReturn(15);


        kitchen.addOrderToShelves(delivery);

        verify(overFlowShelf).add(delivery);
    }

    @Test
    void withColdOrderShelfAtCapacity_addOrderToShelves_placeOrderOnOverflowShelf() {
        Delivery delivery = new Delivery(createColdOrder(), 1, 1);
        when(coldShelf.capacity()).thenReturn(15);
        when(coldShelf.size()).thenReturn(15);

        kitchen.addOrderToShelves(delivery);

        verify(overFlowShelf).add(delivery);
    }

    @Test
    void withFrozenOrderShelfAtCapacity_addOrderToShelves_placeOrderOnOverflowShelf() {
        Delivery delivery = new Delivery(createFrozenOrder(), 1, 1);
        when(frozenShelf.capacity()).thenReturn(15);
        when(frozenShelf.size()).thenReturn(15);

        kitchen.addOrderToShelves(delivery);

        verify(overFlowShelf).add(delivery);
    }

    @Test
    void whenOrderIsOnHotShelf_removeOrderFromShelves_removeOrderFromHotShelf() {
        Delivery delivery = new Delivery(createHotOrder(), 1, 1);

        kitchen.removeOrderFromShelves(delivery);

        verify(hotShelf).remove(delivery);
    }

    @Test
    void whenOrderIsOnColdShelf_removeOrderFromShelves_removeOrderFromColdShelf() {
        Delivery delivery = new Delivery(createColdOrder(), 1, 1);

        kitchen.removeOrderFromShelves(delivery);

        verify(coldShelf).remove(delivery);
    }

    @Test
    void whenOrderIsOnFrozenShelf_removeOrderFromShelves_removeOrderFromFrozenShelf() {
        Delivery delivery = new Delivery(createFrozenOrder(), 1, 1);

        kitchen.removeOrderFromShelves(delivery);

        verify(frozenShelf).remove(delivery);
    }

    @Test
    void whenOrderIsOnOverflowShelf_removeOrderFromShelves_removeOrderFromOverflowShelf() {
        Delivery delivery = new Delivery(createHotOrder(), 1, 1);
        when(hotShelf.contains(delivery)).thenReturn(false);
        when(overFlowShelf.contains(delivery)).thenReturn(true);

        kitchen.removeOrderFromShelves(delivery);

        verify(overFlowShelf).remove(delivery);
    }

    @Test
    void whenShelvesAreEmpty_isEmpty_returnTrue() {
        when(hotShelf.isEmpty()).thenReturn(true);
        when(coldShelf.isEmpty()).thenReturn(true);
        when(frozenShelf.isEmpty()).thenReturn(true);
        when(overFlowShelf.isEmpty()).thenReturn(true);

        assertTrue(kitchen.isEmpty());
    }

    @Test
    void whenShelvesAreNotEmpty_isEmpty_returnFalse() {
        when(hotShelf.isEmpty()).thenReturn(false);
        when(coldShelf.isEmpty()).thenReturn(true);
        when(frozenShelf.isEmpty()).thenReturn(true);
        when(overFlowShelf.isEmpty()).thenReturn(true);

        assertFalse(kitchen.isEmpty());
    }

    @Test
    void addOrderToShelves_updateUi() {
        Delivery delivery = new Delivery(createColdOrder(), 1, 1);

        kitchen.addOrderToShelves(delivery);

        verify(uiCallback).onDataUpdated(any());
    }

    @Test
    void removeOrderFromShelves_updateUi() {
        Delivery delivery = new Delivery(createColdOrder(), 1, 1);

        kitchen.removeOrderFromShelves(delivery);

        verify(uiCallback).onDataUpdated(any());
    }

    //TODO Test removeHighestPriorityOrder
}
