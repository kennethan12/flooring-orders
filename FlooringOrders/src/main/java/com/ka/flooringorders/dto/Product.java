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
public class Product {

    private String type;
    private BigDecimal costPerSqFt;
    private BigDecimal laborCostPerSqFt;

    /**
     * Instantiate Product object with its type, cost per square feet, and labor
     * cost per square feet
     *
     * @param type of Product
     * @param costPerSqFt of Product
     * @param laborCostPerSqFt of Product
     */
    public Product(String type, BigDecimal costPerSqFt, BigDecimal laborCostPerSqFt) {
        this.type = type;
        this.costPerSqFt = costPerSqFt.setScale(2, RoundingMode.HALF_UP);
        this.laborCostPerSqFt = laborCostPerSqFt.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Getter for type of Product
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for the cost per square feet
     *
     * @return cost per square feet
     */
    public BigDecimal getCostPerSqFt() {
        return costPerSqFt;
    }

    /**
     * Getter for the labor cost per square feet
     *
     * @return labor cost per square feet
     */
    public BigDecimal getLaborCostPerSqFt() {
        return laborCostPerSqFt;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.costPerSqFt);
        hash = 23 * hash + Objects.hashCode(this.laborCostPerSqFt);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.costPerSqFt, other.costPerSqFt)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSqFt, other.laborCostPerSqFt)) {
            return false;
        }
        return true;
    }

}
