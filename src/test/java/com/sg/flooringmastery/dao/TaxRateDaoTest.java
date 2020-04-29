/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.TaxRate;
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
public class TaxRateDaoTest {
    
    private TaxRateDao taxDao = new TaxRateDaoFileImpl();
    
    public TaxRateDaoTest() {
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
     * Test of loadListing method, of class TaxRateDao.
     */
    @Test
    public void testLoadListing() throws Exception {
        taxDao.loadListing();
    }
    
    
    @Test
    public void testGetMI() throws Exception {
        taxDao.loadListing();
        TaxRate Mi = taxDao.getTaxRate("MI");
        assertTrue(Mi.getState().equals("MI"));
        assertEquals(Mi.getRate(), new BigDecimal("0.0575"));
    }
    
    @Test
    public void testGetTN() throws Exception {
        taxDao.loadListing();
        TaxRate Tn = taxDao.getTaxRate("TN");
        assertNull(Tn);
    }
    
}
