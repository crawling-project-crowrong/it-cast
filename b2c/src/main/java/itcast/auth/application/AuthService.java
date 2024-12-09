package itcast.auth.application;

import java.net.URI;
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
import itcast.security.JwtUtil;
import itcast.user.repository.UserRepository;
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
	private final JwtUtil jwtUtil;


	public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
		String accessToken = getToken(code);
		KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);
		// user와 admin에 kakaoemail이 있는지 확인후, 있으면 createtoken, 없으면 kakaoemail을 db에 저장후 createtoken
		String jwtToken = jwtUtil.createToken(kakaoUserInfo.kakaoEmail());
		addJwtToCookie(jwtToken, response);
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
		body.add("client_id", "6e5a2156772212f2768019859e1de412");
		body.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
		body.add("code", code);

		WebClient webClient = webClientConfig.webClient();
		String responseBody = webClient.post()
			.uri(uri)
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.bodyValue(body)
			.retrieve()
			.bodyToMono(String.class)
			.block();

		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		return jsonNode.get("access_token").asText();
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

		String responseBody = webClient.post()
			.uri(uri)
			.headers(httpHeaders -> httpHeaders.addAll(headers))
			.retrieve()
			.bodyToMono(String.class)
			.block();

		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
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