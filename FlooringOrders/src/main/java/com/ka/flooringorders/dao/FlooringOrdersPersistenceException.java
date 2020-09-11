/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersPersistenceException extends Exception {
    
    public FlooringOrdersPersistenceException(String message) {
        super(message);
    }
    
    public FlooringOrdersPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
