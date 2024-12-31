package itcast.domain.newsHistory;

import itcast.domain.BaseEntity;
import itcast.domain.news.News;
import itcast.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "news_history", indexes = {
@Index(name = "idx_user_news_created", columnList = "user_id, news_id, created_at")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @Column(name = "is_dummy", nullable = false)
    private boolean isDummy;

    //더미데이터 생성용 빌더
    @Builder
    public NewsHistory(User user, News news, boolean isDummy) {
        this.user = user;
        this.news = news;
        this.isDummy = isDummy;
    }
}
