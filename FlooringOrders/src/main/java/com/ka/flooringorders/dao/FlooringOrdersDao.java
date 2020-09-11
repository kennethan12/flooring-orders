/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author kennethan
 */
public interface FlooringOrdersDao {

    /**
     * Add an order to the temporary map of Orders, associated with the date the
     * Order had taken place. If successfully adds or already exists in the map,
     * return the Order. Return null otherwise.
     *
     * @param date of the Order
     * @param id of the Order
     * @param order itself
     * @return Order if added or already found. Return null otherwise
     * @throws FlooringOrdersPersistenceException
     */
    public Order addOrder(LocalDate date, int id, Order order) throws FlooringOrdersPersistenceException;

    /**
     * Retrieve an Order based on a given date and id. If the order is not
     * found, return null.
     *
     * @param date of the Order
     * @param id of the Order
     * @return Order with the given id, taken place on the given date. Null if
     * not found
     * @throws FlooringOrdersPersistenceException
     */
    public Order getOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException;

    /**
     * Return a sorted list of orders taken place on a certain date. Return null
     * if no such orders are found.
     *
     * @param date of the Orders
     * @return sorted List of Orders that had taken place on that date. Null if
     * not found
     * @throws FlooringOrdersPersistenceException
     */
    public List<Order> getOrdersByDate(LocalDate date) throws FlooringOrdersPersistenceException;

    /**
     * Remove an Order based on a given date and id, and return that Order. If
     * no such order is found, return null.
     *
     * @param date of the Order
     * @param id of the Order
     * @return the Order that is removed. Null if not found.
     * @throws FlooringOrdersPersistenceException
     */
    public Order removeOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException;

    /**
     * Export all the Orders into a DataExport.txt, located in the Backup
     * directory. It examines all the files in the Orders directory. It will not
     * take into account Orders that have been removed.
     *
     * @return true if export was successful, false if any Exceptions or
     * unsuccessful
     * @throws FlooringOrdersPersistenceException
     */
    public boolean exportAllOrders() throws FlooringOrdersPersistenceException;

    /**
     * A method that returns the next available id that would correspond to a
     * new Order.The value is stored in a separate file. If there is no id
     * found, just return 0.
     *
     * @param date of the order file
     * @return the next id available. 0 if no id can be read.
     * @throws com.ka.flooringorders.dao.FlooringOrdersPersistenceException if
     * an id is not found
     */
    public int getNextId(LocalDate date) throws FlooringOrdersPersistenceException;

    /**
     * A method that increments the next available id.This method would only be
     * called once it has been confirmed that the current int available has been
     * used for a new Order.
     *
     * @param date of the order file
     * @throws com.ka.flooringorders.dao.FlooringOrdersPersistenceException
     */
    public void incrementId(LocalDate date) throws FlooringOrdersPersistenceException;

}
