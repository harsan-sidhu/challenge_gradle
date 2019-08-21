package com.challenge.ui;

import com.challenge.kitchen.Kitchen;
import com.challenge.shelf.Shelf;

import java.util.List;

/**
 * Callback to allow the {@link Kitchen} to notify {@link OrderFulfillerUI} to update the display.
 */
public interface DispatcherUICallback {

    void onDataUpdated(List<Shelf> dataToDisplay);
}
