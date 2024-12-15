package itcast.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import itcast.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoEmail(String kakaoEmail);
}
