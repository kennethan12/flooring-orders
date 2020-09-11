/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.Order;
import com.ka.flooringorders.dto.Product;
import com.ka.flooringorders.dto.State;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersDaoFileImpl implements FlooringOrdersDao {

    private Map<Integer, Order> orders = new HashMap<>();
    private String EXPORT_FILE;
    private String ORDERS_FILE_HEAD;
    private String ORDERS_DIRECTORY;
    private String FILE_TAIL;
    private String DELIMITER = ",";
    private String REPLACEMENT = "#";
    private String ID_FILE;
    private int NUMBER_OF_FIELDS = 12;

    public FlooringOrdersDaoFileImpl() {
        this.EXPORT_FILE = "Backup/DataExport.txt";
        this.ORDERS_DIRECTORY = "Orders/";
        this.ORDERS_FILE_HEAD = "Orders_";
        this.FILE_TAIL = ".txt";
        this.ID_FILE = "Data/id.txt";
    }

    public FlooringOrdersDaoFileImpl(String dataExportFile, String ordersDirectory, String idFile) {
        this.EXPORT_FILE = dataExportFile;
        this.ORDERS_DIRECTORY = ordersDirectory;
        this.ORDERS_FILE_HEAD = "Orders_";
        this.FILE_TAIL = ".txt";
        this.ID_FILE = idFile;
    }

    @Override
    public Order addOrder(LocalDate date, int id, Order order) throws FlooringOrdersPersistenceException {
        loadByDate(date);
        Order addedOrder = orders.put(id, order);
        writeByDate(date);
        return addedOrder;
    }

    @Override
    public Order getOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException {
        loadByDate(date);
        return orders.get(id);
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) throws FlooringOrdersPersistenceException {
        loadByDate(date);
        List<Order> ordersList = new ArrayList(orders.values());
        ordersList.sort(Comparator.comparingInt(Order::getId));
        return ordersList;
    }

    @Override
    public Order removeOrder(LocalDate date, int id) throws FlooringOrdersPersistenceException {
        loadByDate(date);
        Order removedOrder = orders.remove(id);
        writeByDate(date);
        return removedOrder;
    }

    @Override
    public boolean exportAllOrders() throws FlooringOrdersPersistenceException {
        boolean hasExported = false;

        try {
            // Make a new File object using the Directory
            File directory = new File(ORDERS_DIRECTORY);

            // Proceed if the File is a directory
            if (directory.isDirectory()) {

                // Set up the export file
                PrintWriter out;
                try {
                    out = new PrintWriter(new FileWriter(EXPORT_FILE));
                } catch (IOException e) {
                    throw new FlooringOrdersPersistenceException("Could not load export file.", e);
                }

                // Insert the header
                String header = "OrderNumber,CustomerName,State,TaxRate,"
                        + "ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,"
                        + "MaterialCost,LaborCost,Tax,Total,OrderDate";
                out.println(header);
                out.flush();

                // Get all the files in the directory
                String[] orderFiles = directory.list();

                if (orderFiles != null) {
                    Arrays.sort(orderFiles);
                    // For each file, strip down the name so that only the date portion remains
                    for (String fileName : orderFiles) {
                        // Make sure it's not a hidden file
                        if (fileName.startsWith(".")) {
                            continue;
                        }

                        // Put each order into the export file. If the String date cannot be
                        // parsed, go to the next file.
                        try {
                            String dateValue = fileName.replace(ORDERS_FILE_HEAD, "").replace(FILE_TAIL, "");

                            // Parse the date portion to an actual LocalDate object
                            LocalDate ld = LocalDate.parse(dateValue, DateTimeFormatter.ofPattern("MMddyyyy"));

                            // Re-format to fit the appropriate date format in the export file
                            String formattedDate = ld.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

                            // Get the Orders of that LocalDate and place them into export file
                            // Add the formatted date at the end of each Order
                            List<Order> ordersList = getOrdersByDate(ld);
                            String orderAsText;
                            for (Order order : ordersList) {
                                orderAsText = marshallOrder(order) + DELIMITER + formattedDate;
                                out.println(orderAsText);
                                out.flush();
                            }
                        } catch (DateTimeParseException e) {

                        }
                    }

                    hasExported = true;
                    out.close();
                }
            }
        } catch (FlooringOrdersPersistenceException e) {
            throw new FlooringOrdersPersistenceException("Files cannot be exported.", e);
        } finally {
            return hasExported;
        }

    }

    @Override
    public int getNextId(LocalDate date) throws FlooringOrdersPersistenceException {
        loadByDate(date);
        int nextId = 0;
        Set<Integer> ids = orders.keySet();
        if (!ids.isEmpty()) {
            nextId = Collections.max(ids);
        }
        return nextId + 1;

//        Scanner scanner;
//
//        try {
//            scanner = new Scanner(new BufferedReader(new FileReader(ID_FILE)));
//        } catch (FileNotFoundException e) {
//            throw new FlooringOrdersPersistenceException("Cannot retrieve new id.", e);
//        }
//
//        int id = 0;
//        if (scanner.hasNextLine()) {
//            id = Integer.parseInt(scanner.nextLine());
//        }
//
//        scanner.close();
//        return id;
    }

    @Override
    public void incrementId(LocalDate date) throws FlooringOrdersPersistenceException {
        int currentId = getNextId(date);

        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(ID_FILE));
        } catch (IOException e) {
            throw new FlooringOrdersPersistenceException("Could not save id data.", e);
        }

        out.println(currentId + 1);
        out.flush();
        out.close();
    }

    /**
     * Marshall Order object into a String to store in an Orders file. Attach
     * between each property our DELIMITER, which is ",".
     *
     * @param order Order object that will be marshalled
     * @return a String of marshalled Order
     */
    private String marshallOrder(Order order) {
        String orderAsText = order.getId() + DELIMITER;
        orderAsText += replaceControlCharacters(order.getCustomer()) + DELIMITER;
        orderAsText += order.getState().getAbbrev() + DELIMITER;
        orderAsText += order.getState().getTaxRate() + DELIMITER;
        orderAsText += order.getProduct().getType() + DELIMITER;
        orderAsText += order.getArea() + DELIMITER;
        orderAsText += order.getProduct().getCostPerSqFt() + DELIMITER;
        orderAsText += order.getProduct().getLaborCostPerSqFt() + DELIMITER;
        orderAsText += order.getMaterialCost() + DELIMITER;
        orderAsText += order.getLaborCost() + DELIMITER;
        orderAsText += order.getTax() + DELIMITER;
        orderAsText += order.getTotal();

        return orderAsText;
    }

    /**
     * Replaces DELIMITER with REPLACEMENT if DELIMITER is present in the
     * inputted String value.
     *
     * @param value String that will have any DELIMITERs replaced
     * @return String with REPLACEMENT if it had DELIMITERs
     */
    private String replaceControlCharacters(String value) {
        return value.replace(DELIMITER, REPLACEMENT).replace("\"", "");
    }

    /**
     * Unmarshall each Order. Its String entry in the file includes:
     * OrderNumber, CustomerName, State, TaxRate, ProductType, Area,
     * CostPerSquareFoot, LaborCostPerSquareFoot, MaterialCost, LaborCost,
     * Tax,and Total.
     *
     * @param orderAsText
     * @return
     */
    private Order unmarshallOrder(String orderAsText, Pattern regexPattern) {
        // If customer is in quotation marks, find it, replace any DELIMITERS,
        // and place the edited customer back into the original String
        Matcher matcher = regexPattern.matcher(orderAsText);
        if (matcher.find()) {
            String customer = matcher.group(1);
            String correctCustomer = replaceControlCharacters(customer);
            orderAsText = orderAsText.replace("\"" + customer + "\"", correctCustomer);
        }

        String[] orderTokens = orderAsText.split(DELIMITER);

        try {
            if (orderTokens.length == NUMBER_OF_FIELDS) {
                // Set id
                int id = Integer.parseInt(orderTokens[0]);
                Order order = new Order(id);

                // Set customer
                order.setCustomer(placeControlCharactersBack(orderTokens[1]));

                // Set State
                String abbrev = orderTokens[2];
                BigDecimal taxRate = new BigDecimal(orderTokens[3]);
                if (taxRate.compareTo(BigDecimal.ZERO) < 0) {
                    return null;
                }
                State state = new State(abbrev, taxRate);
                order.setState(state);

                // Set Product
                String type = orderTokens[4];
                BigDecimal costPerSqFt = new BigDecimal(orderTokens[6]);
                BigDecimal laborCostPerSqFt = new BigDecimal(orderTokens[7]);
                if (costPerSqFt.compareTo(BigDecimal.ZERO) < 0
                        || laborCostPerSqFt.compareTo(BigDecimal.ZERO) < 0) {
                    return null;
                }
                Product product = new Product(type, costPerSqFt, laborCostPerSqFt);
                order.setProduct(product);

                // Set area
                BigDecimal area = new BigDecimal(orderTokens[5]);
                if (area.compareTo(BigDecimal.ZERO) < 0) {
                    return null;
                }
                order.setArea(area);

                return order;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }

    }

    /**
     * Replaces REPLACEMENT with DELIMITER if REPLACEMENT is present in the
     * inputted String value.
     *
     * @param value String that will have any REPLACEMENTs replaced
     * @return String with DELIMITER if it had REPLACEMENTs
     */
    private String placeControlCharactersBack(String value) {
        return value.replace(REPLACEMENT, DELIMITER);
    }

    /**
     * Write the Orders in the Map into the appropriate file based on the
     * LocalDate given.
     *
     * @param date of the Orders. This is used to find the appropriate file
     * @throws FlooringOrdersPersistenceException if the data cannot be saved to
     * a file
     */
    private void writeByDate(LocalDate date) throws FlooringOrdersPersistenceException {
        String dateValue = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName = ORDERS_DIRECTORY + ORDERS_FILE_HEAD + dateValue + FILE_TAIL;

        PrintWriter out;
        try {
            File file = new File(fileName);
            if (!file.exists()) { // Create a new file if that file doesn't exist
                file.createNewFile();
            }
            out = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new FlooringOrdersPersistenceException("Could not save order data.", e);
        }

        String header = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,"
                + "Tax,Total";
        out.println(header);
        out.flush();

        String orderAsText;
        List<Order> ordersList = new ArrayList(orders.values());
        ordersList.sort(Comparator.comparingInt(Order::getId));
        for (Order order : ordersList) {
            orderAsText = marshallOrder(order);
            out.println(orderAsText);
            out.flush();
        }

        out.close();
    }

    /**
     * Loads the Orders from a file of given LocalDate to the Map.
     *
     * @param date of the Orders
     * @throws FlooringOrdersPersistenceException when file cannot be found
     */
    private void loadByDate(LocalDate date) throws FlooringOrdersPersistenceException {
        // Make sure the map is completely empty before loading in new data
        orders.clear();

        String dateValue = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String fileName = ORDERS_DIRECTORY + ORDERS_FILE_HEAD + dateValue + FILE_TAIL;

        Scanner scanner;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            scanner = new Scanner(new BufferedReader(new FileReader(file)));
        } catch (IOException e) {
            throw new FlooringOrdersPersistenceException("Could not load order data into memory.", e);
        }

        String currentLine;
        Order currentOrder;

        String regex = "\"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine, pattern);

            // Make sure currentOrder isn't null
            if (currentOrder != null) {
                orders.put(currentOrder.getId(), currentOrder);
            }
        }

        scanner.close();
    }

}
