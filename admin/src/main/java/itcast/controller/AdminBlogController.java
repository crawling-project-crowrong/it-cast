package itcast.controller;

import itcast.ResponseTemplate;
import itcast.application.AdminBlogService;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog")
public class AdminBlogController {

    private final AdminBlogService adminBlogService;

    @PostMapping
    public ResponseTemplate<AdminBlogResponse> createBlog(
            @RequestParam Long userId,
            @RequestBody AdminBlogRequest adminBlogRequest
    ) {
        AdminBlogResponse response = adminBlogService.createBlog(userId, adminBlogRequest.toEntity(adminBlogRequest));
        return new ResponseTemplate<>(HttpStatus.CREATED, "관리자 블로그 생성 성공", response);
    }

    @DeleteMapping
    public ResponseTemplate<AdminBlogResponse> deleteBlog(@RequestParam Long userId, @RequestParam Long blogId) {
        AdminBlogResponse response = adminBlogService.deleteBlog(userId, blogId);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 삭제 성공", response);
    }
}
