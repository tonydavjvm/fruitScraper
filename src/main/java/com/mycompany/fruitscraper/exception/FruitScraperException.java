package com.mycompany.fruitscraper.exception;

/**
 * Generic exception for the FruitScraper application
 * @author tonydavid
 */
public class FruitScraperException extends RuntimeException {

    public FruitScraperException(String message) {
        super(message);
    }

    public FruitScraperException(String message, Throwable cause) {
        super(message, cause);
    }

    public FruitScraperException(Throwable cause) {
        super(cause);
    }
    
    
}
