package com.challenge.ui;

import com.challenge.order.Delivery;
import com.challenge.order.Order;
import com.challenge.order.OrderType;
import com.challenge.shelf.Shelf;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class OrderFulfillerUI implements DispatcherUICallback {

    private final Map<OrderType, DefaultListModel> typeToList;
    private final JPanel panel;
    private final JFrame jFrame;


    public OrderFulfillerUI(List<OrderType> shelfTypes) {
        typeToList = new LinkedHashMap<>();
        jFrame = new JFrame();
        panel = new JPanel(new GridLayout(2,4,10,0));
        jFrame.add(panel);
        jFrame.setSize(1000, 800);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        layoutUI(shelfTypes);
    }

    private void layoutUI(List<OrderType> shelfTypes) {
        // Headers
        for (OrderType shelfType : shelfTypes) {
            DefaultListModel<Order> listModel = new DefaultListModel<>();
            typeToList.put(shelfType, listModel);
            panel.add(new JLabel(shelfType.name()));
        }

        // Lists
        for (Map.Entry<OrderType, DefaultListModel> entry : typeToList.entrySet()) {
            panel.add(new JList<DefaultListModel<Pair<OrderType, Double>>>(entry.getValue()));
        }
    }

    @Override
    public void onDataUpdated(List<Shelf> dataToDisplay) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(
                    () -> {
                        for (Map.Entry<OrderType, DefaultListModel> entry : typeToList.entrySet()) {
                            for (Shelf shelf : dataToDisplay) {
                                if (entry.getKey().equals(shelf.getType())) {
                                    DefaultListModel listModelForShelf = entry.getValue();
                                    listModelForShelf.clear();
                                    for (Delivery order : shelf.getOrders()) {
                                        listModelForShelf.addElement(new Pair(order.getOrder().getName(), order.getValue()));
                                    }
                                    break;
                                }
                            }
                        }
                    });
        }
    }

    public void show() {
        jFrame.setVisible(true);
    }
}
