package itcast.message.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import itcast.blog.scheduler.BlogCrawlSchedule;
import itcast.blog.scheduler.BlogSelectSchedule;
import itcast.blog.scheduler.BlogSendSchedule;
import itcast.news.common.schedule.AlarmSchedule;
import itcast.news.common.schedule.NewsSchedule;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final BlogCrawlSchedule blogCrawlSchedule;
    private final BlogSelectSchedule blogSelectSchedule;
    private final BlogSendSchedule blogSendSchedule;
    private final NewsSchedule newsSchedule;
    private final AlarmSchedule alarmSchedule;


    @PostMapping("/test/crawling")
    public void testVelogCrawling() {
        blogCrawlSchedule.velogCrawling();
    }

    @PostMapping("/test/select")
    public void testBlogSelect() {
        blogSelectSchedule.selectForSend();
    }
    @PostMapping("/test/send")
    public void testBlogSend() {
        blogSendSchedule.sendMessage();
    }
    @PostMapping("/news/crawling")
    public void testNewsCrawling() {
        try {
            newsSchedule.scheduleNewsCrawling();
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute News Crawling: " + e.getMessage());
        }
    }
    @PostMapping("/news/select")
    public void testNewsSelect() {
        alarmSchedule.selectNewsSchedule();
    }

    @PostMapping("/news/send")
    public void testNewsSend() {
        alarmSchedule.sendMessageAlarmSchedule();
    }




}
