package itcast.user.application;

import org.springframework.stereotype.Service;

import itcast.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
}
