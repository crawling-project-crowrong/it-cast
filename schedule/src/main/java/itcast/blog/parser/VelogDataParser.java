package itcast.blog.parser;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VelogDataParser {
    public List<Blog> parseTrendingPosts(String jsonResponse) {
        List<Blog> posts = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray trendingPosts = jsonObject.getJSONObject("data").getJSONArray("trendingPosts");

        for (int i = 0; i < trendingPosts.length(); i++) {
            JSONObject post = trendingPosts.getJSONObject(i);

            String thumbnail = post.optString("thumbnail", "");
            String title = post.getString("title");
            String username = post.getJSONObject("user").getString("username");
            String urlSlug = post.getString("url_slug");
            String blogUrl = "https://velog.io/@" + username + "/" + urlSlug;

            Blog blog = Blog.builder()
                    .platform(Platform.VELOG)
                    .title(title)
                    .originalContent("content")
                    .publishedAt(LocalDateTime.parse("2024-12-12T12:12:12"))
                    .link(blogUrl)
                    .thumbnail(thumbnail)
                    .status(BlogStatus.ORIGINAL)
                    .build();

            posts.add(blog);
        }
        return posts;
    }
}
