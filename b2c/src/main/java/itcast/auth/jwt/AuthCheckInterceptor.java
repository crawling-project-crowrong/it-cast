package itcast.auth.jwt;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import itcast.domain.user.User;
import itcast.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "사용자 인증")
@Component
@RequiredArgsConstructor
public class AuthCheckInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // HandlerMethod 타입인지를 확인한 후, 형변환
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // @CheckAuth 어노테이션이 붙은 메서드에 대해서만 인증을 진행
            if (handlerMethod.hasMethodAnnotation(CheckAuth.class)) {
                String token = null;

                // Authorization 헤더에서 토큰을 찾을 수 없는 경우 쿠키에서 찾기
                String cookieHeader = request.getHeader("Cookie");
                if (cookieHeader != null && cookieHeader.contains("Authorization=")) {
                    // 쿠키에서 Authorization 값을 추출
                    String[] cookies = cookieHeader.split(";");
                    for (String cookie : cookies) {
                        if (cookie.trim().startsWith("Authorization=")) {
                            token = cookie.split("=")[1];
                            break;
                        }
                    }
                }

                // 쿠키에서 토큰을 찾을 수 없는 경우, Authorization 헤더에서 확인
                if (token == null) {
                    token = request.getHeader("Authorization");
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7); // "Bearer " 제거
                    }
                }

                // 토큰이 없으면 예외 처리
                if (token == null) {
                    log.error("JWT 토큰이 없습니다. 로그인이 필요한 기능입니다.");
                    throw new RuntimeException("로그인이 필요한 기능입니다.");
                }

                // JWT에서 userId 추출
                Long userId = jwtUtil.getUserIdFromToken(token);

                // DB에서 회원 정보 조회
                Optional<User> user = userRepository.findById(userId);
                if (user.isEmpty()) {
                    // DB에 해당 회원이 없으면 인증 실패 처리
                    log.error("유효하지 않은 사용자입니다. userId: {}", userId);
                    throw new RuntimeException("유효하지 않은 사용자입니다.");
                }
                // 인증 성공 후 userId를 HttpServletRequest에 추가
                log.info("사용자 인증 성공. userId: {}", userId);
                request.setAttribute("userId", userId);
                return true;
            }
        }
        // @CheckAuth가 없으면 인증을 건너뛰고 true 반환
        return true;
    }
}