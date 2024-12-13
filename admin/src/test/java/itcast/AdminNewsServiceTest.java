package itcast;

import itcast.application.AdminNewsService;
import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;
import itcast.dto.response.AdminNewsResponse;
import itcast.repository.AdminRepository;
import itcast.repository.NewsRepository;
import itcast.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminNewsServiceTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AdminNewsService adminNewsService;

    @Test
    @DisplayName("뉴스 생성 성공")
    public void SuccessNewsCreate() {
        //given
        Long userId = 1L;
        LocalDateTime fixedTime = LocalDateTime.of(2024, 12, 1, 12, 0);
        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();
        News news = News.builder()
                .title("제목")
                .content("수정본")
                .originalContent("원본")
                .interest(Interest.NEWS)
                .publishedAt(fixedTime)
                .rating(5)
                .link("http://example.com")
                .thumbnail("http://thumbnail.com")
                .status(NewsStatus.SUMMARY)
                .sendAt(fixedTime)
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(newsRepository.save(news)).willReturn(news);

        // When
        AdminNewsResponse response = adminNewsService.createNews(userId, news);

        // Then
        assertEquals("제목", response.title());
        assertEquals(NewsStatus.SUMMARY, response.status());
        verify(newsRepository).save(news);
    }
}
