package itcast.blog.scheduler;

import itcast.blog.application.BlogSendService;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import itcast.message.application.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.FailedMessage;

@Slf4j(topic = "블로그 전송 스케쥴")
@Component
@RequiredArgsConstructor
public class BlogSendSchedule {

    private final BlogSendService blogSendService;

    @Scheduled(cron = "${scheduler.blog.sending}")
    public void sendEmail() {
        log.info("Blog Email Send Start ...");

        LocalDate today = LocalDate.now();
        final String requestId = UUID.randomUUID().toString();
        MDC.put("request_id", requestId);

        try {
            blogSendService.sendBlogForEmail(today);
            log.info("Blog Send Finished !!");
        } catch (ItCastApplicationException e) {
            log.error("이메일을 보낼 때 오류가 발생하였습니다. ErrorCode: {}, Message: {}",
                    e.getErrorCodes(),
                    e.getMessage(),
                    e);
        } finally {
            MDC.clear();
        }

    }

    @Scheduled(cron = "${scheduler.blog.sending}")
    public void sendMessage() {
        log.info("Blog Message Send Start ...");

        LocalDate today = LocalDate.now();
        final String requestId = UUID.randomUUID().toString();
        MDC.put("request_id", requestId);

        try {
            blogSendService.sendBlogForMessage(today);
            log.info("Blog Send Finished !!");
        } catch (Exception exception) {
            throw new ItCastApplicationException(ErrorCodes.MESSAGE_SENDING_FAILED);
        } finally {
            MDC.clear();
        }
    }
}
