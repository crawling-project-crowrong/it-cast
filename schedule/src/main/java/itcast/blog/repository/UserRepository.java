package itcast.blog.repository;

import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByInterest(Interest interest);
}
