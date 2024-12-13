package itcast.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import itcast.auth.jwt.CheckAuth;
import itcast.auth.jwt.LoginMember;

@RestController
public class TestController {
    @CheckAuth
    @GetMapping("/api/test")
    public ResponseEntity<String> getUser(@LoginMember Long userId) {
        return ResponseEntity.ok("로그인된 사용자 ID: " + userId);
    }
}