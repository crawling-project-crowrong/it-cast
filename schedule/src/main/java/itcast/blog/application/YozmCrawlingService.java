package itcast.blog.application;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static itcast.blog.BlogCrawler.getHtmlDocument;

@Service
@Slf4j
public class YozmCrawlingService {

    private static final String BASE_URL = "https://yozm.wishket.com/magazine/list/develop/?sort=new&page=";
    private static final String SORTED_URL = "&sort=new&q=";
    private static final int MAX_PAGES = 5;

    public List<Blog> crawlBlogs() {
        List<Blog> blogs = new ArrayList<>();

        try {
            for (int page = 1; page <= MAX_PAGES; page++) {
                String pageUrl = BASE_URL + page + SORTED_URL;
                log.info("크롤링할 페이지 URL: {}", pageUrl);

                Document doc = getHtmlDocument(pageUrl);
                Elements links = doc.select("a.item-title.link-text.link-underline.text900");

                for (Element link : links) {
                    String href = link.attr("abs:href");
                    Document detailDoc = getHtmlDocument(href);

                    String title = detailDoc.title();
                    String thumbnail = detailDoc.selectFirst("meta[property=og:image]").attr("content");
                    String content = detailDoc.select("div.next-news-contents").text();
                    String publishedDate = detailDoc.select("div.content-meta-elem").eq(5).text();

                    Blog blog = Blog.builder()
                            .platform(Platform.YOZM)
                            .title(title)
                            .originalContent(content)
//                            .publishedAt(publishedDate)
                            .link(href)
                            .thumbnail(thumbnail)
                            .status(BlogStatus.ORIGINAL)
                            .build();
                    blogs.add(blog);
                }
            }
        } catch (Exception e) {
            log.error("크롤링 중 에러", e);
        }
        return blogs;
    }
}
