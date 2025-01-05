package itcast.mail.application;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.composition.TextObject;
import itcast.exception.ItCastApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static itcast.exception.ErrorCodes.SLACK_PARSE_ERROR;

@Service
@RequiredArgsConstructor
public class SlackService {

    private static final String TOKEN = "token";
    private static final String CHANNEL = "channel";

    public void postInquiry(String receiver) {
        try {
            List<TextObject> textObjects = new ArrayList<>();
            textObjects.add(markdownText("*재 발송 실패 날짜:*\n" + LocalDateTime.now()));
            textObjects.add(markdownText("*재 시도 실패 메일:*\n" +receiver));

            MethodsClient methods = Slack.getInstance().methods(TOKEN);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(CHANNEL)
                    .blocks(asBlocks(
                            header(header ->
                                    header.text(plainText("메일 재 발송에 실패 했습니다!!!"))),
                            divider(),
                            section(section ->
                                    section.fields(textObjects)
                            ))).build();

            methods.chatPostMessage(request);
        } catch (Exception e) {
            throw new ItCastApplicationException(SLACK_PARSE_ERROR);
        }
    }
}
