package itcast.ai.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.repository.BlogRepository;
import itcast.domain.user.enums.Interest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GPTService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final BlogRepository blogRepository;

    @Value("${openai.prompt}")
    private String template;

    @Transactional
    public Map<String, Object> prompt(final GPTSummaryRequest gptSummaryRequest) {
        try {
            addTemplate(gptSummaryRequest);

            final String requestBody = objectMapper.writeValueAsString(gptSummaryRequest);

            final String responseBody = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            final Map<String, Object> result = objectMapper.readValue(responseBody, new TypeReference<>() {});

            final String content = extractContent(result);

            final Blog blog = blogRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("오류 발생"));

            blog.applySummaryUpdate(
                    parseSummary(content),
                    Interest.from(parseCategory(content)),
                    parseRating(content),
                    BlogStatus.SUMMARY
            );

            return result;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류 발생: " + e.getMessage(), e);
            // TODO GlobalException 생기면 추가 해야함
        }
    }

    private void addTemplate(final GPTSummaryRequest gptSummaryRequest) {
        gptSummaryRequest.messages().get(0).addTemplate(template);
    }

    private String extractContent(final Map<String, Object> result) {
        final List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
        final Map<String, Object> choice = choices.get(0);
        final Map<String, Object> message = (Map<String, Object>) choice.get("message");
        return (String) message.get("content");
    }

    private String parseSummary(final String content) {
        final String summary = parseContent(content, 2);
        return summary.replace(",", "");
    }

    private String parseCategory(final String content) {
        final String category = parseContent(content, 1);
        return category.replace(",", "");
    }

    private Long parseRating(final String content) {
        final String ratingString = parseContent(content, 3);
        return Long.parseLong(ratingString);
    }

    private String parseContent(final String content, final int index) {
        final String[] parts = content.split("\n");
        return parts[index].split(":")[1].trim().replaceAll("\"", "");
    }
}
