package itcast.dto.request;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import java.time.LocalDateTime;

public record AdminNewsRequest (
        String title,
        String content,
        String originalContent,
        Interest interest,
        LocalDateTime publishedAt,
        Integer rating,
        String link,
        String thumbnail,
        NewsStatus status,
        LocalDateTime sendAt
){
    public static News toEntity(AdminNewsRequest adminNewsRequest) {
        return News.builder()
                .title(adminNewsRequest.title())
                .content(adminNewsRequest.content())
                .originalContent(adminNewsRequest.originalContent())
                .interest(adminNewsRequest.interest())
                .publishedAt(adminNewsRequest.publishedAt())
                .rating(adminNewsRequest.rating())
                .link(adminNewsRequest.link())
                .thumbnail(adminNewsRequest.thumbnail())
                .status(adminNewsRequest.status())
                .sendAt(adminNewsRequest.sendAt())
                .build();
    }

    public void updateToEntity(News news) {
        if (this.title != null && !this.title.isEmpty()) {
            news.setTitle(this.title);
        }
        if (this.content != null && !this.content.isEmpty()) {
            news.setContent(this.content);
        }
        if (this.originalContent != null && !this.originalContent.isEmpty()) {
            news.setOriginalContent(this.originalContent);
        }
        if (this.interest != null) {
            news.setInterest(this.interest);
        }
        if (this.publishedAt != null) {
            news.setPublishedAt(this.publishedAt);
        }
        if (this.rating != null) {
            news.setRating(this.rating);
        }
        if (this.link != null && !this.link.isEmpty()) {
            news.setLink(this.link);
        }
        if (this.thumbnail != null && !this.thumbnail.isEmpty()) {
            news.setThumbnail(this.thumbnail);
        }
        if (this.status != null) {
            news.setStatus(this.status);
        }
        if (this.sendAt != null) {
            news.setSendAt(this.sendAt);
        }
    }
}
