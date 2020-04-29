/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Jeff
 */
public class Product {
    private String type;
    private BigDecimal materialCostPerSqFoot;
    private BigDecimal laborCostPerSqFoot;

    public Product(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getMaterialCostPerSqFoot() {
        return materialCostPerSqFoot;
    }

    public BigDecimal getLaborCostPerSqFoot() {
        return laborCostPerSqFoot;
    }

    public void setMaterialCostPerSqFoot(BigDecimal materialCostPerSqFoot) {
        this.materialCostPerSqFoot = materialCostPerSqFoot;
    }

    public void setLaborCostPerSqFoot(BigDecimal laborCostPerSqFoot) {
        this.laborCostPerSqFoot = laborCostPerSqFoot;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.materialCostPerSqFoot);
        hash = 37 * hash + Objects.hashCode(this.laborCostPerSqFoot);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.materialCostPerSqFoot, other.materialCostPerSqFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSqFoot, other.laborCostPerSqFoot)) {
            return false;
        }
        return true;
    }
    
    
}
