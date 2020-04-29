/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Order;
import com.sg.flooringmastery.model.Product;
import com.sg.flooringmastery.model.TaxRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jeff
 */
public class OrderDaoTest {
    
    private OrderDao orderDao = new OrderDaoFileImpl();
    private Map<LocalDate, HashMap<Integer, Order>> orderBook = new HashMap<>();
    
    public OrderDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2013-06-01"), "Wise", 
        new BigDecimal("100"), "OH", "Wood");
        TaxRate taxRate = new TaxRate(newOrder.getState());
        taxRate.setRate(new BigDecimal("0.00625"));
        newOrder.setTaxRate(taxRate);
        Product product = new Product(newOrder.getProductName());
        product.setLaborCostPerSqFoot(new BigDecimal("4.75"));
        product.setMaterialCostPerSqFoot(new BigDecimal("5.15"));
        newOrder.setProduct(product);
        newOrder.setMaterialCost(newOrder.getArea().multiply(product.getMaterialCostPerSqFoot()));
        newOrder.setLaborCost(newOrder.getArea().multiply(product.getLaborCostPerSqFoot()));
        BigDecimal grossCost = newOrder.getLaborCost().add(newOrder.getMaterialCost());
        newOrder.setTax(grossCost.multiply(taxRate.getRate()));
        newOrder.setTotalCost(grossCost.add(newOrder.getTax()));
        newOrder.setNumber(1);
        orderDao.addOrder(newOrder);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testLoadOrders() throws Exception {
        orderDao.loadOrders();
    }

    /**
     * Test of getOrdersForDate method, of class OrderDao.
     */
    @Test
    public void getOrdersForDate06012013() throws Exception {
        LocalDate testDate = LocalDate.parse("06/01/2013", 
                            DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<Order> orders = orderDao.getOrdersForDate(testDate);
        assertEquals(1, orders.size());
        assertEquals(orders.get(0).getNumber(), 1);
        assertTrue(orders.get(0).getCustomerName().equals("Wise"));
    }
    
    @Test
    public void testGetOrdersForDate01011999() throws Exception {
        LocalDate testDate = LocalDate.parse("01/01/1999", 
                            DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        try {
            List<Order> orders = orderDao.getOrdersForDate(testDate);
            fail("No NullPointerException thrown.");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Test of addOrder method, of class OrderDao.
     */
    @Test
    public void testAddOrderTo06012013() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2013-06-01"), "Jones", 
        new BigDecimal("200"), "OH", "Wood");
        newOrder.setTaxRate(new TaxRate(newOrder.getState()));
        newOrder.setProduct(new Product(newOrder.getProductName()));
        newOrder.setArea(newOrder.getArea());
        newOrder.setNumber(2);        
        orderDao.addOrder(newOrder);
        
        LocalDate testDate = LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<Order> orders = orderDao.getOrdersForDate(testDate);
        
        assertEquals(2, orders.size());
        assertEquals(orderDao.getOrder(testDate, 2), newOrder);
    }
    
    @Test
    public void testAddOrderTo01011999() throws Exception {
        Order newOrder = new Order(LocalDate.parse("1999-01-01"), "Matthew Mara", 
        new BigDecimal("500"), "Mi", "Tile");
        newOrder.setTaxRate(new TaxRate(newOrder.getState()));
        newOrder.setProduct(new Product(newOrder.getProductName()));
        newOrder.setArea(newOrder.getArea());
        newOrder.setNumber(3);
        
        orderDao.addOrder(newOrder);
        LocalDate testDate = LocalDate.parse("01/01/1999", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        List<Order> orders = orderDao.getOrdersForDate(testDate);
        
        assertEquals(orders.size(), 1);
        assertEquals(orderDao.getOrder(testDate, 3), newOrder);
    }

    /**
     * Test of editOrder method, of class OrderDao.
     */
    @Test
    public void testEditOrderChangeName() throws Exception {
        Order existingOrder = orderDao.getOrder(LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy")), 1);
        Order editedOrder = new Order(LocalDate.parse("2013-06-01"), "Wiese", 
        new BigDecimal("100"), "OH", "Wood");
        editedOrder.setTaxRate(new TaxRate(editedOrder.getState()));
        editedOrder.setProduct(new Product(editedOrder.getProductName()));
        editedOrder.setArea(editedOrder.getArea());
        editedOrder.setNumber(1);
        
        orderDao.editOrder(existingOrder, editedOrder);
        Order newOrderNumber1 = orderDao.getOrder(LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy")), 1);
        
        assertEquals(newOrderNumber1.getCustomerName(), "Wiese");
    }
    
    @Test
    public void testEditOrderChangeDate() throws Exception {
        Order existingOrder = orderDao.getOrder(LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy")), 1);
        Order editedOrder = new Order(LocalDate.parse("2013-06-05"), "Wise", 
        new BigDecimal("100"), "OH", "Wood");
        editedOrder.setTaxRate(new TaxRate(editedOrder.getState()));
        editedOrder.setProduct(new Product(editedOrder.getProductName()));
        editedOrder.setArea(editedOrder.getArea());
        editedOrder.setNumber(1);
        editedOrder.setDate(LocalDate.parse("06/05/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        
        orderDao.editOrder(existingOrder, editedOrder);
        Order newOrderNumber1 = orderDao.getOrder(LocalDate.parse("06/05/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy")), 1);
        
        assertEquals(newOrderNumber1.getCustomerName(), "Wise");
        try {
            existingOrder = orderDao.getOrder(LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy")), 1);
            fail("Expected NullPointerException not thrown.");
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Test of getOrder method, of class OrderDao.
     */
    @Test
    public void testGetOrder1() throws Exception {
        LocalDate testDate = LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        Order order = orderDao.getOrder(testDate, 1);
        
        assertNotNull(order);
        assertEquals(order.getDate(), testDate);
        assertTrue(order.getCustomerName().equalsIgnoreCase("Wise"));
    }
    
    @Test
    public void testGetOrder2() throws Exception {
        LocalDate testDate = LocalDate.parse("01/01/1999", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        try {
            Order order = orderDao.getOrder(testDate, 2);
            fail("Expected NullPointerException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    
    /**
     * Test of removeOrder method, of class OrderDao.
     */
    @Test
    public void testRemoveOrder1() throws Exception {
        LocalDate testDate = LocalDate.parse("06/01/2013", 
                    DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        orderDao.removeOrder(testDate, 1);
        
        try {
            Order order = orderDao.getOrder(testDate, 1);
            fail("Expected NullPointerException not thrown.");
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Test of writeOrders method, of class OrderDao.
     */
    @Test
    public void testWriteOrders() throws Exception {
        orderDao.writeOrders();
    }
    
    @Test
    public void getOrderBook() {
        Map<LocalDate, HashMap<Integer, Order>> orderBook = orderDao.getOrderBook();
        assertNotNull(orderBook);
    }
    
}
