package itcast.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenResponse {
    @JsonProperty(value = "access_token", required = true)
    private String accessToken;
}