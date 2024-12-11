package itcast.controller;

import itcast.application.AdminBlogService;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.dto.response.ResponseBodyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminBlogController {

    private final AdminBlogService adminService;

    @PostMapping("/blogs")
    public ResponseEntity<ResponseBodyDto<AdminBlogResponse>> crawlBlogs(@RequestParam Long userId, @RequestBody AdminBlogRequest adminBlogRequest) {
        return new ResponseEntity<>(
                ResponseBodyDto.success("관리자 블로그 생성 성공",
                        adminService.createBlog(userId, adminBlogRequest)), HttpStatus.CREATED);
    }
}
