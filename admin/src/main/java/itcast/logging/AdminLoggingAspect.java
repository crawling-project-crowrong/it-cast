package itcast.logging;

import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j(topic = "Admin Log")
public class AdminLoggingAspect {

    @Before("execution(* itcast.controller.AdminBlogController.createBlog(..))")
    public void logBeforeCreateBlog() {
        log.info("어드민 블로그 생성 메서드 호출, 관리자 ID: {}", MDC.get("userId"));
    }

    @Before("execution(* itcast.controller.AdminBlogController.updateBlog(..))")
    public void logBeforeUpdateBlog() {
        log.info("어드민 블로그 수정 메서드 호출, 관리자 ID: {}", MDC.get("userId"));
    }

    @Before("execution(* itcast.controller.AdminBlogController.deleteBlog(..))")
    public void logBeforeDeleteBlog() {
        log.info("어드민 블로그 삭제 메서드 호출, 관리자 ID: {}", MDC.get("userId"));
    }

    // Define the logging methods for different news operations
    @Before("execution(* itcast.controller.AdminNewsController.createNews(..))")
    public void logBeforeCreateNews() {
        log.info("어드민 뉴스 생성 메서드 호출, 관리자 ID: {}", MDC.get("userId"));
    }

    @Before("execution(* itcast.controller.AdminNewsController.updateNews(..))")
    public void logBeforeUpdateNews() {
        log.info("어드민 뉴스 수정 메서드 호출, 관리자 ID: {}", MDC.get("userId"));
    }

    @Before("execution(* itcast.controller.AdminNewsController.deleteNews(..))")
    public void logBeforeDeleteNews() {
        log.info("어드민 뉴스 삭제 메서드 호출, 관리자 ID: {}", MDC.get("userId"));
    }

    @AfterThrowing(pointcut = "execution(* itcast.controller.AdminNewsController.*(..))", throwing = "ex")
    public void logNewsThrowingException(final Throwable ex) {
        handleErrorLogging(ex);
    }

    @AfterThrowing(pointcut = "execution(* itcast.controller.AdminBlogController.*(..))", throwing = "ex")
    public void logBlogThrowingException(final Throwable ex) {
        handleErrorLogging(ex);
    }

    private void handleErrorLogging(final Throwable ex) {
        if (ex instanceof ItCastApplicationException itCastEx) {
            final ErrorCodes errorCode = itCastEx.getErrorCodes();
            log.error("예외 발생! 관리자 ID: {}, 에러 코드: {}, 에러 메시지: {} 상태: {}",
                    MDC.get("userId"),
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    errorCode.getStatus());
        }
    }
}
