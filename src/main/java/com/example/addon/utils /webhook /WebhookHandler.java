package com.example.addon.utils.webhook;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebhookHandler {

    /**
     * Sends a message to a Discord webhook.
     *
     * @param webhookUrl The Discord webhook URL
     * @param content    The content to send
     * @throws Exception If sending fails
     */
    public static void send(String webhookUrl, WebhookContent content) throws Exception {
        URL url = new URL(webhookUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String json = content.toJson();

        try (OutputStream os = connection.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            throw new Exception("Failed to send webhook, HTTP response code: " + responseCode);
        }

        connection.disconnect();
    }
}
