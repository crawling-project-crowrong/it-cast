package itcast.dto.request;

import itcast.domain.blog.Blog;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {
    public static Blog toEntity(AdminBlogRequest adminBlogRequest) {
        return Blog.builder()
                .platform(adminBlogRequest.getPlatform())
                .title(adminBlogRequest.getTitle())
                .content(adminBlogRequest.getContent())
                .originalContent(adminBlogRequest.getOriginalContent())
                .interest(adminBlogRequest.getInterest())
                .publishedAt(adminBlogRequest.getPublishedAt())
                .rating(adminBlogRequest.getRating())
                .link(adminBlogRequest.getLink())
                .thumbnail(adminBlogRequest.getThumbnail())
                .status(adminBlogRequest.getStatus())
                .sendAt(adminBlogRequest.getSendAt())
                .build();
    }
}
