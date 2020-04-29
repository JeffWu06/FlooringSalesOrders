/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import java.util.stream.Collectors;

/**
 *
 * @author Jeff
 */
public class OrderDaoFileImpl implements OrderDao {
    
    public static final String ORDER_FILE_PREFIX = "Orders_";
    public static final String ORDER_FILE_TYPE = ".txt";
    public static final String ORDER_FILE_DIRECTORY = "data";
    public static final String DELIMITER = ",";
    private DateTimeFormatter fileNameDateFormat = DateTimeFormatter.ofPattern("MMddyyyy");
    private Map<LocalDate, HashMap<Integer, Order>> orderBook = new HashMap<>();

    @Override
    public void loadOrders() throws FlooringOrderPersistenceException {
        
        Scanner scanner;
        File directory = new File(ORDER_FILE_DIRECTORY);
        
        for(File file : directory.listFiles()) {
            if (file.getName().contains(ORDER_FILE_PREFIX)) {
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
                        if (currentLine == null) {
                            continue;
                        } else {
                            lineData = currentLine.split(DELIMITER);
                            Order order = new Order(orderDate, lineData[1], 
                                    new BigDecimal(lineData[5]), lineData[2], lineData[4]);
                            order.setMaterialCost(new BigDecimal(lineData[8]));
                            order.setLaborCost(new BigDecimal(lineData[9]));
                            order.setTax(new BigDecimal(lineData[10]));
                            order.setTotalCost(new BigDecimal(lineData[11]));
                            order.setNumber(Integer.parseInt(lineData[0]));
                            orders.put(order.getNumber(), order);
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        continue;
                    }
                }
                if (!orders.isEmpty()) {
                    orderBook.put(orderDate, orders);
                }
                
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
    public Order removeOrder(LocalDate orderDate, int number) throws 
            FlooringOrderPersistenceException {
        HashMap<Integer, Order> dateSubOrderBook = orderBook.get(orderDate);
        Order removedOrder = dateSubOrderBook.remove(number);
        if (dateSubOrderBook.isEmpty()) {
            orderBook.remove(orderDate);
        }
        return removedOrder;        
    }

    @Override
    public void writeOrders() throws FlooringOrderPersistenceException {
        PrintWriter out;
        Set<LocalDate> dateKeys = orderBook.keySet();
        
        for(LocalDate key : dateKeys) {
            String orderFileName = ORDER_FILE_DIRECTORY + "\\" + ORDER_FILE_PREFIX + key.format(fileNameDateFormat) + ORDER_FILE_TYPE;
            File orderFile = new File(orderFileName);
            if (!orderFile.exists()) {
                try {
                    orderFile.createNewFile();
                } catch (IOException ex) {
                    throw new FlooringOrderPersistenceException("Problem creating " 
                            + ORDER_FILE_PREFIX + key.format(fileNameDateFormat));
                }
            }
            try {
                out = new PrintWriter(new FileWriter(orderFile));
            } catch (IOException e) {
                throw new FlooringOrderPersistenceException("Could not save "
                        + "order data.", e);
            }
            List<Order> orders = getOrdersForDate(key);
            for(Order order : orders) {
                out.println(order.getNumber() + DELIMITER + 
                        order.getCustomerName() + DELIMITER + 
                        order.getTaxRate().getState() + DELIMITER + 
                        order.getTaxRate().getRate().multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
                        + DELIMITER + order.getProductName() + DELIMITER + 
                        order.getArea() + DELIMITER + 
                        order.getProduct().getMaterialCostPerSqFoot() + 
                        DELIMITER + order.getProduct().getLaborCostPerSqFoot() + 
                        DELIMITER + order.getMaterialCost() + DELIMITER + 
                        order.getLaborCost() + DELIMITER + order.getTax() + 
                        DELIMITER + order.getTotalCost());
                out.flush();
            }
            out.close();
        }
    }

    @Override
    public int getLastOrderNumber() {
        List<Integer> keys = orderBook.values() // Collection <HashMap<Integer, Order>>
                .stream()                            // Stream<HashMap<Integer, Order>>
                .map(Map::keySet)                    // Stream<Set<Integer>
                .flatMap(Set::stream)                // Stream<Integer>
                .collect(Collectors.toList());       // List<Integer>
        
        int lastOrderNumber = keys.stream()          
                .max(Integer::compare)
                .get();

        return lastOrderNumber;
    }

    @Override
    public Map<LocalDate, HashMap<Integer, Order>> getOrderBook() {
        return orderBook;
    }
    
}
