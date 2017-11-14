package com.mycompany.fruitscraper.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Model entity to be returned to the client
 * @author tonydavid
 */
public class ItemBoard {
    
    private final Set<Item> results;
    private BigDecimal totalPrice;

    public ItemBoard() {
        this.results = new HashSet<>();
    }

    public Set<Item> getResults() {
        return results;
    }    

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    
}
