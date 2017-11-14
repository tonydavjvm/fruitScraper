/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fruitscraper.test.tool;

import com.mycompany.fruitscraper.model.Item;
import java.math.BigDecimal;

/**
 * Common utilities functions for tests
 * @author tonydavid
 */
public class TestHelper {
    
    public static boolean checkItem(Item item, String description, String title, 
            BigDecimal unitPrice, String size) {
        if(item == null) {
            return false;
        }
        Item newItem = new Item();
        newItem.setDescription(description);
        newItem.setSize(size);
        newItem.setTitle(title);
        newItem.setUnitPrice(unitPrice);
        return item.equals(newItem);
    }
    
}
