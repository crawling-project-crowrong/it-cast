package itcast.ai.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import itcast.ai.client.GPTClient;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.request.Message;
import itcast.ai.dto.response.GPTSummaryResponse;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.exception.ItCastApplicationException;
import itcast.news.repository.NewsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private BlogRepository blogRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private GPTService gptService;

    @Test
    @DisplayName("올바른 요청이 들어오면 블로그 요약에 성공한다.")
    void summary_Blog_Content_success_test() {
        // given
        final Long blogId = 1L;
        final String title = "test title";
        final String originalContent = "test originalContent";
        final LocalDate publishedAt = LocalDate.of(2025, 1, 16);
        final String link = "test link";
        final String thumbnail = "test thumbnail";
        final BlogStatus status = BlogStatus.ORIGINAL;

        final Blog blog = Blog.builder()
                .platform(Platform.VELOG)
                .title(title)
                .originalContent(originalContent)
                .publishedAt(publishedAt)
                .link(link)
                .thumbnail(thumbnail)
                .status(status)
                .build();

        final String model = "gpt-4o-mini";

        final Message message = new Message("user", originalContent);

        final float temperature = 0.7f;

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest(model, message, temperature);

        final String jsonResponse = "{\n" +
                "  \"category\" : \"BACKEND\",\n" +
                "  \"summary\" : \"test summary\",\n" +
                "  \"rating\" : 8\n" +
                "}";

        GPTSummaryResponse.Message messages = new GPTSummaryResponse.Message(jsonResponse);
        GPTSummaryResponse.Choice choice = new GPTSummaryResponse.Choice(messages);
        GPTSummaryResponse response = new GPTSummaryResponse(Collections.singletonList(choice));

        when(gptClient.sendRequest(gptSummaryRequest, ArticleType.BLOG)).thenReturn(response);
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        // when
        gptService.updateBlogBySummaryContent(gptSummaryRequest, blogId);

        // then
        verify(gptClient, times(1)).sendRequest(gptSummaryRequest, ArticleType.BLOG);
        assertThat(blog.getInterest()).isEqualTo(Interest.BACKEND);
        assertThat(blog.getContent()).isEqualTo("test summary");
        assertThat(blog.getRating()).isEqualTo(8L);
    }

    @Test
    @DisplayName("블로그가 유효하지 않으면 요약에 실패한다.")
    void summaryContent_test_not_found_blog() {
        // given
        final Long blogId = 1L;

        final String originalContent = "test originalContent";

        final String model = "gpt-4o-mini";

        final Message message = new Message("user", originalContent);

        final float temperature = 0.7f;

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest(model, message, temperature);

        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> gptService.updateBlogBySummaryContent(gptSummaryRequest, blogId))
                .isInstanceOf(ItCastApplicationException.class);
    }

    @Test
    @DisplayName("올바른 요청이 들어오면 뉴스 요약에 성공한다.")
    void summary_News_Content_success_test() {
        // given
        final Long newsId = 1L;
        final String title = "test title";
        final String originalContent = "test originalContent";
        final LocalDateTime publishedAt = LocalDateTime.of(2025, 1, 16, 12, 12, 12);
        final String link = "test link";
        final String thumbnail = "test thumbnail";
        final NewsStatus status = NewsStatus.ORIGINAL;

        final News news = News.builder()
                .title(title)
                .originalContent(originalContent)
                .publishedAt(publishedAt)
                .link(link)
                .thumbnail(thumbnail)
                .status(status)
                .build();

        final String model = "gpt-4o-mini";

        final Message message = new Message("user", originalContent);

        final float temperature = 0.7f;

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest(model, message, temperature);

        final String jsonResponse = "{\n" +
                "  \"category\" : \"BACKEND\",\n" +
                "  \"summary\" : \"test summary\",\n" +
                "  \"rating\" : 8\n" +
                "}";

        GPTSummaryResponse.Message messages = new GPTSummaryResponse.Message(jsonResponse);
        GPTSummaryResponse.Choice choice = new GPTSummaryResponse.Choice(messages);
        GPTSummaryResponse response = new GPTSummaryResponse(Collections.singletonList(choice));

        when(gptClient.sendRequest(gptSummaryRequest, ArticleType.NEWS)).thenReturn(response);
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        // when
        gptService.updateNewsBySummaryContent(gptSummaryRequest, 1L);

        // then
        verify(gptClient, times(1)).sendRequest(gptSummaryRequest, ArticleType.NEWS);
        assertThat(news.getInterest()).isEqualTo(Interest.BACKEND);
        assertThat(news.getContent()).isEqualTo("test summary");
        assertThat(news.getRating()).isEqualTo(8L);
    }

    @Test
    @DisplayName("뉴스가 유효하지 않으면 요약에 실패한다.")
    void summaryContent_test_not_found_news() {
        // given
        final Long newsId = 1L;

        final String originalContent = "test originalContent";

        final String model = "gpt-4o-mini";

        final Message message = new Message("user", originalContent);

        final float temperature = 0.7f;

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest(model, message, temperature);
        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> gptService.updateNewsBySummaryContent(gptSummaryRequest, newsId))
                .isInstanceOf(ItCastApplicationException.class);
    }
}



