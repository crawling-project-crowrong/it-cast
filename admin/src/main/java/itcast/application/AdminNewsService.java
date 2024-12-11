package itcast.application;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.User;
import itcast.dto.response.AdminNewsResponse;
import itcast.repository.AdminRepository;
import itcast.repository.NewsRepository;
import itcast.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminNewsService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminNewsResponse createNews(Long userId, News news) {
        isAdmin(userId);
        News savedNews = newsRepository.save(news);

        return new AdminNewsResponse(savedNews);
    }

    public Page<AdminNewsResponse> retrieveNews(Long userId, NewsStatus status, int page, int size) {
        isAdmin(userId);
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findAllByStatusOrderBySendAtDesc(status, pageable);
    }

    private void isAdmin(Long id){
        User user = userRepository.findById(id).orElseThrow(()->
                new NullPointerException("해당 id가 존재하지 않습니다."));
        String email = user.getKakaoEmail();
        if(!adminRepository.existsByEmail(email)){
            throw new IllegalArgumentException("접근할 수 없는 유저입니다.");
        }
    }
}