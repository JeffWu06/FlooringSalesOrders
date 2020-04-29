/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.service;

/**
 *
 * @author Jeff
 */
public class NonexistentOrderException extends Exception {
    
    public NonexistentOrderException(String message) {
        super(message);
    }

    public NonexistentOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
