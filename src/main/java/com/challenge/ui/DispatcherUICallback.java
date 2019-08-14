package com.challenge.ui;

import com.challenge.shelf.Shelf;

import java.util.List;

public interface DispatcherUICallback {

    void onDataUpdated(List<Shelf> dataToDisplay);
}
