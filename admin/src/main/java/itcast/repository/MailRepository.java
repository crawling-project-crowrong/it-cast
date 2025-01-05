package itcast.repository;

import itcast.domain.mailEvent.MailEvents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<MailEvents, Long> {
}
