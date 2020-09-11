/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.service;

import com.ka.flooringorders.dao.FlooringOrdersDao;
import com.ka.flooringorders.dao.FlooringOrdersPersistenceException;
import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersDaoStubImpl implements FlooringOrdersDao {

    public Order stubOrder;
    public LocalDate stubDate;
    public int stubId;

    public FlooringOrdersDaoStubImpl() {
        stubOrder = new Order(1);
        stubOrder.setCustomer("Elon Musk");
        stubOrder.setState(new State("CA", new BigDecimal("25.00")));
        stubOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        stubOrder.setArea(new BigDecimal("200.00"));

        stubDate = LocalDate.parse("2020-01-01");
        stubId = stubOrder.getId();
    }

    @Override
    public Order addOrder(LocalDate date, int id, Order order) throws FlooringOrdersPersistenceException {
        if (date.equals(stubDate) && id == stubOrder.getId()) {
            return stubOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order getOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException {
        if (date.equals(stubDate) && id == stubOrder.getId()) {
            return stubOrder;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws FlooringOrdersPersistenceException {
        List<Order> stubList = new ArrayList<>();
        if (date.equals(stubDate)) {
            stubList.add(stubOrder);
        }
        return stubList;
    }

    @Override
    public Order removeOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException {
        if (date.equals(stubDate) && id == stubOrder.getId()) {
            return stubOrder;
        } else {
            return null;
        }
    }

    @Override
    public boolean exportAllOrders() throws FlooringOrdersPersistenceException {
        return true;
    }

    @Override
    public int getNextId(LocalDate date) throws FlooringOrdersPersistenceException {
        return stubId;
    }

    @Override
    public void incrementId(LocalDate date) throws FlooringOrdersPersistenceException {
        stubId++;
    }

}
