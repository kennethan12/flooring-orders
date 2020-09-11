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
public interface FlooringOrdersAuditDao {
    
    /**
     * Enters an entry log of whichever Orders were added, edited, or removed.
     * 
     * @param entry that includes the Order that was updated and the date and
     * time of when it occurred
     * @throws FlooringOrdersPersistenceException 
     */
    public void writeAuditEntry(String entry) throws FlooringOrdersPersistenceException;
}
