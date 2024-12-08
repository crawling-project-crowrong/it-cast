package itcast.ai.dto.request;

import itcast.ai.Message;
import java.util.List;

public record GPTSummaryRequest(
        String model,
        List<Message> messages,
        float temperature
) {
}
