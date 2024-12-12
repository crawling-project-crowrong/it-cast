package itcast.blog.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class YozmJsoupCrawler {

    public static Document getHtmlDocument(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl).get();
    }
}
