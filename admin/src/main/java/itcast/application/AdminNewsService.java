package itcast.application;

import static itcast.exception.ErrorCodes.NEWS_NOT_FOUND;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.User;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminNewsResponse;
import itcast.exception.ErrorCodes;
import itcast.exception.IdNotFoundException;
import itcast.exception.ItCastApplicationException;
import itcast.exception.NotAdminException;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import itcast.repository.NewsRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminNewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminNewsResponse createNews(Long userId, AdminNewsRequest adminNewsRequest) {
        isAdmin(userId);
        News news = AdminNewsRequest.toEntity(adminNewsRequest);
        News savedNews = newsRepository.save(news);
        return new AdminNewsResponse(savedNews);
    }

    public Page<AdminNewsResponse> retrieveNewsList(Long userId, NewsStatus status, LocalDate sendAt, int page, int size) {
        isAdmin(userId);
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findNewsByCondition(status, sendAt, pageable);
    }

    @Transactional
    public AdminNewsResponse updateNews(Long userId, Long newsId, AdminNewsRequest adminNewsRequest) {
        isAdmin(userId);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ItCastApplicationException(NEWS_NOT_FOUND));

        news.update(adminNewsRequest.title(),
                adminNewsRequest.content(),
                adminNewsRequest.originalContent(),
                adminNewsRequest.interest(),
                adminNewsRequest.publishedAt(),
                adminNewsRequest.rating(),
                adminNewsRequest.link(),
                adminNewsRequest.thumbnail(),
                adminNewsRequest.status(),
                adminNewsRequest.sendAt()
        );

        return new AdminNewsResponse(news);
    }

    @Transactional
    public AdminNewsResponse deleteNews(Long userId, Long newsId) {
        isAdmin(userId);
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new ItCastApplicationException(NEWS_NOT_FOUND));
        newsRepository.delete(news);
        return new AdminNewsResponse(news);
    }

    private void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
