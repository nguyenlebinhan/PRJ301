package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AIService {
    private static final Logger LOGGER = Logger.getLogger(AIService.class.getName());
    
    private static final String AI_API_URL = "http://127.0.0.1:8000/check-plagiarism-auto";
    private static final String AI_API_URL2 ="http://127.0.0.1:8000/check-topic-relevance";
    
    public JsonObject checkPlagiarismAuto(String reportFileLink) {
        try {
            URL url = new URL(AI_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Tăng timeout vì AI phải Search Google + Crawl Web nên tốn thời gian hơn
            conn.setConnectTimeout(10000);  // 10 giây kết nối
            conn.setReadTimeout(120000);    // 120 giây (2 phút) để AI quét cả internet
            
            // Chỉ gửi duy nhất thesis_text
            JsonObject inputJson = new JsonObject();
            inputJson.addProperty("report_url", reportFileLink);
            String jsonInputString = inputJson.toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == 200) {
                try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    String response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "{}";
                    return JsonParser.parseString(response).getAsJsonObject();
                }
            } else {
                LOGGER.log(Level.SEVERE, "AI API Error: HTTP Code {0}", code);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi kết nối AI Service: " + e.getMessage(), e);
        }

        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("similarity_score", 0.0);
        errorJson.addProperty("status", "Hệ thống AI đang bận hoặc lỗi");
        errorJson.addProperty("best_source", "N/A");
        return errorJson;
    }
    public JsonObject checkTopicRelevant(String reportFileLink,String topicTitle) {
        try {
            URL url = new URL(AI_API_URL2);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Tăng timeout vì AI phải Search Google + Crawl Web nên tốn thời gian hơn
            conn.setConnectTimeout(10000);  // 10 giây kết nối
            conn.setReadTimeout(120000);    // 120 giây (2 phút) để AI quét cả internet
            
            // Chỉ gửi duy nhất thesis_text
            JsonObject inputJson = new JsonObject();
            inputJson.addProperty("report_url", reportFileLink);
            inputJson.addProperty("topic_title", topicTitle);
            String jsonInputString = inputJson.toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == 200) {
                try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8)) {
                    String response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "{}";
                    return JsonParser.parseString(response).getAsJsonObject();
                }
            } else {
                LOGGER.log(Level.SEVERE, "AI API Error: HTTP Code {0}", code);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi kết nối AI Service: " + e.getMessage(), e);
        }

        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("relevance_score", 0.0);
        errorJson.addProperty("relevance_status", "Hệ thống AI đang bận hoặc lỗi");
        errorJson.addProperty("relevance_analysis", "N/A");
        return errorJson;
    }    
}