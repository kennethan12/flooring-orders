/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.ui;

import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersView {

    UserIO io;

    /**
     * Instantiate the view using the UserIO.
     *
     * @param io that prints Strings and takes in inputs.
     */
    public FlooringOrdersView(UserIO io) {
        this.io = io;
    }

    /**
     * Displays the main menu and gets the user's choice.
     *
     * @return int that represents user's choice
     */
    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("*  <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Choose from 1-6: ", 1, 6);
    }

    /**
     * Displays the two different options (display orders of a date or display a
     * specific order) when user chooses to display orders. It returns the
     * user's choice.
     *
     * @return int that represents the user's choice.
     */
    public int chooseDisplayByDateOrSpecific() {
        io.print("1. Display orders of a date");
        io.print("2. Display specific order");

        return io.readInt("Choose 1 or 2: ", 1, 2);
    }

    /**
     * Ask user for a date and return the LocalDate form of the input. If the
     * user enters an invalid String and the DateTimeParseException is caught,
     * it will ask the user to enter a proper date.
     *
     * @return LocalDate of the user's inputted date.
     */
    public LocalDate getDateOfSearchingOrder() {
        boolean isValid = false;
        LocalDate ld = LocalDate.MIN;
        do {
            try {
                String dateString = io.readString("Enter order date (MM/DD/YYYY): ");
                ld = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                isValid = true;
            } catch (DateTimeParseException e) {
                io.print("Please enter a proper date.");
            }
        } while (!isValid);

        return ld;
    }

    /**
     * Get the order number of the order that the user is searching for.
     *
     * @return int
     */
    public int getOrderNumber() {
        return io.readInt("Enter order number: ");
    }

    /**
     * Display an order's information. If the order is null, display an error
     * message.
     *
     * @param order that will be displayed
     */
    public void displayOrderInfo(Order order) {
        if (order == null) {
            io.print("\nERROR: No such order exists.");
        } else {
            io.print("\nOrder #" + order.getId());
            io.print("Customer: " + order.getCustomer());
            io.print("State: " + order.getState().getAbbrev());
            io.print("Tax Rate: " + order.getState().getTaxRate() + "%");
            io.print("Product: " + order.getProduct().getType());
            io.print("Area: " + order.getArea() + " sq ft");
            io.print("Cost per sq ft: $" + order.getProduct().getCostPerSqFt());
            io.print("Labor Cost per sq ft: $" + order.getProduct().getLaborCostPerSqFt());
            io.print("Material Cost: $" + order.getMaterialCost());
            io.print("Labor Cost: $" + order.getLaborCost());
            io.print("Tax: $" + order.getTax());
            io.print("Total: $" + order.getTotal());
        }
    }

    /**
     * Display a list of orders, with each order's information displayed. If the
     * List of Orders is empty, display an error message.
     *
     * @param orders List of Orders that will be displayed.
     */
    public void displayOrderListInfo(List<Order> orders) {
        if (orders.isEmpty()) {
            io.print("ERROR: No orders took place on that date.");
        } else {
            orders.forEach(order -> {
                displayOrderInfo(order);
            });
        }
    }

    /**
     * Display an "Add Order" banner.
     */
    public void displayAddOrderBanner() {
        io.print("ADDING A NEW ORDER:\n");
    }

    /**
     * Have the user enter the date of the new order and return the LocalDate
     * version of that date. If the user enters a date that is before today, it
     * is invalid and thus the user must enter an appropriate date.
     *
     * @return LocalDate of the new order.
     */
    public LocalDate getDateOfAddingOrder() {
        boolean isValid = false;
        LocalDate ld = LocalDate.MIN;
        do {
            try {
                String dateString = io.readString("Enter order date (MM/DD/YYYY): ");
                ld = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if (ld.equals(LocalDate.now()) || ld.isAfter(LocalDate.now())) {
                    isValid = true;
                } else {
                    io.print("You cannot place orders before today. Please enter another date.");
                }
            } catch (DateTimeParseException e) {
                io.print("Please enter a proper date.");
            }
        } while (!isValid);

        return ld;
    }

    /**
     * Get the customer name from the user.
     *
     * @return String customer
     */
    public String getCustomerName() {
        return io.readString("Enter customer name: ");
    }

    /**
     * Display the available states in the flooring orders program.
     *
     * @param states List of States that are available.
     */
    public void displayStates(List<State> states) {
        io.print("Available states: ");
        states.forEach(state -> {
            io.print("- " + state.getAbbrev() + " (" + state.getName() + ")");
        });
    }

    /**
     * Get the name/abbreviation of the state from the user.
     *
     * @return String name of state.
     */
    public String getState() {
        return io.readString("Enter a state: ");
    }

    /**
     * Display "state not available" if user enters a state that is not
     * available in the program.
     */
    public void displayStateError() {
        io.print("The state you have entered is not available. Please choose another.");
    }

    /**
     * Display the available products in the program.
     *
     * @param products List of Products that are available.
     */
    public void displayProducts(List<Product> products) {
        io.print("Available products: ");
        products.forEach(product -> {
            io.print("- " + product.getType() + ": $" + product.getCostPerSqFt()
                    + " (labor $" + product.getLaborCostPerSqFt()
                    + ") per sq ft");
        });
    }

    /**
     * Get the type of product from the user.
     *
     * @return String type of product.
     */
    public String getProduct() {
        return io.readString("Enter a product: ");
    }

    /**
     * Display "product not available" if user enters a product type that is not
     * available in the program.
     */
    public void displayProductError() {
        io.print("The product you have entered is not available. Please choose another.");
    }

    /**
     * Get the area of new order from the user. If the area is less than a 100
     * square feet or the input cannot be parsed, the user must enter an
     * appropriate number to move on.
     *
     * @return BigDecimal area
     */
    public BigDecimal getArea() {
        boolean isValid = false;
        String areaString;
        BigDecimal area = null;
        do {
            try {
                areaString = io.readString("Enter area (must be at least 100 sq ft): ");
                area = new BigDecimal(areaString).setScale(2, RoundingMode.HALF_UP);
                if (area.compareTo(new BigDecimal(100)) < 0) {
                    io.print("Area must be at least 100 sq ft.");
                } else {
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                io.print("Please enter numbers and decimals only.");
            }
        } while (!isValid);
        return area;
    }

    /**
     * Confirm with the user whether they want to add the order they have
     * created.
     *
     * @param possibleOrder that the user has created
     * @return whether the user has confirmed or not
     */
    public boolean confirmAddOrder(Order possibleOrder) {
        boolean response = false;
        boolean isValid = false;
        io.print("Your order: ");
        displayOrderInfo(possibleOrder);
        String confirm = io.readString("\nPlace order? (y/n): ");
        do {
            if (confirm.equalsIgnoreCase("y")) {
                response = true;
                isValid = true;
            } else if (confirm.equalsIgnoreCase("n")) {
                isValid = true;
            } else {
                io.print("Please enter y or n.");
            }
        } while (!isValid);
        return response;
    }

    /**
     * Display a banner that shows the addition of the order was successful.
     *
     * @param id of the order that has been added.
     */
    public void displayAddOrderSuccess(int id) {
        io.print("Order #" + id + " successfully added.");
    }

    /**
     * Display a banner that shows the order the user has created has been
     * cancelled.
     */
    public void displayOrderCancel() {
        io.print("Order cancelled.");
    }

    /**
     * Display an "Edit Order" banner.
     */
    public void displayEditOrderBanner() {
        io.print("EDITING AN ORDER:\n");
    }

    /**
     * Display the menu of what the user can edit about an order: customer,
     * state, product, and area. The user can also exit if they are done
     * editing.
     *
     * @return int choice that the user has made
     */
    public int printEditingMenuAndGetSelection() {
        io.print("Can edit the following: ");
        io.print("1. Customer name");
        io.print("2. State");
        io.print("3. Product");
        io.print("4. Area");

        return io.readInt("Choose 1-4, or 0 if you are done.", 0, 4);
    }

    /**
     * Display an order's current customer and return the user's inputted new
     * customer.
     *
     * @param previousCustomer of the order that the user is editing
     * @return the order's new customer
     */
    public String editCustomerName(String previousCustomer) {
        io.print("Current customer: " + previousCustomer);
        return io.readString("Enter new customer name: ");
    }

    /**
     * Display that the customer has been edited.
     */
    public void displayEditCustomerSuccess() {
        io.print("Successfully changed customer.");
    }

    /**
     * Display the current state of the order the user is editing, and display
     * the list of states available.
     *
     * @param previousState current state of the order being edited
     * @param states List of states available
     */
    public void displayStates(State previousState, List<State> states) {
        io.print("Current state: " + previousState.getAbbrev());
        if (!states.contains(previousState)) {
            io.print("NOTE: " + previousState.getAbbrev() + " is not available anymore.");
        }
        displayStates(states);
    }

    /**
     * Display that the state has been edited.
     */
    public void displayEditStateSuccess() {
        io.print("Successfully changed states.");
    }

    /**
     * Display the current product of the order being edited, and display the
     * list of products available.
     *
     * @param previousProduct current product of the order
     * @param products List of products available
     */
    public void displayProducts(Product previousProduct, List<Product> products) {
        io.print("Current product: " + previousProduct.getType());
        if (!products.contains(previousProduct)) {
            io.print("NOTE: " + previousProduct.getType() + " is not available anymore.");
        }
        displayProducts(products);
    }

    /**
     * Display that the product has been edited.
     */
    public void displayEditProductSuccess() {
        io.print("Successfully changed products.");
    }

    /**
     * Display the current area of the order being edited, and get the order's
     * new area.
     *
     * @param previousArea current area of the order
     * @return new BigDecimal area
     */
    public BigDecimal editArea(BigDecimal previousArea) {
        io.print("Current area: " + previousArea + " sq ft");
        return getArea();
    }

    /**
     * Display that the area has been edited.
     */
    public void displayEditAreaSuccess() {
        io.print("Successfully changed area.");
    }

    /**
     * Confirm with the user whether they want to commit the edits.
     *
     * @param changedOrder the order that has been edited
     * @return true if the user confirms, false if not.
     */
    public boolean confirmEditOrder(Order changedOrder) {
        boolean response = false;
        boolean isValid = false;
        io.print("Your updated order: ");
        displayOrderInfo(changedOrder);
        String confirm = io.readString("\nEdit order? (y/n): ");
        do {
            if (confirm.equalsIgnoreCase("y")) {
                response = true;
                isValid = true;
            } else if (confirm.equalsIgnoreCase("n")) {
                isValid = true;
            } else {
                io.print("Please enter y or n.");
            }
        } while (!isValid);
        return response;
    }

    /**
     * Display that the order has been successfully updated.
     *
     * @param id of the order that has been edited
     */
    public void displayEditOrderSuccess(int id) {
        io.print("Order #" + id + " successfully edited.");
    }

    /**
     * Display that the edit has been cancelled.
     */
    public void displayUpdateCancel() {
        io.print("Update cancelled.");
    }

    /**
     * Display "Remove Order" banner.
     */
    public void displayRemoveOrderBanner() {
        io.print("REMOVE AN ORDER:\n");
    }

    /**
     * Confirm that the user wants to remove the order.
     *
     * @param removingOrder the order that is being removed
     * @return true if the user confirms, false if not
     */
    public boolean confirmRemoveOrder(Order removingOrder) {
        boolean response = false;
        boolean isValid = false;
        displayOrderInfo(removingOrder);
        String confirm = io.readString("Do you wish to remove this order? (y/n): ");
        do {
            if (confirm.equalsIgnoreCase("y")) {
                do {
                    // Ask again!
                    confirm = io.readString("Are you sure? (y/n): ");
                    if (confirm.equalsIgnoreCase("y")) {
                        response = true;
                        isValid = true;
                    } else if (confirm.equalsIgnoreCase("n")) {
                        isValid = true;
                    } else {
                        io.print("Please enter y or n.");
                    }
                } while (!isValid);
            } else if (confirm.equalsIgnoreCase("n")) {
                isValid = true;
            } else {
                io.print("Please enter y or n.");
            }
        } while (!isValid);

        return response;
    }

    /**
     * Display that the order has been successfully removed.
     *
     * @param id of the order removed
     */
    public void displayRemoveOrderSuccess(int id) {
        io.print("Order #" + id + " successfully removed.");
    }

    /**
     * Display that the remove has been cancelled.
     */
    public void displayRemoveCancel() {
        io.print("Remove cancelled.");
    }

    /**
     * Display that the export of all data in the program is successful.
     */
    public void displayExportSuccess() {
        io.print("Data successfully exported.");
    }

    /**
     * Display that the order the user is looking for is not found.
     */
    public void displayOrderNotFound() {
        io.print("ERROR: No such order exists.");
    }

    /**
     * Asks the user to hit enter to return to the main menu.
     */
    public void returnToMenu() {
        io.readString("\nPlease hit enter to return to menu.");
    }

    /**
     * Displays that the command is unknown.
     */
    public void displayUnknownCommandBanner() {
        io.print("Unknown Command.");
    }

    /**
     * Displays the exit banner.
     */
    public void displayExitBanner() {
        io.print("Good bye!");
    }

    /**
     * Displays the error message of the exception caught.
     *
     * @param errorMsg of the exception
     */
    public void displayErrorMessage(String errorMsg) {
        io.print(errorMsg);
    }
}
