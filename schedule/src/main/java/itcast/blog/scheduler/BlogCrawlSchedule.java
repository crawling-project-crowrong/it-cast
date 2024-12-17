package itcast.blog.scheduler;

import itcast.blog.application.VelogCrawlingService;
import itcast.blog.application.YozmCrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "블로그 크롤링 스케쥴")
@Component
@RequiredArgsConstructor
public class BlogCrawlSchedule {

    private final VelogCrawlingService velogCrawlingService;
    private final YozmCrawlingService yozmCrawlingService;


    @Scheduled(cron = "${scheduler.velog.crawling}")
    public void velogCrawling() {
        log.info("Velog Crawling Start ...");

        velogCrawlingService.crawlBlogs();

        log.info("Velog Crawling & Save!");
    }

    @Scheduled(cron = "${scheduler.yozm.crawling}")
    public void yozmCrawling() {
        log.info("Yozm Crawling Start ...");

        yozmCrawlingService.crawlBlogs();

        log.info("Yozm Crawling & Save!");
    }
}
