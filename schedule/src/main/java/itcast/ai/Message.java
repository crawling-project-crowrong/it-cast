package itcast.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message {

    @JsonProperty("role")
    private String role = "user";

    private String content;

    public Message(final String role, final String content) {
        this.role = role;
        this.content = content;
    }

    public void addPrompt(final String content) {
        this.content += content;
    }
}
