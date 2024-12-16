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
@RequestMapping("/api/blogs")
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

    @PutMapping
    public ResponseTemplate<AdminBlogResponse> updateBlog(
            @RequestParam Long userId,
            @RequestParam Long blogId,
            @RequestBody AdminBlogRequest adminBlogRequest
    ){
        AdminBlogResponse response = adminBlogService.updateBlog(userId, blogId, adminBlogRequest);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 수정 성공", response);
    }
}
