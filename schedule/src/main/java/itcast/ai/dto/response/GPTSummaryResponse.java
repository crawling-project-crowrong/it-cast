package itcast.ai.dto.response;

import java.util.List;

public record GPTSummaryResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {
    }

    public record Message(
            String content
    ) {
    }

    public String getCategory() {
        final String content = choices.get(0).message.content();
        return parseCategory(content);
    }

    public String getSummary() {
        final String content = choices.get(0).message.content();
        return parseSummary(content);
    }

    public Long getRating() {
        final String content = choices.get(0).message.content();
        return parseRating(content);
    }

    private static String parseSummary(final String content) {
        final String summary = parseContent(content, 2);
        return summary.replace(",", "");
    }

    private static String parseCategory(final String content) {
        final String category = parseContent(content, 1);
        return category.replace(",", "");
    }

    private static Long parseRating(final String content) {
        final String ratingString = parseContent(content, 3);
        return Long.parseLong(ratingString);
    }

    public static String parseContent(final String content, final int index) {
        final String[] parts = content.split("\n");
        return parts[index].split(":")[1].trim().replaceAll("\"", "");
    }
}
