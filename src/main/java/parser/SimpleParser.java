package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Class that parse static html page
 */
public class SimpleParser {
    /**
     * Methode with simple parsing
     * @param url is the url that should be parsed
     * @param cssQuery query of the element that should be found
     * @return list of parsed Elements
     */
    public Elements parse(String url, String cssQuery) throws IOException {
        Document doc = Jsoup
                .connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .referrer("http://google.com")
                .get();

        return doc.select(cssQuery);
    }
}
