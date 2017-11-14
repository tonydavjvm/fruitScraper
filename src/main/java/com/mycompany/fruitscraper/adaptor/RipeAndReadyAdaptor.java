package com.mycompany.fruitscraper.adaptor;

import com.mycompany.fruitscraper.exception.FruitScraperException;
import com.mycompany.fruitscraper.model.Item;
import com.mycompany.fruitscraper.tool.FruitScraperUtil;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.BrowserType;
import com.ui4j.api.browser.Page;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Bind the Sainsbury's ripe and ready page with our business domain
 *
 * @author tonydavid
 */
public class RipeAndReadyAdaptor implements IWebScraperAdaptor {

    public static final String URL = "http://www.sainsburys.co.uk/webapp/wcs/stores/servlet/CategoryDisplay?"
            + "listView=true&orderBy=FAVOURITES_FIRST&parent_category_rn=12518&top_category=12518&langId=44&beginIndex=0&"
            + "pageSize=20&catalogId=10137&searchTerm=&categoryId=185749&listId=&storeId=10151&promotionId=#langId=44&storeId=10151&"
            + "catalogId=10137&categoryId=185749&parent_category_rn=12518&top_category=12518&"
            + "pageSize=20&orderBy=FAVOURITES_FIRST&searchTerm=&beginIndex=0&hideFilters=true";

    public static final String PRODUCT_GROUP_ID_IDENTIFIER = "productLister";
    public static final String PRODUCT_CSS_IDENTIFIER = "productInfo";
    public static final String PRODUCT_HEADER_IDENTIFIER = "h3";
    
    public static final String PRODUCT_DETAILED_CSS_IDENTIFIER = "productTitleDescriptionContainer";
    public static final String PRODUCT_DETAILED_HEADER_IDENTIFIER = "h1";
    
    public static final String PRODUCT_PRICE_CSS_IDENTIFIER = "pricePerUnit";
    
    public static final String PRODUCT_INFORMATION_ID_IDENTIFIER = "information";
    public static final String PRODUCT_DESCRIPTION_CSS_IDENTIFIER = "productText";
    
    //GENERIC
    public static final String LINK_TAG = "a";
    public static final String PARAGRAPH_TAG = "p";
    public static final String LINK_ATTR = "href";
    
    private final HtmlRunnableAction htmlRunnableAction = new HtmlRunnableAction();

    @Override
    public Set<Item> scrapeWebSite() {
        try {
            Document doc = Jsoup.parse(buildHtmlLandingPage());
            Set<Item> items = processProducts(doc);
            return items;
        } catch (FruitScraperException fe) {
            throw fe;
        } catch (Exception e) {
            throw new FruitScraperException("A technical error happened", e);
        }
    }

    /**
     * Handle the items extraction from the html
     * @param doc
     * @return 
     */
    private Set<Item> processProducts(final Document doc) {
        //Get all the producst html elements
        IElementsHtmlSelector htmlAction = () ->{
            return doc.select("div#" +PRODUCT_GROUP_ID_IDENTIFIER).get(0).getElementsByClass(PRODUCT_CSS_IDENTIFIER);
        };
        Elements products = htmlRunnableAction.execute(htmlAction);
        Set<Item> items = new LinkedHashSet<>();
        for (Element product : products) {
            try {
                //Retrieve a single product html element
                IElementHtmlSelector htmlProductAction = () -> {
                    return product.getElementsByTag(PRODUCT_HEADER_IDENTIFIER).get(0)
                        .getElementsByTag(LINK_TAG).get(0);
                };
                Element productLink = htmlRunnableAction.execute(htmlProductAction);
                items.add(processProductDetail(productLink.attr(LINK_ATTR)));
            } catch(FruitScraperException e) {
                Logger.getLogger(RipeAndReadyAdaptor.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                Logger.getLogger(RipeAndReadyAdaptor.class.getName()).log(Level.SEVERE, "Different website structure than expected", e);
            }
        }
        return items;
    }

    /**
     * Process and get detail on a single product
     * @param link 
     */
    private Item processProductDetail(String link) {
        if (link == null) {
            throw new IllegalArgumentException("parameter cannot be null");
        }
        try {
            Document productDocument = scrapeProductDetail(link);
            Item product = new Item();
            
            //Get size
            product.setSize(FruitScraperUtil.buildPageSize(productDocument.toString().length()));

            //Get title
            IElementHtmlSelector htmlProductTitleAction = () -> {
                return productDocument.getElementsByClass(PRODUCT_DETAILED_CSS_IDENTIFIER).get(0)
                        .getElementsByTag(PRODUCT_DETAILED_HEADER_IDENTIFIER).get(0);
            };
            product.setTitle(htmlRunnableAction.execute(htmlProductTitleAction).text());
            
            //Get Price
            IElementHtmlSelector htmlProductPriceAction = () -> {
                return productDocument.getElementsByClass(PRODUCT_PRICE_CSS_IDENTIFIER).get(0);
            };
            product.setUnitPrice(FruitScraperUtil.extractNumberFromString(
                        htmlRunnableAction.execute(htmlProductPriceAction).text()));
            
            //Get description
            IElementHtmlSelector htmlProductDescriptionAction = () -> {
                return productDocument.select("div#" +PRODUCT_INFORMATION_ID_IDENTIFIER).get(0)
                        .getElementsByClass(PRODUCT_DESCRIPTION_CSS_IDENTIFIER).get(0);
            };
            product.setDescription(
                htmlRunnableAction.execute(htmlProductDescriptionAction).text());
            return product;
        } catch(FruitScraperException e) {
            throw e;
        } catch (Exception ex) {
            throw new FruitScraperException("Different website structure than expected");
        }
    }
    
    /**
     * Hit a product detail link 
     * @param link
     * @return 
     */
    private Document scrapeProductDetail(String link) {
        try {
            //Get the product page detail
            Connection connection = Jsoup.connect(link);
            connection.timeout(5000);
            return connection.get();
        } catch (IOException iOException) {
            throw new FruitScraperException("Connection issue with the given link");
        }
    }


    //Connecting via ui4j as JSoup cannot handle dynamic loading through JS/Ajax
    private String buildHtmlLandingPage() {
        BrowserEngine webkit = BrowserFactory.getBrowser(BrowserType.WebKit);
        try {
            Page page = webkit.navigate(URL);
            //Showing the page is required to load the html property
            page.show(false);
            com.ui4j.api.dom.Document document = page.getDocument();
            String result = document.getBody().getInnerHTML();
            return result;
        } catch (Exception e) {
            throw new FruitScraperException("Cannot reach the remote server with the given url", e);
        } finally {
            webkit.shutdown();
        }
    }
}
