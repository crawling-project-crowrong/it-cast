package itcast.blog.application;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static itcast.blog.controller.BlogJsoupCrawler.getHtmlDocument;

@Service
@Slf4j
public class YozmCrawlingService {

    private static final String BASE_URL = "https://yozm.wishket.com/magazine/list/develop/?sort=new&page=";
    private static final String SORTED_URL = "&sort=new&q=";
    private static final int MAX_PAGES = 6;

    public List<Blog> crawlBlogs() {
        List<Blog> blogs = IntStream.range(1, MAX_PAGES)
                .mapToObj(page -> BASE_URL + page + SORTED_URL)
                .map(this::getHtmlDocumentOrNull).filter(Objects::nonNull)
                .map(doc -> doc.select("a.item-title.link-text.link-underline.text900"))
                .flatMap(Elements::stream)
                .map(link -> link.attr("abs:href"))
                .map(href -> {
                    Document document = getHtmlDocumentOrNull(href);
                    String title = Objects.requireNonNull(document).title();
                    String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                    String content = document.select("div.next-news-contents").text();
                    String publishedDate = document.select("div.content-meta-elem").eq(5).text();

                    log.info("title: {}", title);
                    return Blog.builder()
                            .platform(Platform.YOZM)
                            .title(title)
                            .originalContent(content)
                            .publishedAt(LocalDateTime.parse("2024-12-12T10:00"))
                            .link(href)
                            .thumbnail(thumbnail)
                            .status(BlogStatus.ORIGINAL)
                            .build();
                })
                .toList();
        return blogs;
    }

    private Document getHtmlDocumentOrNull(String url) {
        try {
            return getHtmlDocument(url);
        } catch (IOException e) {
            log.error("Document Parse Error", e);
            return null;
        }
    }
}
