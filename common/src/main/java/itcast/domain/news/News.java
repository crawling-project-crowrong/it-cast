package itcast.domain.news;

import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import itcast.domain.BaseEntity;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class News extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String originalContent;

    @Enumerated(EnumType.STRING)
    private Interest interest;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String link;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    private LocalDateTime sendAt;

    @Builder
    public News(String title, String content, String originalContent, Interest interest,
                LocalDateTime publishedAt, int rating, String link, String thumbnail, NewsStatus status, LocalDateTime sendAt) {
        this.title = title;
        this.content = content;
        this.originalContent = originalContent;
        this.interest = interest;
        this.publishedAt = publishedAt;
        this.rating = rating;
        this.link = link;
        this.thumbnail = thumbnail;
        this.status = status;
        this.sendAt = sendAt;
    }
}
