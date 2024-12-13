package itcast.auth.application;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import itcast.auth.client.KakaoClient;
import itcast.auth.dto.response.KakaoUserInfo;
import itcast.domain.user.User;
import itcast.security.JwtUtil;
import itcast.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoClient kakaoClient;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public HttpHeaders kakaoLogin(String code) throws JsonProcessingException {
		String accessToken = getToken(code);
		KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

		Optional<User> existingUser = userRepository.findByKakaoEmail(kakaoUserInfo.kakaoEmail());
		User user = existingUser.orElseGet(() -> {
			User newUser = User.builder()
					.kakaoEmail(kakaoUserInfo.kakaoEmail())
					.build();
			return userRepository.save(newUser);
		});

		String jwtToken = jwtUtil.createToken(user.getId(), kakaoUserInfo.kakaoEmail());
		Cookie jwtCookie = createJwtCookie(jwtToken);
		return createCookieHeaders(jwtCookie);  // 쿠키를 포함한 헤더 반환
	}

	private String getToken(String code) throws JsonProcessingException {
		return kakaoClient.getAccessToken(code);
	}

	private KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
		return kakaoClient.getKakaoUserInfo(accessToken);
	}

	private Cookie createJwtCookie(String jwtToken) {
		Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwtToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		return cookie;
	}

	public HttpHeaders createCookieHeaders(Cookie cookie) {
		HttpHeaders headers = new HttpHeaders();
		String cookieHeader = createCookieHeader(cookie);
		headers.add("Set-Cookie", cookieHeader);
		return headers;
	}

	private String createCookieHeader(Cookie cookie) {
		return cookie.getName() + "=" + cookie.getValue() + "; HttpOnly; Path=" + cookie.getPath() + "; Max-Age=" + cookie.getMaxAge();
	}
}