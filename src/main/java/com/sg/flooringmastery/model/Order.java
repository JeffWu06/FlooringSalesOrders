/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author Jeff
 */
public class Order {
    private int number;
    private LocalDate date;
    private String customerName;
    private String state;
    private TaxRate taxRate;
    private String productName;
    private Product product;
    private BigDecimal area;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal totalCost;

    public Order(LocalDate date, String customerName, BigDecimal area, String state, String productName) {
        this.date = date;
        this.customerName = customerName;
        this.area = area;
        this.state = state;
        this.productName = productName;
    }

    public int getNumber() {
        return number;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public String getDateString() {
        return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public String getCustomerName() {
        return customerName;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getArea() {
        return area;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public String getState() {
        return state;
    }

    public String getProductName() {
        return productName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.number;
        hash = 17 * hash + Objects.hashCode(this.date);
        hash = 17 * hash + Objects.hashCode(this.customerName);
        hash = 17 * hash + Objects.hashCode(this.state);
        hash = 17 * hash + Objects.hashCode(this.taxRate);
        hash = 17 * hash + Objects.hashCode(this.productName);
        hash = 17 * hash + Objects.hashCode(this.product);
        hash = 17 * hash + Objects.hashCode(this.area);
        hash = 17 * hash + Objects.hashCode(this.materialCost);
        hash = 17 * hash + Objects.hashCode(this.laborCost);
        hash = 17 * hash + Objects.hashCode(this.tax);
        hash = 17 * hash + Objects.hashCode(this.totalCost);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.number != other.number) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.totalCost, other.totalCost)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order #" + number + " | date: " + 
                date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + 
                " | customer: " + customerName + " | state: " + state + 
                " | product: " + productName + " | quantity: " + area + " sq ft";
    }
    
}
