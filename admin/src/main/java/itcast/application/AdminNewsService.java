package itcast.application;

import itcast.domain.news.News;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminNewsResponse;
import itcast.repository.AdminRepository;
import itcast.repository.NewsRepository;
import itcast.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminNewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminNewsResponse createNews(Long userId, News news) {
        if(!identifyAdmin(userId)) {
            throw new IllegalArgumentException("접근할 수 없는 유저입니다.");
        }

        News savedNews = newsRepository.save(news);

        return new AdminNewsResponse(savedNews);
    }

    private boolean identifyAdmin(Long id){
        String email = userRepository.findById(id).get().getKakaoEmail();
        return adminRepository.existsByEmail(email);
    }
}
