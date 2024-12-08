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

    @JsonProperty("content")
    private String content;

    public Message(final String role, final String content) {
        this.role = role;
        this.content = content;
    }

    public void addTemplate(final String content) {
        this.content += content;
    }
}
