/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.State;
import java.util.List;

/**
 *
 * @author kennethan
 */
public interface FlooringOrdersStateDao {
    
    /**
     * Retrieve a State based on the name of a State. Return null if State
     * with that name is not found.
     * @param label of the State, either abbreviation or name
     * @return State with that name, or null if that State is not found.
     * @throws com.ka.flooringorders.dao.StatePersistenceException
     */
    public State getState(String label) throws StatePersistenceException;
    
    /**
     * Return a list of all the States available.
     * @return List of States
     * @throws com.ka.flooringorders.dao.StatePersistenceException
     */
    public List<State> getAllStates() throws StatePersistenceException;
    
}
