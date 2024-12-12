package itcast.blog.controller;

import itcast.blog.application.YozmCrawlingService;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BlogScheduler {

    private final YozmCrawlingService yozmCrawlingService;
    private final BlogRepository blogRepository;
    private final static int MAX_PAGE = 6;  // 5페이지까지 크롤링(range)

    @Scheduled(cron = "${crawler.yozm.cron}")
    public void scheduleBlogCrawling() {
        log.info("블로그 크롤링 시작 ...");

        List<Blog> blogs = yozmCrawlingService.crawlBlogs(MAX_PAGE);
        blogRepository.saveAll(blogs);

        log.info("블로그 크롤링 및 저장 완료!");
        // 얼마나 걸렸는지 AOP
    }
}
