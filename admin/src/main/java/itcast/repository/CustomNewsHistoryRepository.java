package itcast.repository;

import itcast.dto.response.AdminNewsHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomNewsHistoryRepository {
    Page<AdminNewsHistoryResponse> findNewsHistoriesByCondition(Long userId, Long newsId, LocalDate createdAt, Pageable pageable);
}
