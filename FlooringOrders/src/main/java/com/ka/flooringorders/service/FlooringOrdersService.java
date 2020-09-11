/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.service;

import com.ka.flooringorders.dao.FlooringOrdersPersistenceException;
import com.ka.flooringorders.dao.ProductPersistenceException;
import com.ka.flooringorders.dao.StatePersistenceException;
import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author kennethan
 */
public interface FlooringOrdersService {

    /**
     * Returns the next available Order number.
     *
     * @param date of the order file
     * @return the next available Order number.
     * @throws FlooringOrdersPersistenceException
     */
    public int getNextId(LocalDate date) throws FlooringOrdersPersistenceException;

    /**
     * Add an Order to the application, categorized by date.
     *
     * @param date of the Order
     * @param order that will be added
     * @throws FlooringOrdersPersistenceException if Order cannot be added
     * @throws OrdersDataValidationException if any fields of the order are
     * empty, null, or 0
     * @throws com.ka.flooringorders.service.DuplicateOrderNumberException if an
     * order with that id already exists
     */
    public void addOrder(LocalDate date, Order order) throws
            FlooringOrdersPersistenceException,
            OrdersDataValidationException,
            DuplicateOrderNumberException;

    /**
     * Retrieve an Order of that date with a specific order number.
     *
     * @param date of the Order
     * @param id, or order number, of the Order
     * @return the Order that is retrieved, or null if no Order is found.
     * @throws FlooringOrdersPersistenceException if Order cannot be retrieved
     * due to persistence errors.
     */
    public Order getOrder(LocalDate date, int id) throws
            FlooringOrdersPersistenceException;

    /**
     * Retrieves a list of Orders of that date.
     *
     * @param date of the Orders
     * @return a List of orders. List is empty if no values are found.
     * @throws FlooringOrdersPersistenceException
     */
    public List<Order> getOrdersByDate(LocalDate date) throws
            FlooringOrdersPersistenceException;

    /**
     * Edit an Order by removing the original Order and replacing it with the
     * edited Order.
     *
     * @param date of the order
     * @param changedOrder the Order that is changed
     * @return the changed order, or null if original order does not exist
     * @throws FlooringOrdersPersistenceException
     * @throws com.ka.flooringorders.service.OrdersDataValidationException
     */
    public Order editOrder(LocalDate date, Order changedOrder) throws
            FlooringOrdersPersistenceException,
            OrdersDataValidationException;

    /**
     * Remove an Order based on the date and given order number. Return null if
     * nothing was removed.
     *
     * @param date of the Order
     * @param id, or order number, of the order
     * @return the Order that was removed, or null if no order was found
     * @throws FlooringOrdersPersistenceException
     */
    public Order removeOrder(LocalDate date, int id) throws
            FlooringOrdersPersistenceException;

    /**
     * Export all data of the Orders in the application int one export file,
     * located in the Backup directory. Each Order includes the date that the
     * Order took place.
     *
     * @throws FlooringOrdersPersistenceException
     */
    public void exportAllData() throws FlooringOrdersPersistenceException;

    /**
     * Retrieve a State based on its name, whether the abbreviation or the
     * actual name of the State.
     *
     * @param name of the State, either the abbreviation or actual name
     * @return the State, or null if the State is not logged in.
     * @throws StatePersistenceException
     */
    public State getState(String name) throws StatePersistenceException;

    /**
     * Returns a list of all the States logged in the application.
     *
     * @return a List of States
     * @throws StatePersistenceException
     */
    public List<State> getAllStates() throws StatePersistenceException;

    /**
     * Retrieves the Product of the passed in String type. Null if no such
     * Product is logged in.
     *
     * @param type of the Product
     * @return the Product, or null if the Product is not found
     * @throws ProductPersistenceException
     */
    public Product getProduct(String type) throws ProductPersistenceException;

    /**
     * Returns a list of all the Products available in the application.
     *
     * @return a List of Products
     * @throws ProductPersistenceException
     */
    public List<Product> getAllProducts() throws ProductPersistenceException;

}
