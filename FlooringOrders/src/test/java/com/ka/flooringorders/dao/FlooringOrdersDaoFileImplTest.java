/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersDaoFileImplTest {

    FlooringOrdersDao testDao;

    public FlooringOrdersDaoFileImplTest() {
    }

    @BeforeEach
    public void setUp() {
        String dataExportFile = "testBackUp/DataExport.txt";
        String ordersDirectory = "testOrders/";
        String idFile = "Data/test-id.txt";
        testDao = new FlooringOrdersDaoFileImpl(dataExportFile, ordersDirectory, idFile);
    }

    @AfterEach
    public void tearDown() {
        File deletingFile = new File("testOrders/Orders_01012020.txt");
        deletingFile.delete();
        deletingFile = new File("testOrders/Orders_01022020.txt");
        deletingFile.delete();
    }

    @Test
    public void testAddGetOrder() throws Exception {
        Order testOrder1 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder1.setCustomer("Elon, Musk");
        testOrder1.setState(new State("CA", new BigDecimal("25.00")));
        testOrder1.setProduct(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        testOrder1.setArea(new BigDecimal(200));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder1.getId(), testOrder1);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        Order testOrder2 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder2.setCustomer("Jeff Bezos");
        testOrder2.setState(new State("TX", new BigDecimal("4.45")));
        testOrder2.setProduct(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        testOrder2.setArea(new BigDecimal(350));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder2.getId(), testOrder2);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        assertNotEquals(testOrder1.getId(), testOrder2.getId());

        Order grabbedTestOrder1 = testDao.getOrder(LocalDate.parse("2020-01-01"), testOrder1.getId());
        Order grabbedTestOrder2 = testDao.getOrder(LocalDate.parse("2020-01-01"), testOrder2.getId());

        assertNotNull(grabbedTestOrder1, "Retrieved first Order should not be null.");
        assertNotNull(grabbedTestOrder2, "Retrieved second Order should not be null.");
        assertEquals(grabbedTestOrder1, testOrder1, "Retrieved first Order is not equal to original.");
        assertEquals(grabbedTestOrder2, testOrder2, "Retrieved second Order is not equal to original.");
        assertNotEquals(grabbedTestOrder1.getId(), grabbedTestOrder2.getId(), "The Id's of the two Orders should not be equal.");
    }

    @Test
    public void testAddGetAllOrders() throws Exception {
        Order testOrder1 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder1.setCustomer("Elon Musk");
        testOrder1.setState(new State("CA", new BigDecimal("25.00")));
        testOrder1.setProduct(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        testOrder1.setArea(new BigDecimal(200));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder1.getId(), testOrder1);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        Order testOrder2 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder2.setCustomer("Jeff Bezos");
        testOrder2.setState(new State("TX", new BigDecimal("4.45")));
        testOrder2.setProduct(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        testOrder2.setArea(new BigDecimal(350));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder2.getId(), testOrder2);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        List<Order> ordersList = testDao.getOrdersByDate(LocalDate.parse("2020-01-01"));

        assertNotNull(ordersList, "List of Orders should not be null.");
        assertEquals(2, ordersList.size(), "There should be 2 Orders in the list.");
        assertTrue(ordersList.contains(testOrder1), "The list should contain first Order.");
        assertTrue(ordersList.contains(testOrder2), "The list should contain second Order.");
    }

    @Test
    public void testRemoveOrder() throws Exception {
        Order testOrder1 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder1.setCustomer("Elon Musk");
        testOrder1.setState(new State("CA", new BigDecimal("25.00")));
        testOrder1.setProduct(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        testOrder1.setArea(new BigDecimal(200));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder1.getId(), testOrder1);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        Order testOrder2 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder2.setCustomer("Jeff Bezos");
        testOrder2.setState(new State("TX", new BigDecimal("4.45")));
        testOrder2.setProduct(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        testOrder2.setArea(new BigDecimal(350));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder2.getId(), testOrder2);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        Order removedOrder1 = testDao.removeOrder(LocalDate.parse("2020-01-01"), testOrder1.getId());

        assertEquals(removedOrder1, testOrder1, "First order should have been removed.");

        List<Order> ordersList = testDao.getOrdersByDate(LocalDate.parse("2020-01-01"));

        assertNotNull(ordersList, "List of orders should not be null.");
        assertEquals(1, ordersList.size(), "There should only be 1 Order in the list.");
        assertFalse(ordersList.contains(testOrder1), "First order should not be in the list anymore.");
        assertTrue(ordersList.contains(testOrder2), "Second order should still be in the list.");

        Order removedOrder2 = testDao.removeOrder(LocalDate.parse("2020-01-01"), testOrder2.getId());

        assertEquals(removedOrder2, testOrder2, "Second order should have been removed.");

        ordersList = testDao.getOrdersByDate(LocalDate.parse("2020-01-01"));

        assertTrue(ordersList.isEmpty(), "There should not be any orders in the list.");

        Order retrievedOrder1 = testDao.getOrder(LocalDate.parse("2020-01-01"), testOrder1.getId());
        assertNull(retrievedOrder1, "First order was removed, should be null.");

        Order retrievedOrder2 = testDao.getOrder(LocalDate.parse("2020-01-01"), testOrder2.getId());
        assertNull(retrievedOrder2, "Second order was removed, should be null.");
    }

    @Test
    public void testExportData() throws Exception {
        Order testOrder1 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder1.setCustomer("Elon Musk");
        testOrder1.setState(new State("CA", new BigDecimal("25.00")));
        testOrder1.setProduct(new Product("Carpet", new BigDecimal("2.25"), new BigDecimal("2.10")));
        testOrder1.setArea(new BigDecimal(200));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder1.getId(), testOrder1);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        Order testOrder2 = new Order(testDao.getNextId(LocalDate.parse("2020-01-01")));
        testOrder2.setCustomer("Jeff Bezos");
        testOrder2.setState(new State("TX", new BigDecimal("4.45")));
        testOrder2.setProduct(new Product("Laminate", new BigDecimal("1.75"), new BigDecimal("2.10")));
        testOrder2.setArea(new BigDecimal(350));

        testDao.addOrder(LocalDate.parse("2020-01-01"), testOrder2.getId(), testOrder2);
        testDao.incrementId(LocalDate.parse("2020-01-01"));

        Order testOrder3 = new Order(testDao.getNextId(LocalDate.parse("2020-01-02")));
        testOrder3.setCustomer("Tim Cook");
        testOrder3.setState(new State("KY", new BigDecimal("6.00")));
        testOrder3.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        testOrder3.setArea(new BigDecimal(264.52));

        testDao.addOrder(LocalDate.parse("2020-01-02"), testOrder3.getId(), testOrder3);
        testDao.incrementId(LocalDate.parse("2020-01-02"));

        boolean exportSuccess = testDao.exportAllOrders();

        assertTrue(exportSuccess, "Export should have been successful.");
    }

    @Test
    public void testGetOrdersOfInvalidDate() throws Exception {
        List<Order> ordersList = testDao.getOrdersByDate(LocalDate.parse("2020-01-03"));

        assertTrue(ordersList.isEmpty());
    }
}
