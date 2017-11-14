package com.mycompany.fruitscraper.adaptor;

import com.mycompany.fruitscraper.exception.FruitScraperException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Execute HTML selection on a document
 *
 * @author tonydadvid
 */
class HtmlRunnableAction {
    <T> T execute(IHtmlSelector<T> selector) {
        try {
            return selector.getHtml();
        } catch (Exception e) {
            throw new FruitScraperException("Different website structure than expected");
        }
    }
}

/**
 * Build html expression to select data from an html document
 * @author tonydavid
 * @param <T> 
 */
interface IHtmlSelector<T>{
    T getHtml();
}
@FunctionalInterface
interface IElementsHtmlSelector extends IHtmlSelector<Elements>{}
@FunctionalInterface
interface IElementHtmlSelector extends IHtmlSelector<Element>{}
