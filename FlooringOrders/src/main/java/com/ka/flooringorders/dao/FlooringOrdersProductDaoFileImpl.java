/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author kennethan
 */
public class FlooringOrdersProductDaoFileImpl implements FlooringOrdersProductDao {

    private List<Product> products = new ArrayList<>();
    private String PRODUCTS_FILE;
    private String DELIMITER = ",";
    private int NUMBER_OF_FIELDS = 3;

    /**
     * Instantiate with default file Products.txt.
     */
    public FlooringOrdersProductDaoFileImpl() {
        this.PRODUCTS_FILE = "Data/Products.txt";
    }

    /**
     * Instantiate with a passed in file as PRODUCTS_FILE.
     *
     * @param productTextFile that will be the main file.
     */
    public FlooringOrdersProductDaoFileImpl(String productTextFile) {
        this.PRODUCTS_FILE = productTextFile;
    }

    @Override
    public Product getProduct(String type) throws ProductPersistenceException {
        loadProduct();
        Product product = products.stream()
                .filter(p -> p.getType().equalsIgnoreCase(type))
                .findAny()
                .orElse(null);
        return product;
    }

    @Override
    public List<Product> getAllProducts() throws ProductPersistenceException {
        loadProduct();
        return products;
    }

    /**
     * Take a line of the Products text file and convert it into a Product. If
     * the split text length does not match the number of fields, return null.
     * If there is a NumberFormatException, just return null.
     *
     * @param productAsText
     * @return
     */
    private Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);

        try {
            if (productTokens.length == NUMBER_OF_FIELDS) {
                String type = productTokens[0];
                BigDecimal costPerSqFt = new BigDecimal(productTokens[1]);
                BigDecimal laborCostPerSqFt = new BigDecimal(productTokens[2]);

                Product product = new Product(type, costPerSqFt, laborCostPerSqFt);
                return product;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Load products off of the PRODUCTS_FILE. If there's no file, throw a
     * FileNotFoundException error.
     *
     * @throws ProductPersistenceException
     */
    private void loadProduct() throws ProductPersistenceException {
        products.clear();
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new ProductPersistenceException("Could not load products into memory.", e);
        }

        String currentLine;
        Product currentProduct;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);

            // Make sure currentState is not null before being added
            if (currentProduct != null) {
                products.add(currentProduct);
            }
        }

        scanner.close();
    }
}
