/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringOrderAuditDao;
import com.sg.flooringmastery.dao.FlooringOrderPersistenceException;
import com.sg.flooringmastery.dao.OrderDao;
import com.sg.flooringmastery.dao.ProductDao;
import com.sg.flooringmastery.dao.TaxRateDao;
import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.TaxRate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jeff
 */
public class FlooringOrderServiceLayerImpl implements FlooringOrderServiceLayer {
    
    private OrderDao orderDao;
    private ProductDao productsDao;
    private TaxRateDao taxDao;
    private FlooringOrderAuditDao auditDao;
    private int nextOrderNumber;
    private DateTimeFormatter fileDateFormat = DateTimeFormatter.ofPattern("MMddyyyy");
    private DateTimeFormatter userDateInputFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public FlooringOrderServiceLayerImpl(OrderDao orderDao, 
            ProductDao productsDao, TaxRateDao taxDao, 
            FlooringOrderAuditDao auditDao) {
        this.orderDao = orderDao;
        this.productsDao = productsDao;
        this.taxDao = taxDao;
        this.auditDao = auditDao;
        nextOrderNumber = 0;
    }

    @Override
    public void loadOrders() throws FlooringOrderPersistenceException {
        orderDao.loadOrders();
        Set<LocalDate> orderDates = orderDao.getOrderBook().keySet();
        for (LocalDate orderDate : orderDates) {
            HashMap<Integer, Order> datedOrderSubBook = orderDao.getOrderBook().get(orderDate);
            Set<Integer> orderNums = datedOrderSubBook.keySet();
            for (Integer orderNum : orderNums) {
                Order order = orderDao.getOrder(orderDate, orderNum);
                completeOrderDetails(order);
            }
        }
        setNextOrderNumber(orderDao.getLastOrderNumber() + 1);
    }

    @Override
    public List<Order> getOrdersOfCertainDate(LocalDate date) throws FlooringOrderPersistenceException, NonexistentOrderException {
        try {
            List<Order> orders = orderDao.getOrdersForDate(date);
            if (orders.isEmpty()) {
                throw new NonexistentOrderException("ERROR: No orders entered with date " + date.format(userDateInputFormat) + ".\n");
            } else {
                return orderDao.getOrdersForDate(date);
            }
        } catch (NullPointerException e) {
            throw new NonexistentOrderException("ERROR: No orders entered with date " + date.format(userDateInputFormat) + ".\n");
        }
    }

    @Override
    public void addOrder(Order order) throws FlooringOrderPersistenceException {
        //validateOrderData(order);
        order.setNumber(nextOrderNumber);
        completeOrderDetails(order);
        orderDao.addOrder(order);
        setNextOrderNumber(nextOrderNumber + 1);
    }
    
    @Override
    public void completeOrderDetails(Order order) {
        TaxRate taxRate = taxDao.getTaxRate(order.getState());
        order.setTaxRate(taxRate);
        Product product = productsDao.getProduct(order.getProductName());
        order.setProduct(product);
        BigDecimal orderTotalMatCost = calculateMaterialCost(order.getArea(), product.getMaterialCostPerSqFoot());
        order.setMaterialCost(orderTotalMatCost);
        BigDecimal orderTotalLaborCost = calculateLaborCost(order.getArea(), product.getLaborCostPerSqFoot());
        order.setLaborCost(orderTotalLaborCost);
        BigDecimal orderGrossCost = calculateGrossCost(orderTotalMatCost, orderTotalLaborCost);
        BigDecimal taxes = calculateTax(taxRate.getRate(), orderGrossCost);
        order.setTax(taxes);
        BigDecimal totalCost = calculateTotalCost(taxes, orderGrossCost);
        order.setTotalCost(totalCost);
    }

