/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.TaxRate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Jeff
 */
public class OrderDaoStubImpl implements OrderDao {
    
    public static final String ORDER_FILE_PREFIX = "Orders_";
    public static final String ORDER_FILE_TYPE = ".txt";
    public static final String ORDER_FILE_DIRECTORY = "data\\stub_orders";
    public static final String DELIMITER = ",";
    private DateTimeFormatter fileNameDateFormat = DateTimeFormatter.ofPattern("MMddyyyy");
    private Map<LocalDate, HashMap<Integer, Order>> orderBook = new HashMap<>();

    @Override
    public void loadOrders() throws FlooringOrderPersistenceException {
        
        Scanner scanner;
        File directory = new File(ORDER_FILE_DIRECTORY);
        
        for(File file : directory.listFiles()) {
            if (file.getName().contains("Orders")) {
                try {
                    scanner = new Scanner(new BufferedReader(new FileReader(file)));
                } catch (FileNotFoundException e) {
                    throw new FlooringOrderPersistenceException("Could not load "
                            + "orders into memory.", e);
                }
                HashMap<Integer, Order> orders = new HashMap<>();
                String dateString = file.getName().substring(ORDER_FILE_PREFIX.length(), ORDER_FILE_PREFIX.length() + 8);
                LocalDate orderDate = LocalDate.parse(dateString, fileNameDateFormat);
                String currentLine;
                String[] lineData;
                while (scanner.hasNextLine()) {
                    try {
                        currentLine = scanner.nextLine();
                        lineData = currentLine.split(DELIMITER);
                        Order order = new Order(orderDate, lineData[1], 
                                new BigDecimal(lineData[5]), lineData[2], lineData[4]);
                        BigDecimal taxPercent = new BigDecimal(lineData[3]);
                        taxPercent = taxPercent.divide(new BigDecimal("100.00"), 4, RoundingMode.HALF_UP);
                        TaxRate taxRate = new TaxRate(order.getState());
                        taxRate.setRate(taxPercent);
                        order.setTaxRate(taxRate);
                        Product product = new Product(order.getProductName());
                        product.setMaterialCostPerSqFoot(new BigDecimal(lineData[6]));
                        product.setLaborCostPerSqFoot(new BigDecimal(lineData[7]));
                        order.setMaterialCost(new BigDecimal(lineData[8]));
                        order.setLaborCost(new BigDecimal(lineData[9]));
                        order.setTax(new BigDecimal(lineData[10]));
                        order.setTotalCost(new BigDecimal(lineData[11]));
                        order.setNumber(Integer.parseInt(lineData[0]));
                        orders.put(order.getNumber(), order);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        continue;
                    }                
                }
                orderBook.put(orderDate, orders);
            }
        }
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate searchDate) throws FlooringOrderPersistenceException {
        return new ArrayList<Order>(orderBook.get(searchDate).values());
    }

    @Override
    public Order addOrder(Order order) throws FlooringOrderPersistenceException {
        LocalDate orderDate = order.getDate();
        if (orderBook.get(orderDate) != null) {
            HashMap dateSubOrderBook = orderBook.get(orderDate);
            dateSubOrderBook.put(order.getNumber(), order);
            orderBook.put(orderDate, dateSubOrderBook);
        } else {
            HashMap<Integer, Order> newDateSubOrderBook = new HashMap<>();
            newDateSubOrderBook.put(order.getNumber(), order);
            orderBook.put(orderDate, newDateSubOrderBook);
        }
        return order;
    }

    @Override
    public Order editOrder(Order existingOrder, Order editedOrder) throws FlooringOrderPersistenceException {
        removeOrder(existingOrder.getDate(), existingOrder.getNumber());
        return addOrder(editedOrder);
    }

    @Override
    public Order getOrder(LocalDate orderDate, int number) throws FlooringOrderPersistenceException {
        return orderBook.get(orderDate).get(number);
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int number) throws FlooringOrderPersistenceException {
        HashMap<Integer, Order> dateSubOrderBook = orderBook.get(orderDate);
        Order removedOrder = dateSubOrderBook.remove(number);
        orderBook.put(orderDate, dateSubOrderBook);
        return removedOrder;  
    }

    @Override
    public void writeOrders() throws FlooringOrderPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLastOrderNumber() {
        int lastOrderNumber = 0;
        Set<LocalDate> dateKeys = orderBook.keySet();
        for (LocalDate dateKey : dateKeys) {
            Map<Integer, Order> subOrderBook = orderBook.get(dateKey);
            Set<Integer> orderNums = subOrderBook.keySet();
            for (Integer orderNum : orderNums) {
                if (orderNum > lastOrderNumber) {
                    lastOrderNumber = orderNum;
                }
            }
        }
        return lastOrderNumber;
    }

    @Override
    public Map<LocalDate, HashMap<Integer, Order>> getOrderBook() {
        return orderBook;
    }

}
