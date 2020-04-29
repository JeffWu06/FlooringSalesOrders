/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.view;

import com.sg.flooringmastery.model.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jeff
 */
public class FlooringOrderView {
    
    private UserIO io;    

    public FlooringOrderView(UserIO io) {
        this.io = io;
    }
    
    public void printMenu() {
        io.print("SWG Corp. Flooring Order Management Program");
        io.print("=== MAIN MENU ===");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Save Current Work");
        io.print("6. Quit");
    }
    
    public int getUserMenuChoice() {
        return io.readInt("Please select an option from above", 1, 6);
    }
    
    public void printMessage(String message) {
        io.print(message);
    }
    
    public void displayGetOrdersBanner() {
        io.print("\n=== Display Orders ===");
    }
    
    public LocalDate getUserDateSelection() {
        return io.readDate("Please enter the date you would like to see orders from (MM/DD/YYYY).");
    }
    
    public void printOrderList(List<Order> orders) {
        io.print("***Orders from " + orders.get(0).getDate() + "***");
        for (Order order : orders) {
            io.print("Order #" + order.getNumber() + ", " + order.getCustomerName());
            io.print("Product cost: $" + order.getMaterialCost());
            io.print("Labor cost: $" + order.getLaborCost());
            io.print(order.getState() + " tax: $" + order.getTax());
            io.print("ORDER TOTAL: $" + order.getTotalCost() + "\n");
        }
    }
    
    public void displayAddNewOrderBanner() {
        io.print("\n=== Add a New Order ===");
    }
    
    public Order getNewOrderData() {
        String customer = io.readString("Please enter the customer name.");
        LocalDate date = io.readDate("Please enter the date of the order (MM/DD/YYYY).");
        String stateCode = io.readString("Please enter the customer's state tax jurisdiction (OH, IN, PA, or MI).");
        String product = io.readString("Please enter the name of the flooring product ordered.");
        BigDecimal area = io.readBigDecimal("Please enter the square footage of " + product + " ordered.", BigDecimal.ZERO).setScale(2);
        Order newOrder = new Order(date, customer, area, stateCode, product);
        displayOrderInfo(newOrder);
        return newOrder;
    }
    
    public void displayOrderInfo(Order order) {
        io.print("Order date: " + order.getDate());
        io.print("Customer: " + order.getCustomerName());
        io.print("Product: " + order.getProductName());
        io.print("Qty ordered: " + order.getArea() + " sq ft");
        io.print("State tax jurisdiction: " + order.getState());        
    }
    
    public String getUserConfirmation(String prompt) {
        return io.readString(prompt);
    }
    
    public void displayAddNewOrderSuccessBanner() {
        io.print("Order successfully added!\n");
    }
    
    public void displayRemoveOrderBanner() {
        io.print("\n=== Remove an Existing Order ===");
    }
    
    public List<String> getUserOrderSelection() {
        ArrayList<String> userChoice = new ArrayList<>();
        userChoice.add(io.readString("Please enter the date of the desired order (MM/DD/YYYY)."));
        userChoice.add(io.readString("Please enter the number of the desired order."));
        return userChoice;
    }
    
    public void displayRemoveOrderSuccessBanner() {
        io.print("Order successfully removed!\n");
    }
    
    public void displayRemoveOrderNotCompletedBanner() {
        io.print("Okay, order deleted.\n");
    }
    
    public void displayEditOrderBanner() {
        io.print("\n=== Edit an Existing Order ===");
    }
    
    public List<String> getNewDataForOrderEdit(Order order) {
        String customer = io.readString("Please enter the customer name (" + order.getCustomerName() + ").");
        String date = io.readString("Please enter the date of the order (" + order.getDateString() + ").");
        String stateCode = io.readString("Please enter the customer's state tax jurisdiction (" + order.getState() + ").");
        String product = io.readString("Please enter the name of the flooring product ordered (" + order.getProductName() + ").");
        String area = io.readString("Please enter the square footage of " + product + " ordered (" + order.getArea() + ").");
        ArrayList<String> newData = new ArrayList<>(Arrays.asList(date, customer, area, stateCode, product));
        return newData;
    }
    
    public void displayEditOrderSuccessBanner() {
        io.print("Order successfully edited!\n");
    }
    
    public void displayUnknownCommandBanner() {
        io.print("Unknown command.");
    }

    public void displayExitBanner() {
        io.print("Good bye!");
    }
    
    public void displaySaveSuccessBanner() {
        io.print("Data successfully saved!");
    }
    
    public void displayNoOrdersMessage() {
        io.print("No orders in the system to modify.");
    }

    public void displayAddOrderNotCompletedBanner() {
        io.print("Okay, order not added.");
    }
}
