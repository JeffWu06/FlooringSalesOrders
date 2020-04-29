/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.TaxRate;

/**
 *
 * @author Jeff
 */
public interface TaxRateDao {
    
    void loadListing() throws FlooringOrderPersistenceException;
    
    TaxRate getTaxRate(String state);
}
