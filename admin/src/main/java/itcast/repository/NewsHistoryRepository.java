package itcast.repository;

import itcast.domain.newsHistory.NewsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsHistoryRepository extends JpaRepository<NewsHistory, Long>, CustomNewsHistoryRepository {
}
