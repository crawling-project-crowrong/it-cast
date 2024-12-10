package itcast.auth.application;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itcast.auth.dto.response.KakaoUserInfo;
import itcast.config.WebClientConfig;
import itcast.domain.admin.Admin;
import itcast.domain.user.User;
import itcast.security.JwtUtil;
import itcast.user.repository.UserRepository;
import itcast.repository.AdminRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class AuthService {

	private final WebClientConfig webClientConfig;
	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final JwtUtil jwtUtil;

	public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
		String accessToken = getToken(code);
		KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

		Optional<User> existingUser = userRepository.findByKakaoEmail(kakaoUserInfo.kakaoEmail());
		Optional<Admin> existingAdmin = adminRepository.findByEmail(kakaoUserInfo.kakaoEmail());// 다시 생각

		if (existingUser.isPresent() || existingAdmin.isPresent()) {
			String jwtToken = jwtUtil.createToken(kakaoUserInfo.kakaoEmail());
			addJwtToCookie(jwtToken, response);
		} else {
			User newUser = User.builder()
				.kakaoEmail(kakaoUserInfo.kakaoEmail())
				.build();
			userRepository.save(newUser);

			String jwtToken = jwtUtil.createToken(kakaoUserInfo.kakaoEmail());//jwtToken에 pk값 추가하기 (이메일은 너무 김)
			addJwtToCookie(jwtToken, response); // 반복되는 로직 생략. // 쿠키로 넣어서 set.cookie
		}
	}

	private String getToken(String code) throws JsonProcessingException {
		log.info("인가코드 : " + code);
		URI uri = UriComponentsBuilder
			.fromUriString("https://kauth.kakao.com")
			.path("/oauth/token")
			.encode()
			.build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "6e5a2156772212f2768019859e1de412"); // .yml로 빼기
		body.add("redirect_uri", "http://localhost:8080/auth/kakao/callback"); // .yml로 빼기
		body.add("code", code);

		WebClient webClient = webClientConfig.webClient(); // 빈 직접 주입으로 바꾸기
		KakaoAuthResponse kakaoAuthResponse = webClient.post()
			.uri(uri) // 경로추가
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.bodyValue(body)
			.retrieve()
			.bodyToMono(KakaoAuthResponse.class)
			.block();
		//{"access_token":"abcd"} 토큰이 널값일때도 생각해줘야함.
		return Objects.requireNonNull(kakaoAuthResponse).getAccessToken(); //accesstoken이 1개가 아닐때생각, response에 선언해주기
	}

	private KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
		log.info("accessToken : " + accessToken);
		URI uri = UriComponentsBuilder
			.fromUriString("https://kapi.kakao.com")
			.path("/v2/user/me")
			.encode()
			.build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		WebClient webClient = webClientConfig.webClient();
		String responseBody = webClient.post()
			.uri(uri)// 경로추가
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.bodyValue("")
			.retrieve()
			.bodyToMono(String.class)
			.block();
		// 위 처럼 바꾸기
		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		String email = jsonNode.get("kakao_account")
			.get("email").asText();
		log.info("카카오 사용자 이메일: " + email);
		return new KakaoUserInfo(email);
	}

	private void addJwtToCookie(String jwtToken, HttpServletResponse response) {
		Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwtToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}