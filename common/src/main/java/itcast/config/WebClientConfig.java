package itcast.config;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
	@Bean
	public WebClient webClient() {
		return WebClient.builder()
			.baseUrl("http://example.com/api") // 변경해야함.
			.clientConnector(new ReactorClientHttpConnector(HttpClient.create()
				.responseTimeout(Duration.ofSeconds(5))
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)))
			.build();
	}
} // 확장성 고려. url을 직접 변경해야함. 도메인별로 작성.
