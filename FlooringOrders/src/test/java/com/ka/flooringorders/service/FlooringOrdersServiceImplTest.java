/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.service;

import com.ka.flooringorders.dao.FlooringOrdersPersistenceException;
import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersServiceImplTest {

    private FlooringOrdersService service;

    public FlooringOrdersServiceImplTest() {
        // Run by Spring dependency injection.
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", FlooringOrdersService.class);
    }

    @Test
    public void testAddValidOrder() {
        try {
            Order newOrder = new Order(2);
            newOrder.setCustomer("Elon Musk");
            newOrder.setState(new State("CA", new BigDecimal("25.00")));
            newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
            newOrder.setArea(new BigDecimal("200.00"));

            service.addOrder(LocalDate.parse("2020-01-01"), newOrder);
        } catch (FlooringOrdersPersistenceException
                | OrdersDataValidationException
                | DuplicateOrderNumberException e) {
            fail("Order was valid, no exception should have been thrown.");
        }
    }

    @Test
    public void testAddInvalidCustomerOrder() {
        try {
            Order newOrder = new Order(2);
            newOrder.setCustomer("");
            newOrder.setState(new State("CA", new BigDecimal("25.00")));
            newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
            newOrder.setArea(new BigDecimal("200.00"));

            service.addOrder(LocalDate.parse("2020-01-01"), newOrder);
            fail("An exception should have been thrown.");
        } catch (FlooringOrdersPersistenceException | DuplicateOrderNumberException e) {
            fail("Wrong exception was thrown.");
        } catch (OrdersDataValidationException e) {
            return;
        }
    }

    @Test
    public void testAddInvalidStateOrder() {
        try {
            Order newOrder = new Order(2);
            newOrder.setCustomer("Elon Musk");
            newOrder.setState(null);
            newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
            newOrder.setArea(new BigDecimal("200.00"));

            service.addOrder(LocalDate.parse("2020-01-01"), newOrder);
            fail("An exception should have been thrown.");
        } catch (FlooringOrdersPersistenceException | DuplicateOrderNumberException e) {
            fail("Wrong exception was thrown.");
        } catch (OrdersDataValidationException e) {
            return;
        }
    }

    @Test
    public void testAddInvalidProductOrder() {
        try {
            Order newOrder = new Order(2);
            newOrder.setCustomer("Elon Musk");
            newOrder.setState(new State("CA", new BigDecimal("25.00")));
            newOrder.setProduct(null);
            newOrder.setArea(new BigDecimal("200.00"));

            service.addOrder(LocalDate.parse("2020-01-01"), newOrder);
            fail("An exception should have been thrown.");
        } catch (FlooringOrdersPersistenceException | DuplicateOrderNumberException e) {
            fail("Wrong exception was thrown.");
        } catch (OrdersDataValidationException e) {
            return;
        }
    }

    @Test
    public void testAddInvalidAreaOrder() {
        try {
            Order newOrder = new Order(2);
            newOrder.setCustomer("Elon Musk");
            newOrder.setState(new State("CA", new BigDecimal("25.00")));
            newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
            newOrder.setArea(new BigDecimal("99.00"));

            service.addOrder(LocalDate.parse("2020-01-01"), newOrder);
            fail("An exception should have been thrown.");
        } catch (FlooringOrdersPersistenceException | DuplicateOrderNumberException e) {
            fail("Wrong exception was thrown.");
        } catch (OrdersDataValidationException e) {
            return;
        }
    }

    @Test
    public void testGetOrdersByDate() throws Exception {
        Order newOrder = new Order(service.getNextId(LocalDate.parse("2020-01-01")));
        newOrder.setCustomer("Elon Musk");
        newOrder.setState(new State("CA", new BigDecimal("25.00")));
        newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        newOrder.setArea(new BigDecimal("200.00"));

        assertEquals(1, service.getOrdersByDate(LocalDate.parse("2020-01-01")).size(),
                "There should only be 1 test in the list.");
        assertTrue(service.getOrdersByDate(LocalDate.parse("2020-01-01")).contains(newOrder),
                "The new order should be in the list.");

        assertTrue(service.getOrdersByDate(LocalDate.parse("2020-01-02")).isEmpty(),
                "There should not be a list of orders for 2020-01-02.");
    }

    @Test
    public void testGetOrder() throws Exception {
        Order newOrder = new Order(service.getNextId(LocalDate.parse("2020-01-01")));
        newOrder.setCustomer("Elon Musk");
        newOrder.setState(new State("CA", new BigDecimal("25.00")));
        newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        newOrder.setArea(new BigDecimal("200.00"));

        Order retrievedOrder = service.getOrder(LocalDate.parse("2020-01-01"), newOrder.getId());
        assertNotNull(retrievedOrder, "Retrieved Order should not be null.");
        assertEquals(retrievedOrder, newOrder);

        Order shouldBeNull = service.getOrder(LocalDate.parse("2020-01-01"), newOrder.getId() + 1);
        assertNull(shouldBeNull, "There should not be an order of Order #2.");
    }

    @Test
    public void testRemoveOrder() throws Exception {
        Order newOrder = new Order(service.getNextId(LocalDate.parse("2020-01-01")));
        newOrder.setCustomer("Elon Musk");
        newOrder.setState(new State("CA", new BigDecimal("25.00")));
        newOrder.setProduct(new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15")));
        newOrder.setArea(new BigDecimal("200.00"));

        Order removedOrder = service.removeOrder(LocalDate.parse("2020-01-01"), newOrder.getId());
        assertNotNull(removedOrder, "Removing Order #1 should not be null.");
        assertEquals(removedOrder, newOrder, "Removed Order should be our newOrder.");

        Order shouldBeNull = service.removeOrder(LocalDate.parse("2020-01-02"), newOrder.getId() + 1);
        assertNull(shouldBeNull, "There should not exist such order and cannot be removed.");
    }
}
