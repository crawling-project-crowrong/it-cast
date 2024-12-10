package itcast.dto.request;

import itcast.domain.news.News;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public static News toEntity(AdminNewsRequest adminNewsRequest) {
        return News.builder()
                .title(adminNewsRequest.getTitle())
                .content(adminNewsRequest.getContent())
                .originalContent(adminNewsRequest.getOriginalContent())
                .interest(adminNewsRequest.getInterest())
                .publishedAt(adminNewsRequest.getPublishedAt())
                .rating(adminNewsRequest.getRating())
                .link(adminNewsRequest.getLink())
                .thumbnail(adminNewsRequest.getThumbnail())
                .status(adminNewsRequest.getStatus())
                .sendAt(adminNewsRequest.getSendAt())
                .build();
    }
}