    @Override
    public Order getOrder(List<String> userSelection) throws 
            FlooringOrderPersistenceException, OrderDataValidationException, 
            NonexistentOrderException {
        LocalDate orderDate = parseDate(userSelection.get(0));
        int orderNo = parseOrderNumber(userSelection.get(1));
        try {
            Order order = orderDao.getOrder(orderDate, orderNo);
            if(order == null) {
                throw new NonexistentOrderException("ERROR: No order #" + orderNo + 
                    " dated " + orderDate.format(userDateInputFormat) + " in the system.\n");
            } else {
                return order;
            } 
        } catch (NullPointerException e) {
            throw new NonexistentOrderException("ERROR: No order #" + orderNo + 
                    " dated " + orderDate.format(userDateInputFormat) + " in the system.\n");
        }
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNo) throws 
            FlooringOrderPersistenceException, NonexistentOrderException {
        try {
            return orderDao.removeOrder(orderDate, orderNo);
        } catch (NullPointerException e) {
            throw new NonexistentOrderException("ERROR: No order #" + orderNo + 
                    " dated " + orderDate.format(userDateInputFormat) + " in the system.\n");
        }
    }

    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal materialCostPerSqFt) {
        return area.multiply(materialCostPerSqFt).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSqFt) {
        return area.multiply(laborCostPerSqFt).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateGrossCost(BigDecimal materialCostTotal, BigDecimal laborCostTotal) {
        return materialCostTotal.add(laborCostTotal).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal taxRate, BigDecimal totalGrossCost) {
        return taxRate.multiply(totalGrossCost).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalCost(BigDecimal tax, BigDecimal totalGrossCost) {
        return tax.add(totalGrossCost).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void validateOrderData(Order order) throws OrderDataValidationException {
        if (order.getCustomerName() == null 
                || order.getCustomerName().trim().length() == 0
                || order.getState() == null || order.getState().trim().length() == 0 
                || taxDao.getTaxRate(order.getState().toUpperCase()) == null) {
            throw new OrderDataValidationException("ERROR: State " + 
                    order.getState() + " is invalid. Can only process orders for OH, PA, MI, and IN.");
        } else if (order.getProductName().trim().length() == 0 
                || order.getProductName() == null
                || productsDao.getProduct(order.getProductName()) == null) {
            throw new OrderDataValidationException("ERROR: Product type " + 
                    order.getProductName() + " is invalid. Carpet, Laminate, Tile, and Wood are choices available on order.");
        } else if (order.getArea() == null || order.getArea().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderDataValidationException("ERROR: " + order.getArea() + " is invalid. Area must be greater than 0.");
        } else if (order.getDate() == null) {
            throw new OrderDataValidationException("ERROR: Order must have a valid date.");
        }
    }

    @Override
    public int parseOrderNumber(String userInput) throws OrderDataValidationException {
        try {
            return Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new OrderDataValidationException("ERROR: Invalid input. Must "
                    + "be am integer number.");
        }
    }

    @Override
    public LocalDate parseDate(String userInput) throws OrderDataValidationException {
        try {
            return LocalDate.parse(userInput, userDateInputFormat);
        } catch (DateTimeParseException e) {
            throw new OrderDataValidationException("ERROR: Invalid input. Must "
                    + "be a valid date, and formatted as MM/DD/YYYY.");
        }
    }

    @Override
    public void writeOrders() throws FlooringOrderPersistenceException {
        orderDao.writeOrders();
    }

    public int getNextOrderNumber() {
        return nextOrderNumber;
    }

    public void setNextOrderNumber(int nextOrderNumber) {
        this.nextOrderNumber = nextOrderNumber;
    }

    @Override
    public void loadTaxRates() throws FlooringOrderPersistenceException {
        taxDao.loadListing();
    }

    @Override
    public void loadProductListing() throws FlooringOrderPersistenceException {
        productsDao.loadListing();
    }

    @Override
    public boolean continueProcessing(String userInput) {
        if (userInput.equalsIgnoreCase("Y")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Product getProduct(String productName) throws FlooringOrderPersistenceException{
        return productsDao.getProduct(productName);
    }

    @Override
    public TaxRate getTaxRate(String state) throws FlooringOrderPersistenceException {
        return taxDao.getTaxRate(state);
    }

    @Override
    public void editOrder(Order existingOrder, List<String> editedData) throws 
            FlooringOrderPersistenceException, OrderDataValidationException {
        LocalDate newDate = existingOrder.getDate();
        String newCustomer = existingOrder.getCustomerName();
        BigDecimal newArea = existingOrder.getArea();
        String newState = existingOrder.getState();
        String newProduct = existingOrder.getProductName();
        
        if (!editedData.get(0).isEmpty()) {
            newDate = parseDate(editedData.get(0));
        }
        if (!editedData.get(1).isEmpty()) {
            newCustomer = editedData.get(1);
        }
        if (!editedData.get(2).isEmpty()) {
            newArea = new BigDecimal(editedData.get(2));
        }
        if (!editedData.get(3).isEmpty()) {
            newState = editedData.get(3);
        }
        if (!editedData.get(4).isEmpty()) {
            newProduct = editedData.get(4);
        }
        
        Order editedOrder = new Order(newDate, newCustomer, newArea, newState, 
                newProduct);
        editedOrder.setNumber(existingOrder.getNumber());
        
        validateOrderData(editedOrder);
        completeOrderDetails(editedOrder);
        orderDao.editOrder(existingOrder, editedOrder);
    }

    @Override
    public Map<LocalDate, HashMap<Integer, Order>> getOrderBook() {
        return orderDao.getOrderBook();
    }
    
}
