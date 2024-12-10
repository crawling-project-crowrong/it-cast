package itcast.auth.application;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAuthResponse {
	@JsonProperty(value = "access_token", required = true)
	private String accessToken;
}
