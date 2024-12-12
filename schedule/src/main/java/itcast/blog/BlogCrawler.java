package itcast.blog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class BlogCrawler {

    public static Document getHtmlDocument(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl).get();
    }
}
