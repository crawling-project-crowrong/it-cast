package itcast.ai.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import itcast.ai.Message;
import itcast.ai.client.GPTClient;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.repository.BlogRepository;
import itcast.domain.user.enums.Interest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GPTServiceTest {

    @Mock
    private GPTClient gptClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private GPTService gptService;

    @Test
    @DisplayName("올바른 요청이 들어오면 요약에 성공한다.")
    void summaryContent_success_test() throws JsonProcessingException {
        // given
        final Long blogId = 1L;
        final String originalContent = "test originalContent";

        final Blog blog = mock(Blog.class);

        final List<Message> messages = List.of(new Message("user", originalContent));

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest("gpt-4o-mini", messages, 0.7f);

        final String requestBody =
                """
                        {
                          "model": "gpt-4o-mini",
                          "messages": [
                            {
                              "role": "user",
                              "content": "안녕하세요 테스트입니다~ 내용을 보고 요약해주세요. 그리고 GPT가 생각하기에 프론트엔드 쪽인지 백엔드 쪽인지 판별해주고 읽었을때 점수를 매겨줄 수 있을까요? **필수 조건**: 1. 응답 형식을 필수적으로 맞춰서 작성해야합니다. 2. 카테고리를 정할 때 프론트엔드면 FRONTEND, 백엔드면 BACKEND로 주어야 합니다. 3. 점수는 1에서 10까지의 정수로 알려주시고 GPT가 생각하기에 내용에 대한 유익한 점수를 매겨줄 수 있나요? **응답 형식**: {  "category": [FRONTEND or BACKEND], "summary": [요약 내용], "rating": [점수] }
                            }
                          ],
                          "temperature": 0.7
                        }
                        """;

        final String responseBody =
                """
                        {
                          "category": "FRONTEND",
                          "summary": "안녕하세요 테스트 군요!",
                          "rating": 8
                        }
                        """;

        final Map<String, Object> parsedResult = Map.of(
                "id", "chatcmpl-AbmMprXOwUKHoBGnFJk08S67kSggh",
                "object", "chat.completion",
                "created", 1733567423,
                "model", "gpt-4o-mini-2024-07-18",
                "choices", List.of(Map.of(
                                "index", 0,
                                "message", Map.of(
                                        "role", "assistant",
                                        "content", "{\n"
                                                + "  \"category\": \"FRONTEND\",\n"
                                                + "  \"summary\": \"hello world.\",\n"
                                                + "  \"rating\": 8\n"
                                                + "}"
                                )
                        )
                ),
                "usage", Map.of(
                        "prompt_tokens", 774,
                        "completion_tokens", 144,
                        "total_tokens", 918
                ),
                "system_fingerprint", "fp_bba3c8e70b"
        );

        when(objectMapper.writeValueAsString(gptSummaryRequest)).thenReturn(requestBody);
        when(gptClient.sendRequest(requestBody)).thenReturn(responseBody);
        when(objectMapper.readValue(eq(responseBody), ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(parsedResult);
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        // when
        gptService.updateBlogBySummaryContent(gptSummaryRequest);

        // then
        verify(blog, times(1)).applySummaryUpdate(any(), any(), any(), any());
        verify(blog, times(1)).applySummaryUpdate(eq("hello world."), eq(Interest.FRONTEND), eq(8L), eq(BlogStatus.SUMMARY));
        verify(gptClient, times(1)).sendRequest(requestBody);
    }
}
