/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Jeff
 */
public class ProductDaoFileImpl implements ProductDao {
    
    public static final String PRODUCT_FILE = "data\\Products.txt";
    public static final String DELIMITER = ",";
    private Map<String, Product> productList = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);

    @Override
    public void loadListing() throws FlooringOrderPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringOrderPersistenceException("Could not load products"
                    + " into memory. File not found.", e);
        }
        
        String currentLine;
        String[] currentLineData;
        while (scanner.hasNextLine()) {
            try {
                currentLine = scanner.nextLine();
                currentLineData = currentLine.split(DELIMITER);
                Product currentProduct = new Product(currentLineData[0]);
                BigDecimal productCost = new BigDecimal(currentLineData[1]);
                BigDecimal laborCost = new BigDecimal(currentLineData[2]);
                currentProduct.setMaterialCostPerSqFoot(productCost);
                currentProduct.setLaborCostPerSqFoot(laborCost);
                productList.put(currentProduct.getType(), currentProduct);
            } catch (NumberFormatException e) {
                continue;
            }
        }
    }

    @Override
    public Product getProduct(String typeKey) {
        return productList.get(typeKey);
    }
    
}
