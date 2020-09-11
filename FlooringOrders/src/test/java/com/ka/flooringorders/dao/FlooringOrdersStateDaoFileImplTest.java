/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.State;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersStateDaoFileImplTest {

    FlooringOrdersStateDao testDao;

    public FlooringOrdersStateDaoFileImplTest() {
    }

    @BeforeEach
    public void setUp() {
        testDao = new FlooringOrdersStateDaoFileImpl();
    }

    @Test
    public void testGetState() throws Exception {
        State texasClone = new State("TX", "Texas", new BigDecimal("8.19"));
        State washingtonClone = new State("WA", "Washington", new BigDecimal("9.25"));

        State texas = testDao.getState("TX");
        State washington = testDao.getState("Washington");

        assertNotNull(texas, "Texas should not be a null State.");
        assertNotNull(washington, "Washington should not be a null State.");
        assertEquals(texas, texasClone, "Texas should be equal to its clone.");
        assertEquals(washington, washingtonClone, "Washington should be equal to its clone.");

        State newYork = testDao.getState("NY");

        assertNull(newYork, "There should not be a New York State object.");
    }

    @Test
    public void testGetAllStates() throws Exception {
        State texas = new State("TX", "Texas", new BigDecimal("8.19"));
        State washington = new State("WA", "Washington", new BigDecimal("9.25"));
        State kentucky = new State("KY", "Kentucky", new BigDecimal("6.00"));
        State california = new State("CA", "California", new BigDecimal("25.00"));

        List<State> states = testDao.getAllStates();
        assertNotNull(states, "List of states should not be null.");
        assertEquals(14, states.size(), "There should be 14 states in the list.");
//        assertTrue(states.contains(texas), "List should contain Texas.");
//        assertTrue(states.contains(washington), "List should contain Washington.");
//        assertTrue(states.contains(kentucky), "List should contain Kentucky.");
//        assertTrue(states.contains(california), "List should contain California.");
    }

}
