package itcast.mail.dto.request;

import java.util.List;

public record SendMailRequest(
        List<String> receivers,
        String subject,
        String content
) {
}
