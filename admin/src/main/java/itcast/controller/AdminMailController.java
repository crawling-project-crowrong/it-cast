package itcast.controller;

import itcast.ResponseTemplate;
import itcast.application.AdminMailService;
import itcast.dto.response.MailResponse;
import itcast.dto.response.PageResponse;
import itcast.jwt.CheckAuth;
import itcast.jwt.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/mail-event")
public class AdminMailController {

    private final AdminMailService adminMailService;

    @CheckAuth
    @GetMapping
    public ResponseTemplate<PageResponse<MailResponse>> retrieveMailEvent(
            @LoginMember Long adminId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MailResponse> mailPage = adminMailService.retrieveMailEvent(adminId, page, size);
        PageResponse<MailResponse> pageResponse = new PageResponse<>(
                mailPage.getContent(),
                mailPage.getNumber(),
                mailPage.getSize(),
                mailPage.getTotalPages()
        );
        return new ResponseTemplate<>(HttpStatus.OK, "메일 이벤트 조회 성공", pageResponse);
    }

    @CheckAuth
    @PostMapping("/send")
    public ResponseTemplate<Void> sendMailEvent(
            @LoginMember Long adminId,
            @RequestParam Long userId,
            @RequestParam LocalDate createdAt
    ) {
        adminMailService.sendMailEvent(adminId, userId, createdAt);
        return new ResponseTemplate<>(HttpStatus.OK, "메일이 정상적으로 발송되었습니다", null);
    }
}