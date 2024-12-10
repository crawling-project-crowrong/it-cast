package itcast.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import itcast.user.dto.request.ProfileCreateRequest;
import itcast.user.dto.response.ProfileCreateResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final ResponseTemplate responseTemplate;

	@PostMapping
	public ResponseTemplate<ProfileCreateRequest> createProfile(@RequestBody ProfileCreateRequest request) {
		ProfileCreateResponse response = uerService.createProfile(request);
		return new ResponseTemplate<>(HttpStatus.OK, "회원 정보 작성이 완료되었습니다.", response);
	}
}
