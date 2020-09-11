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
public class Order {
    
    private int id;
    private String customer;
    private State state;
    private Product product;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    
    /**
     * Instantiate Order object using an id.
     * @param id that will be associated with the new Order object
     */
    public Order(int id) {
        this.id = id;
    }

    /**
     * Getter for id
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for customer
     * @return String customer
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Setter for customer
     * @param customer that will be the value of this.customer
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * Getter for state
     * @return State object
     */
    public State getState() {
        return state;
    }

    /**
     * Setter for state
     * @param state that will be the State of this.state
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Getter for product
     * @return Product object
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Setter for product
     * @param product that will be the Product of this.product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Getter for area
     * @return BigDecimal area
     */
    public BigDecimal getArea() {
        return area;
    }

    /**
     * Setter for area
     * @param area of this Order
     */
    public void setArea(BigDecimal area) {
        this.area = area.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * MaterialCost = (Area * CostPerSquareFoot)
     * @return material cost of the Order
     */
    public BigDecimal getMaterialCost() {
        return area.multiply(product.getCostPerSqFt())
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * LaborCost = (Area * LaborCostPerSquareFoot)
     * @return labor cost of the Order
     */
    public BigDecimal getLaborCost() {
        return area.multiply(product.getLaborCostPerSqFt())
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Tax = (MaterialCost + LaborCost) * (TaxRate/100)
     * @return the tax of the Order
     */
    public BigDecimal getTax() {
        BigDecimal totalWithoutTax = getMaterialCost().add(getLaborCost());
        BigDecimal taxPercent = state.getTaxRate().divide(new BigDecimal(100));
        return totalWithoutTax.multiply(taxPercent)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Total = (MaterialCost + LaborCost + Tax)
     * @return total cost of the Order
     */
    public BigDecimal getTotal() {
        return getMaterialCost()
                .add(getLaborCost())
                .add(getTax())
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.customer);
        hash = 79 * hash + Objects.hashCode(this.state);
        hash = 79 * hash + Objects.hashCode(this.product);
        hash = 79 * hash + Objects.hashCode(this.area);
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
        final Order other = (Order) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        return true;
    }

}
