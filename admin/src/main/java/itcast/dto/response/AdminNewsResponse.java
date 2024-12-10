package itcast.dto.response;

import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
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
}
