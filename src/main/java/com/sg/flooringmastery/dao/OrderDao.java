/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jeff
 */
public interface OrderDao {
    
    void loadOrders() throws FlooringOrderPersistenceException;
    
    List<Order> getOrdersForDate(LocalDate searchDate) throws FlooringOrderPersistenceException;
    
    Order addOrder(Order order) throws FlooringOrderPersistenceException;
    
    Order editOrder(Order existingOrder, Order editedOrder) throws FlooringOrderPersistenceException;
    
    Order getOrder(LocalDate orderDate, int number) throws FlooringOrderPersistenceException;
    
    Order removeOrder(LocalDate orderDate, int number) throws 
            FlooringOrderPersistenceException;
    
    void writeOrders() throws FlooringOrderPersistenceException;
    
    int getLastOrderNumber();
    
    Map<LocalDate, HashMap<Integer, Order>> getOrderBook();
}
