package itcast.message.dto.request;

import java.util.List;

import lombok.Getter;
@Getter
public class SendMessageRequest {
    private List<MessageContent> contentList;
    private List<RecieverPhoneNumber> phoneNumbers;
}
