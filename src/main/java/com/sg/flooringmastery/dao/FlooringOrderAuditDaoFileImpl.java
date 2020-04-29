/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 *
 * @author Jeff
 */
public class FlooringOrderAuditDaoFileImpl implements FlooringOrderAuditDao {
    
    public static final String LOG_FILE = "audit.txt";
    
    @Override
    public void writeLogEntry(String entry) throws FlooringOrderPersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(LOG_FILE, true));
        } catch (IOException e) {
            throw new FlooringOrderPersistenceException("Could not write to "
                    + "audit log file.", e);
        }
        
        LocalDateTime timeStamp = LocalDateTime.now();
        out.println(timeStamp.toString() + ": " + entry);
        out.flush();
    }
    
}
