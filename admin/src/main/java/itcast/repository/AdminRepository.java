package itcast.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import itcast.domain.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);
	Optional<Admin> findByEmail(String email);
}
