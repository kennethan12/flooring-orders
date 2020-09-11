/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.controller;

import com.ka.flooringorders.dao.FlooringOrdersPersistenceException;
import com.ka.flooringorders.dao.ProductPersistenceException;
import com.ka.flooringorders.dao.StatePersistenceException;
import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import com.ka.flooringorders.service.DuplicateOrderNumberException;
import com.ka.flooringorders.service.FlooringOrdersService;
import com.ka.flooringorders.service.OrdersDataValidationException;
import com.ka.flooringorders.ui.FlooringOrdersView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersController {

    FlooringOrdersService service;
    FlooringOrdersView view;

    /**
     * Instantiate the controller with implementations of the service layer and
     * the view of the program.
     *
     * @param service The Service Layer of the program
     * @param view The View of the program
     */
    public FlooringOrdersController(FlooringOrdersService service, FlooringOrdersView view) {
        this.service = service;
        this.view = view;
    }

    /**
     * This runs the entire application.
     */
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {
                menuSelection = getSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }

            exitMessage();
        } catch (FlooringOrdersPersistenceException
                | StatePersistenceException
                | ProductPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Get user's selection from the menu.
     *
     * @return the user's selection
     */
    public int getSelection() {
        return view.printMenuAndGetSelection();
    }

    /**
     * Display orders of a date or display a specific order.
     *
     * @throws FlooringOrdersPersistenceException
     */
    public void displayOrders() throws FlooringOrdersPersistenceException {
        int choice = view.chooseDisplayByDateOrSpecific();

        LocalDate date = view.getDateOfSearchingOrder();
        switch (choice) {
            case 1:
                List<Order> orders = service.getOrdersByDate(date);
                view.displayOrderListInfo(orders);
                break;
            case 2:
                int id = view.getOrderNumber();
                Order order = service.getOrder(date, id);
                view.displayOrderInfo(order);
                break;
            default:
                view.displayErrorMessage("This should not happen.");
        }

        view.returnToMenu();
    }

    /**
     * Add an order.
     *
     * @throws FlooringOrdersPersistenceException
     * @throws StatePersistenceException
     * @throws ProductPersistenceException
     */
    public void addOrder() throws
            FlooringOrdersPersistenceException,
            StatePersistenceException,
            ProductPersistenceException {
        view.displayAddOrderBanner();

        // Get date of the new order
        LocalDate date = view.getDateOfAddingOrder();

        // Get customer name
        String customer = view.getCustomerName();

        // Get state
        List<State> states = service.getAllStates();
        view.displayStates(states);
        State state = null;
        do {
            String stateChoice = view.getState();
            state = service.getState(stateChoice);
            if (state == null) {
                view.displayStateError();
            }
        } while (state == null);

        // Get product
        List<Product> products = service.getAllProducts();
        view.displayProducts(products);
        Product product = null;
        do {
            String productChoice = view.getProduct();
            product = service.getProduct(productChoice);
            if (product == null) {
                view.displayProductError();
            }
        } while (product == null);

        // Get area
        BigDecimal area = view.getArea();

        Order possibleOrder = new Order(service.getNextId(date));
        possibleOrder.setCustomer(customer);
        possibleOrder.setState(state);
        possibleOrder.setProduct(product);
        possibleOrder.setArea(area);

        // Confirm order
        boolean confirmOrder = view.confirmAddOrder(possibleOrder);
        if (confirmOrder) {
            try {
                service.addOrder(date, possibleOrder);
                view.displayAddOrderSuccess(possibleOrder.getId());
            } catch (OrdersDataValidationException | DuplicateOrderNumberException e) {
                view.displayErrorMessage(e.getMessage());
                view.displayOrderCancel();
            }
        } else {
            view.displayOrderCancel();
        }

        view.returnToMenu();
    }

    /**
     * Edit an order.
     *
     * @throws FlooringOrdersPersistenceException
     * @throws StatePersistenceException
     * @throws ProductPersistenceException
     */
    public void editOrder() throws
            FlooringOrdersPersistenceException,
            StatePersistenceException,
            ProductPersistenceException {
        view.displayEditOrderBanner();

        LocalDate date = view.getDateOfSearchingOrder();
        int id = view.getOrderNumber();
        Order editingOrder = service.getOrder(date, id);

        // Only edit if that order exists
        if (editingOrder != null) {
            // Display order and store its values in appropriate variables
            view.displayOrderInfo(editingOrder);
            String newCustomer = editingOrder.getCustomer();
            State newState = editingOrder.getState();
            Product newProduct = editingOrder.getProduct();
            BigDecimal newArea = editingOrder.getArea();

            boolean isDoneEditing = false;
            do {
                int choice = view.printEditingMenuAndGetSelection();

                switch (choice) {
                    case 0: // Quit
                        isDoneEditing = true;
                        break;
                    case 1: // Edit Customer
                        newCustomer = view.editCustomerName(newCustomer);
                        view.displayEditCustomerSuccess();
                        break;
                    case 2: // Edit State
                        List<State> states = service.getAllStates();
                        view.displayStates(newState, states);
                        State state = null;
                        do {
                            String stateChoice = view.getState();
                            state = service.getState(stateChoice);
                            if (state == null) {
                                view.displayStateError();
                            }
                        } while (state == null);
                        newState = state;
                        view.displayEditStateSuccess();
                        break;
                    case 3: // Edit Product
                        List<Product> products = service.getAllProducts();
                        view.displayProducts(newProduct, products);
                        Product product = null;
                        do {
                            String productChoice = view.getProduct();
                            product = service.getProduct(productChoice);
                            if (product == null) {
                                view.displayProductError();
                            }
                        } while (product == null);
                        newProduct = product;
                        view.displayEditProductSuccess();
                        break;
                    case 4: // Edit Area
                        newArea = view.editArea(newArea);
                        view.displayEditAreaSuccess();
                        break;
                    default:
                        view.displayErrorMessage("This should never happen.");
                }
            } while (!isDoneEditing);

            // Display updated Order
            Order changedOrder = new Order(editingOrder.getId());
            changedOrder.setCustomer(newCustomer);
            changedOrder.setState(newState);
            changedOrder.setProduct(newProduct);
            changedOrder.setArea(newArea);

            // Confirm that changes are to be made
            boolean confirmChange = view.confirmEditOrder(changedOrder);
            if (confirmChange) {
                try {
                    // Edit order
                    service.editOrder(date, changedOrder);
                    view.displayEditOrderSuccess(changedOrder.getId());
                } catch (OrdersDataValidationException e) {
                    view.displayErrorMessage(e.getMessage());
                    view.displayUpdateCancel();
                }
            } else {
                view.displayUpdateCancel();
            }
        } else {
            // There is no original order
            view.displayOrderNotFound();
        }

        view.returnToMenu();
    }

    /**
     * Remove an order.
     *
     * @throws FlooringOrdersPersistenceException
     */
    public void removeOrder() throws FlooringOrdersPersistenceException {
        view.displayRemoveOrderBanner();

        LocalDate date = view.getDateOfSearchingOrder();
        int id = view.getOrderNumber();
        Order removingOrder = service.getOrder(date, id);

        if (removingOrder != null) {
            boolean confirmRemove = view.confirmRemoveOrder(removingOrder);
            if (confirmRemove) {
                service.removeOrder(date, id);
                view.displayRemoveOrderSuccess(removingOrder.getId());
            } else {
                view.displayRemoveCancel();
            }
        } else {
            view.displayOrderNotFound();
        }

        view.returnToMenu();
    }

    /**
     * Export data of all the orders in the program.
     *
     * @throws FlooringOrdersPersistenceException
     */
    public void exportAllData() throws FlooringOrdersPersistenceException {
        service.exportAllData();
        view.displayExportSuccess();
        view.returnToMenu();
    }

    /**
     * Display unknown command.
     */
    public void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    /**
     * Display exit message.
     */
    public void exitMessage() {
        view.displayExitBanner();
    }
}
