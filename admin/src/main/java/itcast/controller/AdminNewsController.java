package itcast.controller;

import itcast.application.AdminNewsService;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminNewsResponse;
import itcast.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class AdminNewsController {

    private final AdminNewsService adminService;

    @PostMapping
    public ResponseTemplate<AdminNewsResponse> createNews(@RequestParam Long userId, @RequestBody AdminNewsRequest adminNewsRequest) {
        AdminNewsResponse response = adminService.createNews(userId, AdminNewsRequest.toEntity(adminNewsRequest));

        return new ResponseTemplate<>(HttpStatus.CREATED,"관리자 뉴스 생성 성공", response);
    }

    @PutMapping
    public ResponseTemplate<AdminNewsResponse> updateNews(@RequestParam Long userId, @RequestParam Long newsId, @RequestBody AdminNewsRequest adminNewsRequest) {
        AdminNewsResponse response = adminService.updateNews(userId, newsId, adminNewsRequest);

        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 수정 성공", response);
    }
}
