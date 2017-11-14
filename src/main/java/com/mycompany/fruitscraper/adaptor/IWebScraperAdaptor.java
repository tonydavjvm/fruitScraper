/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fruitscraper.adaptor;

import com.mycompany.fruitscraper.model.Item;
import java.util.Set;

/**
 * Bind a web site and the business model to collect items
 * @author tonydavid
 */
public interface IWebScraperAdaptor {
    
    Set<Item> scrapeWebSite();
    
}
