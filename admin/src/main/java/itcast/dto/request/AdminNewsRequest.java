package itcast.dto.request;

import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminNewsRequest {

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
