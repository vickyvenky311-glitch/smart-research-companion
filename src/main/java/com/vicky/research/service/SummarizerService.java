package com.vicky.research.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SummarizerService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    public String summarize(String text) {

        // Limit input to control cost
        if (text.length() > 3000) {
            text = text.substring(0, 3000);
        }

        String requestBody = """
        {
          "model": "gpt-4.1-mini",
          "input": "Summarize the following text in 5–6 clear lines:\\n%s"
        }
        """.formatted(text.replace("\"", ""));

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/responses")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(
                        requestBody,
                        MediaType.parse("application/json")
                ))
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("OpenAI API error: " + response);
            }

            String body = response.body().string();

            JSONObject json = new JSONObject(body);
            JSONArray output = json.getJSONArray("output");
            JSONObject firstOutput = output.getJSONObject(0);
            JSONArray content = firstOutput.getJSONArray("content");

            for (int i = 0; i < content.length(); i++) {
                JSONObject part = content.getJSONObject(i);
                if ("output_text".equals(part.getString("type"))) {
                    return part.getString("text");
                }
            }

            return "No summary text returned.";

        } catch (IOException e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }
}
