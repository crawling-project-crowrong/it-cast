package itcast.blog.parser;

import itcast.blog.client.JsoupCrawler;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class YozmDataParser {

    private static final int MAX_PAGE = 6;
    private static final String BASE_URL = "https://yozm.wishket.com/magazine/list/develop/?sort=new&page=";
    private static final String SORTED_URL = "&sort=new&q=";

    private final JsoupCrawler jsoupCrawler;

    public List<String> getBlogUrls() {
        return IntStream.range(1, MAX_PAGE)
                .mapToObj(pageNum -> BASE_URL + pageNum + SORTED_URL)
                .map(jsoupCrawler::getHtmlDocumentOrNull).filter(Objects::nonNull)
                .map(doc -> doc.select("a.item-title.link-text.link-underline.text900"))
                .map(link -> link.attr("abs:href"))
                .toList();
    }

    public List<Blog> parseTrendingPosts(List<String> blogUrls) {
        final String DEFAULT_PUBLISHED_AT = "2024-12-12T12:12:12";    // 출판일 해결 시 삭제
        return blogUrls.stream()
                .map(href -> {
                    Document document = jsoupCrawler.getHtmlDocumentOrNull(href);
                    String title = Objects.requireNonNull(document).title();
                    String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                    String content = document.select("div.next-news-contents").text();
                    String publishedDate = document.select("div.content-meta-elem").eq(5).text();

                    log.info("title: {}", title);
                    return Blog.createYozmBlog(title, content, DEFAULT_PUBLISHED_AT, href, thumbnail);
                })
                .toList();
    }
}
