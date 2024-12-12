package itcast.mail.application;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import itcast.mail.dto.request.EmailSender;
import itcast.mail.dto.request.SendMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final EmailSender emailSender;

    public void send(final SendMailRequest sendMailRequest) {
        try {
            amazonSimpleEmailService.sendEmail(emailSender.from(sendMailRequest));
        } catch (Exception ex) {
            throw new AmazonClientException(ex.getMessage(), ex);
        }
    }
}
