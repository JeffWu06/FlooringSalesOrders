/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import java.math.BigDecimal;
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
public class ProductDaoTest {
    
    private ProductDao productDao = new ProductDaoFileImpl();
    
    public ProductDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of loadListing method, of class ProductDao.
     */
    @Test
    public void testLoadListing() throws Exception {
        productDao.loadListing();
    }

    /**
     * Test of getProduct method, of class ProductDao.
     */
    @Test
    public void testGetCarpet() throws Exception {
        productDao.loadListing();
        Product carpet = productDao.getProduct("Carpet");
        assertTrue(carpet.getType().equals("Carpet"));
        assertEquals(carpet.getMaterialCostPerSqFoot(), new BigDecimal("2.25"));
        assertEquals(carpet.getLaborCostPerSqFoot(), new BigDecimal("2.10"));
    }
    
    @Test
    public void testGetVinyl() throws Exception {
        productDao.loadListing();
        Product vinyl = productDao.getProduct("Vinyl");
        assertNull(vinyl);
    }

    
}
