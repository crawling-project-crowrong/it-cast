package itcast.dto.response;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class AdminNewsResponse{
    private Long id;
    private String title;
    private String content;
    private String originalContent;
    private Interest interest;
    private LocalDateTime publishedAt;
    private int rating;
    private String link;
    private String thumbnail;
    private NewsStatus status;
    private LocalDateTime sendAt;

    public AdminNewsResponse(News savedNews) {
        this.id = savedNews.getId();
        this.title = savedNews.getTitle();
        this.content = savedNews.getContent();
        this.originalContent = savedNews.getOriginalContent();
        this.interest = savedNews.getInterest();
        this.publishedAt = savedNews.getPublishedAt();
        this.rating = savedNews.getRating();
        this.link = savedNews.getLink();
        this.thumbnail = savedNews.getThumbnail();
        this.status = savedNews.getStatus();
        this.sendAt = savedNews.getSendAt();
    }
}
