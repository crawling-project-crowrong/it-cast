package itcast.controller;

import itcast.application.AdminService;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminNewsResponse;
import itcast.dto.response.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/news")
    public ResponseTemplate<AdminNewsResponse> createNews(@RequestParam Long userId, @RequestBody AdminNewsRequest adminNewsRequest) {
        AdminNewsResponse response = adminService.createNews(userId, adminNewsRequest);

        return new ResponseTemplate<>(HttpStatus.CREATED,"관리자 뉴스 생성 성공", response);
    }
}
