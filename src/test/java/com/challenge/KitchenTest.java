package com.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class KitchenTest {

    private Kitchen kitchen;

    @Mock
    private Shelf hotShelf;
    @Mock
    private Shelf coldShelf;
    @Mock
    private Shelf frozenShelf;
    @Mock
    private Shelf overFlowShelf;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        kitchen = new Kitchen(Arrays.asList(hotShelf, coldShelf, frozenShelf, overFlowShelf));
    }

    @Test
    void withHotOrder_addOrderToShelves_placeOrderOnHotShelf() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");

        kitchen.addOrderToShelves(hotOrder);

        verify(hotShelf).addOrderToShelf(hotOrder);
    }

    @Test
    void withColdOrder_addOrderToShelves_placeOrderOnColdShelf() {
        Order coldOrder = new Order();
        coldOrder.setTemp("cold");

        kitchen.addOrderToShelves(coldOrder);

        verify(coldShelf).addOrderToShelf(coldOrder);
    }

    @Test
    void withFrozenOrder_addOrderToShelves_placeOrderOnFrozenShelf() {
        Order frozenOrder = new Order();
        frozenOrder.setTemp("frozen");

        kitchen.addOrderToShelves(frozenOrder);

        verify(frozenShelf).addOrderToShelf(frozenOrder);
    }

    @Test
    void withHotOrderShelfFull_addOrderToShelves_placeOrderOnOverflowShelf() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");
        when(hotShelf.size()).thenReturn(15);

        kitchen.addOrderToShelves(hotOrder);

        verify(overFlowShelf).addOrderToShelf(hotOrder);
    }

    @Test
    void withColdOrderShelfFull_addOrderToShelves_placeOrderOnOverflowShelf() {
        Order coldOrder = new Order();
        coldOrder.setTemp("cold");
        when(coldShelf.size()).thenReturn(15);

        kitchen.addOrderToShelves(coldOrder);

        verify(overFlowShelf).addOrderToShelf(coldOrder);
    }

    @Test
    void withFrozenOrderShelfFull_addOrderToShelves_placeOrderOnOverflowShelf() {
        Order frozenOrder = new Order();
        frozenOrder.setTemp("frozen");
        when(frozenShelf.size()).thenReturn(15);

        kitchen.addOrderToShelves(frozenOrder);

        verify(overFlowShelf).addOrderToShelf(frozenOrder);
    }


    @Test
    void whenOrderIsOnHotShelf_removeOrderFromShelves_removeOrderFromHotShelf() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");
        when(hotShelf.contains(hotOrder)).thenReturn(true);

        kitchen.removeOrderFromShelves(hotOrder);

        verify(hotShelf).removeOrderFromShelf(hotOrder);
    }

    @Test
    void whenOrderIsOnColdShelf_removeOrderFromShelves_removeOrderFromColdShelf() {
        Order coldOrder = new Order();
        coldOrder.setTemp("cold");
        when(coldShelf.contains(coldOrder)).thenReturn(true);

        kitchen.removeOrderFromShelves(coldOrder);

        verify(coldShelf).removeOrderFromShelf(coldOrder);
    }

    @Test
    void whenOrderIsOnFrozenShelf_removeOrderFromShelves_removeOrderFromFrozenShelf() {
        Order frozenOrder = new Order();
        frozenOrder.setTemp("frozen");
        when(frozenShelf.contains(frozenOrder)).thenReturn(true);

        kitchen.removeOrderFromShelves(frozenOrder);

        verify(frozenShelf).removeOrderFromShelf(frozenOrder);
    }

    @Test
    void whenOrderIsOnOverflowShelf_removeOrderFromShelves_removeOrderFromOverflowShelf() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");
        when(hotShelf.contains(hotOrder)).thenReturn(false);
        when(overFlowShelf.contains(hotOrder)).thenReturn(true);

        kitchen.removeOrderFromShelves(hotOrder);

        verify(overFlowShelf).removeOrderFromShelf(hotOrder);
    }

    @Test
    void whenOrderIsOnShelves_isOrderOnShelves_returnTrue() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");
        when(hotShelf.contains(hotOrder)).thenReturn(true);

        assertTrue(kitchen.isOrderOnShelves(hotOrder));
    }

    @Test
    void whenOrderIsNotOnShelves_isOrderOnShelves_returnFalse() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");

        assertFalse(kitchen.isOrderOnShelves(hotOrder));
    }

    @Test
    void whenOrderIsOnOverFlowShelf_isOrderOnOverFlowShelf_returnTrue() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");
        when(overFlowShelf.contains(hotOrder)).thenReturn(true);

        assertTrue(kitchen.isOrderOnOverFlowShelf(hotOrder));
    }

    @Test
    void whenOrderIsNotOnOverFlowShelf_isOrderOnOverFlowShelf_returnFalse() {
        Order hotOrder = new Order();
        hotOrder.setTemp("hot");

        assertFalse(kitchen.isOrderOnOverFlowShelf(hotOrder));
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

    /* Rewrite at the end
    @Test
    void whenHotShelfHasSpaceAndOrderValueWillReachZero_maybeMoveOverFlowOrders_shouldMoveHotOrderFromOverflowToHotShelf() {
        when(hotShelf.size()).thenReturn(14);
        Order hotOrder = new Order();
        hotOrder.setName("Beef Hash");
        hotOrder.setTemp("hot");
        hotOrder.setDecayRate(.9);
        hotOrder.setShelfLife(20);
        hotOrder.setTimeToPickupOrder(9);
        when(overFlowShelf.getOrders()).thenReturn(Collections.singletonList(hotOrder));
        when(overFlowShelf.contains(hotOrder)).thenReturn(true);

        kitchen.maybeMoveOverFlowOrders();

        verify(overFlowShelf).removeOrderFromShelf(hotOrder);
        verify(hotShelf).addOrderToShelf(hotOrder);
    }

    @Test
    void whenColdShelfHasSpaceAndOrderValueWillReachZero_maybeMoveOverFlowOrders_shouldMoveColdOrderFromOverflowToColdShelf() {
        when(coldShelf.size()).thenReturn(14);
        Order coldOrder = new Order();
        coldOrder.setName("Sushi");
        coldOrder.setTemp("cold");
        coldOrder.setDecayRate(.9);
        coldOrder.setShelfLife(20);
        coldOrder.setTimeToPickupOrder(9);
        when(overFlowShelf.getOrders()).thenReturn(Collections.singletonList(coldOrder));
        when(overFlowShelf.contains(coldOrder)).thenReturn(true);

        kitchen.maybeMoveOverFlowOrders();

        verify(overFlowShelf).removeOrderFromShelf(coldOrder);
        verify(coldShelf).addOrderToShelf(coldOrder);
    }

    @Test
    void whenFrozenShelfHasSpaceAndOrderValueWillReachZero_maybeMoveOverFlowOrders_shouldMoveFrozenOrderFromOverflowToFrozenShelf() {
        when(frozenShelf.size()).thenReturn(14);
        Order frozenOrder = new Order();
        frozenOrder.setName("Icy");
        frozenOrder.setTemp("frozen");
        frozenOrder.setDecayRate(.9);
        frozenOrder.setShelfLife(20);
        frozenOrder.setTimeToPickupOrder(9);
        when(overFlowShelf.getOrders()).thenReturn(Collections.singletonList(frozenOrder));
        when(overFlowShelf.contains(frozenOrder)).thenReturn(true);

        kitchen.maybeMoveOverFlowOrders();

        verify(overFlowShelf).removeOrderFromShelf(frozenOrder);
        verify(frozenShelf).addOrderToShelf(frozenOrder);
    }

    @Test
    void whenOverflowShelfIsEmpty_maybeMoveOverFlowOrders_shouldNotMoveOrder() {
        when(overFlowShelf.isEmpty()).thenReturn(true);

        kitchen.maybeMoveOverFlowOrders();

        verify(overFlowShelf).isEmpty();
        verifyNoMoreInteractions(overFlowShelf);
        verifyZeroInteractions(hotShelf);
        verifyZeroInteractions(coldShelf);
        verifyZeroInteractions(frozenShelf);
    }

    @Test
    void whenOverflowOrderValueWillNotReachZero_maybeMoveOverFlowOrders_shouldNotMoveOrder() {
        when(hotShelf.size()).thenReturn(14);
        Order hotOrder = new Order();
        hotOrder.setName("Beef Hash");
        hotOrder.setTemp("hot");
        hotOrder.setDecayRate(.9);
        hotOrder.setShelfLife(20);
        hotOrder.setTimeToPickupOrder(4);
        when(overFlowShelf.getOrders()).thenReturn(Collections.singletonList(hotOrder));
        when(overFlowShelf.contains(hotOrder)).thenReturn(true);

        kitchen.maybeMoveOverFlowOrders();

        verify(overFlowShelf, never()).removeOrderFromShelf(hotOrder);
        verify(hotShelf, never()).addOrderToShelf(hotOrder);
    }*/
}