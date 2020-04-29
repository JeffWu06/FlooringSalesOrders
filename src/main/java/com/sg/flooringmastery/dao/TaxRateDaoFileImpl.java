/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.TaxRate;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Jeff
 */
public class TaxRateDaoFileImpl implements TaxRateDao {
    
    public static final String TAX_FILE = "data\\Taxes.txt";
    public static final String DELIMITER = ",";
    public static final BigDecimal PERCENT = new BigDecimal("100.00");
    private Map<String, TaxRate> taxRates = new TreeMap<String, TaxRate>(String.CASE_INSENSITIVE_ORDER);

    @Override
    public void loadListing() throws FlooringOrderPersistenceException {
        Scanner scanner;
        
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        } catch (FileNotFoundException ex) {
            throw new FlooringOrderPersistenceException("-_- Could not load tax "
                    + "rate data into memory.", ex);
        }
        
        String currentLine;
        String[] currentLineData;
        
        while(scanner.hasNextLine()) {
            try {
                currentLine = scanner.nextLine();
                currentLineData = currentLine.split(DELIMITER);
                TaxRate currentRate = new TaxRate(currentLineData[0]);
                BigDecimal rate = new BigDecimal(currentLineData[1]);
                rate = rate.divide(PERCENT, 4, RoundingMode.HALF_UP);
                currentRate.setRate(rate);
                taxRates.put(currentRate.getState(), currentRate);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        scanner.close();
    }

    @Override
    public TaxRate getTaxRate(String stateKey) {
        return taxRates.get(stateKey);
    }
    
    
}
