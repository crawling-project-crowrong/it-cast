package itcast.blog.parser;

import itcast.blog.client.JsoupCrawler;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class VelogDataParser {

    private final JsoupCrawler jsoupCrawler;

    public List<String> getBlogUrls(String jsonResponse) {
        List<String> blogUrls = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray trendingPosts = jsonObject.getJSONObject("data").getJSONArray("trendingPosts");

        for (int i = 0; i < trendingPosts.length(); i++) {
            JSONObject post = trendingPosts.getJSONObject(i);

            String username = post.getJSONObject("user").getString("username");
            String urlSlug = post.getString("url_slug");
            String blogUrl = "https://velog.io/@" + username + "/" + urlSlug;

            blogUrls.add(blogUrl);
        }
        return blogUrls;
    }

    public List<Blog> parseTrendingPosts(List<String> blogUrl) {
        List<Blog> blogs = new ArrayList<>();
        for (String url : blogUrl) {
            Document document = jsoupCrawler.getHtmlDocumentOrNull(url);

            String title = Objects.requireNonNull(document).title();
            String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
            String content = document.select("div.sc-eGRUor.gdnhbG.atom-one").text();
            String publishedDate = document.select(".information").eq(3).text();

            log.info("title: {}", title);
            Blog blog = Blog.builder()
                    .platform(Platform.VELOG)
                    .title(title)
                    .originalContent(content)
                    .publishedAt(LocalDateTime.parse("2024-12-12T12:12:12"))
                    .link(url)
                    .thumbnail(thumbnail)
                    .status(BlogStatus.ORIGINAL)
                    .build();
            blogs.add(blog);
        }
        return blogs;
    }
}
