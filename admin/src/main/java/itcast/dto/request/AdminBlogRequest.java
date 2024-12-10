package itcast.dto.request;

import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminBlogRequest {

    private Platform platform;
    private String title;
    private String content;
    private String originalContent;
    private Interest interest;
    private LocalDateTime publishedAt;
    private int rating;
    private String link;
    private String thumbnail;
    private BlogStatus status;
    private LocalDateTime sendAt;
}
