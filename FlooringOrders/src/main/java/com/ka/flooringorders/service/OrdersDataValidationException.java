/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.service;

/**
 *
 * @author kennethan
 */
public class OrdersDataValidationException extends Exception {
    
    public OrdersDataValidationException(String message) {
        super(message);
    }
    
    public OrdersDataValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
