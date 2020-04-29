/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

/**
 *
 * @author Jeff
 */
public class OrderDaoFileTrainingImpl extends OrderDaoFileImpl {

    @Override
    public void writeOrders() throws FlooringOrderPersistenceException {
        throw new FlooringOrderPersistenceException("Save feature not available "
                + "in TRAINING mode. Changes not persisted to file.");
    }
    
}
