package com.mycompany.fruitscraper.service;

/**
 * Available Items functions
 * @author tonydavid
 */
public interface IItemService {

    /**
     * Build a collection of all items and calculate the total price
     * @return a String representing a JSon ItemBoard
     */
    String getItemsBoard();
    
}
