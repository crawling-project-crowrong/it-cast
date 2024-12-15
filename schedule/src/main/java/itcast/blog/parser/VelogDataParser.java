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

    public List<String> getBlogUrls(final String jsonResponse) {
        final List<String> blogUrls = new ArrayList<>();

        final JSONObject jsonObject = new JSONObject(jsonResponse);
        final JSONArray trendingPosts = jsonObject.getJSONObject("data").getJSONArray("trendingPosts");

        for (int i = 0; i < trendingPosts.length(); i++) {
            final JSONObject post = trendingPosts.getJSONObject(i);

            final String username = post.getJSONObject("user").getString("username");
            final String urlSlug = post.getString("url_slug");
            final String blogUrl = "https://velog.io/@" + username + "/" + urlSlug;

            blogUrls.add(blogUrl);
        }
        return blogUrls;
    }

    public List<Blog> parseTrendingPosts(final List<String> blogUrl) {
        final List<Blog> blogs = new ArrayList<>();
        for (String url : blogUrl) {
            final Document document = jsoupCrawler.getHtmlDocumentOrNull(url);

            final String title = Objects.requireNonNull(document).title();
            final String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
            final String content = document.select("div.sc-eGRUor.gdnhbG.atom-one").text();
            final String publishedDate = document.select(".information").eq(3).text();

            log.info("title: {}", title);
            final Blog blog = Blog.builder()
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
