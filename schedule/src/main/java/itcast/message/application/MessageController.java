package itcast.message.application;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.nurigo.sdk.message.model.FailedMessage;

import itcast.ResponseTemplate;
import itcast.message.dto.request.SendMessageRequest;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseTemplate<List<FailedMessage>> sendMessages(@RequestBody SendMessageRequest request) {
        return messageService.sendMessages(request);
    }
}
