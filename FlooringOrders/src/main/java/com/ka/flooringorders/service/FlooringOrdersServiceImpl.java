/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.service;

import com.ka.flooringorders.dao.FlooringOrdersAuditDao;
import com.ka.flooringorders.dao.FlooringOrdersDao;
import com.ka.flooringorders.dao.FlooringOrdersPersistenceException;
import com.ka.flooringorders.dao.FlooringOrdersProductDao;
import com.ka.flooringorders.dao.FlooringOrdersStateDao;
import com.ka.flooringorders.dao.ProductPersistenceException;
import com.ka.flooringorders.dao.StatePersistenceException;
import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersServiceImpl implements FlooringOrdersService {

    FlooringOrdersDao dao;
    FlooringOrdersStateDao stateDao;
    FlooringOrdersProductDao productDao;
    FlooringOrdersAuditDao auditDao;

    public FlooringOrdersServiceImpl(FlooringOrdersDao dao,
            FlooringOrdersStateDao stateDao,
            FlooringOrdersProductDao productDao,
            FlooringOrdersAuditDao auditDao) {
        this.dao = dao;
        this.stateDao = stateDao;
        this.productDao = productDao;
        this.auditDao = auditDao;
    }

    @Override
    public int getNextId(LocalDate date) throws FlooringOrdersPersistenceException {
        return dao.getNextId(date);
    }

    @Override
    public void addOrder(LocalDate date, Order order) throws
            FlooringOrdersPersistenceException,
            OrdersDataValidationException,
            DuplicateOrderNumberException {

        if (dao.getOrder(date, order.getId()) != null) {
            // dao.incrementId();
            throw new DuplicateOrderNumberException("ERROR: Could not add Order. Order #"
                    + order.getId()
                    + " already exists.");
        }

        validateOrderData(order);
        dao.addOrder(date, order.getId(), order);
        //dao.incrementId();

        auditDao.writeAuditEntry("Order #" + order.getId() + " ADDED.");
    }

    @Override
    public Order getOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException {
        return dao.getOrder(date, id);
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws FlooringOrdersPersistenceException {
        return dao.getOrdersByDate(date);
    }

    @Override
    public Order editOrder(LocalDate date, Order changedOrder) throws
            FlooringOrdersPersistenceException,
            OrdersDataValidationException {
        validateOrderData(changedOrder);

        Order previousOrder = dao.getOrder(date, changedOrder.getId());
        if (previousOrder != null) {
            dao.removeOrder(date, changedOrder.getId());
        }
        dao.addOrder(date, changedOrder.getId(), changedOrder);
        auditDao.writeAuditEntry("Order #" + changedOrder.getId() + " UPDATED.");

        return changedOrder;
    }

    @Override
    public Order removeOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException {
        Order removedOrder = dao.removeOrder(date, id);
        if (removedOrder != null) {
            auditDao.writeAuditEntry("Order #" + removedOrder.getId() + " REMOVED.");
        }
        return removedOrder;
    }

    @Override
    public void exportAllData() throws FlooringOrdersPersistenceException {
        dao.exportAllOrders();
    }

    @Override
    public State getState(String name) throws StatePersistenceException {
        return stateDao.getState(name);
    }

    @Override
    public List<State> getAllStates() throws StatePersistenceException {
        return stateDao.getAllStates();
    }

    @Override
    public Product getProduct(String type) throws ProductPersistenceException {
        return productDao.getProduct(type);
    }

    @Override
    public List<Product> getAllProducts() throws ProductPersistenceException {
        return productDao.getAllProducts();
    }

    /**
     * Checks that the passed in Order fulfills the following requirements: 1)
     * the customer must not be an empty String or be null, 2) the state cannot
     * be null, 3) the product cannot be null, 4) the area cannot be less than a
     * 100 square feet.
     *
     * @param order that is checked and validated
     * @throws OrdersDataValidationException if the order is not validated.
     */
    private void validateOrderData(Order order) throws OrdersDataValidationException {
        if (order.getCustomer() == null
                || order.getCustomer().trim().length() == 0
                || order.getState() == null
                || order.getProduct() == null
                || order.getArea().compareTo(new BigDecimal(100)) < 0) {
            throw new OrdersDataValidationException("Customer, State, and Product must have some value; "
                    + "Area cannot be less than 100 square feet.");
        }
    }

}
