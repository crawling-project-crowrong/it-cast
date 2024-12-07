package itcast.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import itcast.auth.application.AuthService;
import itcast.responsetemplate.ResponseTemplate;
import itcast.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/auth/kakao/callback")
	public ResponseTemplate<String> kakaoLogin(@RequestParam String code,
		HttpServletResponse response) throws JsonProcessingException {
		String token = authService.kakaoLogin(code);

		Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7)); // "Bearer " 부분 제외
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);

		return new ResponseTemplate<>(HttpStatus.OK, "로그인되었습니다.", token);
	}
}


