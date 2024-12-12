package itcast.repository;

import itcast.domain.news.enums.NewsStatus;
import itcast.dto.response.AdminNewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomNewsRepository {
    Page<AdminNewsResponse> findNewsBYCondition(NewsStatus status, Pageable pageable);
}
