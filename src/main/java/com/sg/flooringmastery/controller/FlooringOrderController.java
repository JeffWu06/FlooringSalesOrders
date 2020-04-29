/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringOrderPersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.service.FlooringOrderServiceLayer;
import com.sg.flooringmastery.service.NonexistentOrderException;
import com.sg.flooringmastery.service.OrderDataValidationException;
import com.sg.flooringmastery.view.FlooringOrderView;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class FlooringOrderController {

    private FlooringOrderView view;
    private FlooringOrderServiceLayer service;

    public FlooringOrderController(FlooringOrderView view, FlooringOrderServiceLayer service) {
        this.view = view;
        this.service = service;
    }
    
    public void run() {
        boolean keepRunning = true;
        int menuSelection = 0;
        
        try {
            loadProductsAndTaxes();
            loadOrderData();
            while (keepRunning) {
                menuSelection = printMenuGetUserChoice();

                switch (menuSelection) {
                    case 1:
                        getOrders();
                        break;
                    case 2: 
                        addOrder();
                        break;
                    case 3: 
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        saveWork();
                        break;
                    case 6:
                        saveWork();
                        keepRunning = false;
                        break;
                    default:
                        unknownCommand();
                }
            }
            exitMessage();            
        } catch (FlooringOrderPersistenceException | NonexistentOrderException | OrderDataValidationException e) {
            view.printMessage(e.getMessage());
        }

    }
    
    private int printMenuGetUserChoice() {
        view.printMenu();
        return view.getUserMenuChoice();
    }
    
    private void loadOrderData() throws FlooringOrderPersistenceException {
        service.loadOrders();
    }
    
    private void loadProductsAndTaxes() throws FlooringOrderPersistenceException { 
        service.loadProductListing();
        service.loadTaxRates();
    }
    
    private void getOrders() throws FlooringOrderPersistenceException, NonexistentOrderException {
        view.displayGetOrdersBanner();
        LocalDate searchDate = view.getUserDateSelection();
        List<Order> orders = null;
        try {
            orders = service.getOrdersOfCertainDate(searchDate);
            view.printOrderList(orders);
        } catch (NonexistentOrderException e) {
            view.printMessage(e.getMessage());
        }
    }
    
    private void addOrder() throws FlooringOrderPersistenceException {
        view.displayAddNewOrderBanner();
        boolean inputValid;
        Order newOrder;
        do {
            newOrder = view.getNewOrderData();
            try {
                service.validateOrderData(newOrder);
                inputValid = true;
            } catch (OrderDataValidationException e) {
                view.printMessage(e.getMessage());
                inputValid = false;
            }
        } while (!inputValid);
        boolean confirmAdd = confirmCommit("Confirm addition of this order? Enter 'Y' for yes.");
        if (confirmAdd) {
            service.addOrder(newOrder);
            view.displayAddNewOrderSuccessBanner();
        } else {
            view.displayAddOrderNotCompletedBanner();
        }
    }
    
    private void removeOrder() throws FlooringOrderPersistenceException, 
            OrderDataValidationException, NonexistentOrderException {
        if (service.getOrderBook().isEmpty()) {
            view.displayNoOrdersMessage();
        } else {
            Order order = null;
            boolean inputValid;
            view.displayRemoveOrderBanner();
            do {
                order = getOrder();
                view.displayOrderInfo(order);
                inputValid = true;
            } while (!inputValid);
            boolean confirmRemove = confirmCommit("Confirm removal of this order? Enter 'Y' for yes.");
            if (confirmRemove) {
                service.removeOrder(order.getDate(), order.getNumber());
                view.displayRemoveOrderSuccessBanner();
            } else {
                view.displayRemoveOrderNotCompletedBanner();
            }
        }
    }
    
    private Order getOrder() throws FlooringOrderPersistenceException, 
            OrderDataValidationException, NonexistentOrderException {
        Order order = null;
        List<String> orderChoice;
        boolean inputValid;
        do {
            try {
                orderChoice = view.getUserOrderSelection();
                order = service.getOrder(orderChoice);
                inputValid = true;
            } catch (OrderDataValidationException | NonexistentOrderException e) {
                inputValid = false;
                view.printMessage(e.getMessage());
            }
        } while (!inputValid);
        return order;
    }
    
    private boolean confirmCommit(String prompt) {
        String confirmProcessing = view.getUserConfirmation(prompt);
        return service.continueProcessing(confirmProcessing);
    }
    
    private void editOrder() throws FlooringOrderPersistenceException, 
            OrderDataValidationException, NonexistentOrderException {
        if (service.getOrderBook().isEmpty()) {
            view.displayNoOrdersMessage();
        } else {
            Order existingOrder = null;
            boolean inputValid;
            view.displayEditOrderBanner();
            do {
                existingOrder = getOrder();
                inputValid = true;
            } while (!inputValid);

            List<String> editedOrderData = view.getNewDataForOrderEdit(existingOrder);
            try {
                service.editOrder(existingOrder, editedOrderData);
                view.displayEditOrderSuccessBanner();
            } catch (OrderDataValidationException e) {
                view.printMessage(e.getMessage());
            }
        }
    }
    
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
    
    private void exitMessage() {
        view.displayExitBanner();
    }
    
    private void saveWork() throws FlooringOrderPersistenceException {
        try {
            service.writeOrders();
            view.displaySaveSuccessBanner();
        } catch (FlooringOrderPersistenceException e) {
            view.printMessage(e.getMessage());
        }
    }
    
}
