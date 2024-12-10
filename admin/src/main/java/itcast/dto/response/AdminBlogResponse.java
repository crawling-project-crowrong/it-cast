package itcast.dto.response;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.user.enums.Interest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class AdminBlogResponse {
    private Long id;
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

    public AdminBlogResponse(Blog savedBlog) {
        this.id = savedBlog.getId();
        this.platform = savedBlog.getPlatform();
        this.title = savedBlog.getTitle();
        this.content = savedBlog.getContent();
        this.originalContent = savedBlog.getOriginalContent();
        this.interest = savedBlog.getInterest();
        this.publishedAt = savedBlog.getPublishedAt();
        this.rating = savedBlog.getRating();
        this.link = savedBlog.getLink();
        this.thumbnail = savedBlog.getThumbnail();
        this.status = savedBlog.getStatus();
        this.sendAt = savedBlog.getSendAt();
    }
}
