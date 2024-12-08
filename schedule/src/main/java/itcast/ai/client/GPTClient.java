package itcast.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GPTClient {

    private final WebClient webClient;

    @Value("${openai.secret-key}")
    private String secretKey;

    public GPTClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .build();
    }

    public String sendRequest(final String requestBody) {
        return webClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
