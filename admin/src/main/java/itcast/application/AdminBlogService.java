package itcast.application;

import itcast.domain.blog.Blog;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.repository.AdminRepository;
import itcast.repository.BlogRepository;
import itcast.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminBlogResponse createBlog(Long userid, Blog blog) {
        if(!identifyAdmin(userid)) {
            throw new IllegalArgumentException("접근할 수 없는 유저입니다.");
        }

        Blog savedBlogs = blogRepository.save(blog);
        return new AdminBlogResponse(savedBlogs);
    }

    private boolean identifyAdmin(Long id){
        String email = userRepository.findById(id).get().getKakaoEmail();
        return adminRepository.existsByEmail(email);
    }
}
