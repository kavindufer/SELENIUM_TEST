package framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Retry429 {
    public static String getWithRetry(String url, int attempts) throws IOException {
        int delay = 200;
        for (int i = 0; i < attempts; i++) {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            int status = conn.getResponseCode();
            if (status != 429) {
                try (InputStream is = conn.getInputStream()) {
                    return new String(is.readAllBytes());
                }
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignored) {
            }
            delay *= 2;
        }
        throw new IOException("Failed after retries");
    }
}
