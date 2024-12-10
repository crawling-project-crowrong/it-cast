package itcast.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import itcast.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
