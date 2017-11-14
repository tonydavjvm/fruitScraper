/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fruitscraper.test.service;

import com.mycompany.fruitscraper.adaptor.IWebScraperAdaptor;
import com.mycompany.fruitscraper.exception.FruitScraperException;
import com.mycompany.fruitscraper.model.Item;
import com.mycompany.fruitscraper.service.ItemScraperService;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author tonydavid
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemScraperServiceTest {

    private ItemScraperService itemService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private IWebScraperAdaptor adaptor;

    public ItemScraperServiceTest() {
    }

    @Before
    public void init() {
        itemService = new ItemScraperService();
    }

    /*Given a source of 5 items 
    "sweet apple", "red apple" 1.80, "60KO"
    "organic orange", "Brazilian orange " 8.80, "70KO"    
    then I shopuld receive a json with an array of these 2 items with description, title, unit price and size
    and the total price */
    @Test
    public void getItemsBoardWithSourceData() {
        //Source data
        Set<Item> items = new LinkedHashSet<>();
        Item item = new Item();
        item.setDescription("sweet apple");
        item.setTitle("red apple");
        item.setUnitPrice(new BigDecimal("1.80"));
        item.setSize("80KO");
        items.add(item);
        item = new Item();
        item.setDescription("organic orange");
        item.setTitle("Brazilian orange");
        item.setUnitPrice(new BigDecimal("8.80"));
        item.setSize("70KO");
        items.add(item);
        Mockito.when(adaptor.scrapeWebSite()).thenReturn(items);
        itemService.setScraper(adaptor);

        //Service call
        String result = itemService.getItemsBoard();
        Mockito.verify(adaptor).scrapeWebSite();

        //Test checks
        String expected = "{\"results\":[{\"description\":\"organic orange\","
                + "\"title\":\"Brazilian orange\",\"unitPrice\":8.80,\"size\":\"70KO\"}"
                + ",{\"description\":\"sweet apple\",\"title\":\"red apple\","
                + "\"unitPrice\":1.80,\"size\":\"80KO\"}],\"totalPrice\":10.60}";
        assertEquals(expected, result);
    }

    /*
    Given no source data
    then I should receive a Json with an empty array and a total price of 0
     */
    @Test
    public void getItemsBoardWithNoSourceData() {
        //Source data
        Mockito.when(adaptor.scrapeWebSite()).thenReturn(new LinkedHashSet<>());
        itemService.setScraper(adaptor);

        //Service call
        String result = itemService.getItemsBoard();
        Mockito.verify(adaptor).scrapeWebSite();

        //Test checks
        String expected = "{\"results\":[],\"totalPrice\":0}";
        assertEquals(expected, result);
    }
    
    /*
     * Given a different webste structure
     * then I should receive a json with an error regarding the structure
     */
    @Test
    public void scrapeWebSiteWithDifferentStructure() {
        //Source data
        FruitScraperException scraperException = new FruitScraperException("Different website structure than expected");
        Mockito.when(adaptor.scrapeWebSite()).thenThrow(scraperException);
        itemService.setScraper(adaptor);

        //Service call
        String result = itemService.getItemsBoard();
        Mockito.verify(adaptor).scrapeWebSite();

        //Test checks
        String expected = "{\"error\":\"Different website structure than expected\","
                + "\"TypeException\":\"class com.mycompany.fruitscraper.exception.FruitScraperException\"}";
        assertEquals(expected, result);
    }

    /*
    Given an unreachable source data
    then I should receive a json with an error regarding an unreachable server
     */
    @Test
    public void getItemsBoardWithUnreachableSourceData() {
        //Source data
        FruitScraperException scraperException = new FruitScraperException("Cannot reach the remote server with the given url");
        Mockito.when(adaptor.scrapeWebSite()).thenThrow(scraperException);
        itemService.setScraper(adaptor);
        
        //Service call
        String result = itemService.getItemsBoard();
        Mockito.verify(adaptor).scrapeWebSite();

        //Test checks
        String expected = "{\"error\":\"Cannot reach the remote server with the given url\",\"TypeException\":\"class com.mycompany.fruitscraper.exception.FruitScraperException\"}";
        assertEquals(expected, result);
    }
    

}
