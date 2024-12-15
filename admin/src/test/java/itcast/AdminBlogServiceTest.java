package itcast;

import itcast.application.AdminBlogService;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
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

import java.time.LocalDateTime;
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
    @DisplayName("블로그 생성 성공")
    public void SuccessBlogCreate() {
        //given
        Long userId = 1L;
        LocalDateTime fixedTime = LocalDateTime.of(2024, 12, 1, 12, 0);

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        Blog blog = Blog.adminBuilder()
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

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.save(blog)).willReturn(blog);

        //when
        AdminBlogResponse response = adminBlogService.createBlog(userId, blog);

        //then
        assertEquals(blog.getTitle(), response.title());
        assertEquals(blog.getSendAt(), response.sendAt());
        verify(blogRepository).save(blog);
    }
}
