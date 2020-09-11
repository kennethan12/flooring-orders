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
public class StatePersistenceException extends Exception {
    
    public StatePersistenceException(String message) {
        super(message);
    }
    
    public StatePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
