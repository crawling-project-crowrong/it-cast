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
    @DisplayName("블로그 삭제 성공")
    public void successBlogDelete(){
        //Given
        Long userId = 1L;
        Long blogId = 1L;

        User adminUser = User.builder()
                .id(userId)
                .kakaoEmail("admin@kakao.com")
                .build();

        Blog blog = Blog.adminBuilder()
                .id(1L)
                .title("테스트 블로그")
                .content("테스트 내용")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(adminUser));
        given(adminRepository.existsByEmail(adminUser.getKakaoEmail())).willReturn(true);
        given(blogRepository.findById(blogId)).willReturn(Optional.of(blog));

        // When
        AdminBlogResponse response = adminBlogService.deleteBlog(userId, blogId);

        // Then
        assertEquals(blog.getId(), response.id());
        assertEquals(blog.getTitle(), response.title());
        verify(blogRepository).delete(blog);
    }
}