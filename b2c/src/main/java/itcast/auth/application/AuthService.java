package itcast.auth.application;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itcast.auth.dto.response.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class AuthService {

	private final RestTemplate restTemplate;

	public String kakaoLogin(String code) throws JsonProcessingException {
		String accessToken = getToken(code);
		KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);
		// admin 인지 확인하고 처음 로그인시 kakaoemail 저장하는 기능 추가해야함.
		return null;
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

		RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
			.post(uri)
			.headers(headers)
			.body(body);

		ResponseEntity<String> response = restTemplate.exchange(
			requestEntity,
			String.class
		);

		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
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

		RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
			.post(uri)
			.headers(headers)
			.body(new LinkedMultiValueMap<>());

		ResponseEntity<String> response = restTemplate.exchange(
			requestEntity,
			String.class
		);

		JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
		String email = jsonNode.get("kakao_account")
			.get("email").asText();
		log.info("카카오 사용자 이메일: " + email);
		return new KakaoUserInfo(email);
	}
}