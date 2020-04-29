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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Jeff
 */
public class FlooringOrderServiceLayerTest {
    
    private FlooringOrderServiceLayer service;
    
    public FlooringOrderServiceLayerTest() {
//        OrderDao orderDao = new OrderDaoStubImpl();
//        TaxRateDao taxDao = new TaxRateDaoFileImpl();
//        ProductDao productsDao = new ProductDaoFileImpl();
//        
//        service = new FlooringOrderServiceLayerImpl(orderDao, productsDao, taxDao);
    
        ApplicationContext ctx = new ClassPathXmlApplicationContext("prod-config.xml");
        service = ctx.getBean("serviceLayer", FlooringOrderServiceLayer.class);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        service.loadProductListing();
        service.loadTaxRates();
        service.loadOrders();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of loadOrders method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testLoadOrders() throws Exception {
        service.loadOrders();
    }

    /**
     * Test of getOrdersOfCertainDate method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testGetOrdersOfDate06012013() throws Exception {
        List<Order> orders = service.getOrdersOfCertainDate(LocalDate.parse("06/01/2013", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        assertEquals(orders.size(), 1);
    }
    
    @Test
    public void testGetOrdersOfDate06022013() throws Exception {
        List<Order> orders = service.getOrdersOfCertainDate(LocalDate.parse("06/02/2013", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        assertEquals(orders.size(), 1);
    }
    
    @Test
    public void testGetOrdersOfDate01011999() throws Exception {
        try {
            List<Order> orders = service.getOrdersOfCertainDate(LocalDate.parse("01/01/1999", DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            fail("Expected NonexistentOrderException not thrown.");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Test of addOrder method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testAddOrder() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
        new BigDecimal("500"), "Mi", "Tile");
        service.addOrder(newOrder);
        
        assertNotNull(service.getOrdersOfCertainDate(LocalDate.parse("2000-01-01")));
    }
    
    @Test
    public void testAddOrderInvalidData() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "", 
        new BigDecimal("500"), "TN", "Vinyl");
        
        try {
            service.addOrder(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    /**
     * Test of getOrder method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testgetOrder2ValidDateValidNumber() throws Exception {
        ArrayList<String> testArgs = new ArrayList<>();
        testArgs.add("06/02/2013");
        testArgs.add("2");
        Order order = service.getOrder(testArgs);
        
        assertNotNull(order);
    }
    
    @Test
    public void testgetOrder2InvalidDateValidNumber() throws Exception {
        ArrayList<String> testArgs = new ArrayList<>();
        testArgs.add("06/03/2013");
        testArgs.add("2");
        
        try {
            Order order = service.getOrder(testArgs);
            fail("Expected NonexistentOrderException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testgetOrder99ValidDateInvalidNumber() throws Exception {
        ArrayList<String> testArgs = new ArrayList<>();
        testArgs.add("06/01/2013");
        testArgs.add("99");
        
        try {
            Order order = service.getOrder(testArgs);
            fail("Expected NonexistentOrderException not thrown.");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Test of removeOrder method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testRemoveOrder2() throws Exception {
        ArrayList<String> testArgs = new ArrayList<>();
        testArgs.add("06/02/2013");
        testArgs.add("2");
        Order order = service.getOrder(testArgs);
        Order removedOrder = service.removeOrder(order.getDate(), order.getNumber());
        
        assertEquals(order, removedOrder);
        try {
            removedOrder = service.getOrder(testArgs);
            fail("Expected NonexistentOrderException not thrown.");
        } catch (NonexistentOrderException e) {
            return;
        }
    }

    /**
     * Test of calculateMaterialCost method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testCalculateMaterialCost() throws FlooringOrderPersistenceException {
//        productsDao.loadListing();
        Product testProduct = service.getProduct("tile");
        BigDecimal tileMatCost = testProduct.getMaterialCostPerSqFoot();
        BigDecimal testArea = new BigDecimal("200.00");
        BigDecimal totalMatCost = service.calculateMaterialCost(testArea, tileMatCost);
        
        assertEquals(totalMatCost, new BigDecimal("700.00"));
        
    }

    /**
     * Test of calculateLaborCost method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testCalculateLaborCost() throws FlooringOrderPersistenceException {
//        productsDao.loadListing();
        Product testProduct = service.getProduct("tile");
        BigDecimal tileLabCost = testProduct.getLaborCostPerSqFoot();
        BigDecimal testArea = new BigDecimal("200.00");
        BigDecimal totalLabCost = service.calculateMaterialCost(testArea, tileLabCost);
        
        assertEquals(totalLabCost, new BigDecimal("830.00"));
    }

    /**
     * Test of calculateGrossCost method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testCalculateGrossCost() throws FlooringOrderPersistenceException {
//        productsDao.loadListing();
        Product testProduct = service.getProduct("tile");
        BigDecimal tileMatCost = testProduct.getMaterialCostPerSqFoot();
        BigDecimal tileLabCost = testProduct.getLaborCostPerSqFoot();
        BigDecimal testArea = new BigDecimal("200.00");
        BigDecimal totalMatCost = service.calculateMaterialCost(testArea, tileMatCost);
        BigDecimal totalLabCost = service.calculateMaterialCost(testArea, tileLabCost);
        BigDecimal totalGross = service.calculateGrossCost(totalMatCost, totalLabCost);
        
        assertEquals(totalGross, new BigDecimal("1530.00"));
    }

    /**
     * Test of calculateTax method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testCalculateTax() throws FlooringOrderPersistenceException {
//        productsDao.loadListing();
//        taxDao.loadListing();
        Product testProduct = service.getProduct("tile");
        TaxRate testTax = service.getTaxRate("OH");
        BigDecimal tileMatCost = testProduct.getMaterialCostPerSqFoot();
        BigDecimal tileLabCost = testProduct.getLaborCostPerSqFoot();
        BigDecimal testArea = new BigDecimal("200.00");
        BigDecimal totalMatCost = service.calculateMaterialCost(testArea, tileMatCost);
        BigDecimal totalLabCost = service.calculateMaterialCost(testArea, tileLabCost);        
        BigDecimal totalGross = service.calculateGrossCost(totalMatCost, totalLabCost);
        BigDecimal totalTax = service.calculateTax(testTax.getRate(), totalGross);
        
        assertEquals(totalTax, new BigDecimal("95.63"));
    }

    /**
     * Test of calculateTotalCost method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testCalculateTotalCost() throws FlooringOrderPersistenceException {
//        productsDao.loadListing();
//        taxDao.loadListing();
        Product testProduct = service.getProduct("tile");
        TaxRate testTax = service.getTaxRate("OH");
        BigDecimal tileMatCost = testProduct.getMaterialCostPerSqFoot();
        BigDecimal tileLabCost = testProduct.getLaborCostPerSqFoot();
        BigDecimal testArea = new BigDecimal("200.00");
        BigDecimal totalMatCost = service.calculateMaterialCost(testArea, tileMatCost);
        BigDecimal totalLabCost = service.calculateMaterialCost(testArea, tileLabCost);        
        BigDecimal totalGross = service.calculateGrossCost(totalMatCost, totalLabCost);        
        BigDecimal totalTax = service.calculateTax(testTax.getRate(), totalGross);        
        BigDecimal totalOrderCost = service.calculateTotalCost(totalTax, totalGross);
        
        assertEquals(totalOrderCost, new BigDecimal("1625.63"));
    }

    /**
     * Test of validateOrderData method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testValidateOrderDataAllDataValid() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
        new BigDecimal("500"), "Mi", "Tile");
        service.validateOrderData(newOrder);
        // Should throw no exception.
    }
    
    @Test
    public void testValidateOrderDataNoName() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "", 
        new BigDecimal("500"), "Mi", "Tile");
        try {
            service.validateOrderData(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testValidateOrderDataNoArea() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
        new BigDecimal("0"), "Mi", "Tile");
        try {
            service.validateOrderData(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testValidateOrderDataNoState() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
        new BigDecimal("500"), "", "Tile");
        try {
            service.validateOrderData(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testValidateOrderDataInvalidState() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
        new BigDecimal("500"), "TN", "Tile");
        try {
            service.validateOrderData(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testValidateOrderDataNoProduct() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
        new BigDecimal("500"), "TN", "");
        try {
            service.validateOrderData(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testValidateOrderDataInvalidProduct() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2000-01-01"), "Matthew Mara", 
                new BigDecimal("500"), "TN", "Vinyl");
        try {
            service.validateOrderData(newOrder);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Test of parseOrderNumber method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testParseOrderNumber1() throws Exception {
        String number = "1";
        int parsed = service.parseOrderNumber(number);
        assertEquals(parsed, 1);
    }
    
    @Test
    public void testParseOrderNumberA() throws Exception {
        String number = "a";
        try {
            int parsed = service.parseOrderNumber(number);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Test of parseDate method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testParseDate06012013() throws Exception {
        String date = "06/01/2013";
        LocalDate parsed = service.parseDate(date);
        assertEquals(parsed, LocalDate.parse("2013-06-01"));
    }
    
    @Test
    public void testParseDate0601201E() throws Exception {
        String date = "06/01/201E";
        try {
            LocalDate parsed = service.parseDate(date);
            fail("Expected OrderDataValidationException not thrown.");
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Test of writeOrders method, of class FlooringOrderServiceLayer.
     */
    @Test
    public void testWriteOrders() throws Exception {
    }
    
    @Test
    public void testContinueProcessingYes() {
        String testInput = "Y";
        
        assertTrue(service.continueProcessing(testInput));
    }
    
    @Test
    public void testContinueProcessingNo() {
        String testInput = "no";
        
        assertFalse(service.continueProcessing(testInput));
    }
    
    @Test
    public void testCompleteOrderDetails() throws Exception {
        Order newOrder = new Order(LocalDate.parse("2020-01-19"), "Jimmy G", 
                new BigDecimal("1000"), "mi", "Laminate");
        service.completeOrderDetails(newOrder);
        
        assertEquals(newOrder.getLaborCost(), new BigDecimal("2100.00"));
        assertEquals(newOrder.getMaterialCost(), new BigDecimal("1750.00"));
        assertEquals(newOrder.getTax(), new BigDecimal("221.38"));
        assertEquals(newOrder.getTotalCost(), new BigDecimal("4071.38"));
        assertEquals(newOrder.getTaxRate(), service.getTaxRate("MI"));
        assertEquals(newOrder.getProduct(), service.getProduct("Laminate"));
    }
    
    @Test
    public void testEditOrderDifferentDate() throws Exception {
        ArrayList<String> testArgs = new ArrayList<>(Arrays.asList("06/01/2013","1"));
        Order existingOrder = service.getOrder(testArgs);
        ArrayList<String> editedData = new ArrayList<>(Arrays.asList(
                "06/05/2013", "Wise", "100", "OH", "Wood"));
        service.editOrder(existingOrder, editedData);        
        Order editedOrder = service.getOrder(new ArrayList<>(Arrays.asList("06/05/2013","1")));
        
        assertEquals(editedOrder.getDate(), LocalDate.parse("2013-06-05"));
        assertEquals(editedOrder.getNumber(), 1);
        assertEquals(editedOrder.getCustomerName(), "Wise");
        try {
            Order originalOrder = service.getOrder(testArgs);
            fail("Expected NonExistentOrderException not thrown.");
        } catch (NonexistentOrderException e) {
            return;
        }
        
    }
    
    @Test
    public void getOrderBook() {
        assertNotNull(service.getOrderBook());
    }


    
}
