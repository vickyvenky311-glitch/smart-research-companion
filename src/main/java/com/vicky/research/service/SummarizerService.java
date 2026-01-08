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

        if (text == null || text.trim().isEmpty()) {
            return "No text found to summarize.";
        }

        // Limit input length (important for OpenAI)
        if (text.length() > 5000) {
            text = text.substring(0, 5000);
        }

        String requestBody = """
        {
          "model": "gpt-4.1-mini",
          "input": "You are an academic research assistant.\\n\\nSummarize the following content in a detailed, structured format suitable for research notes.\\n\\nRules:\\n- Do NOT write long paragraphs\\n- Each section must be 1–2 lines only\\n- Leave one blank line between sections\\n- Be clear, factual, and concise\\n- Do not repeat ideas\\n- Do not add opinions or filler text\\n\\nUse the following format strictly:\\n\\n### Overview\\n1–2 lines explaining what the article is about.\\n\\n### Key Concepts\\n1–2 lines describing the main ideas or arguments.\\n\\n### Important Details\\n1–2 lines covering notable facts, examples, or explanations.\\n\\n### Implications / Use Case\\n1–2 lines explaining why this information is useful or relevant.\\n\\nIf the article is long or complex, you may add more sections using clear headings.\\nFocus on clarity and depth, not brevity.\\n\\nContent to summarize:\\n%s"
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
                throw new RuntimeException("OpenAI API error: " + response.code());
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

        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }
}
