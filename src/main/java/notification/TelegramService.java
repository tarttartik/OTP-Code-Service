package notification;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramService {

    private final String telegramApiUrl = "https://api.telegram.org/botYOUR_BOT_TOKEN/sendMessage";
    private static final Logger logger = LogManager.getLogger(TelegramService.class);

    public void sendCode(String username, String chatId, String code) {
        String message = String.format(STR."\{username}, your confirmation code is: %s", code);
        String url = String.format("%s?chat_id=%s&text=%s",
                telegramApiUrl,
                chatId,
                urlEncode(message));

        sendTelegramRequest(url);
    }
    private void sendTelegramRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
            }
        } catch (IOException e) {
            logger.error("Ошибка отправки сообщения: {}", e.getMessage());
        }
    }
    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
