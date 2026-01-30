package com.example.addon.utils.webhook;

public class WebhookContent {
    private final String content;
    private final String username;
    private final String avatarUrl;
    private final boolean tts;

    public WebhookContent(String content, String username, String avatarUrl, boolean tts) {
        this.content = content;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isTts() {
        return tts;
    }

    /**
     * Converts the webhook content to JSON format for Discord.
     */
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"content\":\"").append(escapeJson(content)).append("\",");
        json.append("\"username\":\"").append(escapeJson(username)).append("\",");
        json.append("\"avatar_url\":\"").append(escapeJson(avatarUrl)).append("\",");
        json.append("\"tts\":").append(tts);
        json.append("}");
        return json.toString();
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }
}

