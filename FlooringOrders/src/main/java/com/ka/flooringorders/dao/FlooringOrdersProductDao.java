/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ka.flooringorders.dao;

import com.ka.flooringorders.dto.Product;
import java.util.List;

/**
 *
 * @author kennethan
 */
public interface FlooringOrdersProductDao {
    
    /**
     * Retrieve a Product based on the type of Product. Return null if no
     * Product with that type is found.
     * @param type of the Product
     * @return Product, or null if Product isn't found
     * @throws ProductPersistenceException 
     */
    public Product getProduct(String type) throws ProductPersistenceException;
    
    /**
     * Return a list of Products.
     * 
     * @return List of Products
     * @throws ProductPersistenceException 
     */
    public List<Product> getAllProducts() throws ProductPersistenceException;
    
}
