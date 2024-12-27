package itcast;

import itcast.application.AdminNewsHistoryService;
import itcast.domain.user.User;
import itcast.dto.response.AdminNewsHistoryResponse;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import itcast.repository.NewsHistoryRepository;
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
public class AdminNewsHistoryServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private NewsHistoryRepository newsHistoryRepository;
    @InjectMocks
    private AdminNewsHistoryService adminNewsHistoryService;

    @Test
    @DisplayName("히스토리 조회 성공")
    public void SuccessNewHistoryRetrieve(){
        //given
        Long adminId = 1L;
        Long userId = null;
        Long newsId = null;
        LocalDate createdAt = LocalDate.of(2024, 12, 26);
        int page = 0;
        int size = 20;

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        List<AdminNewsHistoryResponse> responses = List.of(
                new AdminNewsHistoryResponse(
                        1L,
                        1L,
                        1L,
                        LocalDateTime.of(2024, 12, 26, 0, 0, 0),
                        LocalDateTime.now()),
                new AdminNewsHistoryResponse(
                        2L,
                        1L,
                        2L,
                        LocalDateTime.of(2024, 12, 26, 23, 59, 59),
                        LocalDateTime.now())
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminNewsHistoryResponse> newsHistoryPage = new PageImpl<>(responses, pageable, responses.size());

        given(userRepository.findById(adminId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(newsHistoryRepository.findNewsHistoriesByCondition(userId, newsId, createdAt, pageable)).willReturn(newsHistoryPage);

        //when
        Page<AdminNewsHistoryResponse> responsePage = adminNewsHistoryService.retrieveNewsHistory(adminId, userId, newsId, createdAt, page, size);

        //Then
        assertEquals(2, responsePage.getContent().size());
        assertEquals(1L, responsePage.getContent().get(0).id());
        assertEquals(2L, responsePage.getContent().get(1).id());
        verify(newsHistoryRepository).findNewsHistoriesByCondition(userId, newsId, createdAt, pageable);
    }
}
