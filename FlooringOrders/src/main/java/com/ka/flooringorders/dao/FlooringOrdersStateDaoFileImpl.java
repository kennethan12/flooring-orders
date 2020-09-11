/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.State;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersStateDaoFileImpl implements FlooringOrdersStateDao {

    private List<State> states = new ArrayList<>();
    private String STATES_FILE;
    private String DELIMITER = ",";
    private int NUMBER_OF_FIELDS = 3;

    /**
     * Instantiate StateDao implementation with the original Taxes.txt
     */
    public FlooringOrdersStateDaoFileImpl() {
        this.STATES_FILE = "Data/Taxes.txt";
    }

    /**
     * Instantiate StateDao implementation with a text name passed in as a
     * parameter.
     *
     * @param statesTextFile that will be STATES_FILE.
     */
    public FlooringOrdersStateDaoFileImpl(String statesTextFile) {
        this.STATES_FILE = statesTextFile;
    }

    @Override
    public State getState(String label) throws StatePersistenceException {
        loadStates();
        State state = states.stream()
                .filter(s -> (s.getAbbrev().equalsIgnoreCase(label) || s.getName().equalsIgnoreCase(label)))
                .findAny()
                .orElse(null);
        return state;
    }

    @Override
    public List<State> getAllStates() throws StatePersistenceException {
        loadStates();
        return states;
    }

    /**
     * Take a line of the States text file and convert it into a State. If the
     * split text length does not match the number of fields, return null. If
     * there is a NumberFormatException, just return null.
     *
     * @param stateAsText that will be unmarshalled to a State object
     * @return State, or null if not able to be marshalled
     */
    private State unmarshallState(String stateAsText) {
        String[] stateTokens = stateAsText.split(DELIMITER);

        try {
            if (stateTokens.length == NUMBER_OF_FIELDS) {
                String abbrev = stateTokens[0];
                String name = stateTokens[1];
                BigDecimal taxRate = new BigDecimal(stateTokens[2]);

                State state = new State(abbrev, name, taxRate);
                return state;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Load states off of the STATES_FILE. If there's no file, throw a
     * FileNotFoundException error.
     *
     * @throws StatePersistenceException
     */
    private void loadStates() throws StatePersistenceException {
        states.clear();
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(STATES_FILE)));
        } catch (FileNotFoundException e) {
            throw new StatePersistenceException("Could not load states into memory.", e);
        }

        String currentLine;
        State currentState;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentState = unmarshallState(currentLine);

            // Make sure currentState is not null before being added
            if (currentState != null) {
                states.add(currentState);
            }
        }

        scanner.close();
    }
}
