/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 *
 * @author kennethan
 */
public class State {

    private String abbrev;
    private String name;
    private BigDecimal taxRate;

    /**
     * Instantiate State object with the abbreviation, name, and tax rate of the
     * state.
     *
     * @param abbrev of the state, i.e. "NY"
     *
     * @param name of the state
     * @param taxRate of the state
     */
    public State(String abbrev, String name, BigDecimal taxRate) {
        this.abbrev = abbrev;
        this.name = name;
        this.taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Instantiate State object with the abbreviation and tax rate of the state.
     * The name property will be null.
     *
     * @param abbrev of the state
     * @param taxRate of the state
     */
    public State(String abbrev, BigDecimal taxRate) {
        this.abbrev = abbrev;
        this.name = null;
        this.taxRate = taxRate;
    }

    /**
     * Getter for the abbreviation of the State.
     *
     * @return abbreviation
     */
    public String getAbbrev() {
        return abbrev;
    }

    /**
     * Getter for State name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for State tax rate.
     *
     * @return tax rate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.abbrev);
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.taxRate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (!Objects.equals(this.abbrev, other.abbrev)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        return true;
    }

}
