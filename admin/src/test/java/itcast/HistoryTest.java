package itcast;

import itcast.domain.news.News;
import itcast.domain.newsHistory.NewsHistory;
import itcast.domain.user.User;
import itcast.jwt.repository.UserRepository;
import itcast.repository.NewsHistoryRepository;
import itcast.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = AdminApplication.class)
public class HistoryTest {

    @Autowired
    NewsHistoryRepository newsHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NewsRepository newsRepository;

    @Test
    void measurePerformance() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        makeDummyData();

        stopWatch.stop();
        System.out.println("걸린 시간: " + stopWatch.getTotalTimeMillis() + " ms");
    }

    @Test
    void makeDummyData() {
        List<User> users = userRepository.findAll();
        List<News> newsList = newsRepository.findAll();
        List<NewsHistory> newsHistories = new ArrayList<>();

        for (long i = 0; i < 10000; i++) {
            User user = users.get((int) (Math.random() * users.size()));
            News news = newsList.get((int) (Math.random() * newsList.size()));

            NewsHistory newsHistory = NewsHistory.builder()
                    .user(user)
                    .news(news)
                    .build();
            newsHistories.add(newsHistory);

            if (newsHistories.size() % 1000 == 0) {
                newsHistoryRepository.saveAll(newsHistories);
                newsHistories.clear();
            }
        }

        if (!newsHistories.isEmpty()) {
            newsHistoryRepository.saveAll(newsHistories);
        }
    }
}