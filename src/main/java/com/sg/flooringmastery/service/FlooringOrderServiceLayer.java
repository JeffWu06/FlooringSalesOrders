/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringOrderPersistenceException;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.TaxRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jeff
 */
public interface FlooringOrderServiceLayer {
    
    int nextOrderNumber = 0;
    
    void loadOrders() throws FlooringOrderPersistenceException;
    
    List<Order> getOrdersOfCertainDate(LocalDate date) throws 
            FlooringOrderPersistenceException, NonexistentOrderException;
    
    void addOrder(Order order) throws FlooringOrderPersistenceException;
    
    void completeOrderDetails(Order order);
    
    Order getOrder(List<String> orderDateAndNo) throws 
            FlooringOrderPersistenceException, OrderDataValidationException, 
            NonexistentOrderException;
    
    Order removeOrder(LocalDate orderDate, int orderNo) throws
            FlooringOrderPersistenceException, NonexistentOrderException;
    
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal materialCostPerSqFt);
    
    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSqFt);
    
    BigDecimal calculateGrossCost(BigDecimal materialCostPerSqFt, 
            BigDecimal laborCostPerSqFt);
    
    BigDecimal calculateTax(BigDecimal taxRate, BigDecimal totalGrossCost);
    
    BigDecimal calculateTotalCost(BigDecimal tax, BigDecimal totalGrossCost);
    
    void validateOrderData(Order order) throws OrderDataValidationException;
    
    int parseOrderNumber(String userInput) throws OrderDataValidationException;
    
    LocalDate parseDate(String userInput) throws OrderDataValidationException;
    
    void writeOrders() throws FlooringOrderPersistenceException;
    
    void loadTaxRates() throws FlooringOrderPersistenceException;
    
    void loadProductListing() throws FlooringOrderPersistenceException;
    
    boolean continueProcessing(String userInput);
    
    Product getProduct(String productName) throws FlooringOrderPersistenceException;
    
    TaxRate getTaxRate(String state) throws FlooringOrderPersistenceException;
    
    void editOrder(Order existingOrder, List<String> editedData) throws FlooringOrderPersistenceException, OrderDataValidationException;
    
    public Map<LocalDate, HashMap<Integer, Order>> getOrderBook();
}
