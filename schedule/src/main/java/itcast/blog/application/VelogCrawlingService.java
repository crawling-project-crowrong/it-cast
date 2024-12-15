package itcast.blog.application;

import itcast.blog.parser.VelogDataParser;
import itcast.blog.client.VelogHttpClient;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class VelogCrawlingService {

    private final VelogHttpClient velogHttpClient = new VelogHttpClient("https://v3.velog.io/graphql");
    private final VelogDataParser velogdataParser;
    private final BlogRepository blogRepository;

    public List<Blog> crawlBlogs() {
        String query = """
                query trendingPosts($input: TrendingPostsInput!) {
                    trendingPosts(input: $input) {
                        id
                        title
                        thumbnail
                        likes
                        user {
                            username
                        }
                        url_slug
                        updated_at
                    }
                }
                """;

        String variables = """
                {
                    "input": {
                        "limit": 20,
                        "offset": 40,
                        "timeframe": "day"
                    }
                }
                """;

        String jsonResponse = velogHttpClient.fetchTrendingPosts(query, variables);
        List<Blog> posts = velogdataParser.parseTrendingPosts(jsonResponse);

        posts.forEach(post -> log.info(post.toString()));
        return posts;
    }

    @Scheduled(cron = "${crawler.yozm.cron}")
    public void velogCrawling() {
        log.info("블로그 크롤링 시작 ...");

        List<Blog> blogs = crawlBlogs();
        blogRepository.saveAll(blogs);

        log.info("블로그 크롤링 및 저장 완료!");
    }
}