package itcast.jwt;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "사용자 인증")
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isSupported = parameter.hasParameterAnnotation(LoginMember.class);
        log.debug("supportsParameter 체크. 지원 여부: {}", isSupported);
        return isSupported;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // HttpServletRequest에서 userId를 추출
        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        Long userId = (Long)request.getAttribute("userId");

        if (userId == null) {
            log.error("유효하지 않은 사용자입니다. userId가 null입니다.");
            throw new RuntimeException("유효하지 않은 사용자입니다.");
        }
        return userId;
    }
}