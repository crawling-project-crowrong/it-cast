package itcast.controller;

import itcast.application.AdminService;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.dto.response.AdminNewsResponse;
import itcast.dto.response.ResponseBodyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/news")
    public ResponseEntity<ResponseBodyDto<AdminNewsResponse>> createNews(@RequestParam Long userId, @RequestBody AdminNewsRequest adminNewsRequest) {
        return new ResponseEntity<>(
                ResponseBodyDto.success("관리자 뉴스 생성 성공",
                        adminService.createNews(userId, adminNewsRequest)), HttpStatus.CREATED);
    }

    @PostMapping("/blogs")
    public ResponseEntity<ResponseBodyDto<AdminBlogResponse>> crawlBlogs(@RequestParam Long userId, @RequestBody AdminBlogRequest adminBlogRequest) {
        return new ResponseEntity<>(
                ResponseBodyDto.success("관리자 블로그 생성 성공",
                        adminService.createBlog(userId, adminBlogRequest)), HttpStatus.CREATED);
    }
}
