package itcast;

import itcast.application.AdminBlogService;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.repository.AdminRepository;
import itcast.repository.BlogRepository;
import itcast.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminBlogServiceTest {
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AdminBlogService adminBlogService;

    @Test
    @DisplayName("블로그 수정 성공")
    public void SuccessBlogUpdate() {
        //given
        Long userId = 1L;
        Long blogId = 1L;
        LocalDateTime fixedTime = LocalDateTime.of(2024, 12, 1, 12, 0);

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        Blog blog = Blog.adminBuilder()
                .id(1L)
                .platform(Platform.VELOG)
                .title("제목")
                .content("수정본")
                .originalContent("원본")
                .interest(Interest.BACKEND)
                .publishedAt(fixedTime)
                .rating(5)
                .link("http://example.com")
                .thumbnail("http://thumbnail.com")
                .status(BlogStatus.SUMMARY)
                .sendAt(fixedTime)
                .build();
        AdminBlogRequest adminBlogRequest = new AdminBlogRequest(
                Platform.VELOG,
                "제목2",
                "수정본2",
                "원본2",
                Interest.NEWS,
                fixedTime,
                3,
                "http://example2.com",
                "http://thumbnail2.com",
                BlogStatus.ORIGINAL,
                fixedTime
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.findById(blogId)).willReturn(Optional.of(blog));

        // when
        AdminBlogResponse response = adminBlogService.updateBlog(userId, blogId, adminBlogRequest);

        // Then
        assertEquals("제목2", response.title());
        assertEquals(BlogStatus.ORIGINAL, response.status());
    }
}
