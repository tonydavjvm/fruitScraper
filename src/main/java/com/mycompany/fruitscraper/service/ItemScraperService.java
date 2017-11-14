package com.mycompany.fruitscraper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mycompany.fruitscraper.adaptor.IWebScraperAdaptor;
import com.mycompany.fruitscraper.adaptor.RipeAndReadyAdaptor;
import com.mycompany.fruitscraper.model.Item;
import com.mycompany.fruitscraper.model.ItemBoard;
import java.math.BigDecimal;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ItemServie using a web scraper to collect the information
 * @author tonydavid
 */
public class ItemScraperService implements IItemService {

    private IWebScraperAdaptor scraper;

    public ItemScraperService() {
        scraper = new RipeAndReadyAdaptor();
    }    

    @Override
    public String getItemsBoard() {
        try {
            Set<Item> items = scraper.scrapeWebSite();
            ItemBoard board = new ItemBoard();
            board.getResults().addAll(items);
            
            //Calculate total price
            board.setTotalPrice(
                items.stream()
                        .map(i -> i.getUnitPrice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
            return transformItemBoardToJson(board);
        } catch (Exception e) {
            return transformErrorToJson(e);
        }
    }
    
    private String transformErrorToJson(Exception e) {
        ObjectNode node = JsonNodeFactory.instance.objectNode(); // initializing
        node.put("error", e.getMessage());
        node.put("TypeException", e.getClass().toString());
        return node.toString();
    }
    
    private String transformItemBoardToJson(Object element) {
        if(element == null) {
            throw new IllegalArgumentException("parameter cannot be null");
        }
        String result = "{}";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(element);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ItemScraperService.class.getName()).log(Level.SEVERE, "Cannot render a Json response", ex);            
        }
        return result;
    }
    
    public IWebScraperAdaptor getScraper() {
        return scraper;
    }

    public void setScraper(IWebScraperAdaptor scraper) {
        this.scraper = scraper;
    }
    
    public static void main(String[] args) {
        ItemScraperService service = new ItemScraperService();
        System.out.println(service.getItemsBoard());
    }
}
