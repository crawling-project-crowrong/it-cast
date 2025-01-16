package itcast.ai.client;

import static org.assertj.core.api.Assertions.assertThat;

import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.request.Message;
import itcast.ai.dto.response.GPTSummaryResponse;
import itcast.domain.user.enums.ArticleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GPTClientTest {

    @Autowired
    private GPTClient gptClient;

    @Test
    @DisplayName("요청을 보내면 요약에 성공한다.")
    void test_news_sendRequest_success() {
        // given
        final String originalContent = "test originalContent";

        final String model = "gpt-4o-mini";

        final Message message = new Message("user", originalContent);

        final float temperature = 0.7f;

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest(model, message, temperature);

        final ArticleType type = ArticleType.BLOG;

        // when
        final GPTSummaryResponse response = gptClient.sendRequest(gptSummaryRequest, type);

        // then
        assertThat(response.choices().get(0).message().content())
                .contains("category")
                .contains("\"summary\"")
                .contains("\"rating\"");
    }
}
