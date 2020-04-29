/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Jeff
 */
public interface UserIO {
    
    void print(String message);
    
    BigDecimal readBigDecimal(String prompt, BigDecimal min);
    
    LocalDate readDate(String prompt);
    
    String readString(String prompt);
    
    int readInt(String prompt);
    
    int readInt(String prompt, int min, int max);
}
